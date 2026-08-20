package com.abdo.JobManagement.entities;

import jakarta.persistence.*;

@Table(name = "skills")
@Entity
public class skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 25)
    private String name;
}
