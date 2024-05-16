package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

/*
NOTA: ACUERDATE DE CERRAR LA CONEXION A LA BASE DE DATOS QUE SERIA EL "con" Y TAMBIÉN EL STATEMENT 
Y SI USAS UN RESULSET TAMBIÉN.Y ESO LO HACES USANDO EL TRY WITH RESOURCES.PRIMERO TIENES QUE DECLARAR
LAS VARIABLES "con" "statement" "resulset" COMO FINAL LUEGO USAR LOS TRY SOLOAMENTE EL TRY CON PARENTESIS
DONDE IRAN LAS VARIABLES QUE DESEAS QUE SE CIERREN AUTOMATICAMENTE.
Recuerda que el uso de "try-with-resources" es más relevante cuando trabajas con recursos que necesitan ser cerrados 
automáticamente para liberar memoria y mantener la integridad del sistema. 
*/

public class ProductoController {
	//aprendimos un nuevo patrón de diseño que es el DAO. La finalidad de este patrón de diseño es tener un objeto que tiene 
	//como responsabilidad acceder a la base de datos y realizar las operaciones necesarias sobre la entidad.
	private ProductoDAO productoDAO;
	
	public ProductoController() {
		System.out.println("Ejecutandose constructor del producto controller");
		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperaConexion());
	}

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		// Lo que pongas dentro del prepareStatement tiene que ser parecido a como lo
		// pongas en mysql
		// usamos el PreparedStatement por seguridad ya que existe el sql injection que
		// se utliza para vulnerar
		// el sistema sea mandando comandos como eliminar la base de datos por los
		// campos solicitados
		try (con) {
			final PreparedStatement statement = con.prepareStatement(
					"UPDATE PRODUCTO SET " + " NOMBRE = ?, " + " DESCRIPCION = ?," + " CANTIDAD = ?" + " WHERE ID = ?");
			try (statement) {
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);
				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;
			}
		}
	}

	public int eliminar(Integer id) throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con) {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
			try (statement) {
				statement.setInt(1, id);
				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;
			}
		}
	}

	public List<Producto> listar() {
		return productoDAO.listar();
	}
	
	public List<Producto> listar(Categoria categoria){
		return productoDAO.listar(categoria.getId());
	}

	public void guardar(Producto producto,Integer categoriaId) {
		producto.setCategoriaId(categoriaId);
		productoDAO.guardar(producto);
		
	}
}
