package Itens;

import Main.Painel;

public class ItemRecurso extends Item {

    private String nome;

    public ItemRecurso(Painel painel) {
        super(painel);
    }

    @Override
    public void usar(String nome) {
        definirRecurso(nome);
        System.out.println("Crafting a ser implementado");
        //getPainel().getInvent().removerItem(getNome(), 1);
    }

    // Itens
    public void definirRecurso(String nome) {
        setTipo("recurso");
        switch (nome) {
            case "Madeira":
                break;

            case "Pedra":
                break;

            case "Punhado de sementes":
                break;

            default:
                throw new IllegalArgumentException("Recurso desconhecido: " + nome);
        }
        setDurabilidadeMax(null);
    }
}
