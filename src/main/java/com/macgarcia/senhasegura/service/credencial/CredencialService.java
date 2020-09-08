package com.macgarcia.senhasegura.service.credencial;

import com.macgarcia.senhasegura.dto.in.CredencialDtoEntrada;
import com.macgarcia.senhasegura.dto.out.CredencialDtoSaida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CredencialService {

    String getMsgErro();

    boolean salvarCredencial(CredencialDtoEntrada dto);
    boolean atualizarCredencial(Long id, CredencialDtoEntrada dto);
    boolean excluirCredencial(Long id);

    CredencialDtoSaida buscarUnica(Long id);
    Page<CredencialDtoEntrada> buscarTodasCredenciaisDoUsuario(Long idUsuario, Pageable pageable);

    boolean validarDados(CredencialDtoEntrada dto);
    boolean verificarExistencia(Long id);

}
