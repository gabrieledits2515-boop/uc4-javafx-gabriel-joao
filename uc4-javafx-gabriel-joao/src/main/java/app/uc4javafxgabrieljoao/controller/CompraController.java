package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Exibicao;
import app.uc4javafxgabrieljoao.model.Filme;
import app.uc4javafxgabrieljoao.model.Pedido;
import app.uc4javafxgabrieljoao.model.Poltrona;
import app.uc4javafxgabrieljoao.service.Catalogo;
import app.uc4javafxgabrieljoao.util.Contexto;
import app.uc4javafxgabrieljoao.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

/** Tela 2: compra de ingresso. */
public class CompraController {

    @FXML private StackPane areaCartaz;
    @FXML private Label titulo, info, sinopse, resumo;
    @FXML private ComboBox<Exibicao> sessao;
    @FXML private Spinner<Integer> quantidade;
    @FXML private ComboBox<String> tipo;
    @FXML private Button btnConfirmar;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm");

    @FXML
    public void initialize() {
        Filme f = Contexto.filme;
        titulo.setText(f.getTitulo());
        sinopse.setText(f.getSinopse());
        areaCartaz.getChildren().add(Navegador.poster(f, 240, 360));
        info.setText(f.getGenero() + " • " + f.getClassificacao() + " • " + f.getDuracaoMin() + " min");

        sessao.setConverter(new StringConverter<Exibicao>() {
            @Override public String toString(Exibicao e) {
                return e == null ? "" : e.getDataHora().format(FMT) + " - Sala " + e.getSala();
            }
            @Override public Exibicao fromString(String s) { return null; }
        });
        sessao.getItems().setAll(Catalogo.getExibicoes(f));
        sessao.setValue(Contexto.exibicao); // restaura ao voltar da Tela 4

        quantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, Contexto.quantidade));
        tipo.getItems().setAll("Inteira", "Meia-entrada");
        tipo.setValue(Contexto.meia ? "Meia-entrada" : "Inteira");

        // mudar sessão ou quantidade invalida os assentos já escolhidos
        sessao.valueProperty().addListener((o, a, b) -> { limparPoltronas(); atualizarResumo(); });
        quantidade.valueProperty().addListener((o, a, b) -> { limparPoltronas(); atualizarResumo(); });
        tipo.valueProperty().addListener((o, a, b) -> atualizarResumo());
        atualizarResumo();
    }

    private boolean isMeia() {
        return "Meia-entrada".equals(tipo.getValue());
    }

    private void limparPoltronas() {
        for (Poltrona p : Contexto.poltronas) {
            if (p.isSelecionada()) p.alternar(); // desmarca
        }
        Contexto.poltronas = new ArrayList<>();
    }

    private void atualizarResumo() {
        Exibicao e = sessao.getValue();
        int qtd = quantidade.getValue();

        String assentos = Contexto.poltronas.isEmpty() ? "nenhum"
                : Contexto.poltronas.stream().map(Poltrona::getCodigo).collect(Collectors.joining(", "));
        double total = e == null ? 0 : e.getPreco() * (isMeia() ? 0.5 : 1) * qtd;

        resumo.setText("Filme: " + Contexto.filme.getTitulo()
                + "\nSessão: " + (e == null ? "-" : e.getDataHora().format(FMT) + " - Sala " + e.getSala())
                + "\nTipo: " + tipo.getValue()
                + "\nQuantidade: " + qtd
                + "\nAssentos: " + assentos
                + String.format("\nTotal: R$ %.2f", total));

        // só confirma com assentos na mesma quantidade dos ingressos
        btnConfirmar.setDisable(e == null || Contexto.poltronas.size() != qtd);
    }

    @FXML
    private void onEscolherAssentos() {
        if (sessao.getValue() == null) {
            Navegador.aviso("Escolha uma sessão primeiro.");
            return;
        }
        Contexto.exibicao = sessao.getValue();
        Contexto.quantidade = quantidade.getValue();
        Contexto.meia = isMeia();
        Navegador.ir("assentos.fxml");
    }

    @FXML
    private void onConfirmar() {
        Exibicao e = sessao.getValue();
        Pedido pedido = new Pedido(Catalogo.proximoId(), e, isMeia(), new ArrayList<>(Contexto.poltronas));
        pedido.finalizar(); // poltronas passam a ocupadas

        String comprovante = "Pedido nº " + pedido.getId()
                + "\nFilme: " + e.getFilme().getTitulo()
                + "\nSessão: " + e.getDataHora().format(FMT) + " - Sala " + e.getSala()
                + "\nTipo: " + tipo.getValue()
                + "\nAssentos: " + pedido.getPoltronas().stream().map(Poltrona::getCodigo).collect(Collectors.joining(", "))
                + String.format("\nTotal: R$ %.2f", pedido.getTotal());

        Alert alerta = new Alert(Alert.AlertType.INFORMATION, comprovante);
        alerta.setTitle("Cinema");
        alerta.setHeaderText("Compra realizada com sucesso!");
        alerta.showAndWait();

        Contexto.limpar();
        Navegador.ir("menu.fxml");
    }

    @FXML
    private void onVoltar() {
        limparPoltronas();
        Contexto.limpar();
        Navegador.ir("menu.fxml");
    }
}