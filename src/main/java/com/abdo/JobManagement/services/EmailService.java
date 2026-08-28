package com.abdo.JobManagement.services;


import com.abdo.JobManagement.entities.applicationstatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async("emailTaskExecutor")
    public void sendNewApplicatioNotification(String recruiterEmail,
                                              String JobofferTitle,
                                              String CandidateName,String CompanyName){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(recruiterEmail);
        message.setSubject("nouvelle candidature reçue - "+JobofferTitle);
        message.setText("""
                Bonjour,
                vous avez reçue une nouvelle candidature de %s pour l'offre de %s de votre entreprise %s.
                
                Connecter vous à votre espace recruteur
                
                -jobmanagement
                """.formatted(CandidateName,JobofferTitle,CompanyName));
        send(message);
    }

    @Async("emailTaskExecutor")
    public void sendUpdateApllicationStatus(String candidateEmail,String joboffertitle, applicationstatus status){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(candidateEmail);
        if(status== applicationstatus.ACCEPTED){
            message.setSubject("candidature acceptée");
            message.setText("""
                    Bonjour,
                    
                    Bonjour j'éspère que vous allez bien ,
                    félicitations votre candidature pour l'offre de %s est accepté
                    
                    Consultez votre éspace candidature.
                    """.formatted(joboffertitle));
        }
        else{
            message.setSubject("candidature redusée");
            message.setText("""
                    Bonjour,
                    
                    Bonjour j'éspère que vous allez bien ,
                    Malheureusement votre candidature pour l'offre de %s est refusé
                    
                    Consultez votre éspace candidature.
                    """.formatted(joboffertitle));
        }
        send(message);
    }

    public void send(SimpleMailMessage mssg){
        try{
            mailSender.send(mssg);
            log.info("Email envoyé avec succès à {}", mssg.getTo() != null ? mssg.getTo()[0] : "?");
        }
        catch (Exception e) {

            log.error("Échec de l'envoi d'email à {} : {}", mssg.getTo(), e.getMessage());
        }
    }
}
