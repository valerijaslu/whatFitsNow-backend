package com.whatfitsnow.whatfitsnowbackend.auth;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class AuthControllerIntegrationTest {

  @Autowired
  private WebApplicationContext context;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    this.mvc = webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void register_and_login_return_bearer_token() throws Exception {
    String email = "auth1@example.com";
    String password = "password-123";

    String registerJson = mvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"%s","password":"%s"}
                """.formatted(email, password)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tokenType").value("Bearer"))
        .andExpect(jsonPath("$.accessToken").isString())
        .andReturn()
        .getResponse()
        .getContentAsString();

    JsonNode reg = objectMapper.readTree(registerJson);
    assertThat(reg.get("accessToken").asText()).isNotBlank();

    mvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"email":"%s","password":"%s"}
                """.formatted(email, password)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tokenType").value("Bearer"))
        .andExpect(jsonPath("$.accessToken").isString());
  }

  @Test
  void register_rejects_duplicate_email() throws Exception {
    String body = """
        {"email":"dup@example.com","password":"password-123"}
        """;

    mvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk());

    mvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict());
  }
}

