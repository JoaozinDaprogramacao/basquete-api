package com.br.joao.basquete_api.infra;

import com.br.joao.basquete_api.app.service.TreinoService;
import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import com.br.joao.basquete_api.domain.jogador.dto.DesempenhoCreateDTO;
import com.br.joao.basquete_api.domain.jogador.dto.DesempenhoResponseDTO;
import com.br.joao.basquete_api.domain.treino.Treino;
import com.br.joao.basquete_api.domain.treino.dto.TreinoCreateDTO;
import com.br.joao.basquete_api.domain.treino.dto.TreinoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/treinos")
public class TreinoController {

    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService) {
        this.treinoService = treinoService;
    }

    // --- Endpoints para Gerenciamento de Treinos (CRUD) ---

    @PostMapping
    public ResponseEntity<TreinoResponseDTO> criarTreino(@Valid @RequestBody TreinoCreateDTO treinoDTO) {
        Treino treinoSalvo = treinoService.criarTreino(treinoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(treinoSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TreinoResponseDTO(treinoSalvo));
    }

    @GetMapping
    public ResponseEntity<List<TreinoResponseDTO>> listarTodosTreinos() {
        List<Treino> treinos = treinoService.listarTodos();
        List<TreinoResponseDTO> responseDTOs = treinos.stream()
                .map(TreinoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreinoResponseDTO> buscarTreinoPorId(@PathVariable UUID id) {
        Treino treino = treinoService.buscarPorId(id);
        return ResponseEntity.ok(new TreinoResponseDTO(treino));
    }

    // --- Endpoint Principal para Lançamento de Desempenho ---

    @PostMapping("/{treinoId}/jogadores/{jogadorId}/desempenho")
    public ResponseEntity<DesempenhoResponseDTO> lancarDesempenho(
            @PathVariable UUID treinoId,
            @PathVariable UUID jogadorId,
            @Valid @RequestBody DesempenhoCreateDTO desempenhoDTO) {

        DesempenhoTreino novoDesempenho = treinoService.lancarDesempenho(treinoId, jogadorId, desempenhoDTO);

        // A URI para um recurso aninhado é um pouco mais complexa de construir,
        // mas para este caso, retornar o objeto criado com status 201 é suficiente e prático.
        return ResponseEntity.status(201).body(new DesempenhoResponseDTO(novoDesempenho));
    }
}