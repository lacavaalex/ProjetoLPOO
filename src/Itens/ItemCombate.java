package Itens;

import Main.Painel;
import Entidade.Jogador;

public class ItemCombate extends Item {

    private Jogador jogador;

    private int poder;
    private String nomeImagem;
    private int durabilidade;
    private boolean durabilidadeAtribuida = false;

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
            case "Galho pontiagudo":
                setDurabilidadeMax(1);
                setPoder(2);
                setNomeImagem("galho_pontiagudo");
                break;

            case "Lasca de pedra":
                setDurabilidadeMax(3);
                setPoder(1);
                setNomeImagem(null);
                break;

            default:
                System.out.println("Arma desconhecida: ");
                break;
        }
    }

    // Getters e setters
    public int getPoder() { return poder; }
    public void setPoder(int poder) { this.poder = poder; }

    public String getNomeImagem() { return nomeImagem; }
    public void setNomeImagem(String nomeImagem) { this.nomeImagem = nomeImagem; }
}
