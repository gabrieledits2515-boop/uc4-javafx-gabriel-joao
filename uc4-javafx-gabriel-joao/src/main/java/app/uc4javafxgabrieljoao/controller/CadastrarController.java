package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Sessao;
import app.uc4javafxgabrieljoao.utils.Rotinas;
import app.uc4javafxgabrieljoao.dao.SessaoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CadastrarController {

    @FXML private TextField nomeFilmeTextField;
    @FXML private DatePicker dataDatePicker;
    @FXML private TextField horarioTextField;
    @FXML private TextField precoTextField;

    @FXML
    public void cadastrar() {
        try {
            String nome = nomeFilmeTextField.getText().trim();
            LocalTime hora = LocalTime.parse(horarioTextField.getText().trim());
            double preco = Double.parseDouble(precoTextField.getText().trim());
            LocalDateTime dataHorario = LocalDateTime.of(dataDatePicker.getValue(), hora);

            if (nome.isEmpty()) {
                throw new IllegalArgumentException();
            }

            SessaoDAO.inserir(new Sessao(nome, dataHorario, preco));
            Rotinas.mostrarAlerta(Alert.AlertType.INFORMATION, "Cadastro", "Sessão cadastrada com sucesso!");

            nomeFilmeTextField.clear();
            dataDatePicker.setValue(null);
            horarioTextField.clear();
            precoTextField.clear();

        } catch (Exception e) {
            Rotinas.mostrarAlerta(Alert.AlertType.ERROR, "Erro ao cadastrar",
                    "Confira os campos: nome preenchido, data escolhida, horário no formato 19:30 e preço como 25.50");
        }
    }

    @FXML
    public void voltar(ActionEvent event) {
        Rotinas.trocarTela(event, "menu.fxml", 220, 200);
    }
}