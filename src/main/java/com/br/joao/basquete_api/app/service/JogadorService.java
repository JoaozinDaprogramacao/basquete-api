package com.br.joao.basquete_api.app.service;

import com.br.joao.basquete_api.app.exception.ResourceNotFoundException;
import com.br.joao.basquete_api.app.repository.JogadorRepository;
import com.br.joao.basquete_api.domain.jogador.Jogador;
import com.br.joao.basquete_api.domain.jogador.dto.JogadorCreateDTO;
import com.br.joao.basquete_api.domain.jogador.dto.JogadorUpdateDTO; // Você precisará criar este DTO
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;

    public JogadorService(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    // CREATE
    @Transactional
    public Jogador criarJogador(JogadorCreateDTO dto) {
        Jogador novoJogador = dto.toJogador();
        return jogadorRepository.save(novoJogador);
    }

    // READ (Get All)
    @Transactional(readOnly = true)
    public List<Jogador> listarTodos() {
        return jogadorRepository.findAll();
    }

    // READ (Get by ID)
    @Transactional(readOnly = true)
    public Jogador buscarPorId(UUID id) {
        return jogadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado com o ID: " + id));
    }

    // UPDATE
    @Transactional
    public Jogador atualizarJogador(UUID id, JogadorUpdateDTO dto) {
        Jogador jogadorExistente = buscarPorId(id);

        // Atualiza os campos básicos (exemplo)
        // O JogadorUpdateDTO deve conter os campos que podem ser alterados
        jogadorExistente.setNome(dto.nome());
        jogadorExistente.setNomeResponsavel(dto.nomeResponsavel());
        jogadorExistente.setContatoResponsavel(dto.contatoResponsavel());
        jogadorExistente.setRgEntregue(dto.rgEntregue());
        jogadorExistente.setModulo(dto.modulo());

        // Você também pode permitir a atualização dos atributos base aqui se fizer sentido
        // jogadorExistente.setAtributosFisicos(dto.atributosFisicos());

        return jogadorRepository.save(jogadorExistente);
    }

    // DELETE
    @Transactional
    public void deletarJogador(UUID id) {
        if (!jogadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Jogador não encontrado com o ID: " + id);
        }
        jogadorRepository.deleteById(id);
    }
}