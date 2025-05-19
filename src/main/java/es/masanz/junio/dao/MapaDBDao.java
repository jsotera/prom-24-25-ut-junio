package es.masanz.junio.dao;

import es.masanz.junio.dto.Mapa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MapaDBDao {

    private String schema = "prom_junio";
    private String jdbcURL = "jdbc:mysql://localhost:3306/"+schema;
    private String user = "root";
    private String pass = "root";

    private Connection conexion;

    public void establecerConexion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(jdbcURL, user, pass);
            System.out.println("Conexión establecida");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Mapa> consultarMapas(){

        List<Mapa> nombreMapas = new ArrayList<>();

        if(conexion==null){
            establecerConexion();
        }

        String sql = "SELECT nombre, filas, columnas FROM mapas";

        PreparedStatement pst = null;
        try {
            pst = conexion.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int filas = rs.getInt("filas");
                int columnas = rs.getInt("columnas");
                String nombreMapa = rs.getString("nombre");
                Mapa mapa = new Mapa(nombreMapa, filas, columnas);
                nombreMapas.add(mapa);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nombreMapas;
    }

    public boolean insertarMapa(String nombre, int filas, int columnas){
        boolean todoBien = false;
        try {
            String sql = "INSERT INTO mapas VALUES (?, ?, ?) ";
            PreparedStatement pst = conexion.prepareStatement(sql) ;
            pst.setString(1, nombre);
            pst.setInt(2, filas);
            pst.setInt(3, columnas);
            int numero = pst.executeUpdate();
            if(numero>0){
                todoBien = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return todoBien;
    }

    public boolean actualizarMapa(String nombre, int filas, int columnas){
        boolean todoBien = false;
        try {
            String sql = "UPDATE mapas SET filas = ?, columnas = ? WHERE nombre like ? ";
            PreparedStatement pst = conexion.prepareStatement(sql) ;
            pst.setInt(1, filas);
            pst.setInt(2, columnas);
            pst.setString(3, nombre);
            int numero = pst.executeUpdate();
            if(numero>0){
                todoBien = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return todoBien;
    }

}
