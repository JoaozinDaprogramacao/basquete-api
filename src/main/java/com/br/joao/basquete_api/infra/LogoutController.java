package com.br.joao.basquete_api.infra;


import com.br.joao.basquete_api.app.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    private final LogoutService logoutService;

    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        if (StringUtils.hasText(token)) {
            logoutService.blockToken(token);
            return ResponseEntity.ok("Logout bem-sucedido.");
        }
        return ResponseEntity.badRequest().body("Nenhum token encontrado na requisição.");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}