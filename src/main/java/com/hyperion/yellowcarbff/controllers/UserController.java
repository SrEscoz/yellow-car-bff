package com.hyperion.yellowcarbff.controllers;

import com.hyperion.yellowcarbff.entities.User;
import com.hyperion.yellowcarbff.model.requests.LoginUserRequest;
import com.hyperion.yellowcarbff.model.requests.RegisterUserRequest;
import com.hyperion.yellowcarbff.model.responses.BasicResponse;
import com.hyperion.yellowcarbff.model.responses.users.UsersResponse;
import com.hyperion.yellowcarbff.model.users.UserDTO;
import com.hyperion.yellowcarbff.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public UsersResponse getUsers() {
        UsersResponse response = new UsersResponse();
        List<User> users = usersService.getUsers();
        List<UserDTO> usersDTO = users
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        response.setUsers(usersDTO);

        return response;
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginUserRequest request) {
        User user = usersService.login(request);
        return modelMapper.map(user, UserDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BasicResponse registerUser(@RequestBody RegisterUserRequest request) {
        return usersService.registerUser(request);
    }

    @GetMapping("/token")
    public BasicResponse generateNewToken(@RequestParam("email") String email) {
        return usersService.generateNewToken(email);
    }

    @GetMapping("/verify")
    public BasicResponse verifyEmail(@RequestParam("token") String token) {
        return usersService.verifyEmail(token);
    }
}
