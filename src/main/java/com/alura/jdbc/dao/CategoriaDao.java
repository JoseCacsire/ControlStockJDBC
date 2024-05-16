package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class CategoriaDao {

	private Connection con;

	public CategoriaDao(Connection con) {
		this.con = con;
	}

	public List<Categoria> listar() {
		List<Categoria> resultadoCategorias = new ArrayList<>();

		try {
			final PreparedStatement preparedStatement = con.prepareStatement("SELECT ID,NOMBRE FROM CATEGORIA");
			try (preparedStatement) {
				// el executeQuery ya nos devuelve un resulset
				final ResultSet resultSet = preparedStatement.executeQuery();
				try (resultSet) {
					while (resultSet.next()) {
						var categoria = new Categoria(resultSet.getInt("ID"),
									resultSet.getString("NOMBRE"));
						resultadoCategorias.add(categoria);
					}
				};
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return resultadoCategorias;
	}

	public List<Categoria> listarConProductos() {
		List<Categoria> resultadoCategorias = new ArrayList<>();
		
		try {
			var querySelect = "SELECT C.ID, C.NOMBRE, P.ID, P.NOMBRE, P.CANTIDAD "
					+ "FROM CATEGORIA C "
					+ "INNER JOIN PRODUCTO P ON C.ID = P.CATEGORIA_ID";
			final PreparedStatement preparedStatement = con.prepareStatement(querySelect);
			try (preparedStatement) {
				// el executeQuery ya nos devuelve un resulset
				final ResultSet resultSet = preparedStatement.executeQuery();
				try (resultSet) {
					while (resultSet.next()) {
						Integer categoriaId = resultSet.getInt("ID");
						String categoriaNombre = resultSet.getString("NOMBRE");
						
						var categoria = resultadoCategorias
								.stream()
								.filter(cat -> cat.getId().equals(categoriaId))
								.findAny().orElseGet(() ->{
									Categoria cat = new Categoria(categoriaId,
											categoriaNombre);
									
									resultadoCategorias.add(cat);
									
									return cat;
								});
						
						Producto producto = new Producto(resultSet.getInt("P.ID"),
								resultSet.getString("P.NOMBRE"),
								resultSet.getInt("P.CANTIDAD"));
						
						categoria.agregar(producto);
					}
				};
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return resultadoCategorias;
	}

}
