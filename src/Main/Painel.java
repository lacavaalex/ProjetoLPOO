package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;
import Evento.EventoCriatura;
import Itens.*;
import UI.*;

import javax.swing.*;
import java.awt.*;

public class Painel extends JPanel implements Runnable {

    // Construtor de classes externas
    Thread gameThread;

    // Definição da tela
    private final int originalTileSize = 16;
    private final int escala = 3;
    private int tileSize = originalTileSize * escala;

    private int larguraTela = 1300;
    private int alturaTela = 700;

    // Definição de FPS
    private final int fps = 60;

    // Chamada de classes
    private Ambiente ambiente = new Ambiente();
    private Jogador jogador = new Jogador();
    private Criatura criatura = new Criatura();

    private Teclado teclado = new Teclado(this);
    private Botões botoes = new Botões(this);

    private Inventario invent = new Inventario(this, botoes);

    private UI ui = new UI(this, jogador);
    private CombateUI combate = new CombateUI(this, jogador);
    //private CardsEspeciaisUI cardsEspeciaisUI;

    private EventoCriatura eventoCriatura = new EventoCriatura(this, ui, jogador, criatura);

    // Game States
    private int gameState;
    private final int titleState = 0;
    private final int gameOverState = 1;
    private final int openingState = 2;
    private final int playState = 3;
    private int playSubState = 0;
    private boolean fightState = false;

    private final int tutorialControles = 10000;
    private final int florestaCardState = 10001;
    private final int lagoCardState = 10002;
    private final int montanhaCardState = 10003;



    public Painel(){

        // Estabelecimento dos dados da tela
        this.setPreferredSize(new Dimension(larguraTela, alturaTela));
        this.setBackground(Color.black);
        this.setLayout(null);

        botoes.configurarComPainel(this);
        botoes.setVisible(true);
        this.add(botoes);
        botoes.mostrarBotaoContinuar();

        // Buffering mais preciso
        this.setDoubleBuffered(true);
        // Faz a classe priorizar o input do teclado mesmo na presença de botões
        this.addKeyListener(teclado);
        this.setFocusable(true);

        // Adição do ambiente
        ambiente.setBounds(0, 0, larguraTela, alturaTela);
        ambiente.setVisible(false);
        this.add(ambiente);

        // Adição das outras UIs
        ui.setAmbiente(ambiente);

        //cardsEspeciaisUI = new CardsEspeciaisUI(this, jogador);
        //ui.setCardsEspeciaisUI(cardsEspeciaisUI);

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

    // Atualização inicial e contínua
    public void update() {
        if (gameState == titleState) {
            botoes.setVisible(false);
        } else {
            botoes.setVisible(true);
        }
    }

    // Visualização (paint component)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Desenha a UI
        if (!fightState) {
            if (gameState == titleState || gameState == openingState || gameState == playState ||
                    gameState == gameOverState || gameState == tutorialControles ||
                    gameState == florestaCardState || gameState == lagoCardState || gameState == montanhaCardState) {
                ui.mostrar(g2);
            }
        } else {
            combate.telaCombate(g2, ui);
        }

        // Desenha a tela de inventário à frente do resto
        if (!invent.isFechado()) {
            invent.telaDeInventario(g2, ui);
        }

        this.requestFocusInWindow();
    }

    // Transição de ambientes
    public void trocarAmbiente(String qual) {

        switch (qual) {
            case "floresta":
                Ambiente floresta = new AmbienteFloresta(this, jogador, ui);
                floresta.descreverAmbiente();
                setAmbiente(floresta);
                ui.setAmbiente(floresta);
                break;

            case "lago":
                Ambiente lago = new AmbienteLago(this, jogador, ui);
                lago.descreverAmbiente();
                setAmbiente(lago);
                ui.setAmbiente(lago);
                break;

            case "montanha":
                Ambiente montanha = new AmbienteMontanha(this, jogador, ui);
                montanha.descreverAmbiente();
                setAmbiente(montanha);
                ui.setAmbiente(montanha);
                break;

            default:
        }
    }


    // Getters e setters

    // G/S dos game states principais
    public int getGameState() { return gameState; }
    public void setGameState(int gameState) {
        this.gameState = gameState;

        botoes.setVisible(false);
        botoes.esconderBotaoInicio();
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
        }

        // Fight state
        if (getEvento().isEventoCriaturaAtivo()) {
            botoes.esconderBotaoMochila();
            botoes.mostrarBotaoContinuar();

            if (fightState) {
                botoes.esconderBotaoContinuar();
                botoes.mostrarBotaoMochila();
            }
        }
    }

    public int getPlaySubState() { return playSubState; }
    public void setPlaySubState(int novoSubState) { this.playSubState = novoSubState; }
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
    public Evento.EventoCriatura getEvento() { return eventoCriatura; }
    public Inventario getInvent() { return invent; }
    public Botões getBotoes() { return botoes; }
    public Ambiente getAmbiente() { return ambiente; }
    public void setAmbiente(Ambiente ambiente) { this.ambiente = ambiente; }

    // G/S das dimensões da tela
    public int getTileSize() { return tileSize; }
    public int getLargura() { return larguraTela; }
    public int getAltura() { return alturaTela; }
}
