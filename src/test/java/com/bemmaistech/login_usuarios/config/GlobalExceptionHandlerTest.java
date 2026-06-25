package com.bemmaistech.login_usuarios.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    void handleRuntimeException_deveRetornarStatus400() {
        RuntimeException exception = new RuntimeException("Usuário não encontrado");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleRuntimeException_deveConterMensagem() {
        RuntimeException exception = new RuntimeException("Usuário não encontrado");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(exception);

        assertNotNull(response.getBody());
        assertEquals("Usuário não encontrado", response.getBody().get("mensagem"));
    }

    @Test
    void handleRuntimeException_deveConterStatus400NoBody() {
        RuntimeException exception = new RuntimeException("Email já cadastrado");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(exception);

        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    void handleRuntimeException_deveConterTimestamp() {
        RuntimeException exception = new RuntimeException("Erro qualquer");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(exception);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().get("timestamp"));
        assertTrue(response.getBody().get("timestamp") instanceof String);
    }
}
