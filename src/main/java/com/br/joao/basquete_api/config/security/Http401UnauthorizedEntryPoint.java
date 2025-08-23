package com.br.joao.basquete_api.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // Simplesmente envia o status 401 Unauthorized.
        // O corpo da resposta pode ser customizado se necessário.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acesso não autorizado");
    }
}