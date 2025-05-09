package Itens;

import Main.Painel;

public class ItemRecurso extends Item {

    private String nome;

    public ItemRecurso(Painel painel) {
        super(painel);
    }

    @Override
    public void usar(String nome) {
        // a definir possivel sistema de crafting
        definirRecurso(nome);
        getPainel().getInvent().removerItem(getNome(), 1);
    }

    // Itens
    public void definirRecurso(String nome) {
        switch (nome) {
            case "Madeira":
                setDurabilidadeMax(null);
                break;

            case "Pedra":
                setDurabilidadeMax(null);
                break;

            default:
                System.out.println("Recurso desconhecido: ");
                break;
        }
    }
}
