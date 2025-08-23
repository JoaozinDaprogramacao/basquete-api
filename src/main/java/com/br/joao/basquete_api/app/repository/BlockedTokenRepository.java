package com.br.joao.basquete_api.app.repository;

import com.br.joao.basquete_api.domain.blockedToken.BlockedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;

public interface BlockedTokenRepository extends JpaRepository<BlockedToken, String> {
    void deleteByExpiryDateBefore(Instant now);
}