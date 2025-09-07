package com.br.joao.basquete_api.app.repository;

import com.br.joao.basquete_api.domain.treino.Treino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, UUID> {
}