package com.abdo.JobManagement.mapper;

import com.abdo.JobManagement.dto.Admin.CandidateResponse;
import com.abdo.JobManagement.dto.Admin.RecruiterResponse;
import com.abdo.JobManagement.dto.Admin.UserResponse;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;

public class UsersMapper {

    public static UserResponse toResponseuser(User user){
        UserResponse response=new UserResponse(user.getId(), user.getEmail(), user.getFirstname(),user.getLastname(),user.isEnabled());
        return response;
    }

    public static CandidateResponse toResponsecandidate(Condidateprofile candidate){
        CandidateResponse response= new CandidateResponse(candidate.getId(),candidate.getEmail(),candidate.getFirstname(),candidate.getLastname(),candidate.getPhone(),candidate.getBio(),candidate.getrole(),candidate.isEnabled());
        return response;
    }
    public static RecruiterResponse toResponserecruiter(Recruiter recruiter){
        RecruiterResponse response= new RecruiterResponse(recruiter.getId(),recruiter.getEmail(),recruiter.getFirstname(),recruiter.getLastname(),recruiter.getrole(),recruiter.isEnabled());
        return response;
    }
}
