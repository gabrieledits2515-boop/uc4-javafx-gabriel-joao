package app.uc4javafxgabrieljoao.controller;

import app.uc4javafxgabrieljoao.model.Filme;
import app.uc4javafxgabrieljoao.service.Catalogo;
import app.uc4javafxgabrieljoao.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Tela 3: catálogo completo com busca e filtro por gênero. */
public class CatalogoController {

    @FXML private TextField busca;
    @FXML private ComboBox<String> filtroGenero;
    @FXML private VBox lista;

    @FXML
    public void initialize() {
        filtroGenero.getItems().add("Todos");
        Catalogo.getFilmes().stream().map(Filme::getGenero).distinct()
                .forEach(filtroGenero.getItems()::add);
        filtroGenero.setValue("Todos");

        busca.textProperty().addListener((o, a, b) -> atualizar());
        filtroGenero.valueProperty().addListener((o, a, b) -> atualizar());
        atualizar();
    }

    private void atualizar() {
        String texto = busca.getText().toLowerCase();
        String genero = filtroGenero.getValue();

        lista.getChildren().clear();
        for (Filme f : Catalogo.getFilmes()) {
            if (!f.getTitulo().toLowerCase().contains(texto)) continue;
            if (!"Todos".equals(genero) && !f.getGenero().equals(genero)) continue;
            lista.getChildren().add(criarCard(f));
        }
    }

    private HBox criarCard(Filme f) {
        ImageView img = new ImageView(Navegador.imagem(f.getCartaz()));
        img.setFitWidth(100);
        img.setPreserveRatio(true);

        Label titulo = new Label(f.getTitulo());
        titulo.getStyleClass().add("titulo-filme");
        Label info = new Label(f.getGenero() + " • " + f.getClassificacao() + " • " + f.getDuracaoMin() + " min");
        Label sinopse = new Label(f.getSinopse());
        sinopse.setWrapText(true);
        Label status = new Label(f.isEmCartaz() ? "● Em cartaz" : "● Fora de cartaz");

        VBox detalhes = new VBox(5, titulo, info, sinopse, status);
        if (f.isEmCartaz()) {
            Button comprar = new Button("Comprar ingresso");
            comprar.setOnAction(e -> Navegador.comprar(f));
            detalhes.getChildren().add(comprar);
        }

        HBox card = new HBox(15, img, detalhes);
        card.getStyleClass().add("card");
        return card;
    }

    @FXML
    private void onVoltar() {
        Navegador.ir("menu.fxml");
    }
}
