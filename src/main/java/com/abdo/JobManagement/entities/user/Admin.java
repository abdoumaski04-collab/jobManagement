package com.abdo.JobManagement.entities.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="admins")
public class Admin extends User{
    public Role getrole(){
        return Role.ADMIN;
    }
}
