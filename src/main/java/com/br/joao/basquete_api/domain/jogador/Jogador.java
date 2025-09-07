package com.br.joao.basquete_api.domain.jogador;

import com.br.joao.basquete_api.domain.jogador.enums.Modulo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
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

    // --- Dados Pessoais ---
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataNascimento; // Alterado para LocalDate para cálculo preciso da idade

    @Column(nullable = false, length = 100)
    private String nomeResponsavel;

    @Column(nullable = false, length = 20)
    private String contatoResponsavel;

    @Column(nullable = false)
    private boolean rgEntregue;

    // --- Atributos Consolidados do Jogador ---
    // IMPORTANTE: Estes atributos agora representam uma MÉDIA ou um VALOR CALCULADO
    // com base no histórico de desempenho do jogador. Eles são atualizados pela sua lógica de negócio.
    @Embedded
    private AtributosFisicos atributosFisicos;

    @Embedded
    private HabilidadesTecnicas habilidadesTecnicas;

    @Embedded
    private AtributosMentais atributosMentais;

    // --- Metadados de Desenvolvimento ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modulo modulo;

    // --- Histórico de Desempenho ---
    // Um jogador tem um histórico com vários registros de desempenho
    @OneToMany(mappedBy = "jogador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DesempenhoTreino> historicoDesempenho = new ArrayList<>();

    /**
     * Calcula a idade do jogador dinamicamente com base na data de nascimento.
     * Este método evita o armazenamento de dados redundantes e inconsistentes no banco.
     * @return A idade atual do jogador em anos.
     */
    public Integer getIdade() {
        if (this.dataNascimento == null) {
            return null;
        }
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }
}