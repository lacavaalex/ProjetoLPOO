package Entidade;

import Itens.Item;

public class Jogador {

    private String nome;
    private String localizacao;
    private int vidaMax = 10;
    private int vida = getVidaMax();
    private boolean sede = false;
    private int fome, energia;
    private boolean sanidade;
    private int ataqueAtual = 1;

    public Jogador() {

    }

// Getters e setters
    public int getAtaqueAtual() { return ataqueAtual; }
    public void setAtaqueAtual(int ataqueAtual) { this.ataqueAtual = ataqueAtual; }

    public String getNome() { return nome; }
    public String setNome(String nome) {
        this.nome = nome;
        return nome;
    }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
    public void resetVida() { setVida(vidaMax); }
    public int getVidaMax() { return vidaMax; }
    public void setVidaMax() { this.vidaMax = vidaMax; }

    public boolean getSede() { return sede; }
    public void setSede(boolean sede) { this.sede = sede; }
}

