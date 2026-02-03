package com.antonk.gymtracker.entity;

import com.antonk.gymtracker.entity.enums.UnitPreference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "users")
public class User implements UserDetails{

    @Id
    @GeneratedValue
    private UUID userId;
    private String userFirstName;
    private String userLastName;
    private @Column(nullable = false, unique = true) String userLoginId; //email
    private String password;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private UnitPreference unitPreference;//kg, lbs
    private LocalDate lastLoggedIn;
    private boolean dataPublic = false;
    private boolean isThemeDark = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return null;
    }

    @Override
    public String getUsername(){
        return userLoginId;
    }


    public User(String userFirstName,
                String userLastName,
                String userLoginId,
                String password,
                LocalDateTime createdAt,
                UnitPreference unitPreference) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userLoginId = userLoginId;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.unitPreference = UnitPreference.KG;
    }
}
