package com.alura.jdbc.controller;

import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.dao.CategoriaDao;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;

public class CategoriaController {

	private CategoriaDao categoriaDao;
	
	public CategoriaController() {
		System.out.println("Ejecutandose constructor de categoria controller");
		var factory = new ConnectionFactory();
		this.categoriaDao = new CategoriaDao(factory.recuperaConexion());
	}
	
	public List<Categoria> listar() {
		
		return categoriaDao.listar();
	}

    public List<Categoria> cargaReporte() {
        return this.categoriaDao.listarConProductos();
    }

}
