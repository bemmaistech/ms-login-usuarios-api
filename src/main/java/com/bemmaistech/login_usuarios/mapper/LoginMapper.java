package com.bemmaistech.login_usuarios.mapper;

import com.bemmaistech.login_usuarios.dto.login.LoginCreateRequestDTO;
import com.bemmaistech.login_usuarios.dto.login.LoginCreateResponseDTO;

import com.bemmaistech.login_usuarios.dto.login.LoginResponseDTO;
import com.bemmaistech.login_usuarios.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public Usuario toEntity(LoginCreateRequestDTO params, String senha) {
        if (params == null || senha == null || senha.isEmpty()) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(params.getNome());
        usuario.setTelefone(params.getTelefone());
        usuario.setSenha(senha);
        usuario.setDataCriacao(java.time.LocalDateTime.now());

        return usuario;
    }
    public LoginCreateResponseDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        LoginCreateResponseDTO response = new LoginCreateResponseDTO();
        response.setNome(usuario.getNome());
        response.setTelefone(usuario.getTelefone());
        response.setEmail(usuario.getEmail());

        return response;
    }

    public LoginResponseDTO toLoginResponse(Usuario usuario, String token) {
        if (usuario == null) {
            return null;
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(usuario.getId());
        response.setToken(token);

        return response;
    }

}
