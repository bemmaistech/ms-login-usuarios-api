package com.bemmaistech.login_usuarios.mapper;

import com.bemmaistech.login_usuarios.model.ConfirmacaoEmail;
import com.bemmaistech.login_usuarios.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmacaoEmailMapperTest {
    @InjectMocks
    private ConfirmacaoEmailMapper mapper;

    private LocalDateTime dataExpiracao;
    private LocalDateTime dataCriacao;

    @BeforeEach
    void setUp() {
        dataExpiracao = LocalDateTime.now().plusMinutes(10);
        dataCriacao = LocalDateTime.now();
    }

    @Test
    void toEntity_comDadosValidos_deveCriarConfirmacaoEmail() {
        ConfirmacaoEmail result = mapper.toEntity("teste@email.com", "123456", dataExpiracao, dataCriacao);

        assertNotNull(result);
    }

    @Test
    void toEntity_comDadosValidos_deveMapearEmail() {
        ConfirmacaoEmail result = mapper.toEntity("teste@email.com", "123456", dataExpiracao, dataCriacao);

        assertNotNull(result);
        assertEquals("teste@email.com", result.getEmail());
    }

    @Test
    void toEntity_comDadosValidos_deveMapearCodigo() {
        ConfirmacaoEmail result = mapper.toEntity("teste@email.com", "123456", dataExpiracao, dataCriacao);

        assertNotNull(result);
        assertEquals("123456", result.getCodigo());
    }

    @Test
    void toEntity_comEmailNulo_deveRetornarNull() {
        ConfirmacaoEmail result = mapper.toEntity(null, "123456", dataExpiracao, dataCriacao);

        assertNull(result);
    }

    @Test
    void toEntity_comCodigoNulo_deveRetornarNull() {
        ConfirmacaoEmail result = mapper.toEntity("teste@email.com", null, dataExpiracao, dataCriacao);

        assertNull(result);
    }

    @Test
    void toEntity_comDataExpiracaoNula_deveRetornarNull() {
        ConfirmacaoEmail result = mapper.toEntity("teste@email.com", "123456", null, dataCriacao);

        assertNull(result);
    }

    @Test
    void toUsuario_comDadosValidos_deveCriarUsuario() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("usuario@teste.com");

        Usuario result = mapper.toUsuario(confirmacao);

        assertNotNull(result);
    }

    @Test
    void toUsuario_comDadosValidos_deveMapearEmail() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("usuario@teste.com");

        Usuario result = mapper.toUsuario(confirmacao);

        assertNotNull(result);
        assertEquals("usuario@teste.com", result.getEmail());
    }

    @Test
    void toUsuario_comNull_deveRetornarNull() {
        Usuario result = mapper.toUsuario(null);

        assertNull(result);
    }
}
