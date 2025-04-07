package Entidade;

import Main.*;

public class Criatura {

    private String nome;
    private int vida = 0;
    private int ataque = 0;

    public Criatura() {

    }


    public void setViboraRubro() {
        setNomeCriatura("Víbora-Rubro");
        setVidaCriatura(3);
        setAtaqueCriatura(5);
    }
    public void setUrsoPai() {
        setNomeCriatura("Urso Pai");
        setVidaCriatura(100);
        setAtaqueCriatura(100);
    }


    public String getDescricao() {
        return "" + getNomeCriatura() + ": " + String.valueOf(getVidaCriatura()) + "HP / " + String.valueOf(getAtaqueCriatura()) + "ATK";
    }
    public String getNomeCriatura() {
        return nome;
    }
    public String setNomeCriatura(String nome) {
        this.nome = nome;
        return nome;
    }
    public int getVidaCriatura() {
        return vida;
    }
    public void setVidaCriatura(int vida) {
        this.vida = vida;
    }
    public int getAtaqueCriatura() {
        return ataque;
    }
    public void setAtaqueCriatura(int ataque) {
        this.ataque = ataque;
    }
}
