package com.abdo.JobManagement.entities.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name= "recruiters")
public class Recruiter extends User{

    public Role getrole(){
        return Role.RECRUITER;
    }
}
