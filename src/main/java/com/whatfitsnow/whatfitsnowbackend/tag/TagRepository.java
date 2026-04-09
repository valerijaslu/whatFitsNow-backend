package com.whatfitsnow.whatfitsnowbackend.tag;

import com.whatfitsnow.whatfitsnowbackend.tag.vo.TagName;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
  List<Tag> findAllByUserOrderByUpdatedAtDesc(User user);

  Optional<Tag> findByIdAndUser(Long id, User user);

  Optional<Tag> findByUserAndName(User user, TagName name);

  boolean existsByUserAndName(User user, TagName name);
}

