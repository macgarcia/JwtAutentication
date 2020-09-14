package com.macgarcia.senhasegura.repository;

import com.macgarcia.senhasegura.entity.Usuario;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository dao;

    @Test
    public void teste00_salvar() {
        var usuario = new Usuario("teste", "123", "Teste para salvar");
        dao.saveAndFlush(usuario);
        assertThat(usuario.getId()).isNotNull();
        assertThat(usuario.getNome()).isEqualTo("Teste para salvar");
    }

    @Test
    public void teste01_excluir() {
        var usuario = new Usuario("teste", "123", "Teste para salvar");
        dao.saveAndFlush(usuario);
        dao.delete(usuario);
        assertThat(dao.findByLogin(usuario.getLogin())).isNull();
    }

    @Test
    public void teste02_atualizar() {
        var usuario = new Usuario("teste", "123", "Teste para salvar");
        dao.saveAndFlush(usuario);
        usuario.setNome("teste-atualizado");
        usuario.setLogin("login-teste-atualizado");
        dao.saveAndFlush(usuario);
        var usuarioAtualizado = dao.findById(usuario.getId()).get();
        assertThat(usuarioAtualizado.getNome()).isEqualTo("teste-atualizado");
        assertThat(usuarioAtualizado.getLogin()).isEqualTo("login-teste-atualizado");
    }

    @Test
    public void teste03_buscarPorLogin() {
        var usuario = new Usuario("teste", "123", "Teste para salvar");
        dao.saveAndFlush(usuario);
        var usuarioRecuperado = dao.findByLogin(usuario.getLogin());
        assertThat(usuarioRecuperado.getLogin()).isEqualTo("teste");
    }

    @Test
    public void teste04_naoPodeSalvarSemSenha() {
        var usuario = new Usuario("teste", "", "Teste para salvar");
        var erros = Assertions.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(usuario)).getConstraintViolations();
        var e = erros.stream().findFirst();
        assertThat(e.get().getMessageTemplate()).isEqualTo("Senha não pode ser nula.");
    }

    @Test
    public void teste05_naoPodeSalvarSemLogin() {
        var usuario = new Usuario("", "123", "Teste para salvar");
        var erros = Assertions.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(usuario)).getConstraintViolations();
        var e = erros.stream().findFirst();
        assertThat(e.get().getMessageTemplate()).isEqualTo("Login não pode ser nulo.");
    }

    @Test
    public void teste06_naoPodeSalvarSemNome() {
        var usuario = new Usuario("teste", "123", "");
        var erros = Assertions.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(usuario)).getConstraintViolations();
        var e = erros.stream().findFirst();
        assertThat(e.get().getMessageTemplate()).isEqualTo("Nome não pode ser nulo.");
    }

    @Test
    public void teste07_naoPodeSalvarUsuarioNulo() {
        var usuario = new Usuario(null, null, null);
        var erros = Assertions.assertThrows(ConstraintViolationException.class,
                () -> dao.saveAndFlush(usuario)).getConstraintViolations();
        assertThat(erros.size()).isEqualTo(3);
    }

    @Test
    public void teste08_excluirUsuario() {
        var usuario = new Usuario("teste", "123", "Teste para salvar");
        var usuarioSalvo = dao.saveAndFlush(usuario);
        var id = usuarioSalvo.getId();
        dao.deleteById(id);
        var result = dao.findById(id);
        assertThat(result).isEmpty();
    }
}
