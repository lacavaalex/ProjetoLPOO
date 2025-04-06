package Entidade;

import Ambiente.*;

import java.util.HashMap;

public class Jogador {

    //Rastreador, mecanico, sobrevivent

    private String nome, localizacao;
    private int vida = 10;
    private boolean sede = false;
    private int fome, energia;
    private boolean sanidade;
    private HashMap<String, Item> inventario = new HashMap<>();

    public void adicionarItem(String nome, int quantidade) {
        if (this.inventario.containsKey(nome)) {
            inventario.get(nome).setQuantidade(quantidade);
        } else {
            inventario.put(nome, new Item(nome, quantidade));
        }
    }

    public HashMap<String, Item> getInventario() {
        return inventario;
    }

// Getters e setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(String nome) {
        this.localizacao = localizacao;
    }

    public int getVida() {
        return vida;
    }
    public void setVida(int vida) {
        this.vida = vida;
    }
    public boolean getSede() {
        return sede;
    }
    public void setSede(boolean sede) {
        this.sede = sede;
    }
}
