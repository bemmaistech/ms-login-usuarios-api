package com.bemmaistech.login_usuarios.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender,
                        @Value("${mail.from}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void enviarCodigoConfirmacao(String destino, String codigo) {
        String assunto = "Confirme seu e-mail | Bem Mais Tech";
        String html = montarTemplateConfirmacao(codigo);
        enviarEmailHtml(destino, assunto, html);
    }

    private void enviarEmailHtml(String destino, String assunto, String htmlConteudo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(destino);
            helper.setSubject(assunto);
            helper.setText(htmlConteudo, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao montar e-mail HTML", e);
        }
    }

    private String montarTemplateConfirmacao(String codigo) {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <title>Confirmacao de e-mail</title>
                </head>
                <body style="margin:0;padding:0;background-color:#f3f4f6;font-family:Arial,Helvetica,sans-serif;">
                  <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="background-color:#f3f4f6;padding:24px 12px;">
                    <tr>
                      <td align="center">
                        <table role="presentation" width="600" cellspacing="0" cellpadding="0" style="max-width:600px;background:#ffffff;border-radius:14px;overflow:hidden;">
                          <tr>
                            <td style="background:#111827;padding:24px;text-align:center;">
                              <div style="font-size:22px;font-weight:700;color:#f59e0b;letter-spacing:0.5px;">BEM MAIS TECH</div>
                              <div style="margin-top:6px;color:#e5e7eb;font-size:13px;">Seguranca e tecnologia para sua conta</div>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:28px 28px 12px 28px;color:#111827;">
                              <h2 style="margin:0 0 10px 0;font-size:24px;">Confirme seu e-mail</h2>
                              <p style="margin:0 0 18px 0;color:#374151;font-size:15px;line-height:1.5;">Use o codigo abaixo para concluir a confirmacao da sua conta.</p>
                              <div style="text-align:center;margin:18px 0 18px 0;">
                                <span style="display:inline-block;background:#f9fafb;border:1px solid #e5e7eb;border-radius:10px;padding:14px 24px;font-size:30px;font-weight:700;letter-spacing:8px;color:#111827;">{{CODIGO}}</span>
                              </div>
                              <p style="margin:0;color:#6b7280;font-size:13px;line-height:1.5;">Valido por 10 minutos. Se voce nao solicitou este codigo, ignore este e-mail.</p>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:16px 28px 24px 28px;color:#9ca3af;font-size:12px;text-align:center;">
                              (c) Bem Mais Tech - Todos os direitos reservados.
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """;

        return html.replace("{{CODIGO}}", codigo);
    }
}
