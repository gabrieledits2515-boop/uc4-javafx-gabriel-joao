package app.uc4javafxgabrieljoao.dao;

import app.uc4javafxgabrieljoao.model.Sessao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SessaoDAO {
    private static final ObservableList<Sessao> sessoes = FXCollections.observableArrayList();
    private static int proximoId = 1;

    public static void inserir(Sessao sessao) {
        sessao.setId(proximoId++);
        sessoes.add(sessao);
    }

    public static ObservableList<Sessao> listarTodos() {
        return sessoes;
    }
}