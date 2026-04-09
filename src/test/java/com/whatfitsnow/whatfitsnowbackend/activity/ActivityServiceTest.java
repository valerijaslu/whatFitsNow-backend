package com.whatfitsnow.whatfitsnowbackend.activity;

import com.whatfitsnow.whatfitsnowbackend.activity.api.CreateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.activity.api.UpdateActivityRequest;
import com.whatfitsnow.whatfitsnowbackend.activity.model.EffortLevel;
import com.whatfitsnow.whatfitsnowbackend.activity.model.LocationType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.SocialType;
import com.whatfitsnow.whatfitsnowbackend.activity.model.WeatherCompatibility;
import com.whatfitsnow.whatfitsnowbackend.auth.api.RegisterRequest;
import com.whatfitsnow.whatfitsnowbackend.auth.AuthService;
import com.whatfitsnow.whatfitsnowbackend.common.exception.NotFoundException;
import com.whatfitsnow.whatfitsnowbackend.user.User;
import com.whatfitsnow.whatfitsnowbackend.user.UserRepository;
import com.whatfitsnow.whatfitsnowbackend.user.vo.UserEmail;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ActivityServiceTest {

  @Autowired
  private ActivityService activityService;

  @Autowired
  private AuthService authService;

  @Autowired
  private UserRepository userRepository;

  @Test
  void create_get_update_list_delete_own_activity() {
    authService.register(new RegisterRequest("u1@example.com", "password-123"));
    User user = userRepository.findByEmail(UserEmail.of("u1@example.com")).orElseThrow();

    var created = activityService.create(user.getId(), new CreateActivityRequest(
        "Read a book",
        "20 minutes of reading",
        20,
        EffortLevel.LOW,
        4,
        5,
        LocationType.INDOOR,
        SocialType.ALONE,
        WeatherCompatibility.ANY,
        1,
        3,
        2,
        true,
        List.of("relax", "learning")
    ));

    var fetched = activityService.getById(user.getId(), created.id());
    assertThat(fetched.title()).isEqualTo("Read a book");
    assertThat(fetched.tags()).containsExactlyInAnyOrder("learning", "relax");

    var updated = activityService.update(user.getId(), created.id(), new UpdateActivityRequest(
        "Read a novel",
        null,
        30,
        EffortLevel.LOW,
        5,
        5,
        LocationType.INDOOR,
        SocialType.ALONE,
        WeatherCompatibility.ANY,
        1,
        4,
        2,
        true,
        List.of("relax")
    ));

    assertThat(updated.title()).isEqualTo("Read a novel");
    assertThat(updated.durationMinutes()).isEqualTo(30);
    assertThat(updated.tags()).containsExactly("relax");

    var list = activityService.list(user.getId());
    assertThat(list).hasSize(1);

    activityService.delete(user.getId(), created.id());
    assertThatThrownBy(() -> activityService.getById(user.getId(), created.id()))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void user_cannot_access_other_users_activity() {
    authService.register(new RegisterRequest("u2@example.com", "password-123"));
    authService.register(new RegisterRequest("u3@example.com", "password-123"));

    User u2 = userRepository.findByEmail(UserEmail.of("u2@example.com")).orElseThrow();
    User u3 = userRepository.findByEmail(UserEmail.of("u3@example.com")).orElseThrow();

    var created = activityService.create(u2.getId(), new CreateActivityRequest(
        "Jogging",
        null,
        15,
        EffortLevel.MEDIUM,
        4,
        4,
        LocationType.OUTDOOR,
        SocialType.ALONE,
        WeatherCompatibility.SUNNY,
        2,
        4,
        2,
        true,
        List.of("fitness")
    ));

    assertThatThrownBy(() -> activityService.getById(u3.getId(), created.id()))
        .isInstanceOf(NotFoundException.class);
    assertThatThrownBy(() -> activityService.delete(u3.getId(), created.id()))
        .isInstanceOf(NotFoundException.class);
  }
}

