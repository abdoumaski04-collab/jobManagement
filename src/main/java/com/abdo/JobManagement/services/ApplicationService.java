package com.abdo.JobManagement.services;


import com.abdo.JobManagement.dto.application.ApplicationResponse;
import com.abdo.JobManagement.dto.application.MyApplicationsResponse;
import com.abdo.JobManagement.entities.application;
import com.abdo.JobManagement.entities.applicationstatus;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.exceptions.OperationWontReapeat;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ApplicationService {

private final ApplicationRepository apprepo;
private final CandidateProfileRepository condidaterepo;
private final JobofferRepository jobofferrepo;
private final RecruiterRepo recruiterrepo;
private final EmailService emailser;
private final CompanyRepository companyrepo;


@Transactional
public ApplicationResponse addApplication(User user,Long jobofferId){
    Long userId=user.getId();
    Condidateprofile candidate=condidaterepo.findById(userId).orElseThrow(()->new RessourceNotFoundException("Candidate not found"));
    joboffer offer=jobofferrepo.findById(jobofferId).orElseThrow(()->new RessourceNotFoundException("job offer not found"));
    if(apprepo.existsByCondidatIdAndOfferId(candidate.getId(), jobofferId)){
        throw new OperationWontReapeat("you applied before");
    }

    if(candidate.getCvurl()==null){
        throw new RessourceNotFoundException("you did not upload ur Cv in your profile");
    }



    application newapp=new application(candidate,offer, applicationstatus.PENDING,candidate.getCvurl());

    String joboffertitle=offer.getTitle();
    String candidatename=candidate.getFirstname()+" "+candidate.getLastname();

    Long companyId=offer.getCompany().getId();
    Long RecuiterId=offer.getCompany().getOwner().getId();
    company company=companyrepo.findById(companyId).orElseThrow(()->new RessourceNotFoundException("Company not found"));
    Recruiter recruiter=recruiterrepo.findById(RecuiterId).orElseThrow(()->new RessourceNotFoundException("recruiter not found"));
    apprepo.save(newapp);
    emailser.sendNewApplicatioNotification(recruiter.getEmail(),joboffertitle,candidatename,company.getName());
    ApplicationResponse appresponse= new ApplicationResponse(newapp.getId(), userId, jobofferId, newapp.getOffer().getTitle(), newapp.getAppliedat(), newapp.getStatus());
    return appresponse;
}

@Transactional
public MyApplicationsResponse getMyApps(User user){
    Long userId=user.getId();
    Condidateprofile candidate=condidaterepo.findById(userId).orElseThrow(()->new RessourceNotFoundException("Candidate not found"));

    List<application> myapps=apprepo.findAllByCondidatId(userId);
    if(myapps.isEmpty()){
        throw new RessourceNotFoundException("you don't have any application now go apply");
    }

    MyApplicationsResponse appresponse= new MyApplicationsResponse(myapps);
    return appresponse;
}

@Transactional
public ApplicationResponse annulerApp(User user,Long appId){

    Long userId=user.getId();
    Condidateprofile candidate=condidaterepo.findById(userId).orElseThrow(()->new RessourceNotFoundException("Candidate not found"));

    if(! apprepo.existsByCondidatIdAndId(userId,appId))
        throw new RessourceNotFoundException("we don't have your application");

    application yourapp=apprepo.findById(appId).orElseThrow((()->new RessourceNotFoundException("app not found")));
    if(yourapp.getStatus() != applicationstatus.PENDING){
        throw new OperationWontReapeat("cette candidature a déjà été traitée");
    }

    yourapp.setStatus(applicationstatus.WITHDRAWN);
    apprepo.save(yourapp);
    ApplicationResponse appr= new ApplicationResponse(yourapp.getId(), userId, yourapp.getOffer().getId(), yourapp.getOffer().getTitle(), yourapp.getAppliedat(), yourapp.getStatus());
    return appr;

}

@Transactional
public Page<application> getappspending(Long jobofferid,User user, Pageable pageable){
    Long userId=user.getId();
    Recruiter recruiter=recruiterrepo.findById(userId).orElseThrow(()->new RessourceNotFoundException("Candidate not found"));

    // on cherche si il ya ce offre
    joboffer jobofferx=jobofferrepo.findById(jobofferid).orElseThrow(()->new RessourceNotFoundException("joboffer not found"));
    // on cherche si recruiter et ce lui qui a publier l offre
    if(!jobofferx.getCompany().getOwner().getId().equals(user.getId()))
        throw new AccessDeniedException("yu are not the owner of joboffer");
    // on cherche s il ya des applications pour cet offre
    if(! apprepo.existsByOfferId(jobofferid))
        throw new RessourceNotFoundException("you don't have any application to this job offer");

    Page<application> pages=apprepo.getapps(jobofferid, applicationstatus.PENDING,pageable);
    return pages;
}

    @Transactional
public ApplicationResponse refuseApplication(Long idApp, User user){
    Long userId=user.getId();
    Recruiter recruiter=recruiterrepo.findById(userId).orElseThrow(()->new RessourceNotFoundException("Candidate not found"));

    application app=apprepo.findById(idApp).orElseThrow(()->new RessourceNotFoundException("application not found"));
    joboffer joboffer=jobofferrepo.findById(app.getOffer().getId()).orElseThrow(()->new RessourceNotFoundException("joboffer not found"));
    if(!joboffer.getCompany().getOwner().getId().equals(user.getId()))
        throw new AccessDeniedException("yu are not the owner of joboffer");
        if(app.getStatus() != applicationstatus.PENDING){
            throw new OperationWontReapeat("cette candidature a déjà été traitée");
        }

    app.setStatus(applicationstatus.REJECTED);
    Long condidatId=app.getCondidat().getId();
    Condidateprofile candidat=condidaterepo.findById(condidatId).orElseThrow(()->new RessourceNotFoundException("condidate not found "));
    apprepo.save(app);
    emailser.sendUpdateApllicationStatus(candidat.getEmail(),joboffer.getTitle(),app.getStatus());
     ApplicationResponse appr= new ApplicationResponse(app.getId(), user.getId(), app.getOffer().getId(), app.getOffer().getTitle(), app.getAppliedat(), app.getStatus());
    return appr;
}
    @Transactional
public ApplicationResponse acceptApplication(Long idApp,User user){
    Long userId=user.getId();
    Recruiter recruiter=recruiterrepo.findById(userId).orElseThrow(()->new RessourceNotFoundException("Candidate not found"));

    application app=apprepo.findById(idApp).orElseThrow(()->new RessourceNotFoundException("application not found"));
    joboffer joboffer=jobofferrepo.findById(app.getOffer().getId()).orElseThrow(()->new RessourceNotFoundException("joboffer not found"));
    if(!joboffer.getCompany().getOwner().getId().equals( user.getId()))
        throw new AccessDeniedException("yu are not the owner of joboffer");
        if(app.getStatus() != applicationstatus.PENDING){
            throw new OperationWontReapeat("cette candidature a déjà été traitée");
        }

    app.setStatus(applicationstatus.ACCEPTED);
        Long condidatId=app.getCondidat().getId();
        Condidateprofile candidat=condidaterepo.findById(condidatId).orElseThrow(()->new RessourceNotFoundException("condidate not found "));
        apprepo.save(app);
        emailser.sendUpdateApllicationStatus(candidat.getEmail(),joboffer.getTitle(),app.getStatus());
        ApplicationResponse appr= new ApplicationResponse(app.getId(), user.getId(), app.getOffer().getId(), app.getOffer().getTitle(), app.getAppliedat(), app.getStatus());
    return appr;

}
}
