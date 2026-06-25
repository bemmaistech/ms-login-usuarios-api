package com.bemmaistech.login_usuarios.controller;

import com.bemmaistech.login_usuarios.dto.confirmaremail.ConfirmarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.dto.confirmaremail.EnviarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.service.ConfirmarEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmarEmailControllerTest {

    @Mock
    private ConfirmarEmailService service;

    @InjectMocks
    private ConfirmarEmailController controller;

    private EnviarCodigoRequestDTO enviarCodigoRequest;
    private ConfirmarCodigoRequestDTO confirmarCodigoRequest;

    @BeforeEach
    void setUp() {
        enviarCodigoRequest = new EnviarCodigoRequestDTO();
        enviarCodigoRequest.setEmail("cliente@teste.com");

        confirmarCodigoRequest = new ConfirmarCodigoRequestDTO();
        confirmarCodigoRequest.setEmail("cliente@teste.com");
        confirmarCodigoRequest.setCodigo("123456");
    }

    @Test
    void enviarCodigo_deveRetornarStatus200() {
        when(service.enviarCodigo(any(EnviarCodigoRequestDTO.class))).thenReturn("Email enviado com sucesso!");

        ResponseEntity<String> response = controller.enviarCodigo(enviarCodigoRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void enviarCodigo_deveRetornarMensagemDeSucesso() {
        when(service.enviarCodigo(any(EnviarCodigoRequestDTO.class))).thenReturn("Email enviado com sucesso!");

        ResponseEntity<String> response = controller.enviarCodigo(enviarCodigoRequest);

        assertEquals("Email enviado com sucesso!", response.getBody());
    }

    @Test
    void enviarCodigo_deveChamarServiceUmaVez() {
        when(service.enviarCodigo(any(EnviarCodigoRequestDTO.class))).thenReturn("ok");

        controller.enviarCodigo(enviarCodigoRequest);

        verify(service).enviarCodigo(enviarCodigoRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    void confirmarCodigo_deveRetornarStatus200() {
        when(service.confirmarCodigo(any(ConfirmarCodigoRequestDTO.class))).thenReturn("Email confirmado com sucesso!");

        ResponseEntity<String> response = controller.confirmarCodigo(confirmarCodigoRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void confirmarCodigo_deveRetornarMensagemDeSucesso() {
        when(service.confirmarCodigo(any(ConfirmarCodigoRequestDTO.class))).thenReturn("Email confirmado com sucesso!");

        ResponseEntity<String> response = controller.confirmarCodigo(confirmarCodigoRequest);

        assertEquals("Email confirmado com sucesso!", response.getBody());
    }

    @Test
    void confirmarCodigo_deveChamarServiceUmaVez() {
        when(service.confirmarCodigo(any(ConfirmarCodigoRequestDTO.class))).thenReturn("ok");

        controller.confirmarCodigo(confirmarCodigoRequest);

        verify(service).confirmarCodigo(confirmarCodigoRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    void enviarEmailRecuperacao_deveRetornarStatus200() {
        when(service.enviarEmailRecuperacao(any(EnviarCodigoRequestDTO.class))).thenReturn("Email de recuperação enviado com sucesso!");

        ResponseEntity<String> response = controller.enviarEmailRecuperacao(enviarCodigoRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void enviarEmailRecuperacao_deveRetornarMensagemDeSucesso() {
        when(service.enviarEmailRecuperacao(any(EnviarCodigoRequestDTO.class))).thenReturn("Email de recuperação enviado com sucesso!");

        ResponseEntity<String> response = controller.enviarEmailRecuperacao(enviarCodigoRequest);

        assertEquals("Email de recuperação enviado com sucesso!", response.getBody());
    }

    @Test
    void enviarEmailRecuperacao_deveChamarServiceUmaVez() {
        when(service.enviarEmailRecuperacao(any(EnviarCodigoRequestDTO.class))).thenReturn("ok");

        controller.enviarEmailRecuperacao(enviarCodigoRequest);

        verify(service).enviarEmailRecuperacao(enviarCodigoRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    void enviarEmailRecuperacao_quandoServiceLancarErro_devePropagarExcecao() {
        doThrow(new RuntimeException("Email não encontrado!")).when(service).enviarEmailRecuperacao(any(EnviarCodigoRequestDTO.class));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> controller.enviarEmailRecuperacao(enviarCodigoRequest));

        assertEquals("Email não encontrado!", exception.getMessage());
        verify(service).enviarEmailRecuperacao(enviarCodigoRequest);
    }
}
