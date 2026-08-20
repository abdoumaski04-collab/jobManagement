package com.abdo.JobManagement.entities.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    @Size(max = 255)
    private String email;

    @Column(nullable = false)
    @Size(max = 255)
    private String password;

    @Column(name = "first_name",nullable = false)
    @Size(max = 100)
    private String fistname;

    @Column(name = "last_name",nullable = false)
    @Size(max = 100)
    private String lastname;

    @Column(nullable = false)
    private boolean enabled=true;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    public abstract Role getrole();
}
