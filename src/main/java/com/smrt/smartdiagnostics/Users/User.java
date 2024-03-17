package com.***REMOVED***.smartdiagnostics.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(unique = true)
    private String email;
    private String name;
    private String surname;
    @Column(unique = true)
    private String username; // if user uses this instead of name/surname
    @Column(unique = true)
    private String password;
}