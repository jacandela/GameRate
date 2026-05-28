package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.service.Catalogo.ICatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @Autowired
    private ICatalogoService catalogoService;

    @GetMapping("/generos")
    public ResponseEntity<List<String>> obtenerGeneros() {
        return ResponseEntity.ok(catalogoService.obtenerTodosLosGeneros());
    }

    @GetMapping("/plataformas")
    public ResponseEntity<List<String>> obtenerPlataformas() {
        return ResponseEntity.ok(catalogoService.obtenerTodasLasPlataformas());
    }
}