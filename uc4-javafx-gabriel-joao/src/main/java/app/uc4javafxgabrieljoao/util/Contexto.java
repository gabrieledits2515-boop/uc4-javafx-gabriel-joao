package app.uc4javafxgabrieljoao.util;

import app.uc4javafxgabrieljoao.model.Exibicao;
import app.uc4javafxgabrieljoao.model.Filme;
import app.uc4javafxgabrieljoao.model.Poltrona;

import java.util.ArrayList;
import java.util.List;

public class Contexto {

    public static Filme filme;
    public static Exibicao exibicao;
    public static int quantidade = 1;
    public static boolean meia;
    public static List<Poltrona> poltronas = new ArrayList<>();

    public static void limpar() {
        filme = null;
        exibicao = null;
        quantidade = 1;
        meia = false;
        poltronas = new ArrayList<>();
    }
}
