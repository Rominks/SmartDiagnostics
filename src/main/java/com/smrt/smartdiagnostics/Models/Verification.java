package com.***REMOVED***.smartdiagnostics.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "unverified_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String code;
    private LocalDateTime registrationDate;

    public Verification(String email, String code, LocalDateTime now) {
        this.email = email;
        this.code = code;
        this.registrationDate = now;
    }
}
