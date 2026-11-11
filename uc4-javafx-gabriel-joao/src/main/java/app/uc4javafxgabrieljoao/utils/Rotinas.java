package app.uc4javafxgabrieljoao.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import app.uc4javafxgabrieljoao.HelloApplication;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

public class Rotinas {
    public static Optional<ButtonType> mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle("");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);

        return alert.showAndWait();

    }

    public static void trocarTela(ActionEvent event, String nomefxml,double largura,double altura){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomefxml));
            Scene scene = new Scene(fxmlLoader.load(), largura, altura);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e ) {
            Rotinas.mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao mostrar tela",
                    "Nao foi possivel carregar a tela" + nomefxml
            );
        }
    }
}
