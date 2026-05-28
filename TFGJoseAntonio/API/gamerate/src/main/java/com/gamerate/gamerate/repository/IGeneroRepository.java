package com.gamerate.gamerate.repository;

import com.gamerate.gamerate.entity.Genero;
import org.springframework.data.repository.CrudRepository; // 🌟 Cambiado a CrudRepository
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IGeneroRepository extends CrudRepository<Genero, Long> {
    Optional<Genero> findByNombre(String nombre);
}