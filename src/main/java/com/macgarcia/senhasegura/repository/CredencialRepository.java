package com.macgarcia.senhasegura.repository;

import com.macgarcia.senhasegura.entity.Credencial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredencialRepository extends JpaRepository<Credencial, Long> {

    @Query("select c from Credencial c where c.usuario.id = :idUsuario")
    Page<Credencial> buscarTodos(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
