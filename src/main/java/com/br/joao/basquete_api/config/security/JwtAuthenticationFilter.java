package com.br.joao.basquete_api.config.security;

import com.br.joao.basquete_api.app.repository.UsuarioRepository;
import com.br.joao.basquete_api.app.service.LogoutService;
import com.br.joao.basquete_api.domain.usuario.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final LogoutService logoutService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UsuarioRepository usuarioRepository, LogoutService logoutService) {
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
        this.logoutService = logoutService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && logoutService.isTokenBlocked(jwt)) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
                return;
            }

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String userEmail = tokenProvider.getEmailFromToken(jwt);

                // Buscamos o usuário no banco pelo email que está no token
                Usuario user = usuarioRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + userEmail));

                // Criamos um UserDetails simples a partir da nossa entidade Usuario
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getEmail(), "", Collections.emptyList());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Não foi possível setar a autenticação do usuário no contexto de segurança", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}