package com.gamerate.gamerate.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "plataformas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plataforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @ManyToMany(mappedBy = "plataformas")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Juego> juegos;
}
