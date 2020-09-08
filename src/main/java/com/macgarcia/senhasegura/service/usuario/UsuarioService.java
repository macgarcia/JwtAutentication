package com.macgarcia.senhasegura.service.usuario;

import com.macgarcia.senhasegura.dto.in.UsuarioDtoEntrada;
import com.macgarcia.senhasegura.dto.out.UsuarioDtoSaida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    String getMsgErro();

    boolean salvarUsuario(UsuarioDtoEntrada dto);
    boolean atualizarUsuario(UsuarioDtoEntrada dto, Long id);
    boolean excluirUsuario(Long id);

    UsuarioDtoSaida buscarUnico(Long id);
    Page<UsuarioDtoSaida> buscarUsuarios(Pageable pageable);

    boolean validarDados(UsuarioDtoEntrada dto);
    boolean verificarExistencia(Long id);

    UsuarioDtoSaida buscarUsuraioPorLogin(String login);
}
