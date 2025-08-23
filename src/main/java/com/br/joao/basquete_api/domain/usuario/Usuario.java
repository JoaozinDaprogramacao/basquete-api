package com.br.joao.basquete_api.domain.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera Getters, Setters, toString, equals e hashCode
@NoArgsConstructor // Gera um construtor sem argumentos (requerido pelo JPA)
@AllArgsConstructor // Gera um construtor com todos os argumentos
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    private String id; // Vindo do atributo 'sub' do Google

    private String nome;
    private String email;
    private String fotoUrl;
}