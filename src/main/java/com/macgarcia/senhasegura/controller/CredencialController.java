package com.macgarcia.senhasegura.controller;

import com.macgarcia.senhasegura.dto.in.CredencialDtoEntrada;
import com.macgarcia.senhasegura.service.credencial.CredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1-credenciais")
public class CredencialController {

    private CredencialService service;

    @Autowired
    public CredencialController(CredencialService service) {
        this.service = service;
    }

    @PostMapping(path = "/user/salvarCredencial", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> salvarCredencial(@RequestBody CredencialDtoEntrada dto) {
        var validou = service.validarDados(dto);
        if (!validou) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(service.getMsgErro());
        }
        var salvou = service.salvarCredencial(dto);
        if (!salvou) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar salvar os dados.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> atualizarCrecendial(@PathVariable("id") Long id, @RequestBody CredencialDtoEntrada dto) {
        var existe = service.verificarExistencia(id);
        if (!existe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não há credencial com o identificador: " + id);
        }
        var validou = service.validarDados(dto);
        if (!validou) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(service.getMsgErro());
        }
        var atualizou = service.atualizarCredencial(id, dto);
        if (!atualizou) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar atualizar os dados.");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity<Object> excluirCredencial(@PathVariable("id") Long id) {
        var existe = service.verificarExistencia(id);
        if (!existe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não há credencial com o identificador: " + id);
        }
        var excluiu = service.excluirCredencial(id);
        if (!excluiu) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar excluir os dados.");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscarUnicaCredencial(@PathVariable("id") Long id) {
        var existe = service.excluirCredencial(id);
        if (!existe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não há credencial com o identificador: " + id);
        }
        var dados = service.buscarUnica(id);
        return ResponseEntity.status(HttpStatus.OK).body(dados);
    }

    @GetMapping(path = "/user/buscarTodos/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscarTodasCredenciaisDoUsuario(@PathVariable("idUsuario") Long idUsuario,
                                                                  @PageableDefault(page = 0, size = 8) Pageable pageable) {
        var dados = service.buscarTodasCredenciaisDoUsuario(idUsuario, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dados);
    }

}
