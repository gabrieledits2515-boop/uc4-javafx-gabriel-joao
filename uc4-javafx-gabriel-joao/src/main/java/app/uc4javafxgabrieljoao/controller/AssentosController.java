package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Exibicao;
import app.uc4javafxgabrieljoao.model.Poltrona;
import app.uc4javafxgabrieljoao.util.Contexto;
import app.uc4javafxgabrieljoao.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/** Tela 4: mapa de poltronas da sessão. */
public class AssentosController {

    @FXML private Label info, contador;
    @FXML private GridPane mapa;
    @FXML private Button btnConfirmar;

    private Exibicao exibicao = Contexto.exibicao;

    @FXML
    public void initialize() {
        info.setText(exibicao.getFilme().getTitulo() + "  |  "
                + exibicao.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " às " + exibicao.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm"))
                + "  |  Sala " + exibicao.getSala());

        for (Poltrona p : exibicao.getPoltronas()) {
            char fileira = p.getCodigo().charAt(0);
            int linha = fileira - 'A';
            int coluna = Integer.parseInt(p.getCodigo().substring(1));

            if (coluna == 1) { // letra da fileira
                Label letra = new Label(String.valueOf(fileira));
                letra.getStyleClass().add("fileira");
                mapa.add(letra, 0, linha);
            }

            Button botao = new Button(String.valueOf(coluna));
            botao.setPrefSize(42, 42);
            botao.getStyleClass().add("assento");
            pintar(botao, p);
            botao.setOnAction(e -> clicar(p, botao));
            mapa.add(botao, coluna, linha);
        }
        atualizarContador();
    }

    private void clicar(Poltrona p, Button botao) {
        if (!p.isSelecionada() && contarSelecionadas() >= Contexto.quantidade) {
            Navegador.aviso("Você só pode escolher " + Contexto.quantidade + " assento(s).");
            return;
        }
        p.alternar();
        pintar(botao, p);
        atualizarContador();
    }

    private void pintar(Button botao, Poltrona p) {
        botao.getStyleClass().removeAll("assento-livre", "assento-ocupado", "assento-selecionado");
        if (p.isOcupada()) {
            botao.getStyleClass().add("assento-ocupado");
            botao.setDisable(true);
        } else if (p.isSelecionada()) {
            botao.getStyleClass().add("assento-selecionado");
        } else {
            botao.getStyleClass().add("assento-livre");
        }
    }

    private int contarSelecionadas() {
        return (int) exibicao.getPoltronas().stream().filter(Poltrona::isSelecionada).count();
    }

    private void atualizarContador() {
        int n = contarSelecionadas();
        contador.setText(n + " / " + Contexto.quantidade + " selecionados");
        btnConfirmar.setDisable(n != Contexto.quantidade);
    }

    @FXML
    private void onConfirmarAssentos() {
        Contexto.poltronas = exibicao.getPoltronas().stream()
                .filter(Poltrona::isSelecionada).collect(Collectors.toList());
        Navegador.ir("compra.fxml");
    }

    @FXML
    private void onVoltar() {
        for (Poltrona p : exibicao.getPoltronas()) {
            if (p.isSelecionada() != Contexto.poltronas.contains(p)) p.alternar();
        }
        Navegador.ir("compra.fxml");
    }
}
