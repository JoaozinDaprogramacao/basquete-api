package com.br.joao.basquete_api.domain.blockedToken;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BlockedToken {

    @Id
    private String token;
    private Instant expiryDate;
}