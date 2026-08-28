package com.abdo.JobManagement.dto.cv;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Setter
@Getter
@AllArgsConstructor
public class cvResponse {

    private String url;
}
