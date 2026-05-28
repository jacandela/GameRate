package com.gamerate.gamerate.repository;

import com.gamerate.gamerate.entity.Juego;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJuegoRepository extends CrudRepository<Juego, Long> {

    List<Juego> findByTituloContainingIgnoreCase(String titulo);

    boolean existsByTitulo(String titulo);

    @Query("SELECT j FROM Juego j WHERE j.desarrollador.nombre = :nombre")
    List<Juego> buscarPorNombreDesarrollador(@Param("nombre") String nombre);

    List<Juego> findByTituloContainingIgnoreCaseAndDesarrolladorNombreContainingIgnoreCase(
            String titulo,
            String desarrollador
    );
}