package com.milansomyk.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Entity
@NoArgsConstructor
@EnableAutoConfiguration
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private int phone;
    private String role;

    public User(int userId, String username, String password, String email, int phone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
    public User(int userId, String username, String email, int phone){
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

}
