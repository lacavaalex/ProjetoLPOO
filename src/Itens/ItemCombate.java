package Itens;

import Main.Painel;

public class ItemCombate extends Item {

    private int poder;
    private int durabilidade;
    private boolean durabilidadeAtribuida = false;

    public ItemCombate(Painel painel) {
        super(painel);
    }

    @Override
    public void usar(String nome) {
        definirArma(nome);

        if (!durabilidadeAtribuida) {
            durabilidade = getDurabilidadeMax();
            durabilidadeAtribuida = true;
        }

        if (durabilidade == getDurabilidadeMax()) {
            durabilidade = (getDurabilidadeMax() - 1);
        } else if (durabilidade < getDurabilidadeMax()) {
            durabilidade = durabilidade - 1;

            if (durabilidade == 0) {
                getPainel().getInvent().removerItem(getNome(), 1);
            }
        }
        System.out.println("Durabilidade " + durabilidade);
    }

    // Armas
    public void definirArma(String nome) {
        switch (nome) {
            case "Galho pontiagudo":
                setDurabilidadeMax(1);
                setPoder(1);
                break;

            case "Lasca de pedra":
                setDurabilidadeMax(3);
                setPoder(1);
                break;

            default:
                System.out.println("Arma desconhecida: ");
                break;
        }
    }

    // Getters e setters
    public int getPoder() { return poder; }
    public void setPoder(int poder) { this.poder = poder; }
}
