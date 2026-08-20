package com.abdo.JobManagement.entities;

import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.Recruiter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "application",
        uniqueConstraints = @UniqueConstraint(columnNames = {"candidat_id", "joboffer_id"})
)
@Entity
public class application {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "condidat_id",nullable = false)
    private Condidateprofile condidat;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "joboffer_id",nullable = false)
    private joboffer offer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private applicationstatus status=applicationstatus.PENDING;

    @Column(nullable = false,name = "applied_at")
    private LocalDateTime appliedat=LocalDateTime.now();

    @Column(nullable = false)
    private String cvsnapshoturl;

    // càd on a pris une decision et la reponse sur application reçu
    public boolean iffinal(){
        return status != applicationstatus.PENDING;
    }
}
