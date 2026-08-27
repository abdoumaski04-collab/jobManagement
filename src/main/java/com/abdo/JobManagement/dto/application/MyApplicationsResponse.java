package com.abdo.JobManagement.dto.application;


import com.abdo.JobManagement.entities.application;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyApplicationsResponse {

    private List<application> myapplications;
}
