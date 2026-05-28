package com.example.gamerate;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Videojuego implements Serializable {

    private Long id;
    private String titulo;

    @SerializedName("nombreDesarrollador")
    private String desarrollador;

    private String descripcion;
    private Double nota;

    @SerializedName("imagenUrl")
    private String imagen;

    @SerializedName("plataformas")
    private List<String> plataformas;

    @SerializedName("generos")
    private List<String> generos;

    public Videojuego() {
    }

    // 🌟 GETTERS Y SETTERS PARA PLATAFORMAS Y GÉNEROS
    public List<String> getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(List<String> plataformas) {
        this.plataformas = plataformas;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }


    public Long getId() {return id;}
    public String getTitulo() { return titulo; }
    public String getDesarrollador() { return desarrollador; }
    public String getDescripcion() { return descripcion; }
    public Double getNota() { return nota; }
    public String getImagen() { return imagen; }

    public void setId(Long id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDesarrollador(String desarrollador) { this.desarrollador = desarrollador; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setNota(Double nota) { this.nota = nota; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}