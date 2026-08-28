package com.abdo.JobManagement.services;


import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.repositories.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CvService {



    @Value("${app.cv.upload-dir}")
    private String uploadDir;
    private final CandidateProfileRepository repo;


    public String storeCv(MultipartFile file){
        if(file.isEmpty()){
            throw new IllegalArgumentException("le fichier cv est vide");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("seuls les fichiers pdf sont autorisés");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = UUID.randomUUID() + ".pdf";
            Path targetPath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), targetPath);

            return targetPath.toString();
        }
        catch(IOException e){
            throw new RuntimeException("erreur de enregistrement du cv ", e);
        }
    }

    public String uploadcv(MultipartFile file, User user){
        Long userId=user.getId();
        Condidateprofile candidat=repo.findById(userId).orElseThrow(()->new RessourceNotFoundException("candidate not found"));

        String oldcvurl=candidat.getCvurl();
        if(oldcvurl==null){
            candidat.setCvurl(storeCv(file));
        }
        else{
            deleteCv(oldcvurl);
            candidat.setCvurl(storeCv(file));
        }
        repo.save(candidat);
        return candidat.getCvurl();
    }

    public void deleteCv(String cvUrl){
        try{
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path path=Paths.get(cvUrl);
            Files.deleteIfExists(path);
        } catch(IOException e){
            System.out.println("impossible de supprimer l ancien cv "+e.getMessage());
        }
    }
}
