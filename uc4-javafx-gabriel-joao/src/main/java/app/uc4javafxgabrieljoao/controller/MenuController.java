package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Filme;
import app.uc4javafxgabrieljoao.service.Catalogo;
import app.uc4javafxgabrieljoao.util.Navegador;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/** Tela 1: filmes em cartaz. */
public class MenuController {

    @FXML private FlowPane grade;

    @FXML
    public void initialize() {
        for (Filme f : Catalogo.getEmCartaz()) {
            ImageView img = new ImageView(Navegador.imagem(f.getCartaz()));
            img.setFitWidth(150);
            img.setPreserveRatio(true);
            img.setOnMouseClicked(e -> Navegador.comprar(f)); // clicar no cartaz também compra

            Label titulo = new Label(f.getTitulo());
            titulo.getStyleClass().add("titulo-filme");
            Label info = new Label(f.getGenero() + " • " + f.getClassificacao() + " • " + f.getDuracaoMin() + " min");

            Button comprar = new Button("Comprar ingresso");
            comprar.setOnAction(e -> Navegador.comprar(f));

            VBox card = new VBox(8, img, titulo, info, comprar);
            card.setAlignment(Pos.CENTER);
            card.getStyleClass().add("card");
            grade.getChildren().add(card);
        }
    }

    @FXML
    private void onCatalogo() {
        Navegador.ir("catalogo.fxml");
    }
}
