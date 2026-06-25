package com.bemmaistech.login_usuarios.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityConfigTest {
    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoderBean_deveExistir() {
        assertNotNull(passwordEncoder);
    }

    @Test
    void passwordEncoderBean_deveSerBCryptPasswordEncoder() {
        assertTrue(passwordEncoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    void passwordEncoder_deveCodificarSenha() {
        String senha = "MinhaSenh@123";
        String encoded = passwordEncoder.encode(senha);

        assertNotNull(encoded);
        assertTrue(encoded.length() > 0);
        assertTrue(!encoded.equals(senha));
    }

    @Test
    void passwordEncoder_deveValidarSenhaCorreta() {
        String senha = "MinhaSenh@123";
        String encoded = passwordEncoder.encode(senha);

        assertTrue(passwordEncoder.matches(senha, encoded));
    }

    @Test
    void passwordEncoder_deveRejeitarSenhaIncorreta() {
        String senha = "MinhaSenh@123";
        String senhaErrada = "OutraSenh@456";
        String encoded = passwordEncoder.encode(senha);

        assertTrue(!passwordEncoder.matches(senhaErrada, encoded));
    }

    @Test
    void securityConfigBean_deveExistir() {
        assertNotNull(securityConfig);
    }

    @Test
    void passwordEncoderBean_deveGerarHashDiferente() {
        String senha = "MinhaSenh@123";
        String encoded1 = passwordEncoder.encode(senha);
        String encoded2 = passwordEncoder.encode(senha);

        assertTrue(!encoded1.equals(encoded2));
    }

    @Test
    void passwordEncoder_deveHandleVazioOuNull() {
        String encoded = passwordEncoder.encode("");
        assertNotNull(encoded);
    }

    @Test
    void passwordEncoder_comSenhaLonga() {
        String senhaLonga = "ABC@1234567890DEFGHIJKLMNOPQRSTUVWXYZ123456789";
        String encoded = passwordEncoder.encode(senhaLonga);

        assertTrue(passwordEncoder.matches(senhaLonga, encoded));
    }

    @Test
    void passwordEncoder_caseSensitive() {
        String senha = "MinhaSenh@123";
        String senhaUpperCase = "minhaSenh@123";
        String encoded = passwordEncoder.encode(senha);

        assertTrue(!passwordEncoder.matches(senhaUpperCase, encoded));
    }
}
