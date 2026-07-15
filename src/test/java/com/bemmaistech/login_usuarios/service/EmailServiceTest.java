package com.bemmaistech.login_usuarios.service;

import jakarta.mail.Address;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, "b1f064001@smtp-brevo.com");
    }

    @Test
    void enviarCodigoConfirmacao_deveChamarCreateMimeMessage() {
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");
        verify(mailSender, times(1)).createMimeMessage();
    }

    @Test
    void enviarCodigoConfirmacao_deveEnviarEmailUmaVez() {
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void enviarCodigoConfirmacao_deveDefinirAssuntoCorreto() throws Exception {
        MimeMessage mime = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mime);
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        assertEquals("Confirme seu e-mail | Bem Mais Tech", captor.getValue().getSubject());
    }

    @Test
    void enviarCodigoConfirmacao_deveDefinirDestinatarioCorreto() throws Exception {
        MimeMessage mime = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mime);
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        Address[] recipients = captor.getValue().getAllRecipients();
        assertNotNull(recipients);
        assertEquals(1, recipients.length);
        assertEquals("cliente@teste.com", recipients[0].toString());
    }

    @Test
    void enviarCodigoConfirmacao_deveConterCodigoNoHtml() throws Exception {
        MimeMessage mime = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mime);
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "654321");

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        assertTrue(captor.getValue().getContent().toString().contains("654321"));
    }

    @Test
    void enviarCodigoConfirmacao_deveConterBrandingNoHtml() throws Exception {
        MimeMessage mime = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mime);
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        String content = captor.getValue().getContent().toString();
        assertTrue(content.contains("BEM MAIS TECH"));
        assertTrue(content.contains("Confirme seu e-mail"));
    }

    @Test
    void enviarCodigoConfirmacao_naoDeveManterPlaceholderNoHtml() throws Exception {
        MimeMessage mime = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mime);
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456");

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        assertFalse(captor.getValue().getContent().toString().contains("{{CODIGO}}"));
    }

    @Test
    void enviarCodigoConfirmacao_comCodigosDiferentes_deveGerarConteudosDiferentes() throws Exception {
        MimeMessage mime1 = new MimeMessage(Session.getDefaultInstance(new Properties()));
        MimeMessage mime2 = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mime1, mime2);

        emailService.enviarCodigoConfirmacao("cliente@teste.com", "111111");
        emailService.enviarCodigoConfirmacao("cliente@teste.com", "222222");

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(2)).send(captor.capture());

        String content1 = captor.getAllValues().get(0).getContent().toString();
        String content2 = captor.getAllValues().get(1).getContent().toString();
        assertNotEquals(content1, content2);
        assertTrue(content1.contains("111111"));
        assertTrue(content2.contains("222222"));
    }

    @Test
    void enviarCodigoConfirmacao_comDadosValidos_naoDeveLancarExcecao() {
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertDoesNotThrow(() -> emailService.enviarCodigoConfirmacao("cliente@teste.com", "123456"));
    }
}
