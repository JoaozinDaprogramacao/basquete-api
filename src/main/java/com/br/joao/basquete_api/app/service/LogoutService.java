package com.br.joao.basquete_api.app.service;

import com.br.joao.basquete_api.app.repository.BlockedTokenRepository;
import com.br.joao.basquete_api.config.security.JwtTokenProvider;
import com.br.joao.basquete_api.domain.blockedToken.BlockedToken;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class LogoutService {

    private final BlockedTokenRepository blockedTokenRepository;
    private final JwtTokenProvider tokenProvider; // Precisaremos dele para extrair a data de expiração

    public LogoutService(BlockedTokenRepository blockedTokenRepository, JwtTokenProvider tokenProvider) {
        this.blockedTokenRepository = blockedTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    public void blockToken(String token) {
        Instant expiryDate = tokenProvider.getExpirationDateFromToken(token); // Você precisará criar este método
        BlockedToken blockedToken = new BlockedToken(token, expiryDate);
        blockedTokenRepository.save(blockedToken);
    }

    public boolean isTokenBlocked(String token) {
        return blockedTokenRepository.existsById(token);
    }

    // Executa a cada hora para limpar tokens expirados da blocklist
    @Transactional
    @Scheduled(fixedRate = 3600000)
    public void cleanUpExpiredTokens() {
        blockedTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}