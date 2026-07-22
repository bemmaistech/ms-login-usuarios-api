package com.bemmaistech.login_usuarios.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "telefone", unique = true)
    private String telefone;

    @Column (name = "email", unique = true)
    private String email;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "senha")
    private String senha;
}
