/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.Conexion;
import model.Categoria;
import model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // a) INSERTAR
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO producto (nombre, precio, cantidad, id_categoria) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setInt(4, p.getCategoria().getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // b) LISTAR (Con JOIN a la tabla categoria)
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.cantidad, " +
                     "c.id_categoria, c.nombre AS categoria_nombre " +
                     "FROM producto p JOIN categoria c ON p.id_categoria = c.id_categoria";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("id_categoria"));
                cat.setNombre(rs.getString("categoria_nombre"));

                Producto prod = new Producto();
                prod.setIdProducto(rs.getInt("id_producto"));
                prod.setNombre(rs.getString("nombre"));
                prod.setPrecio(rs.getDouble("precio"));
                prod.setCantidad(rs.getInt("cantidad"));
                prod.setCategoria(cat); // Asignamos el objeto Categoria construido
                
                lista.add(prod);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos con JOIN: " + e.getMessage());
        }
        return lista;
    }

    // c) BUSCAR POR ID
    public Producto buscarPorId(int id) {
        Producto prod = null;
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.cantidad, " +
                     "c.id_categoria, c.nombre AS categoria_nombre " +
                     "FROM producto p JOIN categoria c ON p.id_categoria = c.id_categoria " +
                     "WHERE p.id_producto = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setIdCategoria(rs.getInt("id_categoria"));
                    cat.setNombre(rs.getString("categoria_nombre"));

                    prod = new Producto();
                    prod.setIdProducto(rs.getInt("id_producto"));
                    prod.setNombre(rs.getString("nombre"));
                    prod.setPrecio(rs.getDouble("precio"));
                    prod.setCantidad(rs.getInt("cantidad"));
                    prod.setCategoria(cat);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto por ID: " + e.getMessage());
        }
        return prod;
    }

    // d) ACTUALIZAR
    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, cantidad = ?, id_categoria = ? WHERE id_producto = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setInt(4, p.getCategoria().getIdCategoria());
            ps.setInt(5, p.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // e) ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}