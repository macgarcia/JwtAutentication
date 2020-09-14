package com.macgarcia.senhasegura.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macgarcia.senhasegura.dto.out.UsuarioDtoSaida;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CredencialControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @WithMockUser
    @Test
    public void teste01_salvarCredencial() throws Exception {
        this.salvarNovoUsuario();
        var token = this.autenticarUsuario();
        var usuario = this.buscarUsuario(token);
        var map = Map.of("url", "http://teste.com.br",
                "nomeDeUsuario", "teste-01",
                "senha", "senha-01",
                "nomeAplicacao", "Facebook",
                "idUsuario", usuario.getId().toString());
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/v1-credenciais/user/salvarCredencial")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isCreated()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(201);

    }

    private void salvarNovoUsuario() throws Exception {
        var map = Map.of("login", "teste-spring", "senha", "123", "nome", "Teste usuario spring");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/v1-usuarios/novoUsuario")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isCreated()).andReturn();
    }

    private String autenticarUsuario() throws Exception {
        var map = Map.of("login", "teste-spring", "senha", "123");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk()).andReturn();
        return result.getResponse().getContentAsString();
    }

    private UsuarioDtoSaida buscarUsuario(String token) throws Exception {
        var result = mockMvc.perform(get("/v1-usuarios/user/buscarPorLogin/teste-spring")
                .header("Authorization", token))
                .andExpect(status().isOk()).andReturn();
        return mapper.readValue(result.getResponse().getContentAsString(), UsuarioDtoSaida.class);
    }

    @WithMockUser
    @Test
    public void teste02_noaPodeSalvarCredencialSemUrl()  throws Exception {
        var token = this.autenticarUsuario();
        var usuario = this.buscarUsuario(token);
        var map = Map.of("url", "",
                "nomeDeUsuario", "teste-01",
                "senha", "senha-01",
                "nomeAplicacao", "Facebook",
                "idUsuario", usuario.getId().toString());
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/v1-credenciais/user/salvarCredencial")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest()).andReturn();
        String msg = result.getResponse().getContentAsString();
        assertThat(msg).contains("Url não pode ser nula.");
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @WithMockUser
    @Test
    public void teste03_naoPodeSalvarCredencialSemNomeDeUsuario() throws Exception {
        var token = this.autenticarUsuario();
        var usuario = this.buscarUsuario(token);
        var map = Map.of("url", "http://www.teste.com.br",
                "nomeDeUsuario", "",
                "senha", "senha-01",
                "nomeAplicacao", "Facebook",
                "idUsuario", usuario.getId().toString());
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/v1-credenciais/user/salvarCredencial")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest()).andReturn();
        String msg = result.getResponse().getContentAsString();
        assertThat(msg).contains("Nome do usuário não pode ser nulo.");
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

}
