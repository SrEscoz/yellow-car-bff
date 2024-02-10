package com.hyperion.yellowcarbff.model.responses.users;

import com.hyperion.yellowcarbff.model.users.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class UsersResponse {

    private List<UserDTO> users;

}
