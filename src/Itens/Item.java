package Itens;

public class Item {

    private String nome;
    private int quantidade;
    private Integer durabilidade;

    public Item() {
        nome = getNome();
        quantidade = getQuantidade();
        durabilidade = getDurabilidade();
    }

// Metodo-base para o polimorfismo da superclasse
    public void usar() {
        if (durabilidade != null && durabilidade > 0) {
            durabilidade--;
        }
    }


// Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public Integer getDurabilidade() { return durabilidade; }
    public void setDurabilidade(Integer durabilidade) { this.durabilidade = durabilidade; }
}