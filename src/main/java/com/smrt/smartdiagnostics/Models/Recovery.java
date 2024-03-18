package com.***REMOVED***.smartdiagnostics.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Password_recovery")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recovery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String code;
    private LocalDateTime requestCreated;

    public Recovery(String email, String code, LocalDateTime now) {
        this.email = email;
        this.code = code;
        this.requestCreated = now;
    }
}
