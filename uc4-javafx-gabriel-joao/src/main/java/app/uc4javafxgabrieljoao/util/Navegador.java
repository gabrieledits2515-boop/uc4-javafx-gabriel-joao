package app.uc4javafxgabrieljoao.util;

import app.uc4javafxgabrieljoao.model.Filme;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.InputStream;

public class Navegador {

    public static Stage stage;

    /** Troca a tela. Ex.: ir("menu.fxml") */
    public static void ir(String tela) {
        try {
            Parent root = FXMLLoader.load(Navegador.class.getResource("/app/uc4javafxgabrieljoao/view/" + tela));
            if (stage.getScene() == null) {
                Scene scene = new Scene(root, 1000, 700);
                var css = Navegador.class.getResource("/app/uc4javafxgabrieljoao/css/estilo.css");
                if (css != null) scene.getStylesheets().add(css.toExternalForm());
                stage.setScene(scene);
            } else {
                stage.getScene().setRoot(root);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao abrir " + tela, e);
        }
    }

    /** Inicia a compra de um filme (vai para a Tela 2). */
    public static void comprar(Filme filme) {
        Contexto.limpar();
        Contexto.filme = filme;
        ir("compra.fxml");
    }

    /** Carrega imagem do classpath; devolve null se não existir. */
    public static Image imagem(String caminho) {
        InputStream in = Navegador.class.getResourceAsStream(caminho);
        return in == null ? null : new Image(in);
    }

    /**
     * Cartaz do filme com cantos arredondados, no tamanho pedido.
     * A imagem é cortada no centro para preencher o espaço (qualquer tamanho funciona).
     * Se a imagem não existir, mostra um bloco colorido com o título.
     */
    public static StackPane poster(Filme filme, double largura, double altura) {
        StackPane caixa = new StackPane();
        caixa.setPrefSize(largura, altura);
        caixa.setMinSize(largura, altura);
        caixa.setMaxSize(largura, altura);

        Image img = imagem(filme.getCartaz());
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(largura);
            iv.setFitHeight(altura);
            iv.setPreserveRatio(false);

            double proporcaoImg = img.getWidth() / img.getHeight();
            double proporcaoCaixa = largura / altura;
            if (proporcaoImg > proporcaoCaixa) {
                double w = img.getHeight() * proporcaoCaixa;
                iv.setViewport(new Rectangle2D((img.getWidth() - w) / 2, 0, w, img.getHeight()));
            } else {
                double h = img.getWidth() / proporcaoCaixa;
                iv.setViewport(new Rectangle2D(0, (img.getHeight() - h) / 2, img.getWidth(), h));
            }
            caixa.getChildren().add(iv);
        } else {
            Label texto = new Label(filme.getTitulo());
            texto.setWrapText(true);
            texto.setTextAlignment(TextAlignment.CENTER);
            texto.setMaxWidth(largura - 24);
            texto.getStyleClass().add("poster-vazio-texto");
            caixa.getChildren().add(texto);
            caixa.getStyleClass().add("poster-vazio");
        }

        Rectangle recorte = new Rectangle(largura, altura);
        recorte.setArcWidth(24);
        recorte.setArcHeight(24);
        caixa.setClip(recorte);
        return caixa;
    }

    public static void aviso(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }
}