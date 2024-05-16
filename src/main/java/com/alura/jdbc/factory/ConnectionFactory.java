package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
public class ConnectionFactory {
	private DataSource dataSource;
	
	public ConnectionFactory() {
		String jdbcUrl = "jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC";
		String username = "root";
		String password = "1234";
		var pooledDataSource = new ComboPooledDataSource();
		pooledDataSource.setJdbcUrl(jdbcUrl);
		pooledDataSource.setUser(username);
		pooledDataSource.setPassword(password);
		pooledDataSource.setMaxPoolSize(10);
		
		this.dataSource = pooledDataSource;
	}
	
	public Connection recuperaConexion() {
		
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void cerrarConexiones() {
		try {
            // Cierra las conexiones y realiza otras tareas necesarias
            // para finalizar correctamente el pool de conexiones
            // Por ejemplo, si estás utilizando ComboPooledDataSource,
            // puedes llamar al método close() para cerrar el pool de conexiones
            ((ComboPooledDataSource) this.dataSource).close();
            
            System.out.println("Conexiones cerradas correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cerrar las conexiones: " + e.getMessage());
        }
    }
	
}

//Esto iba antes
//return DriverManager.getConnection(jdbcUrl,username,password);

