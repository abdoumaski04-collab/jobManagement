package com.abdo.JobManagement.dto.CandidateProfile;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class updateCandidateProfileRequest {
    @Size(max = 100)
    private String fistname;

    @Size(max = 100)
    private String lastname;

    @Size(max = 300)
    private String bio;

    @Size(max = 20)
    private String phone;

}
