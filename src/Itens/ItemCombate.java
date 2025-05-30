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
                setPoder(1);
                setNomeImagem("vara_de_pesca");
                break;

            case "Galho pontiagudo":
                setPoder(3);
                setNomeImagem("galho_pontiagudo");
                break;

            case "Estilingue":
                setPoder(4);
                setNomeImagem("estilingue");
                break;

            case "Faca":
                setPoder(4);
                setNomeImagem("faca");
                break;

            case "Tasco de Gelo":
                setPoder(5);
                setNomeImagem("tasco_gelo");
                break;

            case "Escudo":
                setPoder(5);
                setNomeImagem("escudo");
                break;

            case "Lança":
                setPoder(6);
                setNomeImagem("lanca");
                break;

            case "Espada Basilar":
                setPoder(7);
                setNomeImagem("espada_basilar");
                break;

            case "Machado":
                setPoder(8);
                setNomeImagem("machado");
                break;

            case "Foice":
                setPoder(8);
                setNomeImagem("foice");
                break;

            case "Cimitarra":
                setPoder(8);
                setNomeImagem("cimitarra");
                break;

            case "Espeto crustáceo":
                setPoder(9);
                setNomeImagem("espeto_crustaceo");
                break;

            case "Tridente":
                setPoder(10);
                setNomeImagem("tridente");
                break;

            case "Espada Nobre":
                setPoder(10);
                setNomeImagem("espada_nobre");
                break;

            case "Espada Flamejante":
                setPoder(12);
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
