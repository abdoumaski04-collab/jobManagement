package com.abdo.JobManagement.entities.user;


import com.abdo.JobManagement.entities.skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="candidate_profiles")
@Getter
@Setter
public class Condidateprofile extends User{
    @Size(max = 300)
    private String bio;

    @Size(max = 500)
    @Column(name = "cv_url")
    private String cvurl;

    @Size(max = 20)
    private String phone;

    public Role getrole(){
        return Role.CONDIDATE;
    }

    @ManyToMany
    @JoinTable(
            name="candidate_skill",
            joinColumns =@JoinColumn(name = "CandidateId"),
            inverseJoinColumns=@JoinColumn(name= "skillId")
    )
    private Set<skill> skills=new HashSet<>();
}
