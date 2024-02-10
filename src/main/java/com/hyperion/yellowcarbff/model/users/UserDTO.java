package com.hyperion.yellowcarbff.model.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private String email;

    private String userName;

    private String password;

    private Boolean active;

    private VerificationTokenDTO verification;
}
