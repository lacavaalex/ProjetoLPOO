package Ambiente;

import javax.swing.*;
import java.awt.*;

public class Ambiente extends JPanel {

    private String nome, descricao, dificuldade, recursos, frequenciaEventos, clima;

    // Atributos de gerencia de inventário/eventos
    private boolean recursosColetados = false;
    private boolean recursosGastos = false;
    private boolean encontroSurpresa = false;

    public Ambiente() {
    }

// Metodo-base para o polimorfismo da superclasse
    public void descreverAmbiente() {}

// Metodo-base para integrar a UI
    public void playState(Graphics2D g2) {}


// Getters e setters
    public boolean isRecursosColetados() {
        return recursosColetados;
    }
    public void setRecursosColetados(boolean recursosColetados) {
        this.recursosColetados = recursosColetados;
    }
    public boolean isRecursosGastos() {
        return recursosGastos;
    }
    public void setRecursosGastos(boolean recursosGastos) { this.recursosGastos = recursosGastos; }
    public boolean isEncontroSurpresa() { return encontroSurpresa; }
    public void setEncontroSurpresa(boolean encontroSurpresa) { this.encontroSurpresa = encontroSurpresa; }

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


