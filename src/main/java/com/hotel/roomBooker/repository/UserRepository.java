package com.hotel.roomBooker.repository;

import com.hotel.roomBooker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName (String userName);
    boolean existsByUserNameOrEmail(String userName, String email);

    boolean existsByUserName(String admin);

}
