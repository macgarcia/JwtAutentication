package com.macgarcia.senhasegura.service.usuario;

import com.macgarcia.senhasegura.dto.in.UsuarioDtoEntrada;
import com.macgarcia.senhasegura.dto.out.UsuarioDtoSaida;
import com.macgarcia.senhasegura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validator;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final Validator validator;
    private final UsuarioRepository dao;
    private String msgErro;

    @Autowired
    public UsuarioServiceImpl(Validator validator, UsuarioRepository dao) {
        this.validator = validator;
        this.dao = dao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean salvarUsuario(UsuarioDtoEntrada dto) {
        try {
            var possivelUsuario = dao.findByLogin(dto.getLogin());
            if (possivelUsuario != null) {
                return false;
            }
            var usuario = dto.criar();
            dao.saveAndFlush(usuario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean atualizarUsuario(UsuarioDtoEntrada dto, Long id) {
        try {
            var usuarioAtual = dao.findById(id).get();
            dto.atualizarUsuario(usuarioAtual);
            dao.saveAndFlush(usuarioAtual);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean excluirUsuario(Long id) {
        try {
            dao.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validarDados(UsuarioDtoEntrada dto) {
        var erros = validator.validate(dto);
        if (!erros.isEmpty()) {
            this.msgErro = erros.stream()
                    .map(erro -> erro.getPropertyPath().toString().toUpperCase() + ": " +
                            erro.getMessageTemplate())
                    .collect(Collectors.joining("\n"));
            return false;
        }
        return true;
    }

    @Override
    public boolean verificarExistencia(Long id) {
        return dao.existsById(id);
    }

    @Override
    public UsuarioDtoSaida buscarUsuraioPorLogin(String login) {
        var usuario = dao.findByLogin(login);
        if (usuario == null) {
            return null;
        }
        return new UsuarioDtoSaida(usuario);
    }

    @Override
    public boolean verificarIdentidade(Object user, Long id) {
        var u = (User) user;
        var usuario = dao.findByLogin(((User) user).getUsername());
        if (!(id.equals(usuario.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getMsgErro() {
        return msgErro;
    }
}
