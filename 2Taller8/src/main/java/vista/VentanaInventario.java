/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import model.Categoria;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VentanaInventario extends JFrame {
    
    private JTextField txtIdOculto; 
    private JTextField txtNombre, txtPrecio, txtCantidad;
    private JComboBox<Categoria> cmbCategoria;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    
    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;

    public VentanaInventario() {
        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();

        
        setTitle("Inventario — Tienda de Don Ernesto");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        txtIdOculto = new JTextField(); // Oculto pero útil para saber qué editar/eliminar

        // --- PANEL SUPERIOR: FORMULARIO ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        panelFormulario.add(txtNombre, gbc);

        // Fila 2: Precio
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrecio = new JTextField(15);
        panelFormulario.add(txtPrecio, gbc);

        // Fila 3: Cantidad
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCantidad = new JTextField(15);
        panelFormulario.add(txtCantidad, gbc);

        // Fila 4: Categoría
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbCategoria = new JComboBox<>();
        panelFormulario.add(cmbCategoria, gbc);

        // --- PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelFormulario, BorderLayout.CENTER);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Cant.", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celdas no editables directamente
            }
        };
        tablaProductos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        
        cargarCategoriasEnCombo();
        cargarTablaProductos();

        
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaProductos.getSelectedRow();
                if (fila != -1) {
                    txtIdOculto.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtPrecio.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
                    
                    String catNombre = modeloTabla.getValueAt(fila, 4).toString();
                    for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                        if (cmbCategoria.getItemAt(i).getNombre().equals(catNombre)) {
                            cmbCategoria.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });

       
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                Categoria cat = (Categoria) cmbCategoria.getSelectedItem();

                Producto p = new Producto();
                p.setNombre(nombre);
                p.setPrecio(precio);
                p.setCantidad(cantidad);
                p.setCategoria(cat);

                if (productoDAO.insertar(p)) {
                    JOptionPane.showMessageDialog(this, "¡Producto guardado con éxito!");
                    cargarTablaProductos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo guardar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce números válidos en Precio y Cantidad.", "Formato incorrecto", JOptionPane.WARNING_MESSAGE);
            }
        });

        
        btnActualizar.addActionListener(e -> {
            if (txtIdOculto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int id = Integer.parseInt(txtIdOculto.getText());
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                Categoria cat = (Categoria) cmbCategoria.getSelectedItem();

                Producto p = new Producto();
                p.setIdProducto(id);
                p.setNombre(nombre);
                p.setPrecio(precio);
                p.setCantidad(cantidad);
                p.setCategoria(cat);

                if (productoDAO.actualizar(p)) {
                    JOptionPane.showMessageDialog(this, "¡Producto actualizado correctamente!");
                    cargarTablaProductos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Revisa que los campos numéricos sean correctos.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 4. Botón Eliminar
        btnEliminar.addActionListener(e -> {
            if (txtIdOculto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtIdOculto.getText());
                if (productoDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "¡Producto eliminado!");
                    cargarTablaProductos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 5. Botón Limpiar
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void cargarCategoriasEnCombo() {
        List<Categoria> lista = categoriaDAO.listar();
        cmbCategoria.removeAllItems();
        for (Categoria c : lista) {
            cmbCategoria.addItem(c);
        }
    }

    private void cargarTablaProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = productoDAO.listar();
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                p.getPrecio(),
                p.getCantidad(),
                p.getCategoria().getNombre()
            });
        }
    }

    private void limpiarCampos() {
        txtIdOculto.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
        tablaProductos.clearSelection();
    }
}