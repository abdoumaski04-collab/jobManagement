package com.abdo.JobManagement.dto.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class loginrequest {


    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

}
