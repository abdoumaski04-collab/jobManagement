package com.abdo.JobManagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class registerrequest {

    @NotBlank(message = "email obligatoire")
    @Email(message = "email invalide")
    @Size(max = 255, message = "ne pas dépasser 255 caractères")
    private String email;

    @NotBlank(message = "mot de passe obligatoire")
    @Size(max = 255, message = "ne pas dépasser 255 caractères")
    private String password;

    @NotBlank(message = "firstname obligatoire")
    @Size(max = 100, message = "ne pas dépasser 100 caractères")
    private String firstname;

    @NotBlank(message = "lastname obligatoire")
    @Size(max = 100, message = "ne pas dépasser 100 caractères")
    private String lastname;

    @NotNull(message = "role obligatoire")
    private RegisterRole role;

    public enum RegisterRole {
        RECRUITER,
        CANDIDATE,
        ADMIN
    }
}
