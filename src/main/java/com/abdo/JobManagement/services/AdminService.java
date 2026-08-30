package com.abdo.JobManagement.services;

import com.abdo.JobManagement.dto.Admin.UserResponse;
import com.abdo.JobManagement.dto.company.CompanyResponse;
import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.dto.message.MessageResponse;
import com.abdo.JobManagement.entities.application;
import com.abdo.JobManagement.entities.applicationstatus;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.user.Role;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.mapper.CompanyMapper;
import com.abdo.JobManagement.mapper.JobofferMapper;
import com.abdo.JobManagement.mapper.UsersMapper;
import com.abdo.JobManagement.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userrepo;
    private final AdminRepository adminrepo;
    private final CandidateProfileRepository candidaterepo;
    private final RecruiterRepo recruiterrepo;
    private final CompanyRepository companyrepo;
    private final JobofferRepository offerrepo;
    private final ApplicationRepository apprepo;


    // affichage de all companies
    public Page<CompanyResponse> getAllCompanies(User user, Pageable pageable) {
        Long idAdmin = user.getId();
        adminrepo.findById(idAdmin).orElseThrow(() -> new RessourceNotFoundException("admin not found"));

        Page<company> pages = companyrepo.findAll(pageable);
        if (pages.isEmpty())
            throw new RessourceNotFoundException("any company found");

        return pages.map(CompanyMapper::toResponse);
    }

    @Transactional
    public void rejectAllApplication(List<application> applicationList) {
        for (application a : applicationList) {
            a.setStatus(applicationstatus.REJECTED);
        }
        apprepo.saveAll(applicationList);
    }

    @Transactional
    public void deleteAllJoboffers(List<joboffer> jobOfferList) {
        for (joboffer offer : jobOfferList) {
            Long id = offer.getId();
            List<application> applications = apprepo.findByOfferId(id);
            if (!applications.isEmpty()) {
                rejectAllApplication(applications);
               }
            offerrepo.deleteById(id);
        }
    }

    @Transactional
    public MessageResponse CompanyDelete(User user, Long companyId) {
        Long idAdmin = user.getId();
        adminrepo.findById(idAdmin).orElseThrow(() -> new RessourceNotFoundException("admin not found"));

        List<joboffer> offers = offerrepo.findByCompanyId(companyId);
        if (!offers.isEmpty()) {
            deleteAllJoboffers(offers);
        }
        companyrepo.deleteById(companyId);
        return new MessageResponse("Company supprimée avec ses offres, candidatures associées rejetées.");
    }

    public Page<?> getUser(User user, Pageable pageable, String role) {
        Long idAdmin = user.getId();
        adminrepo.findById(idAdmin).orElseThrow(() -> new RessourceNotFoundException("admin not found"));

        switch (role) {
            case "CANDIDATE":
                return candidaterepo.findAll(pageable).map(UsersMapper::toResponsecandidate);

            case "RECRUITER":
                return recruiterrepo.findAll(pageable).map(UsersMapper::toResponserecruiter);

            case "ALL":
                return userrepo.findAll(pageable).map(UsersMapper::toResponseuser);

            default:
                throw new IllegalArgumentException("invalid type");
        }
    }

    @Transactional
    public MessageResponse deleteUser(User admin, Long idUser) {
        String message;
        Long idAdmin = admin.getId();
        adminrepo.findById(idAdmin).orElseThrow(() -> new RessourceNotFoundException("admin not found"));

        User user = userrepo.findById(idUser).orElseThrow(() -> new RessourceNotFoundException("user not found"));

        if (user.getrole() == Role.CONDIDATE) {
            userrepo.deleteById(idUser);
            message = "Candidat supprimé.";
        } else {
            List<company> list = companyrepo.findByOwnerId(idUser);
            if (!list.isEmpty()) {
                for (company c : list) {
                    CompanyDelete(admin, c.getId());
                }
            }
            userrepo.deleteById(idUser);
            message = "Recruteur supprimé avec ses companies, offres et candidatures rejetées.";
        }
        return new MessageResponse(message);
    }

    @Transactional
    public UserResponse accountActivate(User admin, Long id) {
        adminrepo.findById(admin.getId()).orElseThrow(() -> new RessourceNotFoundException("admin not found"));

        User user = userrepo.findById(id).orElseThrow(() -> new RessourceNotFoundException("user not found"));
        if (user.isEnabled()) {
            throw new IllegalArgumentException("ce compte déjà activé");
        }
        user.setEnabled(true);
        userrepo.save(user);
        return UsersMapper.toResponseuser(user);
    }

    @Transactional
    public UserResponse accountDeactivate(User admin, Long id) {
        adminrepo.findById(admin.getId()).orElseThrow(() -> new RessourceNotFoundException("admin not found"));

        User user = userrepo.findById(id).orElseThrow(() -> new RessourceNotFoundException("user not found"));
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("ce compte déjà désactivé");
        }
        user.setEnabled(false);
        userrepo.save(user);
        return UsersMapper.toResponseuser(user);
    }

    @Transactional
    public Page<JobofferResponse> getAll(User user, Pageable pageable){
        adminrepo.findById(user.getId()).orElseThrow(() -> new RessourceNotFoundException("admin not found"));

        Page<joboffer> response=offerrepo.findAll(pageable);
        return  response.map(JobofferMapper::toResponse);
    }

    @Transactional
    public MessageResponse deleteJoboffer(User user,Long idoffer) {
        adminrepo.findById(user.getId()).orElseThrow(() -> new RessourceNotFoundException("admin not found"));


            List<application> applications = apprepo.findByOfferId(idoffer);
            if (!applications.isEmpty()) {
                rejectAllApplication(applications);
            }
            offerrepo.deleteById(idoffer);
            return new MessageResponse("offre est supprimé et leurs applications sont refusés");
        }
    }
