package Ambiente;

import Controles.Botoes;
import Entidade.Jogador;
import Evento.EventoClimatico;
import Evento.EventoCriatura;
import Main.Painel;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class Ambiente {

    private Painel painel;
    private Jogador jogador;
    private Botoes botoes;

    private String nome;
    private String descricao;
    private String dificuldade;
    private String recursos;
    private String frequenciaEventos;
    private String climaAmbiente;
    private String nomeFundoCombate;
    private boolean cardVisivel;

    // Atributos de gerencia de inventário/eventos
    private boolean recursosColetados = false;
    private boolean recursosGastos = false;
    private boolean chanceTirada = false;

    private int subStateParaRetornar;
    private boolean baseFontedeAlimento = false;
    private boolean baseFogoAceso;
    private int baseFortificacao = 0;

    // Criacao de um set que conta os substates visitados
    private int subStateAtual;
    private int subStateAnterior = -1;
    private Set<Integer> subStatesVisitadosTotal = new HashSet<>();

    // Atributos de transicao
    private float alphaFade = 0f;
    private boolean transicaoIniciada = false;
    private boolean transicaoFinalizada = false;

    public Ambiente(Painel painel, Jogador jogador) {
        this.painel = painel;
        this.jogador = jogador;
        this.botoes = painel.getBotoes();
    }

    public void verCard() {
        cardVisivel = true;
    }
    public void sair() {
        cardVisivel = false;
        botoes.mostrarBotaoCardAmbiente();
    }

    // Metodo-base para o polimorfismo da superclasse
    public abstract void descreverAmbiente();

    // Metodo=base para construir o card de introdução de ambiente
    public abstract void construirCard(Graphics2D g2);

    // Metodo-base para integrar a UI
    public abstract void playState(Graphics2D g2);

    // Metodos de definicao de evento
    public void definirOcorrenciaDeEventoCriatura (Graphics2D g2, EventoCriatura nomeEventoCriatura, int tipo) {
        if (!isChanceTirada()) {
            nomeEventoCriatura.chance(g2, tipo);
            setChanceTirada(true);
        }

        if (nomeEventoCriatura.getExecutavel() == 1) {
            nomeEventoCriatura.setSurpresa(true);
            nomeEventoCriatura.executar(g2, tipo);
        }

        else if (nomeEventoCriatura.getExecutavel() == 0) {
            painel.setPlaySubState(getSubStateParaRetornar());
        }
    }

    public void definirOcorrenciaDeEventoClimatico(Graphics2D g2, EventoClimatico nomeEventoClimatico, int tipo) {
        if (!isChanceTirada()) {
            nomeEventoClimatico.chance(g2, tipo);
            setChanceTirada(true);
        }
        if (nomeEventoClimatico.getExecutavel() == 1) {
            nomeEventoClimatico.executar(g2, tipo);
        }
    }

    public void transicaoDeTelaBoss (Graphics2D g2) {
        if (transicaoIniciada && alphaFade < 1.0f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - alphaFade));
            alphaFade += 0.01f;
            if (alphaFade >= 1.0f) {
                alphaFade = 1.0f;
                transicaoFinalizada = true;
            }
        }
    }

    public void resetAtributosTransicao() {
        alphaFade = 0f;
        transicaoIniciada = false;
        transicaoFinalizada = false;
    }

    // Metodo que define o substate após um evento de criatura
    public void definirSubStateParaRetornar () {
        if (painel.getPlaySubState() < 1000 && painel.getPlaySubState() != 1) {
            setSubStateParaRetornar(painel.getPlaySubState() + 1);
        }
    }

    public void voltarStateAnterior () {
        if (getSubStateAnterior() != -1) {
            painel.setPlaySubState(getSubStateAnterior());
            subStateAnterior = -1;
        }
    }

    // Metodos do Set
    public int getSubState () { return subStateAtual; }

    public void setSubState ( int novoSubState){
        if (novoSubState == 1 && painel.getPlaySubState() != 1) {
            subStateAnterior = painel.getPlaySubState();
        }

        this.subStateAtual = novoSubState;

        painel.getSubStatesVisitadosTemporario().add(novoSubState);
        subStatesVisitadosTotal.add(novoSubState);

        recursosColetados = false;
        recursosGastos = false;
        chanceTirada = false;

    }

    public int getSubStatesVisitadosTotal() { return subStatesVisitadosTotal.size(); }

    public void resetarSubStatesVisitadosTotal() { subStatesVisitadosTotal.clear(); }

    public boolean checarSeSubStateFoiVisitado(int num) { return subStatesVisitadosTotal.contains(num); }

    public void definirTelaDeBotao(String voltarOuContinuar) {
        switch (voltarOuContinuar) {
            case "continuar":
                botoes.mostrarBotaoContinuar();
                break;
            case "voltar":
                botoes.mostrarBotaoVoltar();
                break;
        }
        botoes.esconderBotaoMochila();
        botoes.esconderBotaoBase();
    }

    // Getters e setters
    public boolean isCardVisivel() { return cardVisivel; }

    public boolean isRecursosColetados() { return recursosColetados; }
    public void setRecursosColetados(boolean recursosColetados) { this.recursosColetados = recursosColetados; }

    public boolean isRecursosGastos() { return recursosGastos; }
    public void setRecursosGastos(boolean recursosGastos) { this.recursosGastos = recursosGastos; }

    public boolean isChanceTirada() { return chanceTirada; }
    public void setChanceTirada(boolean chanceTirada) { this.chanceTirada = chanceTirada; }

    public int getSubStateAnterior() { return subStateAnterior; }
    public int getSubStateParaRetornar() { return subStateParaRetornar; }
    public void setSubStateParaRetornar(int subStateParaRetornar) { this.subStateParaRetornar = subStateParaRetornar; }

    public boolean isBaseFonteDeAlimento() { return baseFontedeAlimento; }
    public void setBaseFonteDeAlimento(boolean baseFonteDeAlimento) { this.baseFontedeAlimento = baseFonteDeAlimento; }

    public boolean isBaseFogoAceso() { return baseFogoAceso; }
    public void setBaseFogoAceso(boolean baseFogoAceso) { this.baseFogoAceso = baseFogoAceso; }

    public int getBaseFortificacao() { return baseFortificacao; }
    public void setBaseFortificacao(int baseFortificacao) { this.baseFortificacao = baseFortificacao; }

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

    public String getNomeFundoCombate() { return nomeFundoCombate; }
    public void setNomeFundoCombate(String nomeFundoCombate) { this.nomeFundoCombate = nomeFundoCombate; }

    public boolean isTransicaoIniciada() { return transicaoIniciada; }
    public void setTransicaoIniciada(boolean transicaoIniciada) { this.transicaoIniciada = transicaoIniciada; }

    public boolean isTransicaoFinalizada() { return transicaoFinalizada; }
    public void setTransicaoFinalizada (boolean transicaoFinalizada) { this.transicaoFinalizada = transicaoFinalizada; }
}