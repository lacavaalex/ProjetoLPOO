package Itens;

public class ItemRecurso extends Item {

    public ItemRecurso(String nome, int quantidade, Integer durabilidade) {
        super(nome, quantidade);
    }

    @Override
    public void usar() {

    }

// Itens
    public void definirMadeira(int quantidade) {
        if (getNome().equals("Madeira")) {
            setDurabilidade(null);
        }
    }

    public void definirPedra() {
        if (getNome().equals("Pedra")) {
            setDurabilidade(null);
        }
    }
}
