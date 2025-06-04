package Itens;

import Main.Painel;
import Entidade.Jogador;

public class ItemCombate extends Item {

    private Jogador jogador;

    private int poder;
    private String nomeImagem;

    public ItemCombate(Painel painel) {
        super(painel);
        this.jogador = painel.getJogador();
    }

    @Override
    public void usar(String nome) {
        definirArma(nome);
        jogador.setArmaAtual(nome);
        jogador.setAtaqueAtual(getPoder());
    }

    // Armas
    public void definirArma(String nome) {
        setTipo("combate");
        switch (nome) {
            case "Vara de pesca":
                setPoder(10);
                setNomeImagem("vara_de_pesca");
                break;

            case "Galho pontiagudo":
                setPoder(15);
                setNomeImagem("galho_pontiagudo");
                break;

            case "Estilingue":
                setPoder(18);
                setNomeImagem("estilingue");
                break;

            case "Faca":
                setPoder(19);
                setNomeImagem("faca");
                break;

            case "Escudo":
                setPoder(19);
                setNomeImagem("escudo");
                break;

            case "Lança":
                setPoder(22);
                setNomeImagem("lanca");
                break;

            case "Picareta":
                setPoder(22);
                setNomeImagem("picareta");
                break;

            case "Espada Basilar":
                setPoder(24);
                setNomeImagem("espada_basilar");
                break;

            case "Machado":
                setPoder(26);
                setNomeImagem("machado");
                break;

            case "Cimitarra":
                setPoder(26);
                setNomeImagem("cimitarra");
                break;

            case "Foice":
                setPoder(28);
                setNomeImagem("foice");
                break;

            case "Espeto crustáceo":
                setPoder(28);
                setNomeImagem("espeto_crustaceo");
                break;

            case "Tridente":
                setPoder(30);
                setNomeImagem("tridente");
                break;

            case "Espada Insigne":
                setPoder(35);
                setNomeImagem("espada_insigne");
                break;

            case "Espada Flamejante":
                setPoder(40);
                setNomeImagem("espada_flamejante");
                break;

            default:
                throw new IllegalArgumentException("Arma desconhecida: " + nome);
        }
    }

    // Getters e setters
    public int getPoder() { return poder; }
    public void setPoder(int poder) { this.poder = poder; }

    public String getNomeImagem() { return nomeImagem; }
    public void setNomeImagem(String nomeImagem) { this.nomeImagem = nomeImagem; }
}
