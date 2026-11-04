package app.uc4javafxgabrieljoao.util;

import app.uc4javafxgabrieljoao.model.Filme;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.effect.GaussianBlur;
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
                Scene scene = new Scene(root, 1200, 800);
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


    /** Selo colorido da classificação indicativa (L, 10, 12, 14, 16, 18). */
    public static Label badgeClassificacao(String classificacao) {
        String sigla = classificacao.toLowerCase().startsWith("livre") ? "L" : classificacao.split(" ")[0];
        Label selo = new Label(sigla);
        selo.getStyleClass().addAll("classif", "classif-" + sigla);
        return selo;
    }

    /** Ex.: 106 -> "1h 46min" */
    public static String duracao(int minutos) {
        int h = minutos / 60;
        int m = minutos % 60;
        return h > 0 ? h + "h " + String.format("%02d", m) + "min" : m + "min";
    }

    /** Coloca o cartaz do filme, ampliado e desfocado, como fundo do banner. */
    public static void aplicarFundo(StackPane banner, Filme filme) {
        banner.getChildren().removeIf(n -> n.getStyleClass().contains("banner-fundo"));

        Rectangle recorte = new Rectangle();
        recorte.widthProperty().bind(banner.widthProperty());
        recorte.heightProperty().bind(banner.heightProperty());
        banner.setClip(recorte);

        Image img = imagem(filme.getCartaz());
        if (img == null) return;
        ImageView fundo = new ImageView(img);
        fundo.setPreserveRatio(true);
        fundo.fitWidthProperty().bind(banner.widthProperty());
        fundo.setEffect(new GaussianBlur(30));
        fundo.setOpacity(0.6);
        fundo.getStyleClass().add("banner-fundo");
        banner.getChildren().add(0, fundo);
    }

    public static void aviso(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }
}
