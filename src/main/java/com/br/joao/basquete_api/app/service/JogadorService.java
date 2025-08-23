package com.br.joao.basquete_api.app.service;

import com.br.joao.basquete_api.app.exception.ResourceNotFoundException;
import com.br.joao.basquete_api.app.repository.JogadorRepository;
import com.br.joao.basquete_api.domain.Jogador.Jogador;
import com.br.joao.basquete_api.domain.Jogador.dto.JogadorCreateDTO;
import com.br.joao.basquete_api.domain.Jogador.dto.JogadorUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;

    @Autowired
    public JogadorService(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    // CREATE
    @Transactional
    public Jogador criarJogador(JogadorCreateDTO dto) {
        int anoAtual = LocalDate.now().getYear();
        int idade = anoAtual - dto.anoNascimento();
        Jogador novoJogador = dto.toJogador(idade);
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
        // Primeiro, busca o jogador existente. O método buscarPorId já trata o caso de não encontrar.
        Jogador jogadorExistente = buscarPorId(id);

        // Atualiza os campos
        jogadorExistente.setNome(dto.nome());
        jogadorExistente.setNomeResponsavel(dto.nomeResponsavel());
        jogadorExistente.setContatoResponsavel(dto.contatoResponsavel());
        jogadorExistente.setRgEntregue(dto.rgEntregue());

        // O save() aqui funciona como um "merge", atualizando o registro existente.
        return jogadorRepository.save(jogadorExistente);
    }

    // DELETE
    @Transactional
    public void deletarJogador(UUID id) {
        // Verifica se o jogador existe antes de deletar para poder lançar a exceção.
        if (!jogadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Jogador não encontrado com o ID: " + id);
        }
        jogadorRepository.deleteById(id);
    }
}