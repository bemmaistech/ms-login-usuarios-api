package com.bemmaistech.login_usuarios.service;

import com.bemmaistech.login_usuarios.dto.LoginAlterarSenhaRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateResponseDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginResponseDTO;
import com.bemmaistech.login_usuarios.mapper.ConfirmacaoEmailMapper;
import com.bemmaistech.login_usuarios.mapper.LoginMapper;
import com.bemmaistech.login_usuarios.model.ConfirmacaoEmail;
import com.bemmaistech.login_usuarios.model.Usuario;
import com.bemmaistech.login_usuarios.repository.ConfirmacaoEmailRepository;
import com.bemmaistech.login_usuarios.repository.LoginRepository;
import com.bemmaistech.login_usuarios.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private LoginRepository repository;
    @Mock private ConfirmacaoEmailRepository confirmacaoEmailRepository;
    @Mock private LoginMapper mapper;
    @Mock private ConfirmacaoEmailMapper confirmacaoEmailMapper;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;

    private LoginService service;

    @BeforeEach
    void setUp() {
        service = new LoginService(
                repository,
                confirmacaoEmailRepository,
                mapper,
                confirmacaoEmailMapper,
                jwtService,
                passwordEncoder,
                emailService,
                repository, // repository1
                mapper,     // mapper1
                confirmacaoEmailRepository // confirmacaoEmailRepository1
        );
    }

    @Test
    void cadastrar_deveRetornarDTO_quandoDadosValidos() {
        LoginCreateRequestDTO req = new LoginCreateRequestDTO();
        req.setNome("Joao");
        req.setTelefone("(11) 99999-9999");
        req.setEmail("joao@teste.com");
        req.setSenha("Senha@123");

        Usuario usuario = new Usuario();
        usuario.setEmail(req.getEmail());

        ConfirmacaoEmail conf = new ConfirmacaoEmail();
        conf.setEmail(req.getEmail());

        LoginCreateResponseDTO responseDTO = new LoginCreateResponseDTO();
        responseDTO.setEmail(req.getEmail());

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.of(usuario));
        when(confirmacaoEmailRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(conf));
        when(repository.findByTelefone(req.getTelefone())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(req.getSenha())).thenReturn("hash");
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDTO(any(Usuario.class))).thenReturn(responseDTO);

        LoginCreateResponseDTO result = service.cadastrar(req);

        assertNotNull(result);
        assertEquals("joao@teste.com", result.getEmail());
        assertFalse(conf.isConfirmado());
        verify(repository).save(any(Usuario.class));
        verify(mapper).toDTO(any(Usuario.class));
    }

    @Test
    void cadastrar_deveLancarErro_quandoUsuarioNaoEncontrado() {
        LoginCreateRequestDTO req = new LoginCreateRequestDTO();
        req.setEmail("x@teste.com");

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cadastrar(req));
        assertEquals("Usuário não encontrado!", ex.getMessage());
    }

    @Test
    void cadastrar_deveLancarErro_quandoConfirmacaoNaoEncontrada() {
        LoginCreateRequestDTO req = new LoginCreateRequestDTO();
        req.setEmail("x@teste.com");

        Usuario usuario = new Usuario();
        usuario.setEmail(req.getEmail());

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.of(usuario));
        when(confirmacaoEmailRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cadastrar(req));
        assertEquals("Confirmação de email não encontrada!", ex.getMessage());
    }

    @Test
    void cadastrar_deveLancarErro_quandoTelefoneJaCadastrado() {
        LoginCreateRequestDTO req = new LoginCreateRequestDTO();
        req.setEmail("x@teste.com");
        req.setTelefone("(11) 99999-9999");

        Usuario usuario = new Usuario();
        usuario.setEmail(req.getEmail());

        ConfirmacaoEmail conf = new ConfirmacaoEmail();

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.of(usuario));
        when(confirmacaoEmailRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(conf));
        when(repository.findByTelefone(req.getTelefone())).thenReturn(Optional.of(new Usuario()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.cadastrar(req));
        assertEquals("Telefone já cadastrado!", ex.getMessage());
    }

    @Test
    void login_deveRetornarToken_quandoCredenciaisValidas() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("joao@teste.com");
        req.setSenha("Senha@123");

        Usuario usuario = new Usuario();
        usuario.setEmail(req.getEmail());
        usuario.setSenha("hash");

        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setEmail(req.getEmail());
        responseDTO.setToken("jwt-token");

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(req.getSenha(), usuario.getSenha())).thenReturn(true);
        when(jwtService.gerarToken(usuario)).thenReturn("jwt-token");
        when(mapper.toLoginResponse(usuario, "jwt-token")).thenReturn(responseDTO);

        LoginResponseDTO result = service.login(req);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
    }

    @Test
    void login_deveLancarErro_quandoUsuarioNaoEncontrado() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("naoexiste@teste.com");

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.login(req));
        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void login_deveLancarErro_quandoSenhaInvalida() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("joao@teste.com");
        req.setSenha("errada");

        Usuario usuario = new Usuario();
        usuario.setEmail(req.getEmail());
        usuario.setSenha("hash");

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(req.getSenha(), usuario.getSenha())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.login(req));
        assertEquals("Senha inválida", ex.getMessage());
    }

    @Test
    void alterarSenha_deveAlterarSenha_quandoValida() {
        LoginAlterarSenhaRequestDTO req = new LoginAlterarSenhaRequestDTO();
        req.setEmail("joao@teste.com");
        req.setSenha("Nova@123");

        Usuario usuario = new Usuario();
        usuario.setEmail(req.getEmail());
        usuario.setSenha("hash-antigo");

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(req.getSenha(), usuario.getSenha())).thenReturn(false);
        when(passwordEncoder.encode(req.getSenha())).thenReturn("hash-novo");

        String result = service.alterarSenha(req);

        assertEquals("Senha alterada com sucesso!", result);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertEquals("hash-novo", captor.getValue().getSenha());
    }

    @Test
    void alterarSenha_deveLancarErro_quandoUsuarioNaoEncontrado() {
        LoginAlterarSenhaRequestDTO req = new LoginAlterarSenhaRequestDTO();
        req.setEmail("naoexiste@teste.com");

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.alterarSenha(req));
        assertEquals("Usuário não encontrado!", ex.getMessage());
    }

    @Test
    void alterarSenha_deveLancarErro_quandoNovaSenhaIgualAtual() {
        LoginAlterarSenhaRequestDTO req = new LoginAlterarSenhaRequestDTO();
        req.setEmail("joao@teste.com");
        req.setSenha("Mesma@123");

        Usuario usuario = new Usuario();
        usuario.setEmail(req.getEmail());
        usuario.setSenha("hash-antigo");

        when(repository.findByEmail(req.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(req.getSenha(), usuario.getSenha())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.alterarSenha(req));
        assertEquals("A nova senha não pode ser igual à senha atual", ex.getMessage());
    }
}
