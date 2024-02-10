package com.hyperion.yellowcarbff.services;

import com.hyperion.yellowcarbff.entities.User;
import com.hyperion.yellowcarbff.model.requests.LoginUserRequest;
import com.hyperion.yellowcarbff.model.requests.RegisterUserRequest;
import com.hyperion.yellowcarbff.model.responses.BasicResponse;

import java.util.List;

public interface UsersService {

    List<User> getUsers();

    BasicResponse registerUser(RegisterUserRequest request);

    User login(LoginUserRequest request);

    BasicResponse generateNewToken(String email);

    BasicResponse verifyEmail(String token);

}
