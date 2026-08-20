package com.abdo.JobManagement.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class authresponse {

    private String token;
    private String role;
}
