package com.macgarcia.senhasegura.entity;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.function.Function;

@Entity
@Table(name = "CREDENCIAL")
public class Credencial extends EntidadeAbstrata {

    @NotEmpty(message = "Url não pode ser nula.")
    @URL(message = "Formato incorreto")
    @Column(name = "URL", nullable = false)
    private String url;

    @NotEmpty(message = "Nome do usuário não pode ser nulo.")
    @Column(name = "NOME_DE_USUARIO", nullable = false)
    private String nomeDeUsuario;

    @NotEmpty(message = "Senha não pode ser nula.")
    @Column(name = "SENHA")
    private String senha;

    @NotEmpty(message = "Nome da aplicação não pode ser nula.")
    @Column(name = "NOME_APLICACAO", nullable = false)
    private String nomeAplicacao;

    @NotNull(message = "Usuário não pode ser nulo.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @Deprecated
    public Credencial() {
    }

    public Credencial(String url, String nomeDeUsuario, String senha, String nomeAplicacao, Function<Credencial, Usuario> buscaUsuario) {
        this.url = url;
        this.nomeDeUsuario = nomeDeUsuario;
        this.senha = senha;
        this.nomeAplicacao = nomeAplicacao;
        this.usuario = buscaUsuario.apply(this);
    }

    //** Para contexto de teste **//
    public Credencial(@NotEmpty(message = "Url não pode ser nula.")
                      @URL(message = "Formato incorreto") String url,
                      @NotEmpty(message = "Nome do usuário não pode ser nula.") String nomeDeUsuario,
                      @NotEmpty(message = "Senha não pode ser nula.") String senha,
                      @NotEmpty(message = "Nome da aplicação não pode ser nula.") String nomeAplicacao,
                      @NotNull(message = "Usuário não pode ser nulo.") Usuario usuario) {
        this.url = url;
        this.nomeDeUsuario = nomeDeUsuario;
        this.senha = senha;
        this.nomeAplicacao = nomeAplicacao;
        this.usuario = usuario;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNomeDeUsuario() {
        return nomeDeUsuario;
    }

    public void setNomeDeUsuario(String nomeDeUsuario) {
        this.nomeDeUsuario = nomeDeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeAplicacao() {
        return nomeAplicacao;
    }

    public void setNomeAplicacao(String nomeAplicacao) {
        this.nomeAplicacao = nomeAplicacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
