package com.macgarcia.senhasegura.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "USUARIO")
public class Usuario extends EntidadeAbstrata {

    @NotEmpty(message = "Login não pode ser nulo.")
    @Column(name = "LOGIN", nullable = false, unique = true)
    private String login;

    @NotEmpty(message = "Senha não pode ser nula.")
    @Column(name = "SENHA", nullable = false)
    private String senha;

    @NotEmpty(message = "Nome não pode ser nulo.")
    @Column(name = "NOME", nullable = false)
    private String nome;

    @Deprecated
    public Usuario() {}

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
