package com.curiousfellow.user_authentication.confirmationToken;

import java.time.LocalDateTime;

import com.curiousfellow.user_authentication.domain.AppUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser user;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expiresAt;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime confirmedAt;

    @PrePersist
    void onCreate() {
        this.setCreatedAt(LocalDateTime.now());
        this.setExpiresAt(LocalDateTime.now().plusMinutes(15)); // 15 minutes
        this.setConfirmedAt(null);
    }
}
