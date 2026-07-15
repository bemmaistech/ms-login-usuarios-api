package com.bemmaistech.login_usuarios.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService("test-api-key", "bemmaistech@gmail.com");
    }

    @Test
    void montarTemplateConfirmacao_deveConterCodigoNoHtml() {
        String html = emailService.montarTemplateConfirmacao("654321");
        assertTrue(html.contains("654321"));
    }

    @Test
    void montarTemplateConfirmacao_deveConterBrandingNoHtml() {
        String html = emailService.montarTemplateConfirmacao("123456");
        assertTrue(html.contains("BEM MAIS TECH"));
        assertTrue(html.contains("Confirme seu e-mail"));
    }

    @Test
    void montarTemplateConfirmacao_naoDeveManterPlaceholderNoHtml() {
        String html = emailService.montarTemplateConfirmacao("123456");
        assertFalse(html.contains("{{CODIGO}}"));
    }

    @Test
    void montarTemplateConfirmacao_comCodigosDiferentes_deveGerarConteudosDiferentes() {
        String html1 = emailService.montarTemplateConfirmacao("111111");
        String html2 = emailService.montarTemplateConfirmacao("222222");

        assertNotEquals(html1, html2);
        assertTrue(html1.contains("111111"));
        assertTrue(html2.contains("222222"));
    }

    @Test
    void construtor_comDadosValidos_naoDeveFalhar() {
        assertDoesNotThrow(() -> new EmailService("test-api-key", "bemmaistech@gmail.com"));
    }
}
