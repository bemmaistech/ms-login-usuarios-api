package com.bemmaistech.login_usuarios.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final String BREVO_BASE_URL = "https://api.brevo.com/v3";

    private final RestClient restClient;
    private final String fromEmail;

    public EmailService(
            @Value("${brevo.api.key}") String apiKey,
            @Value("${brevo.from.email}") String fromEmail
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("BREVO_API_KEY environment variable is required but not set");
        }
        if (fromEmail == null || fromEmail.isBlank()) {
            throw new IllegalArgumentException("BREVO_FROM_EMAIL environment variable is required but not set");
        }

        this.restClient = RestClient.builder()
                .baseUrl(BREVO_BASE_URL)
                .defaultHeader("api-key", apiKey.trim())
                .build();
        this.fromEmail = fromEmail.trim();
    }

    public void enviarCodigoConfirmacao(String destino, String codigo) {
        String assunto = "Confirme seu e-mail | Bem Mais Tech";
        String html = montarTemplateConfirmacao(codigo);
        enviarEmailHtml(destino, assunto, html);
    }

    void enviarEmailHtml(String destino, String assunto, String htmlConteudo) {
        String emailDestino = normalizarEmail(destino, "destino");
        String emailRemetente = normalizarEmail(fromEmail, "from");

        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", "Bem Mais Tech", "email", emailRemetente),
                "to", List.of(Map.of("email", emailDestino)),
                "subject", assunto,
                "htmlContent", htmlConteudo
        );

        ResponseEntity<String> response = restClient.post()
                .uri("/smtp/email")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao enviar e-mail pelo Brevo: status=" + response.getStatusCode().value());
        }
    }

    String montarTemplateConfirmacao(String codigo) {
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

    private String normalizarEmail(String email, String campo) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail inválido no campo '" + campo + "'");
        }

        String valor = email.trim();
        if (!valor.contains("@") || !valor.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido no campo '" + campo + "': " + email);
        }

        return valor;
    }
}
