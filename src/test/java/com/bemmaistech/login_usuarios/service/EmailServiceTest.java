package com.bemmaistech.login_usuarios.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private BrevoEmailProvider brevoEmailProvider;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(brevoEmailProvider);
    }

    @Test
    void enviarCodigoConfirmacao_deveEnviarEmailUmaVez() {
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");
        verify(brevoEmailProvider, times(1)).enviarHtml(anyString(), anyString(), anyString());
    }

    @Test
    void enviarCodigoConfirmacao_deveDefinirAssuntoCorreto() {
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");

        ArgumentCaptor<String> assuntoCaptor = ArgumentCaptor.forClass(String.class);
        verify(brevoEmailProvider).enviarHtml(eq("cliente@teste.com"), assuntoCaptor.capture(), anyString());

        assertEquals("Confirme seu e-mail | Bem Mais Tech", assuntoCaptor.getValue());
    }

    @Test
    void enviarCodigoConfirmacao_deveConterCodigoNoHtml() {
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "654321");

        ArgumentCaptor<String> htmlCaptor = ArgumentCaptor.forClass(String.class);
        verify(brevoEmailProvider).enviarHtml(eq("cliente@teste.com"), anyString(), htmlCaptor.capture());

        assertTrue(htmlCaptor.getValue().contains("654321"));
    }

    @Test
    void enviarCodigoConfirmacao_deveConterBrandingNoHtml() {
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");

        ArgumentCaptor<String> htmlCaptor = ArgumentCaptor.forClass(String.class);
        verify(brevoEmailProvider).enviarHtml(eq("cliente@teste.com"), anyString(), htmlCaptor.capture());

        assertTrue(htmlCaptor.getValue().contains("BEM MAIS TECH"));
        assertTrue(htmlCaptor.getValue().contains("Confirme seu e-mail"));
    }

    @Test
    void enviarCodigoConfirmacao_naoDeveManterPlaceholderNoHtml() {
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");

        ArgumentCaptor<String> htmlCaptor = ArgumentCaptor.forClass(String.class);
        verify(brevoEmailProvider).enviarHtml(eq("cliente@teste.com"), anyString(), htmlCaptor.capture());

        assertFalse(htmlCaptor.getValue().contains("{{CODIGO}}"));
    }

    @Test
    void enviarCodigoConfirmacao_comCodigosDiferentes_deveGerarConteudosDiferentes() {
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "111111");
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "222222");

        ArgumentCaptor<String> htmlCaptor = ArgumentCaptor.forClass(String.class);
        verify(brevoEmailProvider, times(2)).enviarHtml(eq("cliente@teste.com"), anyString(), htmlCaptor.capture());

        String content1 = htmlCaptor.getAllValues().get(0);
        String content2 = htmlCaptor.getAllValues().get(1);

        assertNotEquals(content1, content2);
        assertTrue(content1.contains("111111"));
        assertTrue(content2.contains("222222"));
    }

    @Test
    void enviarCodigoConfirmacao_comDadosValidos_naoDeveLancarExcecao() {
        assertDoesNotThrow(() -> emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456"));
    }
}
