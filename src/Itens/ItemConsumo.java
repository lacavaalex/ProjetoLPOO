package Itens;

import Main.Painel;

public class ItemConsumo extends Item {

    private int durabilidade;
    private boolean durabilidadeAtribuida = false;

    public ItemConsumo(Painel painel) {
        super(painel);
    }

    @Override
    public void usar(String nome) {
        System.out.println("ItemConsumo usando: " + nome);
        consumir(nome);
    }

    public void consumir(String nome) {
        definirConsumo(nome);

        if (!durabilidadeAtribuida) {
            durabilidade = getDurabilidadeMax();
            durabilidadeAtribuida = true;
        }

        if (durabilidade == getDurabilidadeMax()) {
            durabilidade = (getDurabilidadeMax() - 1);
        }
        else if (durabilidade < getDurabilidadeMax()) {
            durabilidade = durabilidade - 1;

            if (durabilidade == 0) {
                getPainel().getInvent().removerItem(getNome(), 1);
            }
        }

        System.out.println("Durabilidade " + durabilidade);
    }

    // Itens
    public void definirConsumo(String nome) {
        setTipo("consumo");
        switch (nome) {
            case "Cantil":
                setDurabilidadeMax(5);
            default:
                System.out.println("Alimento desconhecido: " + nome);
        }
    }
}
