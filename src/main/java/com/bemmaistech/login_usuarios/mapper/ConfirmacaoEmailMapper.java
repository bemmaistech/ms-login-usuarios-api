package com.bemmaistech.login_usuarios.mapper;

import com.bemmaistech.login_usuarios.model.ConfirmacaoEmail;
import com.bemmaistech.login_usuarios.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConfirmacaoEmailMapper {
    public ConfirmacaoEmail toEntity(String email, String codigo, LocalDateTime ExpiraEm, LocalDateTime dataCriacao) {
        if (email == null || codigo == null || ExpiraEm == null) {
            return null;
        }

        ConfirmacaoEmail confirmacaoEmail = new ConfirmacaoEmail();
        confirmacaoEmail.setEmail(email);
        confirmacaoEmail.setCodigo(codigo);
        confirmacaoEmail.setExpiraEm(ExpiraEm);
        confirmacaoEmail.setDataCriacao(dataCriacao);

        return confirmacaoEmail;
    }

    public Usuario toUsuario(ConfirmacaoEmail confirmacaoEmail){
        if (confirmacaoEmail == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setEmail(confirmacaoEmail.getEmail());
        usuario.setNome("");
        usuario.setSenha("");
        return usuario;
    }
}
