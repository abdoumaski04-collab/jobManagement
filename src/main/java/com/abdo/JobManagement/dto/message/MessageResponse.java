package com.abdo.JobManagement.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.boot.internal.Abstract;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String message;
}
