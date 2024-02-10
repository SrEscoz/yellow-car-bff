package com.hyperion.yellowcarbff.services;

import com.hyperion.yellowcarbff.entities.User;
import com.hyperion.yellowcarbff.model.responses.BasicResponse;

public interface MailService {

    BasicResponse testMailService(String email);

    void sendVerificationCode(User user, String token);
}
