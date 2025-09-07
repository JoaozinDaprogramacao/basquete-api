package com.br.joao.basquete_api.app.service;

import com.br.joao.basquete_api.app.exception.ResourceNotFoundException;
import com.br.joao.basquete_api.app.repository.JogadorRepository;
import com.br.joao.basquete_api.app.repository.TreinoRepository;
import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import com.br.joao.basquete_api.domain.jogador.HabilidadesTecnicas;
import com.br.joao.basquete_api.domain.jogador.Jogador;
import com.br.joao.basquete_api.domain.jogador.dto.DesempenhoCreateDTO;
import com.br.joao.basquete_api.domain.treino.Treino;
import com.br.joao.basquete_api.domain.treino.dto.TreinoCreateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final JogadorRepository jogadorRepository;
    // O DesempenhoTreinoRepository será salvo via cascata, mas pode ser injetado se precisar de buscas diretas.

    public TreinoService(TreinoRepository treinoRepository, JogadorRepository jogadorRepository) {
        this.treinoRepository = treinoRepository;
        this.jogadorRepository = jogadorRepository;
    }

    // --- Gerenciamento de Treinos ---

    @Transactional
    public Treino criarTreino(TreinoCreateDTO dto) {
        Treino novoTreino = new Treino();
        novoTreino.setData(dto.data());
        novoTreino.setFocoDoTreino(dto.focoDoTreino());
        novoTreino.setObservacoes(dto.observacoes());
        return treinoRepository.save(novoTreino);
    }

    @Transactional(readOnly = true)
    public List<Treino> listarTodos() {
        return treinoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Treino buscarPorId(UUID id) {
        return treinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado com o ID: " + id));
    }

    // --- Lógica Principal: Lançamento de Desempenho ---

    @Transactional
    public DesempenhoTreino lancarDesempenho(UUID treinoId, UUID jogadorId, DesempenhoCreateDTO dto) {
        // 1. Busca as entidades principais
        Treino treino = buscarPorId(treinoId);
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado com o ID: " + jogadorId));

        // 2. Cria o novo registro de desempenho
        DesempenhoTreino novoDesempenho = new DesempenhoTreino();
        novoDesempenho.setTreino(treino);
        novoDesempenho.setJogador(jogador);

        // Mapeia os dados do DTO
        novoDesempenho.setPontos(dto.pontos());
        novoDesempenho.setAssistencias(dto.assistencias());
        novoDesempenho.setRebotes(dto.rebotes());
        novoDesempenho.setRoubosDeBola(dto.roubosDeBola());
        novoDesempenho.setTocos(dto.tocos());
        novoDesempenho.setErrosDePasse(dto.errosDePasse());
        novoDesempenho.setArremessosTentados(dto.arremessosTentados());
        novoDesempenho.setArremessosConvertidos(dto.arremessosConvertidos());
        novoDesempenho.setFeedbackDoTreinador(dto.feedbackDoTreinador());

        // 3. Adiciona o desempenho ao histórico do jogador (a persistência será em cascata)
        jogador.getHistoricoDesempenho().add(novoDesempenho);

        // 4. (A MÁGICA) recalcula os atributos consolidados do jogador
        atualizarAtributosConsolidados(jogador);

        // 5. Salva o jogador. Devido à cascata, o novo desempenho também será salvo.
        jogadorRepository.save(jogador);

        return novoDesempenho;
    }

    /**
     * Este método contém a lógica de negócio para atualizar os atributos de um jogador
     * com base em todo o seu histórico de treinos.
     * @param jogador O jogador a ser atualizado.
     */
    private void atualizarAtributosConsolidados(Jogador jogador) {
        List<DesempenhoTreino> historico = jogador.getHistoricoDesempenho();
        if (historico.isEmpty()) {
            return; // Nada a fazer se não há treinos
        }

        // --- Exemplo de Lógica de Agregação (Média Simples) ---
        // Você pode tornar essa lógica tão complexa quanto quiser (médias ponderadas, etc.)

        double mediaPontos = historico.stream().mapToInt(DesempenhoTreino::getPontos).average().orElse(0.0);
        double mediaAssistencias = historico.stream().mapToInt(DesempenhoTreino::getAssistencias).average().orElse(0.0);

        int totalArremessosTentados = historico.stream().mapToInt(DesempenhoTreino::getArremessosTentados).sum();
        int totalArremessosConvertidos = historico.stream().mapToInt(DesempenhoTreino::getArremessosConvertidos).sum();

        // Calcula o aproveitamento e converte para uma escala de 0-100
        double aproveitamentoArremesso = (totalArremessosTentados > 0)
                ? ((double) totalArremessosConvertidos / totalArremessosTentados) * 100
                : 0.0;

        // Atualiza os atributos do jogador
        HabilidadesTecnicas habilidades = jogador.getHabilidadesTecnicas();
        if (habilidades == null) {
            habilidades = new HabilidadesTecnicas(); // Garante que não seja nulo
        }

        // Aqui você define como cada estatística impacta os atributos
        habilidades.setArremesso((int) Math.round(aproveitamentoArremesso));
        habilidades.setPasse((int) Math.round(mediaAssistencias * 10)); // Ex: multiplicador para escala 0-100

        // ... Lógica para outros atributos (drible, 3 pontos, etc.) ...

        jogador.setHabilidadesTecnicas(habilidades);
    }
}