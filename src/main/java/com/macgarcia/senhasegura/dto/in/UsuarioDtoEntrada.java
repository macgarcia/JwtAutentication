package com.macgarcia.senhasegura.dto.in;

import com.macgarcia.senhasegura.entity.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class UsuarioDtoEntrada implements Serializable {

    @NotEmpty(message = "Login não pode ser nulo.")
    private String login;

    @NotEmpty(message = "Senha não pode ser nula.")
    private String senha;

    @NotEmpty(message = "Nome não pode ser nulo.")
    private String nome;

    //** Contexto de testes **//
    public UsuarioDtoEntrada(@NotEmpty(message = "Login não pode ser nulo.") String login,
                             @NotEmpty(message = "Senha não pode ser nula.") String senha,
                             @NotEmpty(message = "Nome não pode ser nulo.") String nome) {
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

    public Usuario criar() {
        return new Usuario(login, new BCryptPasswordEncoder().encode(this.senha), nome);
    }

    public Usuario atualizarUsuario(Usuario usuarioAtual) {
        usuarioAtual.setNome(this.nome);
        usuarioAtual.setSenha(new BCryptPasswordEncoder().encode(this.senha));
        return usuarioAtual;
    }
}
