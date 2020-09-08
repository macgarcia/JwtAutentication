package com.macgarcia.senhasegura.repository;

import com.macgarcia.senhasegura.entity.Credencial;
import com.macgarcia.senhasegura.entity.Usuario;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CredencialRepositoryTest {

    @Autowired
    private CredencialRepository dao;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario getUsuraio() {
        var usuario = new Usuario("teste", "123", "Usuario de teste");
        return usuarioRepository.saveAndFlush(usuario);
    }

    @Test
    public void teste01_salvarCredencial() {
        var usuarioSalvo = this.getUsuraio();
        var credencial = new Credencial("http://www.teste.com.br", "teste", "123", "Aplicação de teste", usuarioSalvo);
        var credencialSalva = dao.saveAndFlush(credencial);
        assertThat(credencialSalva.getId()).isNotNull();
    }

    @Test
    public void teste02_atualizarCredencial() {
        var usuarioSalvo = this.getUsuraio();
        var credencial = new Credencial("http://www.teste.com.br", "teste", "123", "Aplicação de teste", usuarioSalvo);
        var credencialSalva = dao.saveAndFlush(credencial);
        credencialSalva.setUrl("http://www.teste-atualizado.com.br");
        var credencialAtualizada = dao.saveAndFlush(credencialSalva);
        assertThat(credencialAtualizada.getUrl()).isEqualTo("http://www.teste-atualizado.com.br");
    }

    @Test
    public void teste03_naoPodeSalvarCredencialSemUsuario() {
        Usuario u = null;
        var credencial = new Credencial("http://www.teste.com.br", "teste", "123", "Aplicação de teste", u);
        var erros = Assert.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(credencial)).getConstraintViolations();
        var erro = erros.stream().findFirst().get();
        assertThat(erro.getMessageTemplate()).isEqualTo("Usuário não pode ser nulo.");
    }

    @Test
    public void teste04_naoPodeSalvarCredencialSemUrl() {
        Usuario u = null;
        var credencial = new Credencial(null, "teste", "123", "Aplicação de teste", u);
        var erros = Assert.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(credencial)).getConstraintViolations();
        assertThat(erros.size()).isEqualTo(2);
    }

    @Test
    public void teste05_naoPodeSalvarCredencialSemNomeDeUsuario() {
        var usuario = this.getUsuraio();
        var credencial = new Credencial("http://www.teste.com.br", "", "123", "Aplicação de teste", usuario);
        var erros = Assert.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(credencial)).getConstraintViolations();
        var erro = erros.stream().findFirst();
        assertThat(erro.get().getMessageTemplate()).isEqualTo("Nome do usuário não pode ser nulo.");
    }

    @Test
    public void teste06_naoPodeSalvarCredencialSemSenha() {
        var usuario = this.getUsuraio();
        var credencial = new Credencial("http://www.teste.com.br", "teste", "", "Aplicação de teste", usuario);
        var erros = Assert.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(credencial)).getConstraintViolations();
        var erro = erros.stream().findFirst();
        assertThat(erro.get().getMessageTemplate()).isEqualTo("Senha não pode ser nula.");
    }

    @Test
    public void teste07_naoPodeSalvarCredencialSemNomeAplicacao() {
        var usuario = this.getUsuraio();
        var credencial = new Credencial("http://www.teste.com.br", "teste", "123", "", usuario);
        var erros = Assert.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(credencial)).getConstraintViolations();
        var erro = erros.stream().findFirst();
        assertThat(erro.get().getMessageTemplate()).isEqualTo("Nome da aplicação não pode ser nula.");
    }
}
