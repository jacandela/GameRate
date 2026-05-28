package com.gamerate.gamerate.repository;

import com.gamerate.gamerate.entity.Plataforma;
import org.springframework.data.repository.CrudRepository; // 🌟 Cambiado a CrudRepository
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPlataformaRepository extends CrudRepository<Plataforma, Long> {
    Optional<Plataforma> findByNombre(String nombre);
}