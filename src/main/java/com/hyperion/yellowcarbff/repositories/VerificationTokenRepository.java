package com.hyperion.yellowcarbff.repositories;

import com.hyperion.yellowcarbff.entities.User;
import com.hyperion.yellowcarbff.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
