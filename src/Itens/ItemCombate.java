package Itens;

import Main.Painel;

public class ItemCombate extends Item {

    private int poder;
    Painel painel;

    public ItemCombate(String nome, int quantidade, Integer durabilidade, Painel painel) {
        super(nome, quantidade);
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

    public void galhoPontiagudo() {
        if (getNome().equals("Galho pontiagudo")) {
            setDurabilidade(1);
            setPoder(1);
        }
    }

    // Getters e setters
    public int getPoder() {
        return poder;
    }
    public void setPoder(int poder) {
        this.poder = poder;
    }
}
