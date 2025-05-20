package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;
import Evento.EventoClimatico;
import Evento.EventoCriatura;
import UI.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Painel extends JPanel implements Runnable {

    // Construtor de classes externas
    private Thread gameThread;
    private Random rand;

    // Definição da tela
    private final int originalTileSize = 16;
    private final int escala = 3;
    private int tileSize = originalTileSize * escala;

    private int larguraTela = 1300;
    private int alturaTela = 700;

    // Definição de FPS
    private final int fps = 60;

    // Chamada de classes
    private Ambiente ambienteAtual;
    private Jogador jogador = new Jogador();
    private Criatura criatura = new Criatura();

    private Botoes botoes = new Botoes(this);
    private UI ui = new UI(this, jogador);

    private EventoCriatura eventoCriatura = new EventoCriatura(this, ui, jogador, criatura);
    private EventoClimatico eventoClimatico = new EventoClimatico(this, ui, jogador);

    private CombateUI combate = new CombateUI(this, jogador, botoes, eventoCriatura);
    private InventarioUI invent = new InventarioUI(this, jogador, botoes);
    private ClimaUI clima = new ClimaUI(this, jogador, botoes, eventoClimatico);

    private Teclado teclado = new Teclado(this, invent);

    // Game States
    private int gameState;
    private final int titleState = 0;
    private final int gameOverState = 1;
    private final int openingState = 2;
    private final int playState = 3;
    private int playSubState = 0;
    private boolean fightState = false;

    private final int tutorialControles = 9999;
    private final int florestaCardState = 99991;
    private final int lagoCardState = 99992;
    private final int montanhaCardState = 99993;

    private Map<String, Ambiente> ambientes = new HashMap<>();


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

        this.setLayout(null);
        botoes.setBounds(0, 0, larguraTela, alturaTela);
        this.add(botoes);
    }

    public void iniciarGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
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
                updateBotoes();
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

    // Atualizacoes contínuas de mecanicas do jogo
    public void updateBotoes() {
        if (gameState == titleState || !invent.isFechado() || clima.isAnalisandoClima() ) {
            botoes.setVisible(false);
        } else {
            botoes.setVisible(true);
        }
    }

    public void updateClima() {
        String climaAtual = eventoClimatico.getClima();

        switch(climaAtual) {
            case "chuva":
                if (eventoCriatura.getContadorDeEncontros() >= 5) {
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;
            default: break;
        }
    }

    // Visualização (paint component)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Metodos que devem ser atualizados continuamente
        ui.updateFrames();
        updateClima();
        getAmbienteAtual().definirSubStateParaRetornar();

        // Desenha a UI
        if (!fightState) {
            if (gameState == titleState || gameState == openingState || gameState == playState ||
                    gameState == gameOverState || gameState == tutorialControles ||
                    gameState == florestaCardState || gameState == lagoCardState || gameState == montanhaCardState) {
                ui.mostrarInterface(g2);
                if (gameState == playState) {
                    ui.mostrarStatusEAmbiente(g2);
                }
            }
        } else {
            combate.estruturaTelaCombate(g2, ui);
        }

        // Mostra a situacao climatica
        if (clima.isAnalisandoClima()) {
            clima.estruturaTelaDeClima(g2, ui);
        }

        // Desenha a tela de inventário à frente do resto
        if (!invent.isFechado()) {
            invent.estruturaTelaDeInventario(g2, ui);
        }

        this.requestFocusInWindow();
    }

    // Transição de ambientes
    public void trocarAmbiente(String nome) {
        if (!ambientes.containsKey(nome)) {

             Ambiente novoAmbiente = switch (nome) {
                case "floresta" -> new AmbienteFloresta(this, jogador, ui);
                case "lago" -> new AmbienteLago(this, jogador, ui);
                case "montanha" -> new AmbienteMontanha(this, jogador, ui);
                default -> throw new IllegalArgumentException("Ambiente desconhecido: " + nome);
            };

            ambientes.put(nome, novoAmbiente);
        }
        ambienteAtual = ambientes.get(nome);
        jogador.setLocalizacao(ambienteAtual.getNome());
    }

    // Definicoes de game over
    public void resetAposGameOver() {
        resetPlayState();

        jogador.resetVida();
        jogador.resetFome();
        jogador.setSede(false);
        jogador.setNome(null);

        getEventoCriatura().resetContador();
        getAmbienteAtual().resetarSubstatesVisitados();
    }

    // Gerador de número aleatório entre 1 e 100 (para probabilidades)
    public int definirUmaProbabilidade() {
        return (rand.nextInt(100) + 1);
    }


    // Getters e setters

    // G/S dos game states principais
    public int getGameState() { return gameState; }
    public void setGameState(int gameState) {
        this.gameState = gameState;

        botoes.setVisible(false);
        botoes.esconderBotaoInicio();
        botoes.esconderBotaoClima();
        botoes.esconderBotaoMochila();
        botoes.esconderBotaoVoltar();
        botoes.esconderBotaoContinuar();

        // Não-play states
        if (gameState == openingState) {
            botoes.setVisible(true);
            botoes.mostrarBotaoContinuar();
        }
        if (gameState == tutorialControles) {
            botoes.setVisible(true);
            botoes.mostrarBotaoVoltar();
        }
        if (gameState == gameOverState) {
            botoes.setVisible(true);
            botoes.mostrarBotaoInicio();
        }

        // Cards de ambiente
        if (gameState == florestaCardState) {
            botoes.setVisible(true);
            trocarAmbiente("floresta");
            botoes.mostrarBotaoContinuar();
        }
        if (gameState == lagoCardState) {
            botoes.setVisible(true);
            trocarAmbiente("lago");
            botoes.mostrarBotaoContinuar();
        }
        if (gameState == montanhaCardState) {
            botoes.setVisible(true);
            trocarAmbiente("montanha");
            botoes.mostrarBotaoContinuar();
        }

        // Play state
        if (gameState == playState) {
            botoes.setVisible(true);
            botoes.mostrarBotaoMochila();
            botoes.mostrarBotaoClima();
        }

        // Fight state
        if (getEventoCriatura().isEventoCriaturaAtivo()) {
            botoes.esconderBotaoMochila();
            botoes.mostrarBotaoClima();
            botoes.mostrarBotaoContinuar();

            if (fightState) {
                botoes.esconderBotaoContinuar();
                botoes.mostrarBotaoMochila();
            }
        }
    }

    public int getPlaySubState() {
        return (ambienteAtual != null ? ambienteAtual.getSubstate() : -1);
    }
    public void setPlaySubState(int novoSubState) {
        if (ambienteAtual != null) {
            ambienteAtual.setSubstate(novoSubState);
        }
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

    // G/S dos cards de Ambiente
    public int getFlorestaCardState() { return florestaCardState; }
    public int getLagoCardState() { return lagoCardState; }
    public int getMontanhaCardState() { return montanhaCardState; }

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
