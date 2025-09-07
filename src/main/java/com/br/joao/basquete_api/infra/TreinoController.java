package com.br.joao.basquete_api.infra;

import com.br.joao.basquete_api.app.service.DesempenhoService;
import com.br.joao.basquete_api.app.service.TreinoService;
import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import com.br.joao.basquete_api.domain.treino.Treino;
import com.br.joao.basquete_api.domain.treino.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/treinos")
public class TreinoController {

    private final TreinoService treinoService;
    private final DesempenhoService desempenhoService;

    public TreinoController(TreinoService treinoService, DesempenhoService desempenhoService) {
        this.treinoService = treinoService;
        this.desempenhoService = desempenhoService;
    }

    // 1. CRIAR UM NOVO TREINO
    @PostMapping
    public ResponseEntity<TreinoResponseDTO> criarTreino(@Valid @RequestBody TreinoCreateDTO treinoDTO) {
        Treino treinoSalvo = treinoService.criarTreino(treinoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(treinoSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TreinoResponseDTO(treinoSalvo));
    }

    // 2. ADICIONAR UM JOGADOR AO TREINO
    @PostMapping("/{treinoId}/jogadores")
    public ResponseEntity<Void> adicionarJogador(@PathVariable UUID treinoId, @Valid @RequestBody AdicionarJogadorRequestDTO request) {
        treinoService.adicionarJogadorAoTreino(treinoId, request.jogadorId());
        return ResponseEntity.ok().build();
    }

    // 3. REGISTRAR UM EVENTO DE DESEMPENHO PARA UM JOGADOR NO TREINO
    @PostMapping("/{treinoId}/jogadores/{jogadorId}/eventos")
    public ResponseEntity<DesempenhoResponseDTO> registrarEvento(
            @PathVariable UUID treinoId,
            @PathVariable UUID jogadorId,
            @Valid @RequestBody RegistrarEventoDTO eventoDTO
    ) {
        DesempenhoResponseDTO desempenhoAtualizado = desempenhoService.registrarEvento(treinoId, jogadorId, eventoDTO);
        return ResponseEntity.ok(desempenhoAtualizado);
    }
}