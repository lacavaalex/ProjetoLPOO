package Ambiente;

import Entidade.Jogador;
import Main.Painel;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class Ambiente {

    private Painel painel;
    private Jogador jogador;
    private String nome, descricao, dificuldade, recursos, frequenciaEventos, climaAmbiente;

    // Atributos de gerencia de inventário/eventos
    private boolean recursosColetados = false;
    private boolean recursosGastos = false;
    private boolean chanceTirada = false;
    private int subStateParaRetornar;

    // Criacao de um set que conta os substates visitados
    private int subStateAtual;
    private Set<Integer> subStatesVisitadosTotal = new HashSet<>();
    private Set<Integer> subStatesVisitadosTemporario = new HashSet<>();

    public Ambiente(Painel painel, Jogador jogador) {
        this.painel = painel;
        this.jogador = jogador;
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
        this.subStateAtual = novoSubState;
        subStatesVisitadosTotal.add(novoSubState);
        subStatesVisitadosTemporario.add(novoSubState);

        recursosColetados = false;
        recursosGastos = false;
        chanceTirada = false;
    }

    public int getSubState() { return subStateAtual; }

    public void contagemSubStates(int numLimite) {
        if (numLimite == getSubStatesVisitadosTemporario()) {
            jogador.setFome(jogador.getFome() - 1);
            resetarSubStatesVisitadosTemporario();
        }
        System.out.println("Temporario " + getSubStatesVisitadosTemporario());
    }

    public int getSubStatesVisitadosTotal() { return subStatesVisitadosTotal.size(); }
    public int getSubStatesVisitadosTemporario() { return subStatesVisitadosTemporario.size(); }

    public void resetarSubStatesVisitadosTotal() { subStatesVisitadosTotal.clear(); }
    public void resetarSubStatesVisitadosTemporario() { subStatesVisitadosTemporario.clear(); }

    public boolean checarSeSubStateFoiVisitado(int num) { return subStatesVisitadosTotal.contains(num); }


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