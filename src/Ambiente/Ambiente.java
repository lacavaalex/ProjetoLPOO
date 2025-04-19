package Ambiente;

import Main.*;

import javax.swing.*;
import java.awt.*;

public class Ambiente extends JPanel {

    private String nome, descricao, dificuldade, recursos, frequenciaEventos, clima;

    public void descreverAmbiente() {
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
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }

    public String getRecursos() {
        return recursos;
    }
    public void setRecursos(String recursos) { this.recursos = recursos; }

    public String getFrequenciaEventos() {
        return frequenciaEventos;
    }
    public void setFrequenciaEventos(String frequenciaEventos) { this.frequenciaEventos = frequenciaEventos; }

    public String getClima() {
        return clima;
    }
    public void setClima(String clima) { this.clima = clima; }
}


