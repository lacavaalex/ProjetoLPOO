package Ambiente;

import Main.Painel;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class Ambiente {

    private Painel painel;
    private String nome, descricao, dificuldade, recursos, frequenciaEventos, climaAmbiente;

    // Atributos de gerencia de inventário/eventos
    private boolean recursosColetados = false;
    private boolean recursosGastos = false;
    private boolean chanceTirada = false;
    private int subStateParaRetornar;

    // Criacao de um set que conta os substates visitados
    private int substateAtual;
    private Set<Integer> substatesVisitados = new HashSet<>();

    public Ambiente(Painel painel) {
        this.painel = painel;
    }

    // Metodo-base para o polimorfismo da superclasse
    public abstract void descreverAmbiente();

    // Metodo=base para construir o card de introdução de ambiente
    public abstract void construirCard(Graphics2D g2);

    // Metodo-base para integrar a UI
    public abstract void playState(Graphics2D g2);

    // Metodo que define o substate após um evento de criatura
    public int definirSubStateParaRetornar() {
        if (painel.getPlaySubState() < 1000) {
            subStateParaRetornar = (painel.getPlaySubState() + 1);
            return subStateParaRetornar;
        } else {
            return subStateParaRetornar;
        }
    }

    // Metodos do set
    public void setSubstate(int novoSubState) {
        this.substateAtual = novoSubState;
        substatesVisitados.add(novoSubState);

        recursosColetados = false;
        recursosGastos = false;
        chanceTirada = false;
    }

    public int getSubstate() { return substateAtual; }

    public int getSubstatesVisitados() { return substatesVisitados.size(); }

    public void resetarSubstatesVisitados() { substatesVisitados.clear(); }

    public boolean checarSeSubstateFoiVisitado(int num) { return substatesVisitados.contains(num); }


    // Getters e setters
    public boolean isRecursosColetados() { return recursosColetados; }
    public void setRecursosColetados(boolean recursosColetados) { this.recursosColetados = recursosColetados; }

    public boolean isRecursosGastos() { return recursosGastos; }
    public void setRecursosGastos(boolean recursosGastos) { this.recursosGastos = recursosGastos; }

    public boolean isChanceTirada() { return chanceTirada; }
    public void setChanceTirada(boolean chanceTirada) { this.chanceTirada = chanceTirada; }

    public int getSubStateParaRetornar() { return subStateParaRetornar; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDificuldade() { return dificuldade; }
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }

    public String getRecursos() { return recursos; }
    public void setRecursos(String recursos) { this.recursos = recursos; }

    public String getFrequenciaEventos() { return frequenciaEventos; }
    public void setFrequenciaEventos(String frequenciaEventos) { this.frequenciaEventos = frequenciaEventos; }

    public String getClimaAmbiente() { return climaAmbiente; }
    public void setClimaAmbiente(String climaAmbiente) { this.climaAmbiente = climaAmbiente; }
}