package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

	private Connection con;

	public ProductoDAO(Connection con) {
		this.con = con;
	}

	public void guardar(Producto producto) {
		
		try {
			/*
			 * Con esto tomamos el control de la transaccion Con eso nosotros configuramos
			 * acá que la conexión no va más a tener el control de la transacción y el que
			 * tiene el control de la transacción somos nosotros, y nosotros ahora podemos
			 * decir dónde el commit va a ser realizado.
			 */
			// con.setAutoCommit(false);
			// Nuestra logica del insert
			// Statement.Return_Generated_Keys --> Con esto podemos tomar los id que fueron
			// creados con los insert
			final PreparedStatement statement = con.prepareStatement(
					"INSERT INTO PRODUCTO " + "(nombre,descripcion,cantidad,categoria_id)" + "VALUES (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			try (statement) {
				ejecutaRegistro(producto, statement);
				// Sino hay ningun error ejecuta el registro
				// con.commit();
				// System.out.println("COMMIT");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
			// Si existe algun error no ejecuta el registro.Por ejemplo ingreso 60,pero yo
			// he puesto que solo
			// debe registrar como maximo 50 cantidad.Con este rollback hara q no me
			// registre nada.Es <= a 50
			// o nada.
			// con.rollback();
			// System.out.println("ROLLBACK");
		}

	}

	private void ejecutaRegistro(Producto producto, PreparedStatement statement) throws SQLException {

		statement.setString(1, producto.getNombre());
		statement.setString(2, producto.getDescripcion());
		statement.setInt(3, producto.getCantidad());
		statement.setInt(4, producto.getCategoriaId());

		statement.execute();
		// Aca estamos tomando el listado de id que fueron creados.Ya que en un insert
		// podemos insertar mas de un valor
		final ResultSet resultSet = statement.getGeneratedKeys();
		// Recorriendo mi listado de id q esta en resultSet
		// Recorriendo el conjunto de resultados para obtener los IDs generados
		// Utiliza resultSet.getInt(1) para obtener el valor del primer campo (el ID) de
		// la fila actual del conjunto de resultados.
		// En el caso de resultSet.getInt(0), esta notación no es válida en Java porque
		// los índices de columna en un ResultSet
		// se basan en uno, no en cero.
		// En una base de datos, las columnas se numeran a partir de 1. Por lo tanto,
		// cuando llamas a resultSet.getInt(1), estás
		// obteniendo el valor de la primera columna del conjunto de resultados.
		try (resultSet) {
			while (resultSet.next()) {
				producto.setId(resultSet.getInt(1));
				System.out.println(String.format("Fue insertado el %s", producto));
			}
		}
	}

	public List<Producto> listar() {
		List<Producto> resultadoList = new ArrayList<>();

		try {
			final PreparedStatement statement = con
					.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");
			try (statement) {
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				try (resultSet) {
					while (resultSet.next()) {
						Producto fila = new Producto(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
								resultSet.getString("DESCRIPCION"), resultSet.getInt("CANTIDAD"));
						resultadoList.add(fila);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultadoList;
	}

	public List<Producto> listar(Integer categoriaId) {
		
		List<Producto> resultadoList = new ArrayList<>();

		try {
			final PreparedStatement statement = con
					.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD "
							+ "FROM PRODUCTO"
							+" WHERE CATEGORIA_ID = ?");
			try (statement) {
				statement.setInt(1, categoriaId);
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				try (resultSet) {
					while (resultSet.next()) {
						Producto fila = new Producto(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
								resultSet.getString("DESCRIPCION"), resultSet.getInt("CANTIDAD"));
						resultadoList.add(fila);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultadoList;
	}
}
