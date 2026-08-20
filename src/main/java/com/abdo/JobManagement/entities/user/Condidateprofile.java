package com.abdo.JobManagement.entities.user;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
}
