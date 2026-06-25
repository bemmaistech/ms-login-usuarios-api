package com.bemmaistech.login_usuarios.dto.login;

import lombok.Data;

@Data
public class LoginCreateResponseDTO {
    private String nome;

    private String telefone;

    private String email;
}
