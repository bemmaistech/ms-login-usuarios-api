package com.bemmaistech.login_usuarios.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    @Test
    void deveSetarEObterId() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        assertEquals(1L, usuario.getId());
    }

    @Test
    void deveSetarEObterNome() {
        Usuario usuario = new Usuario();
        usuario.setNome("Joao Silva");

        assertEquals("Joao Silva", usuario.getNome());
    }

    @Test
    void deveSetarEObterTelefone() {
        Usuario usuario = new Usuario();
        usuario.setTelefone("(11) 99999-9999");

        assertEquals("(11) 99999-9999", usuario.getTelefone());
    }

    @Test
    void deveSetarEObterEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@teste.com");

        assertEquals("joao@teste.com", usuario.getEmail());
    }

    @Test
    void deveSetarEObterDataCriacao() {
        Usuario usuario = new Usuario();
        LocalDateTime agora = LocalDateTime.of(2026, 6, 23, 10, 0);
        usuario.setDataCriacao(agora);

        assertEquals(agora, usuario.getDataCriacao());
    }

    @Test
    void deveSetarEObterSenha() {
        Usuario usuario = new Usuario();
        usuario.setSenha("hash-da-senha");

        assertEquals("hash-da-senha", usuario.getSenha());
    }

    @Test
    void equals_deveSerTrueParaObjetosComMesmosDados() {
        Usuario u1 = new Usuario();
        u1.setId(1L);
        u1.setNome("Joao");
        u1.setTelefone("(11) 99999-9999");
        u1.setEmail("joao@teste.com");
        u1.setDataCriacao(LocalDateTime.of(2026, 6, 23, 10, 0));
        u1.setSenha("hash");

        Usuario u2 = new Usuario();
        u2.setId(1L);
        u2.setNome("Joao");
        u2.setTelefone("(11) 99999-9999");
        u2.setEmail("joao@teste.com");
        u2.setDataCriacao(LocalDateTime.of(2026, 6, 23, 10, 0));
        u2.setSenha("hash");

        assertEquals(u1, u2);
    }

    @Test
    void equals_deveSerFalseParaObjetosDiferentes() {
        Usuario u1 = new Usuario();
        u1.setId(1L);
        u1.setEmail("a@teste.com");

        Usuario u2 = new Usuario();
        u2.setId(2L);
        u2.setEmail("b@teste.com");

        assertNotEquals(u1, u2);
    }

    @Test
    void hashCode_deveSerIgualParaObjetosIguais() {
        Usuario u1 = new Usuario();
        u1.setId(1L);
        u1.setNome("Joao");

        Usuario u2 = new Usuario();
        u2.setId(1L);
        u2.setNome("Joao");

        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void toString_deveConterNomeDaClasse() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Joao");

        String texto = usuario.toString();

        assertNotNull(texto);
        assertTrue(texto.contains("Usuario"));
    }
}
