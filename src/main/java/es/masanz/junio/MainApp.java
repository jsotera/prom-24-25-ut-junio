package es.masanz.junio;

import es.masanz.junio.controller.EditorController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinFreemarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp {

    private static final Logger logger = LogManager.getLogger(MainApp.class);

    public static void main(String[] args) {

        logger.info("ARRANCANDO APLICACION");

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinFreemarker());
        }).start(4567);

        EditorController editorController = new EditorController();

        app.get("/", editorController::cargarEditorMapa);

        app.post("/colocarSprite", editorController::colocarSpritePost);

        app.post("/seleccionar-mapa", editorController::seleccionarMapa);

        // app.get("/colocarSprite/{fila}/{columna}/{sprite}", editorController::colocarSpriteGet);

    }
}