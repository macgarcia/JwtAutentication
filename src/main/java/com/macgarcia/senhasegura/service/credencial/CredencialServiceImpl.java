package com.macgarcia.senhasegura.service.credencial;

import com.macgarcia.senhasegura.dto.in.CredencialDtoEntrada;
import com.macgarcia.senhasegura.dto.out.CredencialDtoSaida;
import com.macgarcia.senhasegura.entity.Credencial;
import com.macgarcia.senhasegura.entity.Usuario;
import com.macgarcia.senhasegura.repository.CredencialRepository;
import com.macgarcia.senhasegura.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CredencialServiceImpl implements CredencialService {

    private String msgErro;
    private CredencialRepository dao;
    private UsuarioRepository usuarioRepository;
    private Validator validator;

    @Autowired
    public CredencialServiceImpl(CredencialRepository dao, UsuarioRepository usuarioRepository, Validator validator) {
        this.dao = dao;
        this.usuarioRepository = usuarioRepository;
        this.validator = validator;
    }

    @Override
    public String getMsgErro() {
        return this.msgErro;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean salvarCredencial(CredencialDtoEntrada dto) {
        try {
            var buscaUsuario = this.buscarUsuarioParaCredencial(usuarioRepository, dto.getIdUsuario());
            var novaCredencial = dto.criar(buscaUsuario);
            dao.saveAndFlush(novaCredencial);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean atualizarCredencial(Long id, CredencialDtoEntrada dto) {
        try {
            var credencialAtual = dao.findById(id).get();
            dto.atualizarCredencial(credencialAtual);
            dao.saveAndFlush(credencialAtual);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean excluirCredencial(Long id) {
        try {
            dao.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CredencialDtoSaida buscarUnica(Long id) {
        var credencial = dao.findById(id).get();
        return new CredencialDtoSaida(credencial);
    }

    @Override
    public Page<CredencialDtoEntrada> buscarTodasCredenciaisDoUsuario(Long idUsuario, Pageable pageable) {
        if (idUsuario <= 0 || idUsuario == null) {
            return Page.empty();
        }
        var dados = dao.buscarTodos(idUsuario, pageable);
        var saida = dados.stream().map(CredencialDtoSaida::new).collect(Collectors.toList());
        return new PageImpl(saida, pageable, dados.getTotalElements());
    }

    @Override
    public boolean validarDados(CredencialDtoEntrada dto) {
        var erros = validator.validate(dto);
        if (!erros.isEmpty()) {
            this.msgErro = erros.stream()
                    .map(erro -> erro.getPropertyPath().toString().toUpperCase() + ": " + erro.getMessageTemplate())
                    .collect(Collectors.joining("\n"));
            return false;
        }
        return true;
    }

    @Override
    public boolean verificarExistencia(Long id) {
        return dao.existsById(id);
    }

    private Function<Credencial, Usuario> buscarUsuarioParaCredencial(UsuarioRepository usuarioRepository, Long id) {
        return (credencial) -> {
            var usuario = usuarioRepository.findById(id);
            if (usuario.isEmpty()) {
                return null;
            }
            return usuario.get();
        };
    }
}
