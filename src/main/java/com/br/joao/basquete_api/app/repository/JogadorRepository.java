package com.br.joao.basquete_api.app.repository;


import com.br.joao.basquete_api.domain.Jogador.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, UUID> {
}