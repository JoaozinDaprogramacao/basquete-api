package com.br.joao.basquete_api.infra;

import com.br.joao.basquete_api.app.repository.UsuarioRepository;
import com.br.joao.basquete_api.domain.usuario.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public Usuario getUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return null;
        }

        String id = principal.getAttribute("sub");
        String nome = principal.getAttribute("name");
        String email = principal.getAttribute("email");
        String fotoUrl = principal.getAttribute("picture");

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setFotoUrl(fotoUrl);

        usuarioRepository.save(usuario);

        return usuario;
    }
}