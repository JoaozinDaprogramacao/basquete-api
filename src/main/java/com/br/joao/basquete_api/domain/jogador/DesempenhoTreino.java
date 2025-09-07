package com.br.joao.basquete_api.domain.jogador;

import com.br.joao.basquete_api.domain.treino.Treino;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "desempenho_treino")
public class DesempenhoTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Muitos desempenhos estão associados a UM Jogador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogador_id", nullable = false)
    private Jogador jogador;

    // Muitos desempenhos estão associados a UM Treino
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treino_id", nullable = false)
    private Treino treino;

    // --- Estatísticas específicas DESTE treino ---
    // Você pode adicionar todos os campos que quiser medir
    private Integer pontos;
    private Integer assistencias;
    private Integer rebotes;
    private Integer roubosDeBola;
    private Integer tocos;
    @Column(name = "erros_de_passe")
    private Integer errosDePasse; // Turnovers
    private Integer arremessosTentados;
    private Integer arremessosConvertidos;
    // ...etc

    @Column(columnDefinition = "TEXT")
    private String feedbackDoTreinador;
}