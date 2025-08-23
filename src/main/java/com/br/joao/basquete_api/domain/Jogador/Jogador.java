package com.br.joao.basquete_api.domain.Jogador;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jogadores")
public class Jogador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Integer anoNascimento;

    @Column(nullable = false)
    private Integer idade; // Este campo será calculado

    @Column(nullable = false, length = 100)
    private String nomeResponsavel;

    @Column(nullable = false, length = 20)
    private String contatoResponsavel;

    @Column(nullable = false)
    private boolean rgEntregue; // Campo baseado na sua planilha
}