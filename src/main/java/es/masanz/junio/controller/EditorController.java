package es.masanz.junio.controller;

import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

public class EditorController {

    private int filas;
    private int columnas;

    private static final Logger logger = LogManager.getLogger(EditorController.class);

    public static final String SPRITES_SERVER_FOLDER = "sprites";
    public static final String SPRITES_LOCAL_FOLDER = "src/main/resources/public/"+SPRITES_SERVER_FOLDER;

    public EditorController(int filas, int columnas){
        this.filas = filas;
        this.columnas = columnas;
    }

    public void cargarEditorMapa(Context context) {
        Map<String, Object> model = new HashMap<>();

        String[] rutasImagenes = cargarSrites();
        //String[][] tablero = generarMapaVacio();
        String[][] tablero = generarMapaAleatorio(rutasImagenes);

        model.put("tablero", tablero);
        model.put("imagenes", rutasImagenes);
        context.render("/templates/index.ftl", model);
    }

    private String[] cargarSrites() {
        String[] rutasImagenes = null;
        File carpeta = new File(SPRITES_LOCAL_FOLDER);
        if(carpeta.exists() && !carpeta.isFile()){
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                int numSprites = 0;
                for (int i = 0; i < archivos.length; i++) {
                    File archivo = archivos[i];
                    if (archivo.isFile() && archivo.getName().endsWith(".png")) {
                        numSprites++;
                    }
                }
                rutasImagenes = new String[numSprites];
                int auxPos = 0;
                for (int i = 0; i < archivos.length; i++) {
                    File archivo = archivos[i];
                    if (archivo.isFile() && archivo.getName().endsWith(".png")) {
                        String nombreArchivo = archivo.getName();
                        String rutaWeb = "/" + SPRITES_SERVER_FOLDER + "/" + nombreArchivo;
                        rutasImagenes[auxPos] = rutaWeb;
                        auxPos++;
                    }
                }
            }
        }
        return rutasImagenes;
    }

    private String[][] generarMapaAleatorio(String[] rutasImagenes) {
        String[][] tablero = new String[this.filas][this.columnas];
        for (int fila = 0; fila < this.filas; fila++) {
            for (int columna = 0; columna < this.columnas; columna++) {
                tablero[fila][columna] = rutasImagenes[(int) (Math.random() * rutasImagenes.length)];
            }
        }
        return tablero;
    }

    private String[][] generarMapaVacio() {
        String[][] tablero = new String[this.filas][this.columnas];
        for (int fila = 0; fila < this.filas; fila++) {
            for (int columna = 0; columna < this.columnas; columna++) {
                tablero[fila][columna] = "";
            }
        }
        return tablero;
    }
}
