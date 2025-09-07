package com.br.joao.basquete_api.infra;

import com.br.joao.basquete_api.app.service.JogadorService;
import com.br.joao.basquete_api.domain.jogador.Jogador;
import com.br.joao.basquete_api.domain.jogador.dto.JogadorCreateDTO;
import com.br.joao.basquete_api.domain.jogador.dto.JogadorResponseDTO;
import com.br.joao.basquete_api.domain.jogador.dto.JogadorUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/jogadores")
public class JogadorController {

    private final JogadorService jogadorService;

    @Autowired
    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    // POST (Create)
    @PostMapping
    public ResponseEntity<JogadorResponseDTO> criarJogador(@Valid @RequestBody JogadorCreateDTO jogadorDTO) {
        Jogador jogadorSalvo = jogadorService.criarJogador(jogadorDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(jogadorSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(new JogadorResponseDTO(jogadorSalvo));
    }

    // GET (Get All)
    @GetMapping
    public ResponseEntity<List<JogadorResponseDTO>> listarTodosJogadores() {
        List<Jogador> jogadores = jogadorService.listarTodos();
        // Mapeia a lista de Entidades para uma lista de DTOs de resposta
        List<JogadorResponseDTO> responseDTOs = jogadores.stream()
                .map(JogadorResponseDTO::new) // Usa o construtor do record
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // GET (Get by ID)
    @GetMapping("/{id}")
    public ResponseEntity<JogadorResponseDTO> buscarJogadorPorId(@PathVariable UUID id) {
        Jogador jogador = jogadorService.buscarPorId(id);
        return ResponseEntity.ok(new JogadorResponseDTO(jogador));
    }

    // PUT (Update)
    @PutMapping("/{id}")
    public ResponseEntity<JogadorResponseDTO> atualizarJogador(@PathVariable UUID id, @Valid @RequestBody JogadorUpdateDTO jogadorDTO) {
        Jogador jogadorAtualizado = jogadorService.atualizarJogador(id, jogadorDTO);
        return ResponseEntity.ok(new JogadorResponseDTO(jogadorAtualizado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarJogador(@PathVariable UUID id) {
        jogadorService.deletarJogador(id);
        // Retorna status 204 No Content, que é o padrão para deletes bem-sucedidos.
        return ResponseEntity.noContent().build();
    }
}