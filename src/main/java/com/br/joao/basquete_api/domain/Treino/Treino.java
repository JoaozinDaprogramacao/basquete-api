package com.br.joao.basquete_api.domain.Treino;

import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "treinos")
public class Treino {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(length = 255)
    private String focoDoTreino; // Ex: "Arremesso de 3 pontos", "Defesa", "Jogo coletivo"

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Um treino tem vários registros de desempenho (um para cada jogador que participou)
    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesempenhoTreino> desempenhos = new ArrayList<>();

    // Método utilitário para saber quantos jogadores participaram
    public Integer getJogadoresPresentes() {
        return desempenhos.size();
    }
}