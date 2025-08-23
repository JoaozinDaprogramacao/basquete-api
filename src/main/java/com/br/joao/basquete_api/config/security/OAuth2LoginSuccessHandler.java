package com.br.joao.basquete_api.config.security;

import com.br.joao.basquete_api.app.repository.UsuarioRepository;
import com.br.joao.basquete_api.domain.usuario.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String id = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");
        String fotoUrl = oAuth2User.getAttribute("picture");

        // Salva ou atualiza o usuário no banco de dados
        usuarioRepository.findByEmail(email).orElseGet(() -> {
            Usuario novoUsuario = new Usuario(id, nome, email, fotoUrl);
            return usuarioRepository.save(novoUsuario);
        });

        String token = tokenProvider.createToken(authentication);

        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/login/success")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}