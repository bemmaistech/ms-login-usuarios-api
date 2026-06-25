package com.bemmaistech.login_usuarios.service;

import com.bemmaistech.login_usuarios.dto.LoginAlterarSenhaRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateResponseDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginResponseDTO;
import com.bemmaistech.login_usuarios.mapper.ConfirmacaoEmailMapper;
import com.bemmaistech.login_usuarios.mapper.LoginMapper;
import com.bemmaistech.login_usuarios.model.ConfirmacaoEmail;
import com.bemmaistech.login_usuarios.model.Usuario;
import com.bemmaistech.login_usuarios.repository.ConfirmacaoEmailRepository;
import com.bemmaistech.login_usuarios.repository.LoginRepository;
import com.bemmaistech.login_usuarios.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final LoginRepository repository;

    private final ConfirmacaoEmailRepository confirmacaoEmailRepository;

    private final LoginMapper mapper;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(LoginRepository repository, ConfirmacaoEmailRepository confirmacaoEmailRepository, LoginMapper mapper, ConfirmacaoEmailMapper confirmacaoEmailMapper, JwtService jwtService, PasswordEncoder passwordEncoder, EmailService emailService, LoginRepository repository1, LoginMapper mapper1, ConfirmacaoEmailRepository confirmacaoEmailRepository1) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.confirmacaoEmailRepository = confirmacaoEmailRepository;
    }

    public LoginCreateResponseDTO cadastrar(LoginCreateRequestDTO params) {

        Usuario usuario = repository.findByEmail(params.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado!"));
        ConfirmacaoEmail confirmacaoEmail = confirmacaoEmailRepository.findByEmail(params.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Confirmação de email não encontrada!"));

        if (repository.findByTelefone(params.getTelefone()).isPresent()) {
            throw new RuntimeException("Telefone já cadastrado!");
        }
        usuario.setNome(params.getNome());
        confirmacaoEmail.setConfirmado(false);
        usuario.setTelefone(params.getTelefone());
        usuario.setDataCriacao(java.time.LocalDateTime.now());
        usuario.setSenha(passwordEncoder.encode(params.getSenha()));


        usuario = repository.save(usuario);

        return mapper.toDTO(usuario);
    }

    public LoginResponseDTO login(LoginRequestDTO params) {
        Usuario usuario = repository.findByEmail(params.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não encontrado"));

        boolean senhaValida = passwordEncoder.matches(params.getSenha(), usuario.getSenha());
        if (!senhaValida) {
            throw new RuntimeException("Senha inválida");
        }
        String token = jwtService.gerarToken(usuario);
        return mapper.toLoginResponse(usuario, token);
    }

    public String alterarSenha(LoginAlterarSenhaRequestDTO params) {
        Usuario usuario = repository.findByEmail(params.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        if (passwordEncoder.matches(params.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("A nova senha não pode ser igual à senha atual");
        }

        usuario.setSenha(passwordEncoder.encode(params.getSenha()));
        repository.save(usuario);

        return "Senha alterada com sucesso!";
    }
}
