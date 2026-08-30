package com.abdo.JobManagement.dto.Admin;

import com.abdo.JobManagement.entities.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RecruiterResponse {

    private Long id;
    private String email;
    private String fistname;
    private String lastname;
    private Role role;
    public boolean isenabled;


}
