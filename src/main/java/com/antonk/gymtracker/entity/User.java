package com.antonk.gymtracker.entity;

import com.antonk.gymtracker.entity.enums.UnitPreference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;
    private String userFirstName;
    private String userLastName;
    private @Column(nullable = false, unique = true) String userLoginId; //email
    private String userPassword;
    private LocalDateTime createdAt;
    private UnitPreference unitPreference = UnitPreference.Kg; //kg, lbs


    public User(String userFirstName,
                String userLastName,
                String userLoginId,
                String password) {
        this.userId = UUID.randomUUID();
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userLoginId = userLoginId;
        this.userPassword = password;
    }
}
