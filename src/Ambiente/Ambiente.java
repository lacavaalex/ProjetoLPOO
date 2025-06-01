package Ambiente;

import Controles.Botoes;
import Entidade.Jogador;
import Evento.EventoClimatico;
import Evento.EventoCriatura;
import UI.UI;
import Main.Painel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public abstract class Ambiente {

    private Painel painel;
    private UI ui;
    private Jogador jogador;
    private Botoes botoes;

    private String nome;
    private String descricao;
    private String dificuldade;
    private String recursos;
    private String frequenciaEventos;
    private String climaAmbiente;
    private String nomeFundoCombate;
    private String nomeFundoCard;
    private BufferedImage fundoCard;
    private boolean cardVisivel;

    // Atributos de gerencia de inventário/eventos
    private boolean recursosColetados = false;
    private boolean recursosGastos = false;
    private boolean chanceTirada = false;
    private boolean chanceClimaTirada = false;
    private boolean eventoEspecialDefinido = false;

    private int subStateParaRetornar;

    // Atributos do acampamento
    private boolean baseFontedeAlimento = false;
    private boolean colheitaPronta = false;
    private boolean baseFogoAceso;
    private int baseFortificacao = 0;
    private int timerAcampamento = 0;

    // Atributos da pescaria e caça
    private int contadorTimer = 0;
    private final int tempoDeEsperaEmFPS = 600;
    private boolean aguardando = true;

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
        this.ui = painel.getUi();
        this.botoes = painel.getBotoes();
    }

    // Metodo-base para o polimorfismo da superclasse
    public abstract void descreverAmbiente();

    // Metodo-base para integrar a UI
    public abstract void playState(Graphics2D g2);

    // Metodo-base para construir o card de introdução de ambiente
    public void construirCard(Graphics2D g2, String nomeFundoCard) {
        if (isCardVisivel()) {

            int tileSize = painel.getTileSize();
            int y = tileSize * 3;

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

            fundoCard = ui.setupImagens(nomeFundoCard, "background");

            if (fundoCard != null) {
                ui.desenharPlanoDeFundo(fundoCard);
            }

            g2.setColor(Color.white);
            ui.escreverTexto(getNome(), y += tileSize);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
            y = tileSize * 5;

            ui.escreverTexto(getDescricao(), y += tileSize);
            ui.escreverTexto(" ", y += tileSize);
            ui.escreverTexto("Condições de exploração: " + getDificuldade(), y += tileSize);
            ui.escreverTexto("Recursos possíveis: " + getRecursos(), y += tileSize);
            ui.escreverTexto("Ecossistema: " + getFrequenciaEventos(), y += tileSize);
            ui.escreverTexto("Clima: " + getClimaAmbiente(), y += tileSize);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
            String textoEsc = ("Aperte [esc] para sair");
            g2.drawString(textoEsc, painel.getLargura() - tileSize * 6,painel.getAltura() - tileSize);
            g2.setColor(Color.white);
        }
    }

    public void verCard() {
        cardVisivel = true;
    }
    public void sair() {
        cardVisivel = false;
        botoes.mostrarBotao("Local");
    }

    // Atualização da condição do acampamento
    public void atualizarAcampamento() {
        int numLimite = 40;
        timerAcampamento++;

        if (timerAcampamento == numLimite) {
            if (isBaseFonteDeAlimento()) {
                setColheitaPronta(true);
            }
            if (isBaseFogoAceso()) {
                setBaseFogoAceso(false);
            }
            timerAcampamento = 0;
        }
    }

    // Metodos de definicao de evento
    public void definirOcorrenciaDeEventoCriatura (Graphics2D g2, EventoCriatura nomeEventoCriatura, int tipo) {
        if (!isChanceTirada()) {
            nomeEventoCriatura.chance(g2, tipo);
            if (nomeEventoCriatura.getExecutavel() == 1) {
                nomeEventoCriatura.setSurpresa(true);
            }
            setChanceTirada(true);
        }

        if (nomeEventoCriatura.getExecutavel() == 1) {
            nomeEventoCriatura.executar(g2, tipo);
        }

        else if (nomeEventoCriatura.getExecutavel() == 0) {
            painel.setPlaySubState(getSubStateParaRetornar());
        }
    }

    public void definirOcorrenciaDeEventoClimatico(Graphics2D g2, EventoClimatico nomeEventoClimatico, int tipo) {
        if (!isChanceClimaTirada()) {
            nomeEventoClimatico.chance(g2, tipo);
            setChanceClimaTirada(true);
        }

        if (nomeEventoClimatico.getExecutavel() == 1) {
            nomeEventoClimatico.executar(g2, tipo);
        }
    }

    // Recursos visuais do playstate
    public void definirTelaDeBotao(String voltarOuContinuar) {
        switch (voltarOuContinuar) {
            case "continuar":
                botoes.mostrarBotao("Continuar");
                break;
            case "voltar":
                botoes.mostrarBotao("Voltar");
                break;
        }
        botoes.esconderBotao("Abrir mochila");
        botoes.esconderBotao("Voltar à base");
    }

    public void desenharImagemZoom(Graphics2D g2, BufferedImage imagem) {
        int tileSize = painel.getTileSize();
        int escala = tileSize * 12;
        int x = painel.getLargura()/2 - escala/2;

        g2.drawImage(imagem, x, tileSize/2, escala, escala, null);
    }

    public void transicaoDeTela(Graphics2D g2) {
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

    // Reestruturação do timer de pescaria e caça
    public void iniciarEspera() {
        contadorTimer = 0;
        aguardando = true;
    }

    // Metodo que define o substate após um evento de criatura
    public void definirSubStateParaRetornar () {
        int subState = painel.getPlaySubState();

        if (subState < 1000 && subState != 1 && subState != 4) {
            setSubStateParaRetornar(painel.getPlaySubState() + 1);
        }
    }

    public void voltarStateAnterior () {
        if (getSubStateAnterior() != -1) {
            painel.setPlaySubState(getSubStateAnterior());
            subStateAnterior = -1;
        }
    }

    // Metodos do Set de substates
    public int getSubState () { return subStateAtual; }

    public void setSubState ( int novoSubState){
        if (novoSubState == 1 && painel.getPlaySubState() != 1) {
            subStateAnterior = painel.getPlaySubState();
        }
        if (novoSubState >= 10 &&
                subStateAnterior != painel.getPlaySubState()) {
            atualizarAcampamento();
        }

        this.subStateAtual = novoSubState;

        painel.getSubStatesVisitadosTemporario().add(novoSubState);
        subStatesVisitadosTotal.add(novoSubState);

        recursosColetados = false;
        recursosGastos = false;
        chanceTirada = false;
        chanceClimaTirada = false;
        eventoEspecialDefinido = false;

    }

    public int getSubStatesVisitadosTotal() { return subStatesVisitadosTotal.size(); }

    public void resetarSubStatesVisitadosTotal() { subStatesVisitadosTotal.clear(); }

    public boolean checarSeSubStateFoiVisitado(int num) { return subStatesVisitadosTotal.contains(num); }

    // Getters e setters
    public boolean isCardVisivel() { return cardVisivel; }

    public boolean isRecursosColetados() { return recursosColetados; }
    public void setRecursosColetados(boolean recursosColetados) { this.recursosColetados = recursosColetados; }

    public boolean isRecursosGastos() { return recursosGastos; }
    public void setRecursosGastos(boolean recursosGastos) { this.recursosGastos = recursosGastos; }

    public boolean isChanceTirada() { return chanceTirada; }
    public void setChanceTirada(boolean chanceTirada) { this.chanceTirada = chanceTirada; }

    public boolean isChanceClimaTirada() { return chanceClimaTirada; }
    public void setChanceClimaTirada(boolean chanceClimaTirada) { this.chanceClimaTirada = chanceClimaTirada; }

    public boolean isEventoEspecialDefinido() { return eventoEspecialDefinido; }
    public void setEventoEspecialDefinido(boolean eventoEspecialDefinido) { this.eventoEspecialDefinido = eventoEspecialDefinido; }

    public int getSubStateAnterior() { return subStateAnterior; }
    public int getSubStateParaRetornar() { return subStateParaRetornar; }
    public void setSubStateParaRetornar(int subStateParaRetornar) { this.subStateParaRetornar = subStateParaRetornar; }

    public boolean isBaseFonteDeAlimento() { return baseFontedeAlimento; }
    public void setBaseFonteDeAlimento(boolean baseFonteDeAlimento) { this.baseFontedeAlimento = baseFonteDeAlimento; }

    public boolean isColheitaPronta() { return colheitaPronta; }
    public void setColheitaPronta(boolean colheitaPronta) { this.colheitaPronta = colheitaPronta; }

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

    public String getNomeFundoCard() { return nomeFundoCard; }
    public void setNomeFundoCard(String nomeFundoCard) { this.nomeFundoCard = nomeFundoCard; }

    public boolean isTransicaoIniciada() { return transicaoIniciada; }
    public void setTransicaoIniciada(boolean transicaoIniciada) { this.transicaoIniciada = transicaoIniciada; }

    public boolean isTransicaoFinalizada() { return transicaoFinalizada; }
    public void setTransicaoFinalizada (boolean transicaoFinalizada) { this.transicaoFinalizada = transicaoFinalizada; }

    public int getContadorTimer() { return contadorTimer; }
    public void setContadorTimer(int contadorTimer) { this.contadorTimer = contadorTimer; }

    public int getTempoDeEsperaEmFPS() { return tempoDeEsperaEmFPS; }

    public boolean isAguardando() { return aguardando; }
    public void setAguardando(boolean aguardando) { this.aguardando = aguardando; }
}