package com.bemmaistech.login_usuarios.security;

import com.bemmaistech.login_usuarios.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    private JwtService jwtService;
    private static final String SECRET_VALIDA = "MinhaChaveSuperSecretaComMaisDe32Caracteres123456";

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        setSecret(SECRET_VALIDA);
    }

    @Test
    void gerarToken_deveRetornarTokenNaoNulo() {
        Usuario usuario = criarUsuario("cliente@teste.com");

        String token = jwtService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void gerarToken_deveRetornarFormatoJwtValido() {
        Usuario usuario = criarUsuario("cliente@teste.com");

        String token = jwtService.gerarToken(usuario);

        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void gerarToken_deveConterSubjectComEmailDoUsuario() {
        Usuario usuario = criarUsuario("cliente@teste.com");

        String token = jwtService.gerarToken(usuario);
        Claims claims = parseClaims(token);

        assertEquals("cliente@teste.com", claims.getSubject());
    }

    @Test
    void gerarToken_deveConterIssuedAt() {
        Usuario usuario = criarUsuario("cliente@teste.com");

        String token = jwtService.gerarToken(usuario);
        Claims claims = parseClaims(token);

        assertNotNull(claims.getIssuedAt());
    }

    @Test
    void gerarToken_deveConterExpiration() {
        Usuario usuario = criarUsuario("cliente@teste.com");

        String token = jwtService.gerarToken(usuario);
        Claims claims = parseClaims(token);

        assertNotNull(claims.getExpiration());
    }

    @Test
    void gerarToken_expirationDeveSerDepoisDeIssuedAt() {
        Usuario usuario = criarUsuario("cliente@teste.com");

        String token = jwtService.gerarToken(usuario);
        Claims claims = parseClaims(token);

        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

    @Test
    void gerarToken_expirationDeveSerAproximadamente24Horas() {
        Usuario usuario = criarUsuario("cliente@teste.com");

        String token = jwtService.gerarToken(usuario);
        Claims claims = parseClaims(token);

        long diffMillis = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        long vinteETresHorasECinquentaMin = 23L * 60 * 60 * 1000 + 50L * 60 * 1000;
        long vinteEQuatroHorasEDezMin = 24L * 60 * 60 * 1000 + 10L * 60 * 1000;

        assertTrue(diffMillis >= vinteETresHorasECinquentaMin);
        assertTrue(diffMillis <= vinteEQuatroHorasEDezMin);
    }

    @Test
    void gerarToken_comUsuarioNulo_deveLancarNullPointerException() {
        assertThrows(NullPointerException.class, () -> jwtService.gerarToken(null));
    }

    @Test
    void gerarToken_comSecretFraca_deveLancarWeakKeyException() throws Exception {
        setSecret("chave-curta");

        Usuario usuario = criarUsuario("cliente@teste.com");

        assertThrows(WeakKeyException.class, () -> jwtService.gerarToken(usuario));
    }

    private Usuario criarUsuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        return usuario;
    }

    private Claims parseClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_VALIDA.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void setSecret(String secret) throws Exception {
        Field field = JwtService.class.getDeclaredField("secret");
        field.setAccessible(true);
        field.set(jwtService, secret);
    }
}
