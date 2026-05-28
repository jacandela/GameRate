package com.gamerate.gamerate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "juegos")
@Data
@NoArgsConstructor
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_lanzamiento")
    private java.time.LocalDate fechaLanzamiento;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "desarrollador_id")
    private Desarrollador desarrollador;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "juego_generos",
            joinColumns = @JoinColumn(name = "juego_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Genero> generos;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "juego_plataformas",
            joinColumns = @JoinColumn(name = "juego_id"),
            inverseJoinColumns = @JoinColumn(name = "plataforma_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Plataforma> plataformas;
}
