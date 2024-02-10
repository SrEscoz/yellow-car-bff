package com.hyperion.yellowcarbff.model.users;

import lombok.Data;

import java.util.Date;

@Data
public class VerificationTokenDTO {

    private String token;

    private Date expirationDate;
}
