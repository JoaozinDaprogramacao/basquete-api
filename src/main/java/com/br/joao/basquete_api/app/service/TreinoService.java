package com.br.joao.basquete_api.app.service;

import com.br.joao.basquete_api.app.exception.ResourceNotFoundException;
import com.br.joao.basquete_api.app.repository.DesempenhoTreinoRepository;
import com.br.joao.basquete_api.app.repository.JogadorRepository;
import com.br.joao.basquete_api.app.repository.TreinoRepository;
import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import com.br.joao.basquete_api.domain.jogador.Jogador;
import com.br.joao.basquete_api.domain.treino.Treino;
import com.br.joao.basquete_api.domain.treino.dto.TreinoCreateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final JogadorRepository jogadorRepository;
    private final DesempenhoTreinoRepository desempenhoTreinoRepository;

    public TreinoService(TreinoRepository treinoRepository, JogadorRepository jogadorRepository, DesempenhoTreinoRepository desempenhoTreinoRepository) {
        this.treinoRepository = treinoRepository;
        this.jogadorRepository = jogadorRepository;
        this.desempenhoTreinoRepository = desempenhoTreinoRepository;
    }

    @Transactional
    public Treino criarTreino(TreinoCreateDTO dto) {
        Treino novoTreino = new Treino();
        novoTreino.setData(dto.data());
        novoTreino.setFocoDoTreino(dto.focoDoTreino());
        novoTreino.setObservacoes(dto.observacoes());
        return treinoRepository.save(novoTreino);
    }

    @Transactional(readOnly = true)
    public Treino buscarPorId(UUID id) {
        return treinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado com o ID: " + id));
    }


    @Transactional
    public DesempenhoTreino adicionarJogadorAoTreino(UUID treinoId, UUID jogadorId) {
        // Valida se o jogador já não está no treino para evitar duplicatas
        if (desempenhoTreinoRepository.findByTreinoIdAndJogadorId(treinoId, jogadorId).isPresent()) {
            throw new IllegalStateException("Jogador já adicionado a este treino.");
        }

        Treino treino = buscarPorId(treinoId);
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado com o ID: " + jogadorId));

        // Cria a folha de desempenho zerada para o jogador neste treino
        DesempenhoTreino novoDesempenho = new DesempenhoTreino();
        novoDesempenho.setTreino(treino);
        novoDesempenho.setJogador(jogador);
        novoDesempenho.setPontos(0);
        novoDesempenho.setAssistencias(0);
        novoDesempenho.setRebotes(0);
        novoDesempenho.setRoubosDeBola(0);
        novoDesempenho.setTocos(0);
        novoDesempenho.setErrosDePasse(0);
        novoDesempenho.setArremessosTentados(0);
        novoDesempenho.setArremessosConvertidos(0);

        return desempenhoTreinoRepository.save(novoDesempenho);
    }
}