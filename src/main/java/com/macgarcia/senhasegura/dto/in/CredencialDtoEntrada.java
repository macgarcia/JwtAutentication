package com.macgarcia.senhasegura.dto.in;

import com.macgarcia.senhasegura.entity.Credencial;
import com.macgarcia.senhasegura.entity.Usuario;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.function.Function;

public class CredencialDtoEntrada implements Serializable {

    @NotEmpty(message = "Url não pode ser nula.")
    @URL(message = "Formato incorreto.")
    private String url;

    @NotEmpty(message = "Nome do usuário não pode ser nulo.")
    private String nomeDeUsuario;

    @NotEmpty(message = "Senha não pode ser nula.")
    private String senha;

    @NotEmpty(message = "Nome da aplicação não pode ser nula.")
    private String nomeAplicacao;

    @Positive(message = "Identificador inválido.")
    @NotEmpty(message = "Identificador do usuário não pode ser nulo.")
    private Long idUsuario;

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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Credencial criar(Function<Credencial, Usuario> buscaUsuario) {
        return new Credencial(url, nomeDeUsuario, senha, nomeAplicacao, buscaUsuario);
    }

    public Credencial atualizarCredencial(Credencial credencialAtual) {
        credencialAtual.setUrl(this.url);
        credencialAtual.setNomeDeUsuario(this.nomeDeUsuario);
        credencialAtual.setSenha(this.senha);
        credencialAtual.setNomeAplicacao(this.nomeAplicacao);
        return credencialAtual;
    }
}
