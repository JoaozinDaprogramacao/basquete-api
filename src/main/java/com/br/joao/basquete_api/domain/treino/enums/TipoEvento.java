package com.br.joao.basquete_api.domain.treino.enums;

public enum TipoEvento {
    // Eventos de Jogo (atualizam DesempenhoTreino)
    CESTA_1_PONTO,
    CESTA_2_PONTOS,
    CESTA_3_PONTOS,
    ARREMESSO_TENTADO,
    ASSISTENCIA,
    REBOTE,
    ROUBO_DE_BOLA,
    TOCO,
    ERRO_DE_PASSE,

    // Eventos de Evolução (atualizam atributos permanentes do Jogador)
    // Opcional: Você pode querer evoluir atributos baseado nos eventos de jogo
    // ou ter eventos específicos para isso.
    PONTO_FISICO,
    PONTO_DRIBLE,
    PONTO_ARREMESSO,
    PONTO_BANDEJA,
    PONTO_PASSE,
    PONTO_VISAO_DE_JOGO
}