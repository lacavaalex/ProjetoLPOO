package Itens;

public class ItemRecurso extends Item {

    public ItemRecurso() {
        super();
    }

    @Override
    public void usar() {

    }

    // Itens
    public void definirRecurso(String nome) {
        switch (nome) {
            case "Madeira":
                setDurabilidade(null);
                break;

            case "Pedra":
                setDurabilidade(null);
                break;

            default:
                System.out.println("Recurso desconhecido: ");
                break;
        }
    }
}
