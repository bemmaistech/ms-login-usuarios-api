package com.bemmaistech.login_usuarios.service;

import com.bemmaistech.login_usuarios.dto.confirmaremail.ConfirmarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.dto.confirmaremail.EnviarCodigoRequestDTO;
import com.bemmaistech.login_usuarios.mapper.ConfirmacaoEmailMapper;
import com.bemmaistech.login_usuarios.model.ConfirmacaoEmail;
import com.bemmaistech.login_usuarios.model.Usuario;
import com.bemmaistech.login_usuarios.repository.ConfirmacaoEmailRepository;
import com.bemmaistech.login_usuarios.repository.LoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmarEmailServiceTest {
    @Mock
    private EmailService emailService;

    @Mock
    private ConfirmacaoEmailRepository repository;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private ConfirmacaoEmailMapper mapper;

    @InjectMocks
    private ConfirmarEmailService service;

    private EnviarCodigoRequestDTO enviarDto;
    private ConfirmarCodigoRequestDTO confirmarDto;

    @BeforeEach
    void setUp() {
        enviarDto = new EnviarCodigoRequestDTO();
        enviarDto.setEmail("cliente@teste.com");

        confirmarDto = new ConfirmarCodigoRequestDTO();
        confirmarDto.setEmail("cliente@teste.com");
        confirmarDto.setCodigo("123456");
    }

    @Test
    void enviarCodigo_deveRetornarMensagemDeSucesso() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(entity);

        String result = service.enviarCodigo(enviarDto);

        assertEquals("Email enviado com sucesso!", result);
    }


    @Test
    void enviarCodigo_deveChamarMapperUmaVez() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ConfirmacaoEmail());

        service.enviarCodigo(enviarDto);

        verify(mapper, times(1)).toEntity(eq(enviarDto.getEmail()), anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void enviarCodigo_deveSalvarEntidadeRetornadaPeloMapper() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        ConfirmacaoEmail entity = new ConfirmacaoEmail();
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(entity);

        service.enviarCodigo(enviarDto);

        verify(repository).save(entity);
    }

    @Test
    void enviarCodigo_deveEnviarEmailComMesmoEmailDoDto() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ConfirmacaoEmail());

        service.enviarCodigo(enviarDto);

        verify(emailService).enviarCodigoConfirmacao(eq("cliente@teste.com"), anyString());
    }

    @Test
    void enviarCodigo_deveGerarCodigoComSeisDigitos() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ConfirmacaoEmail());

        service.enviarCodigo(enviarDto);

        ArgumentCaptor<String> codigoCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).enviarCodigoConfirmacao(eq("cliente@teste.com"), codigoCaptor.capture());
        assertTrue(codigoCaptor.getValue().matches("\\d{6}"));
    }

    @Test
    void enviarCodigo_deveDefinirExpiracaoCercaDe10MinutosAposCriacao() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ConfirmacaoEmail());

        service.enviarCodigo(enviarDto);

        ArgumentCaptor<LocalDateTime> expiraCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> criacaoCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(mapper).toEntity(eq("cliente@teste.com"), anyString(), expiraCaptor.capture(), criacaoCaptor.capture());

        LocalDateTime expira = expiraCaptor.getValue();
        LocalDateTime criacao = criacaoCaptor.getValue();

        assertTrue(expira.isAfter(criacao.plusMinutes(9)));
        assertTrue(expira.isBefore(criacao.plusMinutes(11)));
    }

    @Test
    void enviarCodigo_deveChamarFindByEmailUmaVez() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ConfirmacaoEmail());

        service.enviarCodigo(enviarDto);

        verify(repository, times(1)).findByEmail("cliente@teste.com");
    }

    @Test
    void enviarCodigo_deveChamarEnviarCodigoConfirmacaoUmaVez() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toEntity(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ConfirmacaoEmail());

        service.enviarCodigo(enviarDto);

        verify(emailService, times(1)).enviarCodigoConfirmacao(eq("cliente@teste.com"), anyString());
    }

    @Test
    void confirmarCodigo_deveRetornarMensagemDeSucesso() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("cliente@teste.com");
        confirmacao.setCodigo("123456");
        confirmacao.setExpiraEm(LocalDateTime.now().plusMinutes(5));

        when(repository.findByEmail(confirmarDto.getEmail())).thenReturn(Optional.of(confirmacao));
        when(mapper.toUsuario(confirmacao)).thenReturn(new Usuario());

        String result = service.confirmarCodigo(confirmarDto);

        assertEquals("Email confirmado com sucesso!", result);
    }

    @Test
    void confirmarCodigo_quandoEmailNaoEncontrado_deveLancarExcecao() {
        when(repository.findByEmail(confirmarDto.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.confirmarCodigo(confirmarDto));

        assertEquals("Email não encontrado!", ex.getMessage());
    }

    @Test
    void confirmarCodigo_quandoEmailNaoEncontrado_naoDeveSalvarNada() {
        when(repository.findByEmail(confirmarDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.confirmarCodigo(confirmarDto));

        verify(loginRepository, never()).save(any());
        verify(repository, never()).save(any(ConfirmacaoEmail.class));
    }

    @Test
    void confirmarCodigo_quandoCodigoInvalido_deveLancarExcecao() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("cliente@teste.com");
        confirmacao.setCodigo("000000");
        confirmacao.setExpiraEm(LocalDateTime.now().plusMinutes(5));
        when(repository.findByEmail(confirmarDto.getEmail())).thenReturn(Optional.of(confirmacao));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.confirmarCodigo(confirmarDto));

        assertEquals("Código inválido!", ex.getMessage());
    }

    @Test
    void confirmarCodigo_quandoCodigoExpirado_deveLancarExcecao() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("cliente@teste.com");
        confirmacao.setCodigo("123456");
        confirmacao.setExpiraEm(LocalDateTime.now().minusSeconds(1));
        when(repository.findByEmail(confirmarDto.getEmail())).thenReturn(Optional.of(confirmacao));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.confirmarCodigo(confirmarDto));

        assertEquals("Código expirado!", ex.getMessage());
    }

    @Test
    void confirmarCodigo_sucesso_deveSalvarUsuario() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("cliente@teste.com");
        confirmacao.setCodigo("123456");
        confirmacao.setExpiraEm(LocalDateTime.now().plusMinutes(5));

        Usuario usuario = new Usuario();

        when(repository.findByEmail(confirmarDto.getEmail())).thenReturn(Optional.of(confirmacao));
        when(mapper.toUsuario(confirmacao)).thenReturn(usuario);

        service.confirmarCodigo(confirmarDto);

        verify(loginRepository).save(usuario);
    }

    @Test
    void confirmarCodigo_sucesso_deveMarcarConfirmadoTrueESalvarConfirmacao() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("cliente@teste.com");
        confirmacao.setCodigo("123456");
        confirmacao.setExpiraEm(LocalDateTime.now().plusMinutes(5));

        when(repository.findByEmail(confirmarDto.getEmail())).thenReturn(Optional.of(confirmacao));
        when(mapper.toUsuario(confirmacao)).thenReturn(new Usuario());

        service.confirmarCodigo(confirmarDto);

        ArgumentCaptor<ConfirmacaoEmail> captor = ArgumentCaptor.forClass(ConfirmacaoEmail.class);
        verify(repository).save(captor.capture());

        assertTrue(captor.getValue().isConfirmado());
    }

    @Test
    void enviarEmailRecuperacao_deveRetornarMensagemDeSucesso() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("cliente@teste.com");
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.of(confirmacao));

        String result = service.enviarEmailRecuperacao(enviarDto);

        assertEquals("Email de recuperação enviado com sucesso!", result);
    }

    @Test
    void enviarEmailRecuperacao_quandoEmailNaoEncontrado_deveLancarExcecao() {
        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.enviarEmailRecuperacao(enviarDto));

        assertEquals("Email não encontrado!", ex.getMessage());
    }

    @Test
    void enviarEmailRecuperacao_sucesso_deveAtualizarCodigoExpiracaoConfirmadoSalvarEEnviarEmail() {
        ConfirmacaoEmail confirmacao = new ConfirmacaoEmail();
        confirmacao.setEmail("cliente@teste.com");
        confirmacao.setCodigo("111111");
        confirmacao.setConfirmado(true);
        confirmacao.setExpiraEm(LocalDateTime.now().minusMinutes(1));

        when(repository.findByEmail(enviarDto.getEmail())).thenReturn(Optional.of(confirmacao));

        service.enviarEmailRecuperacao(enviarDto);

        ArgumentCaptor<ConfirmacaoEmail> confirmacaoCaptor = ArgumentCaptor.forClass(ConfirmacaoEmail.class);
        verify(repository).save(confirmacaoCaptor.capture());

        ConfirmacaoEmail salvo = confirmacaoCaptor.getValue();
        assertNotNull(salvo.getCodigo());
        assertTrue(salvo.getCodigo().matches("\\d{6}"));
        assertFalse(salvo.isConfirmado());
        assertTrue(salvo.getExpiraEm().isAfter(LocalDateTime.now().minusSeconds(1)));

        ArgumentCaptor<String> codigoEmailCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).enviarCodigoConfirmacao(eq("cliente@teste.com"), codigoEmailCaptor.capture());
        assertEquals(salvo.getCodigo(), codigoEmailCaptor.getValue());
    }
}
