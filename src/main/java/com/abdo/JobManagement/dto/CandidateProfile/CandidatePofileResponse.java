package com.abdo.JobManagement.dto.CandidateProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CandidatePofileResponse {

    private Long id;
    private String email;
    private String fistname;
    private String lastname;
    private String bio;
    private String phone;
}
