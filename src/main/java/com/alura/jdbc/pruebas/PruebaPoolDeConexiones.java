package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.alura.jdbc.factory.ConnectionFactory;

public class PruebaPoolDeConexiones {

	public static void main(String[] args) throws SQLException {
//		ConnectionFactory connectionFactory = new ConnectionFactory();
//
//		for (int i = 0; i < 20; i++) {
//			//Esto esta mál por que estas generando el pool de conexiones en cada recorrido
//			//Ya que estas instanciando la clase y ejecutandose el constructor por cada recorrido.
//			//Por lo tanto es recomendable generar una única instancia de la clase connectionfactory fuera del for
//			//Esto garantiza que ambas pruebas utilicen el mismo pool de conexiones y respeten el limite que es 10 conexiones
//			//Esta mal  --> Connection con = new ConnectionFactory().recuperaConexion();
//
//			//Correcion
//			Connection con = connectionFactory.recuperaConexion();
//			System.out.println("Abriendo la conexiòn de nùmero "+(i+1));
//
//		}
	}

}
