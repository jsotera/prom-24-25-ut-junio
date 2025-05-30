package es.masanz.junio.controller;

import es.masanz.junio.dao.EditorDBDao;
import es.masanz.junio.dao.MapaDBDao;
import es.masanz.junio.dto.Mapa;
import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class EditorController {

    private int filas;
    private int columnas;
    private String[][] mapa;

    private static final Logger logger = LogManager.getLogger(EditorController.class);

    public static final String SPRITES_SERVER_FOLDER = "sprites";
    public static final String SPRITES_LOCAL_FOLDER = "src/main/resources/public/"+SPRITES_SERVER_FOLDER;

    public static String NOMBRE_MAPA = "";

    private EditorDBDao editorDao;
    private MapaDBDao mapaDao;

    public EditorController(){
        this.editorDao = new EditorDBDao();
        this.editorDao.establecerConexion();
        this.mapaDao = new MapaDBDao();
        this.mapaDao.establecerConexion();
    }

    public void cargarEditorMapa(Context context) {
        Map<String, Object> model = new HashMap<>();

        String[] rutasImagenes = cargarSrites();

        List<Mapa> listadoMapas = mapaDao.consultarMapas();

        if(mapa==null && listadoMapas.size()>0){

            if(NOMBRE_MAPA.isEmpty()){
                NOMBRE_MAPA = listadoMapas.get(0).getNombre();
                this.filas = listadoMapas.get(0).getFilas();
                this.columnas = listadoMapas.get(0).getColumnas();
            } else {
                for (Mapa mapa : listadoMapas) {
                    if(mapa.getNombre().equals(NOMBRE_MAPA)) {
                        NOMBRE_MAPA = mapa.getNombre();
                        this.filas = mapa.getFilas();
                        this.columnas = mapa.getColumnas();
                    }
                }
            }


            mapa = generarMapaVacio();
            editorDao.consultarMapa(NOMBRE_MAPA, mapa);
        }



        model.put("mapasListado", listadoMapas);
        model.put("mapaSeleccionado", NOMBRE_MAPA);
        model.put("tablero", mapa);
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

    public void colocarSpritePost(Context context) {

        int fila = Integer.parseInt(context.formParam("fila"));
        int columna = Integer.parseInt(context.formParam("columna"));
        String sprite = context.formParam("sprite");

        String[] spriteSpitted = sprite.split("\"");
        if(spriteSpitted.length == 3){
            sprite = spriteSpitted[1];
        }

        mapa[fila][columna] = sprite;

        boolean haSidoModificado = editorDao.actualizarSprite(NOMBRE_MAPA, fila, columna, sprite);
        if(!haSidoModificado){
            editorDao.insertarSprite(NOMBRE_MAPA, fila, columna, sprite);
        }

    }

    public void colocarSpriteGet(Context context) {

        String fila = context.pathParam("fila");
        String columna = context.pathParam("columna");
        String sprite = context.pathParam("sprite");

        System.out.println("ENTRANDO VIA GET");

        System.out.println("fila: "+fila);
        System.out.println("columna: "+columna);
        System.out.println("sprite: "+sprite);

    }

    public void seleccionarMapa(Context context) {
        String mapaEditable = context.formParam("mapaEditable");
        NOMBRE_MAPA = mapaEditable;
        mapa = null;
        context.redirect("/");
    }
}
