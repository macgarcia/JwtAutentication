package com.macgarcia.senhasegura.service.usuario;

import com.macgarcia.senhasegura.dto.in.UsuarioDtoEntrada;
import com.macgarcia.senhasegura.dto.out.UsuarioDtoSaida;
import com.macgarcia.senhasegura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public UsuarioDtoSaida buscarUnico(Long id) {
        var usuario = dao.findById(id).get();
        return new UsuarioDtoSaida(usuario);
    }

    @Override
    public Page<UsuarioDtoSaida> buscarUsuarios(Pageable pageable) {
        var usuarios = dao.findAll(pageable);
        var dadosSaida = usuarios.stream()
                .map(UsuarioDtoSaida::new).collect(Collectors.toList());
        return new PageImpl(dadosSaida, pageable, usuarios.getTotalElements());
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
    public String getMsgErro() {
        return msgErro;
    }
}
