package com.bemmaistech.login_usuarios.controller;

import com.bemmaistech.login_usuarios.dto.LoginAlterarSenhaRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.*;
import com.bemmaistech.login_usuarios.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/login")
public class LoginController {

    final LoginService service;


    @Autowired
    public LoginController(LoginService loginService) {
        this.service = loginService;
    }

    @PostMapping("/criar")
    ResponseEntity<LoginCreateResponseDTO> cadastrar(@Valid @RequestBody LoginCreateRequestDTO params){
        return ResponseEntity.ok().body(service.cadastrar(params));
    }

    @PostMapping("/entrar")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO params) {
        return ResponseEntity.ok().body(service.login(params));
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<String> alterarSenha(@RequestBody @Valid LoginAlterarSenhaRequestDTO params) {
        return ResponseEntity.ok().body(service.alterarSenha(params));

    }
}
