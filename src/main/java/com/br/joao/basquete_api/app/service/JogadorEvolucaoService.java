package com.br.joao.basquete_api.app.service;

import com.br.joao.basquete_api.domain.jogador.Jogador;
import com.br.joao.basquete_api.domain.treino.enums.TipoEvento;
import org.springframework.stereotype.Service;
import com.br.joao.basquete_api.app.repository.JogadorRepository;

@Service
public class JogadorEvolucaoService {

    private final JogadorRepository jogadorRepository;

    public JogadorEvolucaoService(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    // IMPORTANTE: Este método não precisa ser @Transactional se for chamado
    // por outro método @Transactional (como o DesempenhoService.registrarEvento)
    public void processarEvolucao(Jogador jogador, TipoEvento tipo) {
        // Exemplo de lógica de evolução:
        // A cada 2 cestas de 2 pontos, aumenta 1 ponto no atributo de arremesso.
        // Esta lógica pode ser muito mais complexa (ex: usar o total de arremessos convertidos).

        switch (tipo) {
            case CESTA_2_PONTOS:
            case CESTA_3_PONTOS:
                // A cada evento de cesta, aumenta o arremesso
                var habilidades = jogador.getHabilidadesTecnicas();
                habilidades.setArremesso(habilidades.getArremesso() + 1);
                break;

            case ASSISTENCIA:
                var mentais = jogador.getAtributosMentais();
                mentais.setVisaoDeJogo(mentais.getVisaoDeJogo() + 1);
                break;

            // Eventos diretos de "upar" atributo
            case PONTO_FISICO:
                var fisicos = jogador.getAtributosFisicos();
                fisicos.setFisico(fisicos.getFisico() + 1); // Aumenta 1 ponto de físico
                break;
            case PONTO_DRIBLE:
                var habDrible = jogador.getHabilidadesTecnicas();
                habDrible.setDrible(habDrible.getDrible() + 1);
                break;

            default:
                // Nenhum atributo permanente é alterado para este evento
                break;
        }
        // A alteração no objeto 'jogador' será persistida quando a transação do método chamador (registrarEvento) for concluída.
    }
}