package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;
import Evento.EventoClimatico;
import Evento.EventoCriatura;
import UI.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Painel extends JPanel implements Runnable {

    // Definição da tela
    private final int originalTileSize = 16;
    private final int escala = 3;
    private int tileSize = originalTileSize * escala;

    private int larguraTela = 1300;
    private int alturaTela = 700;

    // Construtor de classes externas
    private Thread gameThread;
    private Random rand;

    // Construtor de classes internas
    private Ambiente ambienteAtual;
    private Jogador jogador = new Jogador(this);
    private Criatura criatura = new Criatura();

    private Botoes botoes = new Botoes(this);
    private UI ui = new UI(this, jogador);

    private EventoCriatura eventoCriatura = new EventoCriatura(this, ui, jogador, criatura);
    private EventoClimatico eventoClimatico = new EventoClimatico(this, ui, jogador);

    private CombateUI combate = new CombateUI(this, jogador, botoes, eventoCriatura);
    private InventarioUI invent = new InventarioUI(this, jogador, botoes);
    private ClimaUI clima = new ClimaUI(this, jogador, botoes, eventoClimatico);

    private Teclado teclado = new Teclado(this, invent);

    // Definição de FPS
    private final int fps = 60;

    // Game States
    private int gameState;
    private final int titleState = 0;
    private final int gameOverState = 1;
    private final int openingState = 2;
    private final int playState = 3;
    private boolean fightState = false;

    private final int tutorialControles = 9999;

    // Atributos do sistema de substate
    private Map<String, Ambiente> ambientes = new HashMap<>();
    private Set<Integer> subStatesVisitadosTemporario = new HashSet<>();

    private final int numSubStatesAtualizacao = 7;


    public Painel(){

        // Estabelecimento dos dados da tela
        this.setPreferredSize(new Dimension(larguraTela, alturaTela));
        this.setBackground(Color.black);
        this.setLayout(null);

        botoes.configurarComPainel(this);
        botoes.setVisible(true);
        this.add(botoes);

        // Buffering mais preciso
        this.setDoubleBuffered(true);
        // Faz a classe priorizar o input do teclado mesmo na presença de botões
        this.addKeyListener(teclado);
        this.setFocusable(true);

        // Adição do ambiente
        ambienteAtual = new AmbienteFloresta(this, jogador, ui);

        rand = new Random();
    }

    // Inicialização do jogo e aplicação da Thread
    public void setupJogo() {
        gameState = titleState;
        jogador.setAtaqueAtual(jogador.getAtaqueInicial());

        this.setLayout(null);
        botoes.setBounds(0, 0, larguraTela, alturaTela);
        this.add(botoes);

        this.requestFocusInWindow();
    }

    public void iniciarGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Atualização contínua
    public void update() {
        ui.updateFrames();

        updateClima();

        getAmbienteAtual().definirSubStateParaRetornar();

        jogador.atualizarCondicaoJogador(numSubStatesAtualizacao);

        updateBotoes();
    }

    // Atualizacoes contínuas de mecanicas do jogo
    public void updateBotoes() {
        if (gameState == titleState || !invent.isFechado()
                || clima.isAnalisandoClima() || ambienteAtual.isCardVisivel()) {
            botoes.setVisible(false);
        } else {
            botoes.setVisible(true);
        }
    }

    public void updateClima() {
        String climaAtual = eventoClimatico.getClima();
        switch (climaAtual) {
            case "chuva":
            case "tornado":
                if (eventoCriatura.getContadorDeEncontros() >= 5) {
                    getEventoCriatura().resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;

            case "tempestade":
                if (eventoCriatura.getContadorDeEncontros() >= 6) {
                    getEventoCriatura().resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;

            case "cavernoso":
                if (!jogador.getLocalizacao().equals("GRUTA DE SAL")) {
                    getEventoCriatura().resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;

            case "salgado":
                if (!jogador.getLocalizacao().equals("GRUTA DE SAL")) {
                    getEventoCriatura().resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                } else {
                    if (eventoCriatura.getContadorDeEncontros() >= 4) {
                        getEventoCriatura().resetContador();
                        eventoClimatico.setClima("cavernoso");
                    }
                }
                break;

            default:
                break;
        }
    }

    // Implementação do game loop
    public void run() {

        double intervalo = 1000000000 / fps;
        double delta = 0; long lastTime = System.nanoTime();
        long currentTime; long timer = 0; int drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / intervalo;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // Visualização (paint component)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Desenha a UI
        if (!fightState) {
            if (gameState == titleState || gameState == openingState || gameState == playState ||
                    gameState == gameOverState || gameState == tutorialControles) {
                ui.mostrarInterface(g2);
                if (gameState == playState) {
                    ui.mostrarStatus(g2);
                }
            }
        } else {
            combate.estruturaTelaCombate(g2);
        }

        // Mostra a situacao climatica
        if (clima.isAnalisandoClima()) {
            clima.estruturaTelaDeClima(g2, ui);
        }

        // Mostra o car do ambiente atual
        if (ambienteAtual.isCardVisivel()) {
            ambienteAtual.construirCard(g2, ambienteAtual.getNomeFundoCard());
        }

        // Desenha a tela de inventário à frente do resto
        if (!invent.isFechado()) {
            invent.estruturaTelaDeInventario(g2, ui);
        }
    }

    // Transição de ambientes
    public void trocarAmbiente(String nome, int subStateNoNovoAmbiente) {
        if (!ambientes.containsKey(nome)) {

            Ambiente novoAmbiente = switch (nome) {
                case "floresta" -> new AmbienteFloresta(this, jogador, ui);
                case "lago" -> new AmbienteLago(this, jogador, ui);
                case "gruta" -> new AmbienteGruta(this, jogador, ui);
                case "montanha" -> new AmbienteMontanha(this, jogador, ui);
                default -> throw new IllegalArgumentException("Ambiente desconhecido: " + nome);
            };

            ambientes.put(nome, novoAmbiente);
        }
        ui.setNumComando(0);
        ambienteAtual = ambientes.get(nome);
        jogador.setLocalizacao(ambienteAtual.getNome());

        if (!getAmbienteAtual().checarSeSubStateFoiVisitado(subStateNoNovoAmbiente)) {
            ambienteAtual.verCard();
        }
        setPlaySubState(subStateNoNovoAmbiente);
    }


    // Gerador de número aleatório entre 1 e 100 (para probabilidades)
    public int definirUmaProbabilidade() {
        int probabilidade = (rand.nextInt(100) + 1);
        System.out.println("Probabilidade :" + probabilidade);
        return probabilidade;
    }

    // Definicoes de game over
    public void resetAposGameOver() {
        botoes.esconderBotao("Voltar à base");

        resetPlayState();

        getCombate().resetarCriaturaEmCombate();

        invent.esvazearInventario();

        eventoClimatico.setClima("ameno");

        jogador.resetarTodosOsAtributos();

        getEventoCriatura().resetContador();
        ambienteAtual.resetarSubStatesVisitadosTotal();

        ambienteAtual.setBaseFogoAceso(false);
        ambienteAtual.setBaseFonteDeAlimento(false);
    }


    // Getters e setters
    public int getQuantidadeSubStatesVisitadosTemporario() { return subStatesVisitadosTemporario.size(); }

    public Set<Integer> getSubStatesVisitadosTemporario() { return subStatesVisitadosTemporario; }

    public void resetarSubStatesVisitadosTemporario() { subStatesVisitadosTemporario.clear(); }


    // G/S dos game states principais
    public int getGameState() { return gameState; }
    public void setGameState(int gameState) {
        this.gameState = gameState;

        botoes.setVisible(false);
        botoes.esconderBotao("Voltar ao início");
        botoes.esconderBotao("Clima");
        botoes.esconderBotao("Local");
        botoes.esconderBotao("Abrir mochila");
        botoes.esconderBotao("Voltar");
        botoes.esconderBotao("Continuar");
        botoes.esconderBotao("Voltar à base");

        // Não-play states
        if (gameState == openingState) {
            botoes.setVisible(true);
            botoes.mostrarBotao("Continuar");
        }
        if (gameState == tutorialControles) {
            botoes.setVisible(true);
            botoes.mostrarBotao("Voltar");
        }
        if (gameState == gameOverState) {
            botoes.setVisible(true);
            botoes.esconderBotao("Clima");
            botoes.mostrarBotao("Voltar ao início");
        }

        // Play state
        if (gameState == playState) {
            botoes.setVisible(true);
            botoes.mostrarBotao("Abrir mochila");
            botoes.mostrarBotao("Clima");
            botoes.mostrarBotao("Local");
            if (getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
                botoes.mostrarBotao("Voltar à base");
            }
        }

        // Fight state
        if (getEventoCriatura().isEventoCriaturaAtivo()) {
            botoes.esconderBotao("Voltar à base");
            botoes.esconderBotao("Abrir mochila");
            botoes.esconderBotao("Local");
            botoes.mostrarBotao("Continuar");

            if (fightState) {
                botoes.esconderBotao("Local");
                botoes.esconderBotao("Abrir mochila");
                botoes.esconderBotao("Clima");
                botoes.esconderBotao("Continuar");
            }
        }
    }

    public int getPlaySubState() {
        return (ambienteAtual != null ? ambienteAtual.getSubState() : -1);
    }
    public void setPlaySubState(int novoSubState) {
        if (ambienteAtual != null) {
            ambienteAtual.setSubState(novoSubState);
        }
        System.out.println("Substate atual: " + getPlaySubState());
        System.out.println("Substate para retornar: " + getAmbienteAtual().getSubStateParaRetornar());
        System.out.println("Fome: " + jogador.getFome() + " / Sede: " + jogador.estaComSede() + " / Energia: " + jogador.getEnergia());
    }

    public boolean getFightState() { return fightState; }
    public void setFightState(boolean fightState) { this.fightState = fightState; }
    public int getPlayState() { return playState; }
    public void resetPlayState() { setPlaySubState(0); }

    // G/S dos game states diversos
    public int getTitleState() { return titleState; }
    public int getTutorialControles() { return tutorialControles; }
    public int getOpeningState() { return openingState; }
    public int getGameOverState() { return gameOverState; }

    // G/S das classes intermediadas pelo Painel
    public UI getUi() { return ui; }
    public CombateUI getCombate() { return combate; }
    public Jogador getJogador() { return jogador; }
    public Evento.EventoCriatura getEventoCriatura() { return eventoCriatura; }
    public Evento.EventoClimatico getEventoClimatico() { return eventoClimatico; }
    public InventarioUI getInvent() { return invent; }
    public ClimaUI getClima() { return clima; }
    public Botoes getBotoes() { return botoes; }
    public Ambiente getAmbienteAtual() { return ambienteAtual; }

    // G/S das dimensões da tela
    public int getTileSize() { return tileSize; }
    public int getLargura() { return larguraTela; }
    public int getAltura() { return alturaTela; }
}