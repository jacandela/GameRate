package com.gamerate.gamerate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "desarrolladores")
@Data
@NoArgsConstructor
public class Desarrollador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    private String pais;
}
