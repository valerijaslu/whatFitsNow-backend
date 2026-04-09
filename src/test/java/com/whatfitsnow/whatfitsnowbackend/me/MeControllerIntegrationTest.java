package com.whatfitsnow.whatfitsnowbackend.me;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class MeControllerIntegrationTest {

  @Autowired
  private WebApplicationContext context;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    this.mvc = webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void me_requires_auth_and_returns_basic_info() throws Exception {
    mvc.perform(get("/api/me"))
        .andExpect(status().is4xxClientError());

    String email = "me@example.com";
    String registerJson = mvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"%s","password":"password-123"}
                """.formatted(email)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    JsonNode reg = objectMapper.readTree(registerJson);
    String token = reg.get("accessToken").asText();

    mvc.perform(get("/api/me").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.email").value(email));
  }
}

