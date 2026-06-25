package com.bemmaistech.login_usuarios.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginCreateRequestDTO {
    private String nome;
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
            message = "Telefone inválido. Use formato como (11) 99999-9999")
    private String telefone;
    @Email(message = "O campo email deve ser um endereço de email válido")
    private String email;

    private String senha;
}
