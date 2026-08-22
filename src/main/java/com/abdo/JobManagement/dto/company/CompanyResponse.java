package com.abdo.JobManagement.dto.company;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyResponse {

    private Long id;
    private String name;
    private String descripton;
    private String website;
    private Long ownerid;

}
