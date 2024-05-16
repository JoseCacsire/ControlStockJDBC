package com.alura.jdbc.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

import javax.management.RuntimeErrorException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.alura.jdbc.controller.CategoriaController;
import com.alura.jdbc.controller.ProductoController;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class ControlDeStockFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	//agregando linea de codigo factory
	private ConnectionFactory connectionFactory;
	//
	private JLabel labelNombre, labelDescripcion, labelCantidad, labelCategoria;
	private JTextField textoNombre, textoDescripcion, textoCantidad;
	private JComboBox<Categoria> comboCategoria;
	private JButton botonGuardar, botonModificar, botonLimpiar, botonEliminar, botonReporte;
	private JTable tabla;
	private DefaultTableModel modelo;
	private ProductoController productoController;
	private CategoriaController categoriaController;
	
	
	
	public ControlDeStockFrame() {
		
		super("Productos");
		System.out.println("Instanciando CategoriaController desde StockFrame");
		this.categoriaController = new CategoriaController();
		System.out.println("Instanciando ProductoController desde StockFrame");
		this.productoController = new ProductoController();
		Container container = getContentPane();
		setLayout(null);
		// Crea una instancia de ConnectionFactory
        this.connectionFactory = new ConnectionFactory();
        //
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarConexiones();
            }
        });
        
		configurarCamposDelFormulario(container);

		configurarTablaDeContenido(container);

		configurarAccionesDelFormulario();
		
	}
	
	// Método para cerrar las conexiones
    private void cerrarConexiones() {
        // Aquí cerrarás las conexiones a la base de datos
        // Puedes llamar al método adecuado de tu ConnectionFactory
        // por ejemplo:
        this.connectionFactory.cerrarConexiones();
    }

	private void configurarTablaDeContenido(Container container) {
		tabla = new JTable();

		modelo = (DefaultTableModel) tabla.getModel();
		modelo.addColumn("Identificador del Producto");
		modelo.addColumn("Nombre del Producto");
		modelo.addColumn("Descripción del Producto");
		modelo.addColumn("Cantidad");

		cargarTabla();

		tabla.setBounds(10, 205, 760, 280);

		botonEliminar = new JButton("Eliminar");
		botonModificar = new JButton("Modificar");
		botonReporte = new JButton("Ver Reporte");
		botonEliminar.setBounds(10, 500, 80, 20);
		botonModificar.setBounds(100, 500, 80, 20);
		botonReporte.setBounds(190, 500, 80, 20);

		container.add(tabla);
		container.add(botonEliminar);
		container.add(botonModificar);
		container.add(botonReporte);

		setSize(800, 600);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	private void configurarCamposDelFormulario(Container container) {
		labelNombre = new JLabel("Nombre del Producto");
		labelDescripcion = new JLabel("Descripción del Producto");
		labelCantidad = new JLabel("Cantidad");
		labelCategoria = new JLabel("Categoría del Producto");

		labelNombre.setBounds(10, 10, 240, 15);
		labelDescripcion.setBounds(10, 50, 240, 15);
		labelCantidad.setBounds(10, 90, 240, 15);
		labelCategoria.setBounds(10, 130, 240, 15);

		labelNombre.setForeground(Color.BLACK);
		labelDescripcion.setForeground(Color.BLACK);
		labelCategoria.setForeground(Color.BLACK);

		textoNombre = new JTextField();
		textoDescripcion = new JTextField();
		textoCantidad = new JTextField();
		comboCategoria = new JComboBox<>();
		comboCategoria.addItem(new Categoria(0,"Elige una Categoría"));

		// TODO
		var categorias = this.categoriaController.listar();
		categorias.forEach(categoria -> comboCategoria.addItem(categoria));

		textoNombre.setBounds(10, 25, 265, 20);
		textoDescripcion.setBounds(10, 65, 265, 20);
		textoCantidad.setBounds(10, 105, 265, 20);
		comboCategoria.setBounds(10, 145, 265, 20);

		botonGuardar = new JButton("Guardar");
		botonLimpiar = new JButton("Limpiar");
		botonGuardar.setBounds(10, 175, 80, 20);
		botonLimpiar.setBounds(100, 175, 80, 20);

		container.add(labelNombre);
		container.add(labelDescripcion);
		container.add(labelCantidad);
		container.add(labelCategoria);
		container.add(textoNombre);
		container.add(textoDescripcion);
		container.add(textoCantidad);
		container.add(comboCategoria);
		container.add(botonGuardar);
		container.add(botonLimpiar);
	}

	private void configurarAccionesDelFormulario() {
		botonGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
				limpiarTabla();
				cargarTabla();
			}
		});

		botonLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiarFormulario();
			}
		});

		botonEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
				limpiarTabla();
				cargarTabla();
			}
		});

		botonModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modificar();
				limpiarTabla();
				cargarTabla();
			}
		});

		botonReporte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirReporte();
			}
		});
	}

	private void abrirReporte() {
		new ReporteFrame(this);
	}

	private void limpiarTabla() {
		modelo.getDataVector().clear();
	}

	private boolean tieneFilaElegida() {
		return tabla.getSelectedRowCount() == 0 || tabla.getSelectedColumnCount() == 0;
	}

	private void modificar() {
		if (tieneFilaElegida()) {
			JOptionPane.showMessageDialog(this, "Por favor, elije un item");
			return;
		}

		Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()))
				.ifPresentOrElse(fila -> {
					Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());
					String nombre = (String) modelo.getValueAt(tabla.getSelectedRow(), 1);
					String descripcion = (String) modelo.getValueAt(tabla.getSelectedRow(), 2);
					Integer cantidad = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 3).toString());
					int contador;
					try {
						contador = this.productoController.modificar(nombre, descripcion, cantidad, id);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
					JOptionPane.showMessageDialog(this, contador + " Item modificado con éxito!");
				}, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
	}

	private void eliminar() {
		if (tieneFilaElegida()) {
			JOptionPane.showMessageDialog(this, "Por favor, elije un item");
			return;
		}

		Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()))
				.ifPresentOrElse(fila -> {
					Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());

					int contador;
					try {
						contador = this.productoController.eliminar(id);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}

					modelo.removeRow(tabla.getSelectedRow());

					JOptionPane.showMessageDialog(this, contador + " Item eliminado con éxito!");
				}, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
	}

	private void cargarTabla() {

		var productos = this.productoController.listar();
		// Para enviar los valores a la pantalla.Osea los registros que tenemos en mysql
		productos.forEach(producto -> modelo.addRow(new Object[] {producto.getId(), producto.getNombre(),
				producto.getDescripcion(), producto.getCantidad()}));

	}

	private void guardar() {
		if (textoNombre.getText().isBlank() || textoDescripcion.getText().isBlank()) {
			JOptionPane.showMessageDialog(this, "Los campos Nombre y Descripción son requeridos.");
			return;
		}

		Integer cantidadInt;

		try {
			cantidadInt = Integer.parseInt(textoCantidad.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, String
					.format("El campo cantidad debe ser numérico dentro del rango %d y %d.", 0, Integer.MAX_VALUE));
			return;
		}

		// Haciendolo por hashmap sin usar un modelo producto

		// Creando un objeto "producto" de tipo HashMap con clave y valor
		// var producto = new HashMap<String,String>();

		// Agregando su clave y valor para cada elemento

		/*
		 * producto.put("NOMBRE", textoNombre.getText()); producto.put("DESCRIPCION",
		 * textoDescripcion.getText()); producto.put("CANTIDAD",
		 * String.valueOf(cantidadInt));
		 */

		// Usando el modelo producto

		var producto = new Producto(textoNombre.getText(), textoDescripcion.getText(), cantidadInt);

		var categoria = (Categoria) comboCategoria.getSelectedItem();
		System.out.println(categoria);

		this.productoController.guardar(producto,categoria.getId());

		JOptionPane.showMessageDialog(this, "Registrado con éxito!");

		this.limpiarFormulario();
	}

	private void limpiarFormulario() {
		this.textoNombre.setText("");
		this.textoDescripcion.setText("");
		this.textoCantidad.setText("");
		this.comboCategoria.setSelectedIndex(0);
	}

}
