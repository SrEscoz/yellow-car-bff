package com.hyperion.yellowcarbff.repositories;

import com.hyperion.yellowcarbff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("usersRepository")
public interface UsersRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
}
