package com.practice.server.application.model.entity;

import com.practice.server.application.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(columnDefinition = "bytea")
    private byte[] profilePicture;

    @Column(name = "google_profile_picture_url")
    private String googleProfilePictureUrl;

    @Column()
    private String provider;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
