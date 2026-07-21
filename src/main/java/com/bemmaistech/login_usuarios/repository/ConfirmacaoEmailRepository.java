package com.bemmaistech.login_usuarios.repository;

import com.bemmaistech.login_usuarios.model.ConfirmacaoEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmacaoEmailRepository extends JpaRepository<ConfirmacaoEmail, Long> {
    Optional<ConfirmacaoEmail> findByEmail(String email);

    Optional<ConfirmacaoEmail> deleteByEmail(String codigo);

}
