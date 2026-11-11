package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.utils.Rotinas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MenuController {

    @FXML
    public void exibirCadastro(ActionEvent event) {
        Rotinas.trocarTela(event, "cadastrar.fxml", 420, 340);
    }

    @FXML
    public void exibirLista(ActionEvent event) {
        Rotinas.trocarTela(event, "listar.fxml", 640, 420);
    }

    @FXML
    public void sair() {
        ButtonType resposta = Rotinas.mostrarAlerta(
                Alert.AlertType.CONFIRMATION,
                "Saindo do programa",
                "Deseja realmente sair?"
        ).orElse(ButtonType.CANCEL);

        if (resposta == ButtonType.OK) {
            Platform.exit();
        }
    }
}