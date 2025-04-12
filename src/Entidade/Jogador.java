package Entidade;

import Itens.Item;

public class Jogador {

    private String nome;
    private String localizacao;
    private int vidaMax = 10;
    private int vida;
    private boolean sede = false;
    private int fome, energia;
    private boolean sanidade;


// Getters e setters
    public String getNome() {
        return nome;
    }
    public String setNome(String nome) {
        this.nome = nome;
        return nome;
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
    public void resetVida() {
        setVida(vidaMax);
    }
    public int getVidaMax() {
        return vidaMax;
    }
    public void setVidaMax() {
        this.vidaMax = vidaMax;
    }

    public boolean getSede() {
        return sede;
    }
    public void setSede(boolean sede) {
        this.sede = sede;
    }
}

