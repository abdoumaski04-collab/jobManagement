package com.abdo.JobManagement.services;

import com.abdo.JobManagement.dto.CandidateProfile.CandidatePofileResponse;
import com.abdo.JobManagement.dto.CandidateProfile.updateCandidateProfileRequest;
import com.abdo.JobManagement.dto.skill.AddSkillRequest;
import com.abdo.JobManagement.dto.skill.SkillsResponse;
import com.abdo.JobManagement.entities.skill;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.repositories.CandidateProfileRepository;
import com.abdo.JobManagement.repositories.SkillRepository;
import com.abdo.JobManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateProfileService {

    private final CandidateProfileRepository profilerepo;
    private final UserRepository userrepo;
    private final SkillRepository skillrepo;

    public ResponseEntity<CandidatePofileResponse> updateprofile(updateCandidateProfileRequest request, User Currentuser){

        User user=userrepo.findById(Currentuser.getId()).orElseThrow(()->new RessourceNotFoundException("user not found"));
        Condidateprofile profile=profilerepo.findById(Currentuser.getId()).orElseThrow(()->new RessourceNotFoundException("user not found"));
        if(request.getFistname()!=null) user.setFirstname(user.getFirstname());
        if(request.getLastname()!=null) user.setLastname(user.getLastname());

        userrepo.save(user);

        if(request.getBio()!=null) profile.setBio(request.getBio());
        if(request.getPhone()!=null) profile.setPhone(request.getPhone());

        profilerepo.save(profile);
        return ResponseEntity.ok(new CandidatePofileResponse(user.getId(),user.getEmail(),user.getFirstname(),user.getLastname(),profile.getBio(), profile.getPhone()));

    }

    public ResponseEntity<CandidatePofileResponse> getProfile(User Currentuser){
        User user=userrepo.findById(Currentuser.getId()).orElseThrow(()->new RessourceNotFoundException("user not found"));
        Condidateprofile profile=profilerepo.findById(Currentuser.getId()).orElseThrow(()->new RessourceNotFoundException("user not found"));
        return ResponseEntity.ok(new CandidatePofileResponse(user.getId(),user.getEmail(),user.getEmail(),user.getLastname(),profile.getBio(), profile.getPhone()));

    }
    private String normalise(String name){
        return name.trim().toLowerCase().replaceAll("\\s"," ")
                .replaceAll("[éèê]","e")
                .replaceAll("[àáâ]","a")
                .replaceAll("[ôóò]","o");
    }
    skill findorcreateskill(String name){
        String namenormalised=normalise(name);
        Optional<skill> skill=skillrepo.findByName(namenormalised);
        if(skill.isPresent())
            return skill.get();

        skill newskill=new skill(namenormalised);
        return skillrepo.save(newskill);

    }
    public ResponseEntity<SkillsResponse> addskill(AddSkillRequest request,User user){
        Condidateprofile candidate=profilerepo.findById(user.getId()).orElseThrow(()->new RessourceNotFoundException("user not found"));
        skill skillcurrent=findorcreateskill(request.getName());
        candidate.getSkills().add(skillcurrent);
        profilerepo.save(candidate);
        Set<String> skillNames = candidate.getSkills().stream()
                .map(skill::getName)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new SkillsResponse(skillNames));

    }

    public ResponseEntity<SkillsResponse> getskills(User user){
        Condidateprofile candidate=profilerepo.findById(user.getId()).orElseThrow(()->new RessourceNotFoundException("user not found"));
        if(candidate.getSkills().isEmpty())
            throw new RessourceNotFoundException("tou don't have any skill");
        else{
            Set<String> skillNames = candidate.getSkills().stream()
                    .map(skill::getName)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(new SkillsResponse(skillNames));

        }
    }
}
