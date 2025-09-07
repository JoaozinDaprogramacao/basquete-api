package com.br.joao.basquete_api.config.security;

import com.br.joao.basquete_api.app.repository.UsuarioRepository;
import com.br.joao.basquete_api.app.service.LogoutService;
import com.br.joao.basquete_api.domain.usuario.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String jwt = getJwtFromRequest(request);

        // Se não há token, a requisição segue para a próxima etapa.
        // Se for um endpoint público, será permitido. Se for protegido, o Spring barrará.
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Se o token estiver na blocklist (logout), a requisição é barrada.
            if (logoutService.isTokenBlocked(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido (logout).");
                return; // Interrompe a cadeia
            }

            // Valida o token e, se válido, configura a autenticação no contexto de segurança.
            if (tokenProvider.validateToken(jwt)) {
                String userEmail = tokenProvider.getEmailFromToken(jwt);

                // Buscamos o usuário no banco pelo email que está no token.
                Usuario user = usuarioRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + userEmail));

                // **MELHORIA:** Carregamos as permissões (roles) reais do usuário.
                // Assumindo que a entidade Usuario tem um método getRole() que retorna uma String (ex: "ROLE_ADMIN").
                // Se seu sistema de permissões for mais complexo, ajuste esta lógica.
                List<GrantedAuthority> authorities = user.getRole() != null ?
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole())) :
                        Collections.emptyList();

                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getEmail(), "", authorities);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception ex) {
            // Se qualquer exceção ocorrer durante a validação do token ou busca do usuário,
            // a requisição é barrada com status 401 Unauthorized.
            logger.error("Não foi possível setar a autenticação do usuário no contexto de segurança", ex);
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
            return; // Interrompe a cadeia
        }

        // Continua a cadeia de filtros apenas se a autenticação for bem-sucedida.
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