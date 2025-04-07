package Ambiente;

import Main.*;

import javax.swing.*;
import java.awt.*;

public class Ambiente extends JPanel {

    Painel painel;
    Font pixelsans_10;

    private String nome, descricao, dificuldade, recursos, frequenciaEventos, clima;

    public void ambienteFloresta() {
        this.nome = "FLORESTA MACABRA";
        this.descricao = "Escura, densa, barulhenta.";
        this.dificuldade = "medianas.";
        this.recursos = "frutas, água, madeira, pedras.";
        this.frequenciaEventos = "muitas criaturas, esconderijos, riscos à saúde.";
        this.clima = "levemente frio.";
    }
    public void ambienteLago() {
        this.nome = "LAGO SERENO";
        this.descricao = "Limpo, vasto, estranhamente silencioso.";
        this.dificuldade = "tranquilas.";
        this.recursos = "água.";
        this.frequenciaEventos = "quieto, um certo ar de misticismo.";
        this.clima = "levemente frio";
    }
    public void ambienteMontanha() {
        this.nome = "MONTANHA EPOPEICA";
        this.descricao = "Imponente, desafiadora, majestosa.";
        this.dificuldade = "perigosas.";
        this.recursos = "indefinido.";
        this.frequenciaEventos = "desastres naturais, perigos escondidos.";
        this.clima = "altamente frio, piora com altitude.";
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDificuldade() {
        return dificuldade;
    }
    public String getRecursos() {
        return recursos;
    }
    public String getFrequenciaEventos() {
        return frequenciaEventos;
    }
    public String getClima() {
        return clima;
    }
}


