package com.abdo.JobManagement.dto.skill;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class SkillsResponse {

    private Set<String> skills;
}
