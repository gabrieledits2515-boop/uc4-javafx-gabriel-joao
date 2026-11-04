package app.uc4javafxgabrieljoao.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Exibicao extends Entidade {

    private Filme filme;
    private int sala;
    private LocalDateTime dataHora;
    private double preco;
    private List<Poltrona> poltronas = new ArrayList<>();

    public Exibicao(int id, Filme filme, int sala, LocalDateTime dataHora,
                    double preco, int fileiras, int colunas) {
        super(id);
        this.filme = filme;
        this.sala = sala;
        this.dataHora = dataHora;
        this.preco = preco;

        for (int f = 0; f < fileiras; f++) {
            for (int c = 1; c <= colunas; c++) {
                poltronas.add(new Poltrona("" + (char) ('A' + f) + c));
            }
        }
    }

    public Filme getFilme() { return filme; }
    public int getSala() { return sala; }
    public LocalDateTime getDataHora() { return dataHora; }
    public double getPreco() { return preco; }
    public List<Poltrona> getPoltronas() { return poltronas; }
}
