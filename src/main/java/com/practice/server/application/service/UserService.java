package com.practice.server.application.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.request.LoginRequest;
import com.practice.server.application.dto.request.RegisterRequest;
import com.practice.server.application.dto.request.UpdateUserRequest;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.utils.JwtTokenProvider;
import com.practice.server.application.model.enums.Role;
import com.practice.server.application.model.entity.User;
import com.practice.server.application.service.interfaces.IUserService;
import com.practice.server.application.repository.UsersRepository;
import com.practice.server.application.utils.MessageUtils;
import com.practice.server.application.utils.Utils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.practice.server.application.constants.Constants.*;

@Service
public class UserService implements IUserService {

    @Value("${google.client-id}")
    private String googleClientId;

    private final UsersRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UsersRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByFullName(request.getFullName())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setProvider(LOCAL);
        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            throw new PracticeException(INVALID_CREDENTIALS, MessageUtils.getMessage(INVALID_CREDENTIALS));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new PracticeException(INVALID_CREDENTIALS, MessageUtils.getMessage(INVALID_CREDENTIALS));
        }
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
        response.setHeader(SET_COOKIE, "token=" + token + "; Secure; SameSite=Strict; Path=/; Max-Age=3600");
        return token;
    }

    @Override
    public String refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Token de refresco inválido");
        }

        String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
        Role role = jwtTokenProvider.getRoleFromToken(refreshToken);

        return jwtTokenProvider.generateToken(email, role);
    }

    @Override
    public Boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByFullName(username)
                .orElseThrow(() -> new PracticeException(404, USER_404));

        return mapToDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new PracticeException(404, USER_404));

        return mapToDTO(user);
    }

    @Override
    public PracticeResponse handleGoogleLoginOrRegister(String idToken, HttpServletResponse response) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new PracticeException(INVALID_GOOGLE_TOKEN, MessageUtils.getMessage(INVALID_GOOGLE_TOKEN));
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");
            boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());

            if (!emailVerified) {
                throw new PracticeException(GOOGLE_EMAIL_NOT_VERIFIED, MessageUtils.getMessage(GOOGLE_EMAIL_NOT_VERIFIED));
            }

            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user;

            if (optionalUser.isPresent()) {
                user = optionalUser.get();

                String provider = user.getProvider();

                if (provider == null || LOCAL.equalsIgnoreCase(provider)) {
                    return new PracticeResponse(
                            ACCOUNT_EXISTS_NEEDS_CONFIRMATION,
                            MessageUtils.getMessage(ACCOUNT_EXISTS_NEEDS_CONFIRMATION)
                    );
                }

                if (!GOOGLE.equalsIgnoreCase(provider)) {
                    throw new PracticeException(
                            INVALID_PROVIDER,
                            MessageUtils.getMessage(INVALID_PROVIDER)
                    );
                }
            } else {
                // Create new user with Google provider
                user = new User();
                user.setEmail(email);
                user.setFullName(name);
                user.setProfilePicture(picture.getBytes(StandardCharsets.UTF_8));
                user.setGoogleProfilePictureUrl(picture);
                user.setProvider(GOOGLE);
                user.setRole(Role.USER);
                user.setCreatedAt(LocalDateTime.now());

                String generatedPassword = Utils.generateSecureRandomPassword(12);
                String encodedPassword = passwordEncoder.encode(generatedPassword);
                user.setPassword(encodedPassword);

                user = userRepository.save(user);
            }

            String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
            response.setHeader(SET_COOKIE, "token=" + token + "; Secure; SameSite=Strict; Path=/; Max-Age=3600");

            return new PracticeResponse(0, "Login exitoso con Google");

        } catch (PracticeException e) {
            throw e;
        } catch (Exception e) {
            throw new PracticeException(GOOGLE_LOGIN_FAILED, MessageUtils.getMessage(GOOGLE_LOGIN_FAILED) + ": " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUserByToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        String email;
        try {
            email = jwtTokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            return null;
        }
        return this.getUserByEmail(email);
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest request, String token) {
        try {
            UserDTO currentUser = getUserByToken(token);
            User user = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new PracticeException(USER_NOT_FOUND, MessageUtils.getMessage(USER_NOT_FOUND)));

            Optional.ofNullable(request.getFullName()).ifPresent(user::setFullName);
            Optional.ofNullable(request.getPhoneNumber()).ifPresent(user::setPhoneNumber);
            Optional.ofNullable(request.getProfilePicture()).ifPresent(user::setProfilePicture);

            userRepository.save(user);

            return mapToDTO(user);
        } catch (PracticeException e) {
            throw e;
        } catch (Exception e) {
            throw new PracticeException(ERROR_CODE, MessageUtils.getMessage(ERROR_CODE) + ": " + e.getMessage());
        }
    }

    public User getUserFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new PracticeException(400, "Token missing");
        }
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String username = jwtTokenProvider.getUsernameFromToken(token);
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException(USER_404));
        } catch (Exception e) {
            throw new PracticeException(400, "Invalid token");
        }
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getProfilePicture(),
                user.getGoogleProfilePictureUrl(),
                user.getCreatedAt()
        );
    }

}
