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
                setPoder(5);
                setNomeImagem("vara_de_pesca");
                break;

            case "Galho pontiagudo":
                setPoder(10);
                setNomeImagem("galho_pontiagudo");
                break;

            case "Estilingue":
                setPoder(14);
                setNomeImagem("estilingue");
                break;

            case "Faca":
                setPoder(15);
                setNomeImagem("faca");
                break;

            case "Escudo":
                setPoder(15);
                setNomeImagem("escudo");
                break;

            case "Lança":
                setPoder(17);
                setNomeImagem("lanca");
                break;

            case "Picareta":
                setPoder(18);
                setNomeImagem("picareta");
                break;

            case "Espada Basilar":
                setPoder(20);
                setNomeImagem("espada_basilar");
                break;

            case "Machado":
                setPoder(24);
                setNomeImagem("machado");
                break;

            case "Cimitarra":
                setPoder(24);
                setNomeImagem("cimitarra");
                break;

            case "Foice":
                setPoder(26);
                setNomeImagem("foice");
                break;

            case "Espeto crustáceo":
                setPoder(26);
                setNomeImagem("espeto_crustaceo");
                break;

            case "Tridente":
                setPoder(28);
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
