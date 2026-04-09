package com.whatfitsnow.whatfitsnowbackend.user;

import com.whatfitsnow.whatfitsnowbackend.user.vo.UserEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(UserEmail email);

  boolean existsByEmail(UserEmail email);
}

