package es.masanz.junio.dao;

import java.sql.*;

public class EditorDBDao {

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

    public void consultarMapa(String nombre, String[][] mapa){
        if(conexion==null){
            establecerConexion();
        }

        String sql = "SELECT fila, columna, sprite FROM editor_mapa WHERE nombre LIKE ? ";

        PreparedStatement pst = null;
        try {
            pst = conexion.prepareStatement(sql);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int fila = rs.getInt("fila");
                int columna = rs.getInt("columna");
                String sprite = rs.getString("sprite");
                mapa[fila][columna] = sprite;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void insertarSprite(String nombre, int fila, int columna, String sprite){

        try {
            String sql = "INSERT INTO editor_mapa VALUES (null, ?, ?, ?, ?) ";
            PreparedStatement pst = conexion.prepareStatement(sql) ;
            pst.setString(1, nombre);
            pst.setInt(2, fila);
            pst.setInt(3, columna);
            pst.setString(4, sprite);
            int numero = pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean actualizarSprite(String nombre, int fila, int columna, String sprite){

        boolean exito = false;
        try {
            String sql = "UPDATE editor_mapa SET sprite = ? WHERE nombre like ? AND fila = ? AND columna = ?";
            PreparedStatement pst = conexion.prepareStatement(sql) ;
            pst.setString(1, sprite);
            pst.setString(2, nombre);
            pst.setInt(3, fila);
            pst.setInt(4, columna);
            int numero = pst.executeUpdate();
            if(numero>0){
                exito = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exito;
    }

}
