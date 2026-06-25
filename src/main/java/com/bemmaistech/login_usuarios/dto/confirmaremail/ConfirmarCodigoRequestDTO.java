package com.bemmaistech.login_usuarios.dto.confirmaremail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmarCodigoRequestDTO {
    @NotNull(message = "O email é obrigatório!!")
    @Email(message = "O campo email deve ser um endereço de email válido")
    private String email;
    @NotNull(message = "O código é obrigatório!!")
    private String codigo;
}
