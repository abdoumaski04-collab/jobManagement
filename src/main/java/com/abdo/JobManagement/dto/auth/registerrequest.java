package com.abdo.JobManagement.dto.auth;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class registerrequest {

    @Column(nullable = false,unique = true)
    @Size(max = 255)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(max = 255)
    private String password;

    @Column(name = "first_name",nullable = false)
    @Size(max = 100)
    private String firstname;

    @Column(name = "last_name",nullable = false)
    @Size(max = 100)
    private String lastname;

    @NotNull
    private RegisterRole role;

    public enum RegisterRole{
        RECRUITER,
        CANDIDATE,
        ADMIN
    }
}
