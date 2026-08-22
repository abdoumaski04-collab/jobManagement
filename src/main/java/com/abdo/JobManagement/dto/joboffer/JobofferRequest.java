package com.abdo.JobManagement.dto.joboffer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobofferRequest {

    @Size(max = 50)
    @NotBlank(message = "titre obligatoire")
    private String title;

    @Size(max = 3000)
    @NotBlank(message = "description obligatoire")
    private String description;

    @Size(max = 255)
    @NotBlank(message = "location obligatoire")
    private String location;

    @Size(max = 50)
    @NotBlank(message = "salaire obligatoire")
    private String salaryrange;

    @NotNull(message = "company id est obligatoire")
    private Long companyId;

}
