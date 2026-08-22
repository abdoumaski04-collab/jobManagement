package com.abdo.JobManagement.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "job_offers")
@Entity
@Setter
@Getter
public class joboffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String title;

    @Column(nullable = false,length = 3000)
    private String description;

    @Column(nullable = false,length = 255)
    private String location;

    @Column(name ="salary_range",nullable= false,length = 50)
    private String salaryrange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private jobstatus status=jobstatus.OPEN;

    @Column(name= "created_at",nullable = false)
    private LocalDateTime createdat=LocalDateTime.now();

    public boolean isopen(){
        if (status == jobstatus.OPEN) {
            return true;
        }
        return false;
    }
}
