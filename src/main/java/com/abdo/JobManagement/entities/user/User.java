package com.abdo.JobManagement.entities.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    @Size(max = 255)
    private String email;

    @Column(nullable = false)
    @Size(max = 255)
    private String password;

    @Column(name = "first_name",nullable = false)
    @Size(max = 100)
    private String firstname;

    @Column(name = "last_name",nullable = false)
    @Size(max = 100)
    private String lastname;

    @Column(nullable = false)
    private boolean enabled=true;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt=LocalDateTime.now();

    public abstract Role getrole();

    // cette fonction qui affiche les autorisations de user après la cnx selon le role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+getrole().name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    // utiliser pour verfier si le compte est activé ou non m^me si le pwd correcte il peut bloquer accès
    public boolean isEnabled(){
        return enabled;
    }
}
