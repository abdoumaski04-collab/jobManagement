package com.abdo.JobManagement.dto.application;

import com.abdo.JobManagement.entities.applicationstatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ApplicationResponse {

    private Long id;
    private Long candidateId;
    private Long JobofferId;
    private String JobofferTitle;
    private LocalDateTime appliedat;
    private applicationstatus status;

}
