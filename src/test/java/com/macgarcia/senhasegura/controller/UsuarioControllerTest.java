package com.macgarcia.senhasegura.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macgarcia.senhasegura.dto.in.UsuarioDtoEntrada;
import com.macgarcia.senhasegura.dto.out.UsuarioDtoSaida;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuarioControllerTest {

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
    public void teste00_salvarNovoUsuario() throws Exception {
        var map = Map.of("login", "teste-spring", "senha", "123", "nome", "Teste usuario spring");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/v1-usuarios/novoUsuario")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isCreated()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }

    @WithMockUser
    @Test
    public void teste01_naoPodeSalvarNovoUsuarioDuplicado() throws Exception {
        var map = Map.of("login", "teste-spring", "senha", "123", "nome", "Teste usuario spring");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/v1-usuarios/novoUsuario")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isInternalServerError()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(500);
    }

    @WithMockUser
    @Test
    public void teste02_autenticarNovoUsuario() throws Exception {
        var map = Map.of("login", "teste-spring", "senha", "123");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk()).andReturn();
        var auth = result.getResponse().getContentAsString();
        assertThat(auth).isNotEmpty();
        assertThat(auth.startsWith("Bearer"));
    }

    @WithMockUser
    @Test
    public void teste03_buscarUsuarioPorLogin() throws Exception {
        var token = this.getToken();
        var result = mockMvc.perform(get("/v1-usuarios/user/buscarPorLogin/teste-spring")
                .header("Authorization", token))
                .andExpect(status().isOk()).andReturn();
        var usuario = mapper.readValue(result.getResponse().getContentAsString(), UsuarioDtoSaida.class);
        assertThat(usuario.getId()).isNotNull();
        assertThat(usuario.getLogin()).isNotEmpty();
        assertThat(usuario.getNome()).isNotEmpty();
    }

    private String getToken() throws Exception {
        var map = Map.of("login", "teste-spring", "senha", "123");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk()).andReturn();
        return result.getResponse().getContentAsString();
    }

    @WithMockUser
    @Test
    public void teste04_naoAutorizado() throws Exception {
        var map = Map.of("login", "1", "senha", "1");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isUnauthorized()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }

    @WithMockUser
    @Test
    public void teste05_atualizaUsuario() throws Exception {
        var token = this.getToken();
        var usuarioAtual = this.buscarUsuario(token);
        var usuarioAtualizado = new UsuarioDtoEntrada(usuarioAtual.getLogin(), "123", "Teste usuario spring atualizado");
        var json = mapper.writeValueAsString(usuarioAtualizado);
        var result = mockMvc.perform(put("/v1-usuarios/user/atualizarUsuario/" + usuarioAtual.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    private UsuarioDtoSaida buscarUsuario(String token) throws Exception {
        var result = mockMvc.perform(get("/v1-usuarios/user/buscarPorLogin/teste-spring")
                .header("Authorization", token))
                .andExpect(status().isOk()).andReturn();
        return mapper.readValue(result.getResponse().getContentAsString(), UsuarioDtoSaida.class);
    }

    @WithMockUser
    @Test
    public void teste06_naoPodeAtualizarUsuarioErrado() throws Exception {
        var token = this.getToken();
        var usuarioAtual = this.buscarUsuario(token);
        var usuarioAtualizado = new UsuarioDtoEntrada(usuarioAtual.getLogin(), "123", "Teste usuario spring atualizado");
        var json = mapper.writeValueAsString(usuarioAtualizado);
        var result = mockMvc.perform(put("/v1-usuarios/user/atualizarUsuario/1")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @WithMockUser
    @Test
    public void teste07_naoPodeExcluirUsuarioErrado()throws Exception {
        var token = this.getToken();
        var usuario = this.buscarUsuario(token);
        var result = mockMvc.perform(delete("/v1-usuarios/user/1")
                .header("Authorization", token))
                .andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @WithMockUser
    @Test
    public void teste08_excluirUsuario() throws Exception {
        var token = this.getToken();
        var usuario = this.buscarUsuario(token);
        var result = mockMvc.perform(delete("/v1-usuarios/user/" + usuario.getId())
                .header("Authorization", token))
                .andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @WithMockUser
    @Test
    public void teste09_naoPodeSalvarNovoUsuarioErroNosDados() throws Exception {
        var map = Map.of("login", "teste-spring", "senha", "123", "nome", "");
        var json = mapper.writeValueAsString(map);
        var result = mockMvc.perform(post("/v1-usuarios/novoUsuario")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }
}
