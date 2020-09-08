package com.macgarcia.senhasegura.dto.out;

import com.macgarcia.senhasegura.entity.Usuario;

import java.io.Serializable;

public class UsuarioDtoSaida implements Serializable {

    private Long id;
    private String login;
    private String nome;

    @Deprecated
    public UsuarioDtoSaida() {}

    public UsuarioDtoSaida(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.nome = usuario.getNome();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
