package com.bemmaistech.login_usuarios.mapper;

import com.bemmaistech.login_usuarios.dto.login.LoginCreateRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateResponseDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginResponseDTO;
import com.bemmaistech.login_usuarios.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoginMapperTest {
    @InjectMocks
    private LoginMapper mapper;

    private LoginCreateRequestDTO createRequest;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        createRequest = new LoginCreateRequestDTO();
        createRequest.setNome("João Silva");
        createRequest.setTelefone("(11) 99999-9999");
        createRequest.setEmail("joao@teste.com");
        createRequest.setSenha("Senha@123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setTelefone("(11) 99999-9999");
        usuario.setEmail("joao@teste.com");
        usuario.setSenha("hash-da-senha");
        usuario.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void toEntity_comDadosValidos_deveCriarUsuario() {
        Usuario result = mapper.toEntity(createRequest, "senha-hash");

        assertNotNull(result);
    }

    @Test
    void toEntity_comDadosValidos_deveMapearNome() {
        Usuario result = mapper.toEntity(createRequest, "senha-hash");

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
    }

    @Test
    void toEntity_comDadosValidos_deveMapearTelefone() {
        Usuario result = mapper.toEntity(createRequest, "senha-hash");

        assertNotNull(result);
        assertEquals("(11) 99999-9999", result.getTelefone());
    }

    @Test
    void toEntity_comDTONulo_deveRetornarNull() {
        Usuario result = mapper.toEntity(null, "senha-hash");

        assertNull(result);
    }

    @Test
    void toEntity_comSenhaNula_deveRetornarNull() {
        Usuario result = mapper.toEntity(createRequest, null);

        assertNull(result);
    }

    @Test
    void toEntity_comSenhaVazia_deveRetornarNull() {
        Usuario result = mapper.toEntity(createRequest, "");

        assertNull(result);
    }

    @Test
    void toDTO_comUsuarioValido_deveCriarResponseDTO() {
        LoginCreateResponseDTO result = mapper.toDTO(usuario);

        assertNotNull(result);
    }

    @Test
    void toDTO_comUsuarioValido_deveMapearEmail() {
        LoginCreateResponseDTO result = mapper.toDTO(usuario);

        assertNotNull(result);
        assertEquals("joao@teste.com", result.getEmail());
    }

    @Test
    void toLoginResponse_comUsuarioEToken_deveCriarResponseDTO() {
        String token = "jwt-token-aqui";
        LoginResponseDTO result = mapper.toLoginResponse(usuario, token);

        assertNotNull(result);
        assertEquals(token, result.getToken());
    }

    @Test
    void toDTO_comUsuarioNulo_deveRetornarNull() {
        LoginCreateResponseDTO result = mapper.toDTO(null);

        assertNull(result);
    }
}
