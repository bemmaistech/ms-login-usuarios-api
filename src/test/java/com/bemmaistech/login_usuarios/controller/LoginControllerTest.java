package com.bemmaistech.login_usuarios.controller;

import com.bemmaistech.login_usuarios.dto.LoginAlterarSenhaRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateResponseDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginResponseDTO;
import com.bemmaistech.login_usuarios.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService service;

    @InjectMocks
    private LoginController controller;

    private LoginCreateRequestDTO createRequest;
    private LoginRequestDTO loginRequest;
    private LoginAlterarSenhaRequestDTO alterarSenhaRequest;

    @BeforeEach
    void setUp() {
        createRequest = new LoginCreateRequestDTO();
        createRequest.setNome("Cliente Teste");
        createRequest.setTelefone("(11) 99999-9999");
        createRequest.setEmail("cliente@teste.com");
        createRequest.setSenha("Senha@123");

        loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("cliente@teste.com");
        loginRequest.setSenha("Senha@123");

        alterarSenhaRequest = new LoginAlterarSenhaRequestDTO();
        alterarSenhaRequest.setEmail("cliente@teste.com");
        alterarSenhaRequest.setSenha("NovaSenha@123");
    }

    @Test
    void cadastrar_deveRetornarStatus200() {
        LoginCreateResponseDTO responseDTO = new LoginCreateResponseDTO();
        responseDTO.setNome("Cliente Teste");
        responseDTO.setTelefone("(11) 99999-9999");
        responseDTO.setEmail("cliente@teste.com");
        when(service.cadastrar(any(LoginCreateRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<LoginCreateResponseDTO> response = controller.cadastrar(createRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void cadastrar_deveRetornarBodyCorreto() {
        LoginCreateResponseDTO responseDTO = new LoginCreateResponseDTO();
        responseDTO.setNome("Cliente Teste");
        responseDTO.setTelefone("(11) 99999-9999");
        responseDTO.setEmail("cliente@teste.com");
        when(service.cadastrar(any(LoginCreateRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<LoginCreateResponseDTO> response = controller.cadastrar(createRequest);

        assertNotNull(response.getBody());
        assertEquals("cliente@teste.com", response.getBody().getEmail());
    }

    @Test
    void cadastrar_deveChamarServiceUmaVez() {
        when(service.cadastrar(any(LoginCreateRequestDTO.class))).thenReturn(new LoginCreateResponseDTO());

        controller.cadastrar(createRequest);

        verify(service).cadastrar(createRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    void login_deveRetornarStatus200() {
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setEmail("cliente@teste.com");
        responseDTO.setToken("jwt-token");
        when(service.login(any(LoginRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<LoginResponseDTO> response = controller.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void login_deveRetornarBodyCorreto() {
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setEmail("cliente@teste.com");
        responseDTO.setToken("jwt-token");
        when(service.login(any(LoginRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<LoginResponseDTO> response = controller.login(loginRequest);

        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().getToken());
    }

    @Test
    void login_deveChamarServiceUmaVez() {
        when(service.login(any(LoginRequestDTO.class))).thenReturn(new LoginResponseDTO());

        controller.login(loginRequest);

        verify(service).login(loginRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    void alterarSenha_deveRetornarStatus200() {
        when(service.alterarSenha(any(LoginAlterarSenhaRequestDTO.class))).thenReturn("Senha alterada com sucesso!");

        ResponseEntity<String> response = controller.alterarSenha(alterarSenhaRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void alterarSenha_deveRetornarMensagemDeSucesso() {
        when(service.alterarSenha(any(LoginAlterarSenhaRequestDTO.class))).thenReturn("Senha alterada com sucesso!");

        ResponseEntity<String> response = controller.alterarSenha(alterarSenhaRequest);

        assertEquals("Senha alterada com sucesso!", response.getBody());
    }

    @Test
    void alterarSenha_deveChamarServiceUmaVez() {
        when(service.alterarSenha(any(LoginAlterarSenhaRequestDTO.class))).thenReturn("ok");

        controller.alterarSenha(alterarSenhaRequest);

        verify(service).alterarSenha(alterarSenhaRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    void alterarSenha_quandoServiceLancarErro_devePropagarExcecao() {
        doThrow(new RuntimeException("A nova senha não pode ser igual à senha atual"))
                .when(service).alterarSenha(any(LoginAlterarSenhaRequestDTO.class));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> controller.alterarSenha(alterarSenhaRequest));

        assertEquals("A nova senha não pode ser igual à senha atual", exception.getMessage());
        verify(service).alterarSenha(alterarSenhaRequest);
    }
}
