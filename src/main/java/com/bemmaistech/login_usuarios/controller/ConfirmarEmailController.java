package com.bemmaistech.login_usuarios.controller;

import com.bemmaistech.login_usuarios.dto.confirmaremail.ConfirmarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.dto.confirmaremail.EnviarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.service.ConfirmarEmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirmar-email")
public class ConfirmarEmailController {

    final ConfirmarEmailService service;

    public ConfirmarEmailController(ConfirmarEmailService service) {
        this.service = service;
    }

    @PostMapping("/enviar-codigo")
    public ResponseEntity<String> enviarCodigo(@Valid @RequestBody EnviarCodigoRequestDTO dto) {

        return ResponseEntity.ok().body(service.enviarCodigo(dto));
    }

    @PostMapping("/confirmar-codigo")
    public ResponseEntity<String> confirmarCodigo(@Valid @RequestBody ConfirmarCodigoRequestDTO dto){
        return ResponseEntity.ok().body(service.confirmarCodigo(dto));
    }

    @PostMapping("/enviar-email-recuperacao")
    public ResponseEntity<String> enviarEmailRecuperacao(@RequestBody @Valid EnviarCodigoRequestDTO params) {
        return ResponseEntity.ok(service.enviarEmailRecuperacao(params));
    }
}
