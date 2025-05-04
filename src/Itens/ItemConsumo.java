package Itens;

import Main.Painel;

public class ItemConsumo extends Item {

    private Painel painel;

    public ItemConsumo() {
        super();
    }

    @Override
    public void usar() {
        consumir();
    }

    public void consumir() {
        int nivel = getDurabilidade() - 1;
        setDurabilidade(nivel);

        if (nivel == 0) {
            getPainel().getInvent().removerItem(getNome(), 1);
        }
    }

    // Itens
    public void definirConsumo(String nome) {
        switch (nome) {
            case "Cantil":
                setDurabilidade(5);
                break;

            default:
                System.out.println("Alimento desconhecido: ");
                break;
        }
    }

    public Painel getPainel() {
        return painel;
    }
}
