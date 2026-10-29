package app.uc4javafxgabrieljoao.service;

import app.uc4javafxgabrieljoao.model.Exibicao;
import app.uc4javafxgabrieljoao.model.Filme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Dados de exemplo em memória (sem banco de dados). */
public class Catalogo {

    private static final String IMG = "/app/uc4javafxgabrieljoao/images/";
    private static List<Filme> filmes = new ArrayList<>();
    private static List<Exibicao> exibicoes = new ArrayList<>();
    private static int ultimoId = 100;

    // ===== COMO ADICIONAR UM FILME =====
    // 1) Coloque a imagem do cartaz em resources/app/uc4javafxgabrieljoao/images/
    // 2) Adicione uma linha filmes.add(new Filme(id, titulo, genero, classificacao,
    //    duracaoEmMinutos, sinopse, IMG + "nome-do-arquivo.jpg", emCartaz));
    //    (emCartaz = true aparece no menu inicial; false só aparece no catálogo)
    static {
        // ----- Em cartaz -----
        filmes.add(new Filme(1, "Jogos Vorazes: Em Chamas", "Ficção científica", "12 anos", 146,
                "Depois de vencer os Jogos, Katniss vira símbolo de esperança nos distritos e é forçada a competir de novo.",
                IMG + "jogos-vorazes-em-chamas.jpg", true));
        filmes.add(new Filme(2, "Duna: Parte Dois", "Ficção científica", "14 anos", 166,
                "Paul Atreides se une aos Fremen em busca de vingança e precisa decidir o destino de todo o universo.",
                IMG + "duna-parte-dois.jpg", true));
        filmes.add(new Filme(3, "Divertida Mente 2", "Animação", "Livre", 96,
                "Riley entra na adolescência e novas emoções, como a Ansiedade, chegam para bagunçar a sua cabeça.",
                IMG + "divertida-mente-2.jpg", true));
        filmes.add(new Filme(4, "Oppenheimer", "Drama", "14 anos", 180,
                "A história do físico J. Robert Oppenheimer e do desenvolvimento da bomba atômica na Segunda Guerra.",
                IMG + "oppenheimer.jpg", true));
        filmes.add(new Filme(5, "Ainda Estou Aqui", "Drama", "14 anos", 137,
                "Nos anos 1970, Eunice Paiva reconstrói a vida da família após o desaparecimento do marido durante a ditadura militar.",
                IMG + "ainda-estou-aqui.jpg", true));

        // ----- Só no catálogo (fora de cartaz) -----
        filmes.add(new Filme(6, "Interestelar", "Ficção científica", "10 anos", 169,
                "Com a Terra à beira do colapso, astronautas atravessam um buraco de minhoca em busca de um novo lar.",
                IMG + "interestelar.jpg", false));
        filmes.add(new Filme(7, "Coringa", "Suspense", "16 anos", 122,
                "Arthur Fleck, um comediante fracassado de Gotham, é levado pela marginalização à loucura e ao crime.",
                IMG + "coringa.jpg", false));
        filmes.add(new Filme(8, "O Auto da Compadecida", "Comédia", "12 anos", 104,
                "As aventuras de João Grilo e Chicó, dois amigos pobres do sertão nordestino que vivem de pequenos golpes.",
                IMG + "auto-da-compadecida.jpg", false));
        filmes.add(new Filme(9, "Cidade de Deus", "Drama", "18 anos", 130,
                "Dois jovens crescem em lados opostos da violência de uma favela do Rio de Janeiro, entre os anos 1960 e 1980.",
                IMG + "cidade-de-deus.jpg", false));
        filmes.add(new Filme(10, "Parasita", "Suspense", "16 anos", 132,
                "Uma família pobre se infiltra aos poucos na casa de uma família rica, e o plano toma rumos inesperados.",
                IMG + "parasita.jpg", false));

        int id = 1;
        for (Filme f : filmes) {
            if (!f.isEmCartaz()) continue;
            exibicoes.add(new Exibicao(id++, f, 1, LocalDate.now().atTime(18, 0), 30, 5, 8));
            exibicoes.add(new Exibicao(id++, f, 2, LocalDate.now().atTime(21, 0), 30, 5, 8));
            exibicoes.add(new Exibicao(id++, f, 1, LocalDate.now().plusDays(1).atTime(19, 30), 30, 5, 8));
        }
        // deixa algumas poltronas ocupadas para demonstração
        for (Exibicao e : exibicoes) {
            e.getPoltronas().get(2).ocupar();
            e.getPoltronas().get(3).ocupar();
            e.getPoltronas().get(12).ocupar();
        }
    }

    public static List<Filme> getFilmes() { return filmes; }

    public static List<Filme> getEmCartaz() {
        return filmes.stream().filter(Filme::isEmCartaz).collect(Collectors.toList());
    }

    public static List<Exibicao> getExibicoes(Filme filme) {
        return exibicoes.stream().filter(e -> e.getFilme().equals(filme)).collect(Collectors.toList());
    }

    public static int proximoId() { return ++ultimoId; }
}
