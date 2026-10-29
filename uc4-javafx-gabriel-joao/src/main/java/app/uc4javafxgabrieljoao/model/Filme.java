package app.uc4javafxgabrieljoao.model;

public class Filme extends Entidade {

    private String titulo, genero, classificacao, sinopse, cartaz;
    private int duracaoMin;
    private boolean emCartaz;

    public Filme(int id, String titulo, String genero, String classificacao,
                 int duracaoMin, String sinopse, String cartaz, boolean emCartaz) {
        super(id);
        this.titulo = titulo;
        this.genero = genero;
        this.classificacao = classificacao;
        this.duracaoMin = duracaoMin;
        this.sinopse = sinopse;
        this.cartaz = cartaz;
        this.emCartaz = emCartaz;
    }

    public String getTitulo() { return titulo; }
    public String getGenero() { return genero; }
    public String getClassificacao() { return classificacao; }
    public int getDuracaoMin() { return duracaoMin; }
    public String getSinopse() { return sinopse; }
    public String getCartaz() { return cartaz; }
    public boolean isEmCartaz() { return emCartaz; }

    @Override
    public String toString() { return titulo; }
}
