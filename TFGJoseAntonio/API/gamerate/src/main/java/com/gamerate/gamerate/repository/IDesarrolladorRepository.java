package com.gamerate.gamerate.repository;

import com.gamerate.gamerate.entity.Desarrollador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IDesarrolladorRepository extends CrudRepository<Desarrollador, Long> {

    Optional<Desarrollador> findByNombre(String nombre);
}