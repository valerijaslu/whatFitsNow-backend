package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
  List<Activity> findAllByUserOrderByUpdatedAtDesc(User user);

  List<Activity> findAllByUserAndIsActiveTrue(User user);

  Optional<Activity> findByIdAndUser(Long id, User user);
}

