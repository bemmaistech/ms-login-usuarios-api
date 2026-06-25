package com.bemmaistech.login_usuarios.service;

import com.bemmaistech.login_usuarios.dto.confirmaremail.ConfirmarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.dto.confirmaremail.EnviarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.mapper.ConfirmacaoEmailMapper;
import com.bemmaistech.login_usuarios.model.ConfirmacaoEmail;
import com.bemmaistech.login_usuarios.repository.ConfirmacaoEmailRepository;
import com.bemmaistech.login_usuarios.repository.LoginRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ConfirmarEmailService {

    private final EmailService service;

    private final ConfirmacaoEmailRepository repository;

    private final LoginRepository loginRepository;

    private final ConfirmacaoEmailMapper mapper;

    public ConfirmarEmailService(EmailService emailService, ConfirmacaoEmailRepository rpository, LoginRepository loginRepository, ConfirmacaoEmailMapper mapper) {
        this.service = emailService;
        this.repository = rpository;
        this.loginRepository = loginRepository;
        this.mapper = mapper;
    }

    public String enviarCodigo(EnviarCodigoRequestDTO dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado!");
        }

        String codigo = String.format("%06d", new Random().nextInt(1000000));

        LocalDateTime expiraEm = LocalDateTime.now().plusMinutes(10);

        LocalDateTime dataCriacao = LocalDateTime.now();


        ConfirmacaoEmail confirmacaoEmail = mapper.toEntity(dto.getEmail(), codigo, expiraEm, dataCriacao);

        repository.save(confirmacaoEmail);

        service.enviarCodigoConfirmacao(dto.getEmail(), codigo);

        return "Email enviado com sucesso!";
    }

    public String confirmarCodigo(ConfirmarCodigoRequestDTO dto){
        ConfirmacaoEmail confirmacaoEmail = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não encontrado!"));

        validacaoCodigoEDataExpiracao(confirmacaoEmail, dto.getCodigo(), dto);
        confirmacaoEmail.setConfirmado(true);
        loginRepository.save(mapper.toUsuario(confirmacaoEmail));
        repository.save(confirmacaoEmail);

        return "Email confirmado com sucesso!";
    }

    private void validacaoCodigoEDataExpiracao(ConfirmacaoEmail confirmacaoEmail, String codigo, ConfirmarCodigoRequestDTO dto) {
        if (!confirmacaoEmail.getCodigo().equals(dto.getCodigo())) {
            throw new RuntimeException("Código inválido!");
        }

        if (confirmacaoEmail.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado!");
        }
    }

    public String enviarEmailRecuperacao(EnviarCodigoRequestDTO params) {
        ConfirmacaoEmail confirmacaoEmail = repository.findByEmail(params.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não encontrado!"));

        String codigo = String.format("%06d", new Random().nextInt(1000000));

        LocalDateTime expiraEm = LocalDateTime.now().plusMinutes(10);

        confirmacaoEmail.setCodigo(codigo);
        confirmacaoEmail.setExpiraEm(expiraEm);
        confirmacaoEmail.setConfirmado(false);

        repository.save(confirmacaoEmail);

        service.enviarCodigoConfirmacao(params.getEmail(), codigo);

        return "Email de recuperação enviado com sucesso!";
    }

}
