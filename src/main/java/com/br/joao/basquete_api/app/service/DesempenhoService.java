package com.br.joao.basquete_api.app.service;

import com.br.joao.basquete_api.app.exception.ResourceNotFoundException;
import com.br.joao.basquete_api.app.repository.DesempenhoTreinoRepository;
import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import com.br.joao.basquete_api.domain.treino.dto.DesempenhoResponseDTO; // 1. IMPORTAR O NOVO DTO
import com.br.joao.basquete_api.domain.treino.dto.RegistrarEventoDTO;
import com.br.joao.basquete_api.domain.treino.enums.TipoEvento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DesempenhoService {

    private final DesempenhoTreinoRepository desempenhoRepository;
    private final JogadorEvolucaoService jogadorEvolucaoService;

    public DesempenhoService(DesempenhoTreinoRepository desempenhoRepository, JogadorEvolucaoService jogadorEvolucaoService) {
        this.desempenhoRepository = desempenhoRepository;
        this.jogadorEvolucaoService = jogadorEvolucaoService;
    }

    @Transactional
    // 2. ALTERAR O TIPO DE RETORNO DO MÉTODO
    public DesempenhoResponseDTO registrarEvento(UUID treinoId, UUID jogadorId, RegistrarEventoDTO eventoDTO) {
        DesempenhoTreino desempenho = desempenhoRepository.findByTreinoIdAndJogadorId(treinoId, jogadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Desempenho não encontrado para o jogador " + jogadorId + " no treino " + treinoId));

        // Atualiza as estatísticas do treino (lógica permanece a mesma)
        atualizarEstatisticas(desempenho, eventoDTO.tipo());

        // Dispara a lógica de evolução permanente do jogador (lógica permanece a mesma)
        jogadorEvolucaoService.processarEvolucao(desempenho.getJogador(), eventoDTO.tipo());

        // Salva a entidade atualizada no banco
        DesempenhoTreino desempenhoSalvo = desempenhoRepository.save(desempenho);

        // 3. CONVERTER A ENTIDADE SALVA PARA O DTO E RETORNAR
        return new DesempenhoResponseDTO(desempenhoSalvo);
    }

    private void atualizarEstatisticas(DesempenhoTreino desempenho, TipoEvento tipo) {
        // Nenhuma alteração necessária aqui, a lógica está correta.
        switch (tipo) {
            case CESTA_1_PONTO:
                desempenho.setPontos(desempenho.getPontos() + 1);
                desempenho.setArremessosTentados(desempenho.getArremessosTentados() + 1);
                desempenho.setArremessosConvertidos(desempenho.getArremessosConvertidos() + 1);
                break;
            case CESTA_2_PONTOS:
                desempenho.setPontos(desempenho.getPontos() + 2);
                desempenho.setArremessosTentados(desempenho.getArremessosTentados() + 1);
                desempenho.setArremessosConvertidos(desempenho.getArremessosConvertidos() + 1);
                break;
            case CESTA_3_PONTOS:
                desempenho.setPontos(desempenho.getPontos() + 3);
                desempenho.setArremessosTentados(desempenho.getArremessosTentados() + 1);
                desempenho.setArremessosConvertidos(desempenho.getArremessosConvertidos() + 1);
                break;
            case ARREMESSO_TENTADO:
                desempenho.setArremessosTentados(desempenho.getArremessosTentados() + 1);
                break;
            case ASSISTENCIA:
                desempenho.setAssistencias(desempenho.getAssistencias() + 1);
                break;
            case REBOTE:
                desempenho.setRebotes(desempenho.getRebotes() + 1);
                break;
            case ROUBO_DE_BOLA:
                desempenho.setRoubosDeBola(desempenho.getRoubosDeBola() + 1);
                break;
            case TOCO:
                desempenho.setTocos(desempenho.getTocos() + 1);
                break;
            case ERRO_DE_PASSE:
                desempenho.setErrosDePasse(desempenho.getErrosDePasse() + 1);
                break;
            default:
                // Eventos de evolução de atributos não alteram as estatísticas do treino.
                break;
        }
    }
}