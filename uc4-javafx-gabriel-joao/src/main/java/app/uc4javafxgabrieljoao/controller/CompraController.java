package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Exibicao;
import app.uc4javafxgabrieljoao.model.Filme;
import app.uc4javafxgabrieljoao.model.Pedido;
import app.uc4javafxgabrieljoao.model.Poltrona;
import app.uc4javafxgabrieljoao.service.Catalogo;
import app.uc4javafxgabrieljoao.util.Contexto;
import app.uc4javafxgabrieljoao.util.Navegador;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Tela 2: compra de ingresso (dia, horário, quantidade e tipo). */
public class CompraController {

    @FXML private StackPane banner, areaCartaz;
    @FXML private Label genero, titulo, sinopse, fichaGenero, fichaDuracao, fichaClassif, resumo;
    @FXML private HBox linhaInfo, datas;
    @FXML private FlowPane horarios;
    @FXML private Spinner<Integer> quantidade;
    @FXML private ComboBox<String> tipo;
    @FXML private Button btnConfirmar;

    private static final Locale PT = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm");

    private List<Exibicao> todas;
    private ToggleGroup grupoData = new ToggleGroup();
    private ToggleGroup grupoHora = new ToggleGroup();
    private Exibicao escolhida; // sessão selecionada

    @FXML
    public void initialize() {
        Filme f = Contexto.filme;

        // ----- informações do filme -----
        Navegador.aplicarFundo(banner, f);
        areaCartaz.getChildren().add(Navegador.poster(f, 170, 255));
        genero.setText(f.getGenero());
        titulo.setText(f.getTitulo());
        sinopse.setText(f.getSinopse());
        Label separador = new Label("|");
        separador.getStyleClass().add("sub");
        Label duracao = new Label(Navegador.duracao(f.getDuracaoMin()));
        duracao.getStyleClass().add("destaque-texto");
        linhaInfo.getChildren().addAll(Navegador.badgeClassificacao(f.getClassificacao()), separador, duracao);
        fichaGenero.setText(f.getGenero());
        fichaDuracao.setText(Navegador.duracao(f.getDuracaoMin()));
        fichaClassif.setText(f.getClassificacao());

        // ----- quantidade e tipo -----
        quantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, Contexto.quantidade));
        tipo.getItems().setAll("Inteira", "Meia-entrada");
        tipo.setValue(Contexto.meia ? "Meia-entrada" : "Inteira");

        // ----- dias disponíveis -----
        todas = Catalogo.getExibicoes(f);
        List<LocalDate> dias = todas.stream().map(e -> e.getDataHora().toLocalDate())
                .distinct().sorted().collect(Collectors.toList());
        LocalDate diaInicial = Contexto.exibicao != null ? Contexto.exibicao.getDataHora().toLocalDate()
                : (dias.isEmpty() ? null : dias.get(0));

        ToggleButton chipInicial = null;
        for (LocalDate dia : dias) {
            ToggleButton chip = criarChipData(dia);
            chip.setToggleGroup(grupoData);
            datas.getChildren().add(chip);
            if (dia.equals(diaInicial)) chipInicial = chip;
        }

        grupoData.selectedToggleProperty().addListener((o, antigo, novo) -> {
            if (novo == null) { antigo.setSelected(true); return; } // não deixa desmarcar
            mostrarHorarios((LocalDate) novo.getUserData());
        });

        // mudar a quantidade invalida os assentos já escolhidos
        quantidade.valueProperty().addListener((o, a, b) -> { limparPoltronas(); atualizarResumo(); });
        tipo.valueProperty().addListener((o, a, b) -> atualizarResumo());

        if (chipInicial != null) chipInicial.setSelected(true);
        atualizarResumo();
    }

    private ToggleButton criarChipData(LocalDate dia) {
        String nome = dia.equals(LocalDate.now()) ? "HOJE"
                : dia.getDayOfWeek().getDisplayName(TextStyle.FULL, PT).split("-")[0].toUpperCase();
        String mes = dia.getMonth().getDisplayName(TextStyle.SHORT, PT).replace(".", "").toUpperCase();

        Label l1 = new Label(nome);
        l1.getStyleClass().add("chip-dia");
        Label l2 = new Label(String.valueOf(dia.getDayOfMonth()));
        l2.getStyleClass().add("chip-num");
        Label l3 = new Label(mes);
        l3.getStyleClass().add("chip-dia");
        VBox conteudo = new VBox(0, l1, l2, l3);
        conteudo.setAlignment(Pos.CENTER);

        ToggleButton chip = new ToggleButton();
        chip.setGraphic(conteudo);
        chip.getStyleClass().add("chip-data");
        chip.setUserData(dia);
        return chip;
    }

    /** Mostra os horários do dia escolhido. */
    private void mostrarHorarios(LocalDate dia) {
        trocarSessao(null); // trocar de dia invalida a sessão e os assentos
        horarios.getChildren().clear();
        grupoHora = new ToggleGroup();

        ToggleButton restaurar = null;
        for (Exibicao e : todas) {
            if (!e.getDataHora().toLocalDate().equals(dia)) continue;
            ToggleButton chip = new ToggleButton(e.getDataHora().format(DateTimeFormatter.ofPattern("HH:mm"))
                    + "\nSala " + e.getSala());
            chip.getStyleClass().add("chip-hora");
            chip.setUserData(e);
            chip.setToggleGroup(grupoHora);
            horarios.getChildren().add(chip);
            if (e == Contexto.exibicao) restaurar = chip; // ao voltar da Tela 4
        }

        grupoHora.selectedToggleProperty().addListener((o, a, b) ->
                trocarSessao(b == null ? null : (Exibicao) b.getUserData()));
        if (restaurar != null) restaurar.setSelected(true);
    }

    private void trocarSessao(Exibicao nova) {
        if (nova == escolhida) return;
        if (escolhida != null) limparPoltronas(); // assentos eram da sessão anterior
        escolhida = nova;
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
        int qtd = quantidade.getValue();

        String assentos = Contexto.poltronas.isEmpty() ? "nenhum"
                : Contexto.poltronas.stream().map(Poltrona::getCodigo).collect(Collectors.joining(", "));
        double total = escolhida == null ? 0 : escolhida.getPreco() * (isMeia() ? 0.5 : 1) * qtd;

        resumo.setText("Filme: " + Contexto.filme.getTitulo()
                + "\nSessão: " + (escolhida == null ? "-" : escolhida.getDataHora().format(FMT) + " - Sala " + escolhida.getSala())
                + "\nTipo: " + tipo.getValue()
                + "\nQuantidade: " + qtd
                + "\nAssentos: " + assentos
                + String.format("\n\nTotal: R$ %.2f", total));

        // só confirma com assentos na mesma quantidade dos ingressos
        btnConfirmar.setDisable(escolhida == null || Contexto.poltronas.size() != qtd);
    }

    @FXML
    private void onEscolherAssentos() {
        if (escolhida == null) {
            Navegador.aviso("Escolha o dia e o horário da sessão primeiro.");
            return;
        }
        Contexto.exibicao = escolhida;
        Contexto.quantidade = quantidade.getValue();
        Contexto.meia = isMeia();
        Navegador.ir("assentos.fxml");
    }

    @FXML
    private void onConfirmar() {
        Pedido pedido = new Pedido(Catalogo.proximoId(), escolhida, isMeia(), new ArrayList<>(Contexto.poltronas));
        pedido.finalizar(); // poltronas passam a ocupadas

        String comprovante = "Pedido nº " + pedido.getId()
                + "\nFilme: " + escolhida.getFilme().getTitulo()
                + "\nSessão: " + escolhida.getDataHora().format(FMT) + " - Sala " + escolhida.getSala()
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
