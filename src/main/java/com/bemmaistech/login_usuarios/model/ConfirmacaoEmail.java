package com.bemmaistech.login_usuarios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_confirmacao_email")
public class ConfirmacaoEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "confirmado")
    private boolean confirmado;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "expira_em")
    private LocalDateTime expiraEm;

}
