package com.macgarcia.senhasegura.controller;

import com.macgarcia.senhasegura.dto.in.UsuarioDtoEntrada;
import com.macgarcia.senhasegura.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1-usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping(path = "/novoUsuario", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> adicionarUsuario(@RequestBody UsuarioDtoEntrada dto) {
        var validou = service.validarDados(dto);
        if (!validou) {
            return this.getBadRequest(service.getMsgErro());
        }
        var salvou = service.salvarUsuario(dto);
        if (!salvou) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar salvar novo usuário.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/user/atualizarUsuario/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> atualizarUsuario(@RequestBody UsuarioDtoEntrada dto,
                                                   @PathVariable("id") Long id,
                                                   Authentication auth) {
        var existe = service.verificarExistencia(id);
        if (!existe) {
            return this.getBadRequest("Identificador inválido");
        }
        var identidade = service.verificarIdentidade(auth.getPrincipal(), id);
        if (!identidade) {
            return this.getBadRequest("Identificador não pertence ao usuário autenticado.");
        }
        var validou = service.validarDados(dto);
        if (!validou) {
            return this.getBadRequest(service.getMsgErro());
        }
        var atualizou = service.atualizarUsuario(dto, id);
        if (!atualizou) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar atualizar os dados.");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(path = "/user/buscarPorLogin/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscarUsuarioPorLogin(@PathVariable("login") String login) {
        if (login == null || login.isEmpty()) {
            return this.getBadRequest("Login inválido.");
        }
        var usuario = service.buscarUsuraioPorLogin(login);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity<Object> excluirUsuario(@PathVariable("id") Long id, Authentication auth) {
        var existe = service.verificarExistencia(id);
        if (!existe) {
            return this.getBadRequest("Não há usuário com o identificador: " + id);
        }
        var identidade = service.verificarIdentidade(auth.getPrincipal(), id);
        if (!identidade) {
            return this.getBadRequest("Idedntificador não pertence ao usuário autenticado.");
        }
        var excluiu = service.excluirUsuario(id);
        if (!excluiu) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar excluir");
        }
        return ResponseEntity.ok().build();
    }

    //**=======================================================================================================**//
    //** Metodos de utilização generica**//

    private ResponseEntity<Object> getBadRequest(String msg) {
        return ResponseEntity.badRequest().body(msg);
    }

}
