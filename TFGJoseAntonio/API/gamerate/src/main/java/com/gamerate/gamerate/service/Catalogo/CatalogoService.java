package com.gamerate.gamerate.service.Catalogo;

import com.gamerate.gamerate.entity.Genero;
import com.gamerate.gamerate.entity.Plataforma;
import com.gamerate.gamerate.repository.IGeneroRepository;
import com.gamerate.gamerate.repository.IPlataformaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CatalogoService implements ICatalogoService {

    @Autowired
    private IGeneroRepository generoRepository;

    @Autowired
    private IPlataformaRepository plataformaRepository;

    @Override
    public List<String> obtenerTodosLosGeneros() {
        return StreamSupport.stream(generoRepository.findAll().spliterator(), false)
                .map(Genero::getNombre)
                .toList();
    }

    @Override
    public List<String> obtenerTodasLasPlataformas() {
        return StreamSupport.stream(plataformaRepository.findAll().spliterator(), false)
                .map(Plataforma::getNombre)
                .toList();
    }
}