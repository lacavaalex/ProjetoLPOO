package Itens;

public class ItemConsumo extends Item {

    Inventario invent;

    public ItemConsumo(String nome, int quantidade, Integer durabilidade) {
        super(nome, quantidade);
    }

    @Override
    public void usar() {
        consumir();
    }

    public void consumir() {
        int nivel = getDurabilidade() - 1;
        setDurabilidade(nivel);

        if (nivel == 0) {
            invent.removerItem(getNome(), 1);
        }
    }

// Itens
    public void definirCantil() {
        if (getNome().equals("Cantil")) {
            setDurabilidade(5);
        }
    }
}
