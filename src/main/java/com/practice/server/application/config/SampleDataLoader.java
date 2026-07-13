package com.practice.server.application.config;

import com.practice.server.application.model.entity.*;
import com.practice.server.application.model.enums.Role;
import com.practice.server.application.model.enums.RoomType;
import com.practice.server.application.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

    private static final int TARGET_HOTEL_COUNT = 10;
    private static final String DEMO_EMAIL = "demo@roomradar.com";

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ServiceRepository serviceRepository;
    private final UsersRepository usersRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${app.sample-data.enabled:true}")
    private boolean sampleDataEnabled;

    @Override
    @Transactional
    public void run(String... args) {
        if (!sampleDataEnabled) {
            log.info("Sample data loader disabled");
            return;
        }

        log.info("Running sample data loader...");
        syncPostgresSequences();
        Map<String, com.practice.server.application.model.entity.Service> services = seedServices();
        ensureRoomsAreAvailable();
        User demoUser = ensureDemoUser();
        ensureHotelImages();
        seedHotelsIfNeeded(services);
        ensureReviews(demoUser);
        log.info("Sample data loader finished. Hotels: {}", hotelRepository.count());
    }

    private void syncPostgresSequences() {
        List<String> tables = List.of(
                "reviews", "hotels", "hotel_images", "rooms", "room_images", "services", "users"
        );
        for (String table : tables) {
            try {
                entityManager.createNativeQuery(
                        "SELECT setval(pg_get_serial_sequence('" + table + "', 'id'), " +
                                "COALESCE((SELECT MAX(id) FROM " + table + "), 0) + 1, false)"
                ).getSingleResult();
            } catch (Exception e) {
                log.debug("Could not sync sequence for table {}: {}", table, e.getMessage());
            }
        }
    }

    private Map<String, com.practice.server.application.model.entity.Service> seedServices() {
        List<String> serviceNames = List.of(
                "WiFi gratis", "Piscina", "Spa", "Parking", "Restaurante", "Gimnasio",
                "Aire acondicionado", "Desayuno incluido", "Bar", "Servicio de habitaciones"
        );

        Map<String, com.practice.server.application.model.entity.Service> result = new LinkedHashMap<>();
        for (String name : serviceNames) {
            result.computeIfAbsent(name, n ->
                    serviceRepository.findAll().stream()
                            .filter(s -> s.getName().equalsIgnoreCase(n))
                            .findFirst()
                            .orElseGet(() -> serviceRepository.save(
                                    com.practice.server.application.model.entity.Service.builder().name(n).build()
                            ))
            );
        }
        return result;
    }

    private void ensureRoomsAreAvailable() {
        roomRepository.findAll().forEach(room -> {
            if (!Boolean.TRUE.equals(room.getAvailable())) {
                room.setAvailable(true);
                roomRepository.save(room);
            }
        });
    }

    private User ensureDemoUser() {
        return usersRepository.findByEmail(DEMO_EMAIL).orElseGet(() ->
                usersRepository.save(User.builder()
                        .fullName("Demo User")
                        .email(DEMO_EMAIL)
                        .password(passwordEncoder.encode("demo123"))
                        .role(Role.USER)
                        .createdAt(LocalDateTime.now())
                        .build())
        );
    }

    private void ensureHotelImages() {
        hotelRepository.findAll().forEach(hotel -> {
            ensureHotelCollections(hotel);
            boolean hasMain = hotel.getImages().stream().anyMatch(img -> Boolean.TRUE.equals(img.getIsMain()));
            if (hotel.getImages().isEmpty() || !hasMain) {
                addImagesToHotel(hotel, "existing-" + hotel.getId(), 3);
                hotelRepository.save(hotel);
                log.info("Added images to existing hotel: {}", hotel.getName());
            }
        });
    }

    private void seedHotelsIfNeeded(Map<String, com.practice.server.application.model.entity.Service> services) {
        long toCreate = TARGET_HOTEL_COUNT - hotelRepository.count();
        if (toCreate <= 0) {
            return;
        }

        List<HotelSeed> seeds = sampleHotels();
        int created = 0;
        for (HotelSeed seed : seeds) {
            if (created >= toCreate) break;
            if (hotelRepository.findAll().stream().anyMatch(h -> h.getName().equalsIgnoreCase(seed.name()))) {
                continue;
            }
            Hotel hotel = buildHotel(seed, services);
            hotelRepository.save(hotel);
            created++;
            log.info("Created sample hotel: {}", seed.name());
        }
    }

    private void ensureReviews(User demoUser) {
        List<Hotel> hotels = hotelRepository.findAll();
        double[] ratings = {4.9, 4.7, 4.5, 4.3, 4.1, 3.9, 4.8, 4.6, 4.4, 4.2};

        for (int i = 0; i < hotels.size(); i++) {
            Hotel hotel = hotels.get(i);
            if (!reviewRepository.findByHotelId(hotel.getId()).isEmpty()) {
                continue;
            }
            double rating = ratings[i % ratings.length];
            Review review = Review.builder()
                    .overallRating(rating)
                    .comment("Excelente estancia, muy recomendable.")
                    .createdAt(LocalDateTime.now().minusDays(i + 1L))
                    .user(demoUser)
                    .hotel(hotel)
                    .criteriaRatings(new ArrayList<>())
                    .build();
            try {
                reviewRepository.save(review);
            } catch (Exception e) {
                log.warn("Could not seed review for hotel {}: {}", hotel.getName(), e.getMessage());
            }
        }
    }

    private Hotel buildHotel(HotelSeed seed, Map<String, com.practice.server.application.model.entity.Service> services) {
        Hotel hotel = Hotel.builder()
                .name(seed.name())
                .description(seed.description())
                .address(seed.address())
                .city(seed.city())
                .country(seed.country())
                .stars(seed.stars())
                .createdAt(LocalDateTime.now().minusDays(seed.daysAgo()))
                .build();

        Set<com.practice.server.application.model.entity.Service> hotelServices = new HashSet<>();
        for (String serviceName : seed.serviceNames()) {
            com.practice.server.application.model.entity.Service service = services.get(serviceName);
            if (service != null) {
                hotelServices.add(service);
            }
        }
        hotel.setServices(hotelServices);
        ensureHotelCollections(hotel);

        addImagesToHotel(hotel, seed.imageSeed(), 4);

        List<Room> rooms = new ArrayList<>();
        rooms.add(buildRoom(hotel, "101", RoomType.SINGLE, 65.0, 1));
        rooms.add(buildRoom(hotel, "201", RoomType.DOUBLE, 95.0, 2));
        rooms.add(buildRoom(hotel, "301", RoomType.SUITE, 180.0, 4));
        hotel.setRooms(rooms);

        return hotel;
    }

    private Room buildRoom(Hotel hotel, String number, RoomType type, double price, int maxGuests) {
        return Room.builder()
                .roomNumber(number)
                .type(type)
                .pricePerNight(price)
                .available(true)
                .maxGuests(maxGuests)
                .hotel(hotel)
                .build();
    }

    private void ensureHotelCollections(Hotel hotel) {
        if (hotel.getImages() == null) {
            hotel.setImages(new ArrayList<>());
        }
        if (hotel.getRooms() == null) {
            hotel.setRooms(new ArrayList<>());
        }
        if (hotel.getServices() == null) {
            hotel.setServices(new HashSet<>());
        }
    }

    private void addImagesToHotel(Hotel hotel, String seed, int count) {
        ensureHotelCollections(hotel);
        String[] descriptions = {"Vista exterior", "Habitación", "Piscina", "Restaurante", "Lobby"};
        for (int i = 0; i < count; i++) {
            byte[] imageData = downloadImage("https://picsum.photos/seed/" + seed + "-" + i + "/640/480");
            HotelImage image = HotelImage.builder()
                    .imageData(imageData)
                    .description(descriptions[i % descriptions.length])
                    .isMain(i == 0)
                    .hotel(hotel)
                    .build();
            hotel.getImages().add(image);
        }
    }

    private byte[] downloadImage(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(12000);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "RoomRadar-SampleData/1.0");
            connection.connect();

            try (InputStream inputStream = connection.getInputStream()) {
                return inputStream.readAllBytes();
            }
        } catch (Exception e) {
            log.warn("Could not download image from {}, using placeholder", imageUrl);
            return minimalJpeg();
        }
    }

    private byte[] minimalJpeg() {
        return Base64.getDecoder().decode(
                "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRof" +
                "Hh0gHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIy" +
                "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAED" +
                "ASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAn/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFQEB" +
                "AQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCwAB//2Q=="
        );
    }

    private List<HotelSeed> sampleHotels() {
        return List.of(
                new HotelSeed("Gran Hotel Madrid Central", "Elegante hotel en pleno centro de Madrid con vistas panorámicas.",
                        "Calle Alcalá 25", "Madrid", "España", 5, 2,
                        "madrid-central", List.of("WiFi gratis", "Spa", "Restaurante", "Gimnasio", "Bar")),
                new HotelSeed("Barceló Carmen Granada", "Hotel con terraza y vistas a la Alhambra.",
                        "Calle Acera del Darro 62", "Granada", "España", 4, 5,
                        "granada-carmen", List.of("WiFi gratis", "Piscina", "Desayuno incluido", "Bar")),
                new HotelSeed("NH Collection Barcelona", "Diseño contemporáneo cerca de Passeig de Gràcia.",
                        "Gran Via de les Corts Catalanes 764", "Barcelona", "España", 5, 8,
                        "barcelona-nh", List.of("WiFi gratis", "Gimnasio", "Restaurante", "Aire acondicionado")),
                new HotelSeed("Parador de Sevilla", "Histórico parador junto al río Guadalquivir.",
                        "Calle Armas 52", "Sevilla", "España", 4, 12,
                        "sevilla-parador", List.of("WiFi gratis", "Piscina", "Restaurante", "Parking")),
                new HotelSeed("Meliá Valencia", "Hotel urbano con spa y piscina en el centro.",
                        "Avenida de Les Corts Valencianes 52", "Valencia", "España", 4, 15,
                        "valencia-melia", List.of("WiFi gratis", "Spa", "Piscina", "Gimnasio")),
                new HotelSeed("Iberostar Las Canteras", "Frente a la playa de Las Canteras.",
                        "Calle Madrid 1", "Las Palmas", "España", 5, 20,
                        "canteras-iberostar", List.of("WiFi gratis", "Piscina", "Restaurante", "Servicio de habitaciones")),
                new HotelSeed("Hotel Marqués de Riscal", "Arquitectura icónica en la Rioja Alavesa.",
                        "Calle Torrea 1", "Elciego", "España", 5, 25,
                        "rioja-marques", List.of("WiFi gratis", "Spa", "Restaurante", "Bar", "Parking")),
                new HotelSeed("AC Hotel Málaga Palacio", "Vistas al puerto y la catedral.",
                        "Calle Cortina del Muelle 1", "Málaga", "España", 4, 30,
                        "malaga-ac", List.of("WiFi gratis", "Gimnasio", "Bar", "Aire acondicionado")),
                new HotelSeed("Hotel Ritz Madrid", "Lujo clásico en el Paseo del Prado.",
                        "Plaza de la Lealtad 5", "Madrid", "España", 5, 35,
                        "madrid-ritz", List.of("WiFi gratis", "Spa", "Restaurante", "Servicio de habitaciones", "Parking")),
                new HotelSeed("Hotel Bahía Santander", "Frente a la bahía con vistas al mar.",
                        "Calle Hernán Cortés 3", "Santander", "España", 4, 40,
                        "santander-bahia", List.of("WiFi gratis", "Restaurante", "Bar", "Desayuno incluido"))
        );
    }

    private record HotelSeed(
            String name,
            String description,
            String address,
            String city,
            String country,
            int stars,
            int daysAgo,
            String imageSeed,
            List<String> serviceNames
    ) {}
}
