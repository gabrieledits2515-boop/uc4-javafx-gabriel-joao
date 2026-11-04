package app.uc4javafxgabrieljoao.model;

import java.util.List;

public class Pedido extends Entidade {

    private Exibicao exibicao;
    private boolean meia;
    private List<Poltrona> poltronas;

    public Pedido(int id, Exibicao exibicao, boolean meia, List<Poltrona> poltronas) {
        super(id);
        this.exibicao = exibicao;
        this.meia = meia;
        this.poltronas = poltronas;
    }

    public Exibicao getExibicao() { return exibicao; }
    public boolean isMeia() { return meia; }
    public List<Poltrona> getPoltronas() { return poltronas; }

    public double getTotal() {
        double valor = meia ? exibicao.getPreco() / 2 : exibicao.getPreco();
        return valor * poltronas.size();
    }

    public void finalizar() {
        poltronas.forEach(Poltrona::ocupar);
    }
}
