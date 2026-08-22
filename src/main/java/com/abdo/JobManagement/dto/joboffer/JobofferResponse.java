package com.abdo.JobManagement.dto.joboffer;


import com.abdo.JobManagement.entities.jobstatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class JobofferResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String salaryrange;
    private Long companyId;
    private LocalDateTime createdat;
    private jobstatus status;
}
