package model;

import java.time.LocalDateTime;

public class
Sessao {
    private  int id;
    private String nomeFilme;
    private LocalDateTime dataHorario;
    private double precoIngresso;

    public Sessao(int id, String nomeFilme, LocalDateTime dataHorario, double precoIngresso) {
        this.id = id;
        this.nomeFilme = nomeFilme;
        this.dataHorario = dataHorario;
        this.precoIngresso = precoIngresso;
    }

    public Sessao(String nomeFilme, LocalDateTime dataHorario, double precoIngresso) {
        this.nomeFilme = nomeFilme;
        this.dataHorario = dataHorario;
        this.precoIngresso = precoIngresso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeFilme() {
        return nomeFilme;
    }

    public void setNomeFilme(String nomeFilme) {
        this.nomeFilme = nomeFilme;
    }

    public LocalDateTime getDataHorario() {
        return dataHorario;
    }

    public void setDataHorario(LocalDateTime dataHorario) {
        this.dataHorario = dataHorario;
    }

    public double getPrecoIngresso() {
        return precoIngresso;
    }

    public void setPrecoIngresso(double precoIngresso) {
        this.precoIngresso = precoIngresso;
    }
}