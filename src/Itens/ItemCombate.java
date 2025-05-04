package Itens;

import Main.Painel;

public class ItemCombate extends Item {

    private int poder;
    private Painel painel;

    public ItemCombate(Painel painel) {
        super();
        this.painel = painel;
    }

    @Override
    public void usar() {
        if (getDurabilidade() != null && getDurabilidade() > 0) {
            setDurabilidade(getDurabilidade() - 1);
        } else {
            painel.getInvent().removerItem(getNome(), 1);
        }
    }

    // Armas
    public void definirArma(String nome) {
        switch (nome) {
            case "Galho pontiagudo":
                setDurabilidade(1);
                setPoder(1);
                break;

            case "Lasca de pedra":
                setDurabilidade(1);
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
