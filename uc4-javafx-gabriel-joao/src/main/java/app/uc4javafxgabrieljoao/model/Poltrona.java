package app.uc4javafxgabrieljoao.model;

public class Poltrona {

    private String codigo; // ex.: "B5"
    private boolean ocupada;
    private boolean selecionada;

    public Poltrona(String codigo) { this.codigo = codigo; }

    public String getCodigo() { return codigo; }
    public boolean isOcupada() { return ocupada; }
    public boolean isSelecionada() { return selecionada; }

    public void ocupar() {
        ocupada = true;
        selecionada = false;
    }

    /** Clique do usuário: seleciona ou desmarca (se não estiver ocupada). */
    public void alternar() {
        if (!ocupada) selecionada = !selecionada;
    }
}
