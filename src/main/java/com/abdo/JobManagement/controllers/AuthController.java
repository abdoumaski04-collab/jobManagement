package com.abdo.JobManagement.controllers;


import com.abdo.JobManagement.dto.auth.authresponse;
import com.abdo.JobManagement.dto.auth.loginrequest;
import com.abdo.JobManagement.dto.auth.registerrequest;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.abdo.JobManagement.config.jwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userrepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final jwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<authresponse> register(@Valid @RequestBody registerrequest request){
        if(userrepo.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("ce compte existe déjà");
        }

        User user;
        if(request.getRole() == registerrequest.RegisterRole.RECRUITER){
            user=new Recruiter();
        }
        else {

            user = new Condidateprofile();
        }
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userrepo.save(user);

        String token=jwtService.generateToken(user.getEmail(),user.getrole().name());
        return ResponseEntity.ok(new authresponse(token,user.getrole().name()));

    }

    @PostMapping("/login")

    public ResponseEntity<authresponse> login(@Valid @RequestBody loginrequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user=userrepo.findByEmail(request.getEmail()).orElseThrow(()->new IllegalArgumentException("user introuvable"));

        String token=jwtService.generateToken(user.getEmail(),user.getrole().name());

        return ResponseEntity.ok(new authresponse(token,user.getrole().name()));
    }
}
