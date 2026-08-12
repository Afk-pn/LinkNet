package com.linknet.backend.repository;

import com.linknet.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRep extends JpaRepository<User, Integer> {
   
     Optional<User> findByUsername(String username);
      Optional<User> findByEmail(String email);
}
