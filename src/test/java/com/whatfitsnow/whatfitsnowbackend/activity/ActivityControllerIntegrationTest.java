package com.whatfitsnow.whatfitsnowbackend.activity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class ActivityControllerIntegrationTest {

  @Autowired
  private WebApplicationContext context;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    this.mvc = webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void activity_endpoints_require_auth_and_enforce_ownership() throws Exception {
    mvc.perform(get("/api/activities"))
        .andExpect(status().is4xxClientError());

    String token1 = registerAndGetToken("act1@example.com");
    String token2 = registerAndGetToken("act2@example.com");

    String createdJson = mvc.perform(post("/api/activities")
            .header("Authorization", "Bearer " + token1)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "title":"Walk",
                  "description":null,
                  "durationMinutes":10,
                  "effortLevel":"LOW",
                  "locationType":"OUTDOOR",
                  "socialType":"ALONE",
                  "weatherCompatibility":"ANY",
                  "healthCompatibility":"ANY",
                  "isActive":true,
                  "tags":["fresh air"]
                }
                """))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    JsonNode created = objectMapper.readTree(createdJson);
    long id = created.get("id").asLong();
    assertThat(id).isPositive();

    // other user can't access it (we intentionally respond 404 for non-owned)
    mvc.perform(get("/api/activities/" + id).header("Authorization", "Bearer " + token2))
        .andExpect(status().isNotFound());
  }

  private String registerAndGetToken(String email) throws Exception {
    String json = mvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"%s","password":"password-123"}
                """.formatted(email)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    return objectMapper.readTree(json).get("accessToken").asText();
  }
}

