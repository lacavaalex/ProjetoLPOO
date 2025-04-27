package Itens;

public class ItemConsumo extends Item {

    Inventario invent;

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
            invent.removerItem(getNome(), 1);
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
}
