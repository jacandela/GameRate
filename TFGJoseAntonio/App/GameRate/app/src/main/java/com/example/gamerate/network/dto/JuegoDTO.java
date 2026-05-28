package com.example.gamerate.network.dto;

import java.io.Serializable;
import java.util.List;

public class JuegoDTO implements Serializable {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private String nombreDesarrollador;
    private List<String> plataformas;
    private List<String> generos;

    public JuegoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagen() { return imagenUrl; }
    public void setImagen(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getNombreDesarrollador() { return nombreDesarrollador; }
    public void setNombreDesarrollador(String nombreDesarrollador) { this.nombreDesarrollador = nombreDesarrollador; }

    public List<String> getPlataformas() { return plataformas; }
    public void setPlataformas(List<String> plataformas) { this.plataformas = plataformas; }

    public List<String> getGeneros() { return generos; }
    public void setGeneros(List<String> generos) { this.generos = generos; }
}
