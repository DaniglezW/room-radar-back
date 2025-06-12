package com.practice.server.application.service;

import com.practice.server.application.dto.UserDTO;
import com.practice.server.application.dto.request.LoginRequest;
import com.practice.server.application.dto.request.RegisterRequest;
import com.practice.server.application.exception.PracticeException;
import com.practice.server.application.utils.JwtTokenProvider;
import com.practice.server.application.model.enums.Role;
import com.practice.server.application.model.entity.User;
import com.practice.server.application.service.interfaces.IUserService;
import com.practice.server.application.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserService implements IUserService {

    private static final String USER_404 = "User not found";

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

        User user1 = userRepository.findById(1L).orElse(null);
        user.setProfilePicture(Objects.requireNonNull(user1).getProfilePicture());

        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
    }

    @Override
    public String refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Token de refresco inválido");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        Role role = jwtTokenProvider.getRoleFromToken(refreshToken);

        return jwtTokenProvider.generateToken(username, role);
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

    public User getUserFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new PracticeException(400, "Token missing");
        }
        try {
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
                user.getCreatedAt()
        );
    }

}
