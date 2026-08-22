package com.abdo.JobManagement.dto.company;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyRequest {

    @NotBlank(message = "le nom est obligatoire")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "la description est obligatoire")
    @Size(max = 255)
    private String description;

    @NotBlank(message = "le site web est obligatoire")
    @Size(max = 255)
    private String website;

    //owner id va être recupéré depuis JWT
}
