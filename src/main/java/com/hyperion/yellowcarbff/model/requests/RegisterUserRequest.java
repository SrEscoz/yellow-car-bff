package com.hyperion.yellowcarbff.model.requests;

import lombok.Data;

@Data
public class RegisterUserRequest {

    private String email;

    private String userName;

    private String password;

}

