package com.abdo.JobManagement.dto.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AddSkillRequest {
    @NotBlank(message = "nom ne doit pas être vide")
    @Size(max = 25,message = "25 caractères à ne pas dépasser ")
    private String name;
}
