package com.br.joao.basquete_api.domain.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios") // ou o nome que você deu à sua tabela de usuários
public class Usuario {

    @Id
    private String id; // ID do Google

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "foto_url")
    private String fotoUrl;

    // 👇👇👇 ADICIONE ESTA LINHA 👇👇👇
    @Column(nullable = false)
    private String role; // Ex: "ROLE_USER", "ROLE_ADMIN"

}