package Itens;

import Main.Painel;

public class Item {

    private Painel painel;

    private String nome;
    private int quantidade;
    private String tipo;
    private Integer durabilidadeMax;

    public Item(Painel painel) {
        this.painel = painel;

        nome = getNome();
        quantidade = getQuantidade();
        tipo = getTipo();
        durabilidadeMax = getDurabilidadeMax();
    }

    // Metodo-base para o polimorfismo da superclasse
    public void usar(String nome) {
        //if (durabilidade != null && durabilidade > 0) {
        //    durabilidade--;
        //}
    }


    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getDurabilidadeMax() { return durabilidadeMax; }
    public void setDurabilidadeMax(Integer durabilidadeMax) { this.durabilidadeMax = durabilidadeMax; }

    public Painel getPainel() { return painel; }
}