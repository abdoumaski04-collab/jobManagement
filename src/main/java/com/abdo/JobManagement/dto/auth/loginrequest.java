package com.abdo.JobManagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class loginrequest {

    @NotBlank(message = "email obligatoire")
    @Email(message = "email invalide")
    private String email;

    @NotBlank(message = "mot de passe obligatoire")
    private String password;
}
