package com.abdo.JobManagement.entities;


import com.abdo.JobManagement.entities.user.Recruiter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "companies")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(nullable = false,length = 255)
    private String description;

    @Column(nullable = false,length = 255)
    private String website;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "owner_id",nullable = false)
    private Recruiter owner;
}
