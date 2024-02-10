package com.hyperion.yellowcarbff.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Usuarios")
@Data
public class User {

    @Id
    private String email;

    private String userName;

    private String password;

    private Boolean active = false;

    @OneToOne(mappedBy = "user")
    private VerificationToken token;

}
