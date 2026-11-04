package app.uc4javafxgabrieljoao;

import app.uc4javafxgabrieljoao.util.Navegador;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Cinema");
        Navegador.stage = stage;
        Navegador.ir("menu.fxml");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}