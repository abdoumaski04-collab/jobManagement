package com.abdo.JobManagement.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skills")
@NoArgsConstructor
@Getter
public class skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 25)
    private String name;

    public skill(String name){
        this.name=name;
    }

    public static String getName(String name) {
        return name;
    }
}