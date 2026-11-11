package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Sessao;
import app.uc4javafxgabrieljoao.utils.Rotinas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import app.uc4javafxgabrieljoao.dao.SessaoDAO;

import java.time.LocalDateTime;

public class ListarController {

    @FXML private TableView<Sessao> sessoesTableView;
    @FXML private TableColumn<Sessao, Integer> idColumn;
    @FXML private TableColumn<Sessao, String> nomeFilmeColumn;
    @FXML private TableColumn<Sessao, LocalDateTime> dataHorarioColumn;
    @FXML private TableColumn<Sessao, Double> precoColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeFilmeColumn.setCellValueFactory(new PropertyValueFactory<>("nomeFilme"));
        dataHorarioColumn.setCellValueFactory(new PropertyValueFactory<>("dataHorario"));
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoIngresso"));

        sessoesTableView.setItems(SessaoDAO.listarTodos());
    }

    @FXML
    public void voltar(ActionEvent event) {
        Rotinas.trocarTela(event, "menu.fxml", 220, 200);
    }
}