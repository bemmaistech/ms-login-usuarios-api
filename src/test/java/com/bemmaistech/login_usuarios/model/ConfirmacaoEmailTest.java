package com.bemmaistech.login_usuarios.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ConfirmacaoEmailTest {
    @Test
    void deveSetarEObterId() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        entity.setId(10L);

        assertEquals(10L, entity.getId());
    }

    @Test
    void deveSetarEObterEmail() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        entity.setEmail("cliente@teste.com");

        assertEquals("cliente@teste.com", entity.getEmail());
    }

    @Test
    void deveSetarEObterCodigo() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        entity.setCodigo("123456");

        assertEquals("123456", entity.getCodigo());
    }

    @Test
    void deveSetarEObterConfirmado() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        entity.setConfirmado(true);

        assertTrue(entity.isConfirmado());
    }

    @Test
    void deveSetarEObterDataCriacao() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        LocalDateTime agora = LocalDateTime.now();
        entity.setDataCriacao(agora);

        assertEquals(agora, entity.getDataCriacao());
    }

    @Test
    void deveSetarEObterExpiraEm() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        LocalDateTime expira = LocalDateTime.now().plusMinutes(10);
        entity.setExpiraEm(expira);

        assertEquals(expira, entity.getExpiraEm());
    }

    @Test
    void confirmadoDeveSerFalsePorPadrao() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();

        assertFalse(entity.isConfirmado());
    }

    @Test
    void equalsEHashCode_devemSerIguaisParaObjetosComMesmoEstado() {
        LocalDateTime dataCriacao = LocalDateTime.now();
        LocalDateTime expiraEm = dataCriacao.plusMinutes(10);

        ConfirmacaoEmail a = new ConfirmacaoEmail();
        a.setId(1L);
        a.setEmail("cliente@teste.com");
        a.setCodigo("123456");
        a.setConfirmado(true);
        a.setDataCriacao(dataCriacao);
        a.setExpiraEm(expiraEm);

        ConfirmacaoEmail b = new ConfirmacaoEmail();
        b.setId(1L);
        b.setEmail("cliente@teste.com");
        b.setCodigo("123456");
        b.setConfirmado(true);
        b.setDataCriacao(dataCriacao);
        b.setExpiraEm(expiraEm);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_deveSerDiferenteQuandoEstadoMuda() {
        ConfirmacaoEmail a = new ConfirmacaoEmail();
        a.setId(1L);
        a.setEmail("a@teste.com");

        ConfirmacaoEmail b = new ConfirmacaoEmail();
        b.setId(2L);
        b.setEmail("b@teste.com");

        assertNotEquals(a, b);
    }

    @Test
    void toString_deveConterNomeDaClasse() {
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        entity.setId(1L);
        entity.setEmail("cliente@teste.com");

        String value = entity.toString();

        assertNotNull(value);
        assertTrue(value.contains("ConfirmacaoEmail"));
    }
}
