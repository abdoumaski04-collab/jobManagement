package com.abdo.JobManagement.dto.Admin;

import com.abdo.JobManagement.entities.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CandidateResponse {

    private Long id;
    private String email;
    private String fistname;
    private String lastname;
    private String phone;
    private String bio;
    private Role role;
    private boolean isenabled;

}
