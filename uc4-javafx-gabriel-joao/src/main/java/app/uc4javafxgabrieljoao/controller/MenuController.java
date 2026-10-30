package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Filme;
import app.uc4javafxgabrieljoao.service.Catalogo;
import app.uc4javafxgabrieljoao.util.Navegador;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.util.Duration;

import java.util.List;

/** Tela 1: banner de destaque + carrossel dos filmes em cartaz. */
public class MenuController {

    @FXML private StackPane banner;
    @FXML private Label bannerGenero, bannerTitulo, bannerSinopse;
    @FXML private HBox bannerInfo;
    @FXML private Button bannerBotao;
    @FXML private ScrollPane carrossel;
    @FXML private HBox faixa;

    private Filme destaque;
    private Timeline animacao; // rolagem suave do carrossel
    private double alvo;      // posição final da rolagem (0 a 1)

    @FXML
    public void initialize() {
        List<Filme> emCartaz = Catalogo.getEmCartaz();
        for (Filme f : emCartaz) {
            faixa.getChildren().add(criarCard(f));
        }
        bannerBotao.setOnAction(e -> Navegador.comprar(destaque));
        if (!emCartaz.isEmpty()) mostrarDestaque(emCartaz.get(0));
    }

    /** Atualiza o banner grande com o filme escolhido. */
    private void mostrarDestaque(Filme f) {
        if (f == destaque) return;
        destaque = f;
        bannerGenero.setText(f.getGenero());
        bannerTitulo.setText(f.getTitulo());
        bannerSinopse.setText(f.getSinopse());

        Label separador = new Label("|");
        separador.getStyleClass().add("sub");
        Label duracao = new Label(Navegador.duracao(f.getDuracaoMin()));
        duracao.getStyleClass().add("destaque-texto");
        bannerInfo.getChildren().setAll(Navegador.badgeClassificacao(f.getClassificacao()), separador, duracao);

        Navegador.aplicarFundo(banner, f);
    }

    private VBox criarCard(Filme f) {
        StackPane poster = Navegador.poster(f, 170, 255);
        Label selo = Navegador.badgeClassificacao(f.getClassificacao());
        StackPane.setAlignment(selo, Pos.TOP_RIGHT);
        StackPane.setMargin(selo, new Insets(8));
        StackPane capa = new StackPane(poster, selo);
        capa.setCursor(Cursor.HAND);
        capa.setOnMouseClicked(e -> Navegador.comprar(f)); // clicar no cartaz também compra

        Label titulo = new Label(f.getTitulo());
        titulo.getStyleClass().add("titulo-filme");
        titulo.setWrapText(true);
        titulo.setMaxWidth(170);
        titulo.setMinHeight(40);

        Label genero = new Label(f.getGenero());
        genero.getStyleClass().add("sub");

        Label duracao = new Label(Navegador.duracao(f.getDuracaoMin()));
        duracao.getStyleClass().add("sub");
        Button comprar = new Button("▶");
        comprar.getStyleClass().add("play");
        comprar.setTooltip(new Tooltip("Comprar ingresso"));
        comprar.setOnAction(e -> Navegador.comprar(f));
        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);
        HBox rodape = new HBox(8, duracao, espaco, comprar);
        rodape.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, capa, titulo, genero, rodape);
        card.getStyleClass().add("card");
        card.setPrefWidth(194);
        card.setMinWidth(194);
        card.setOnMouseEntered(e -> mostrarDestaque(f)); // passar o mouse troca o banner
        return card;
    }

    @FXML
    private void onCatalogo() {
        Navegador.ir("catalogo.fxml");
    }

    @FXML private void onAnterior() { rolar(-420); }
    @FXML private void onProximo()  { rolar(420); }

    /** Rola o carrossel com animação suave (início e fim lentos). */
    private void rolar(double pixels) {
        double excesso = faixa.getWidth() - carrossel.getViewportBounds().getWidth();
        if (excesso <= 0) return;

        // se clicar de novo durante a animação, continua a partir do destino anterior
        boolean rodando = animacao != null && animacao.getStatus() == Animation.Status.RUNNING;
        double base = rodando ? alvo : carrossel.getHvalue();
        alvo = Math.max(0, Math.min(1, base + pixels / excesso));

        if (animacao != null) animacao.stop();
        animacao = new Timeline(new KeyFrame(Duration.millis(450),
                new KeyValue(carrossel.hvalueProperty(), alvo, Interpolator.EASE_BOTH)));
        animacao.play();
    }
}