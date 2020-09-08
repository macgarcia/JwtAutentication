package com.macgarcia.senhasegura.dto.out;

import com.macgarcia.senhasegura.entity.Credencial;

import java.io.Serializable;

public class CredencialDtoSaida implements Serializable {

    private Long id;
    private String url;
    private String nomeDeUsuario;
    private String senha;
    private String nomeAplicacao;

    @Deprecated
    public CredencialDtoSaida(Long id) {
    }

    public CredencialDtoSaida(Credencial credencial) {
        this.id = credencial.getId();
        this.url = credencial.getUrl();
        this.nomeDeUsuario = credencial.getNomeDeUsuario();
        this.senha = credencial.getSenha();
        this.nomeAplicacao = credencial.getNomeAplicacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getnomeDeUsuario() {
        return nomeDeUsuario;
    }

    public void setnomeDeUsuario(String nomeDeUsuario) {
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
}
