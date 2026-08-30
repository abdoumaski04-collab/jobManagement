package com.abdo.JobManagement.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserResponse {

    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    public boolean isenabled;


}
