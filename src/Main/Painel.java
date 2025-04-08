package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;

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

// Chamada de classe
    private Jogador jogador = new Jogador();
    private UI ui = new UI(this, jogador);
    private Teclado teclado = new Teclado(this);
    private Botões botoes = new Botões(this);
    private Ambiente ambiente = new Ambiente();


// Game State
    private int gameState;
    private final int titleState = 0;
    private final int tutorialControles = 1000;
    private final int openingState = 1;
    private final int playState = 3;
    private final int gameOverState = 4;
    private int playSubState = 0;
    private final int florestaCardState = 101;
    private final int lagoCardState = 102;
    private final int montanhaCardState = 103;

    public Painel(){

    // Estabelecimento dos dados da tela
        this.setPreferredSize(new Dimension(larguraTela, alturaTela));
        this.setBackground(Color.black);
        this.setLayout(null);

        botoes.configurarComPainel(this);
        botoes.setVisible(true);
        this.add(botoes);
        botoes.mostrarBotaoContinuar();

    // Dica recebida: Buffering mais preciso
        this.setDoubleBuffered(true);

    // Traz os controles e "foca" a classe em receber o input
        this.addKeyListener(teclado);
        this.setFocusable(true);

    // Adição do ambiente
        ambiente.setBounds(0, 0, larguraTela, alturaTela);
        ambiente.setVisible(false);
        this.add(ambiente);



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
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

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

        if (gameState == titleState || gameState == openingState || gameState == playState ||
                gameState == gameOverState || gameState == tutorialControles ||
                gameState == florestaCardState || gameState == lagoCardState || gameState == montanhaCardState) {
            ui.mostrar(g2);
        }
    }


// Getters e setters
    public int getTileSize() {
        return tileSize;
    }
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
    public int getLargura() {
        return larguraTela;
    }
    public void setLargura(int largura) {
        this.larguraTela = largura;
    }
    public int getAltura() {
        return alturaTela;
    }
    public void setAltura(int altura) {
        this.alturaTela = altura;
    }


    public int getGameState() {
        return gameState;
    }
    public void setGameState(int gameState) {
        this.gameState = gameState;
        botoes.setVisible(false);
        botoes.esconderBotaoInicio();

        if (gameState == openingState) {
            botoes.setVisible(true);
            botoes.mostrarBotaoContinuar();
            botoes.esconderBotaoVoltar();
        }
        if (gameState == tutorialControles) {
            botoes.setVisible(true);
            botoes.mostrarBotaoVoltar();
            botoes.esconderBotaoContinuar();
        }
        if (gameState == gameOverState) {
            botoes.setVisible(true);
            botoes.mostrarBotaoInicio();
            botoes.esconderBotaoContinuar();
            botoes.esconderBotaoVoltar();
        }

        // Cards de ambiente
        if (gameState == florestaCardState) {
            botoes.setVisible(true);
            ambiente.ambienteFloresta();
            botoes.mostrarBotaoContinuar();
        }
        if (gameState == lagoCardState) {
            botoes.setVisible(true);
            ambiente.ambienteLago();
            botoes.mostrarBotaoContinuar();
        }
        if (gameState == montanhaCardState) {
            botoes.setVisible(true);
            ambiente.ambienteMontanha();
            botoes.mostrarBotaoContinuar();
        }

        if (gameState == playState) {
            botoes.setVisible(true);
            botoes.esconderBotaoContinuar();
        }
    }


    public int getTitleState() {
        return titleState;
    }
    public int getTutorialControles() {
        return tutorialControles;
    }
    public int getOpeningState() {
        return openingState;
    }
    public int getGameOverState() {
        return gameOverState;
    }
    public int getPlayState() {
        return playState;
    }
    public int getPlaySubState() {
        return playSubState;
    }
    public void setPlaySubState(int novoSubState) {
        this.playSubState = novoSubState;

        if (novoSubState > 0 && novoSubState < 10 || novoSubState == 12 || novoSubState == 31
        || novoSubState == 32 || novoSubState == 34) {
            botoes.mostrarBotaoContinuar();
        } else {
            botoes.esconderBotaoContinuar();
        }
        if (novoSubState == 20) {
            botoes.esconderBotaoContinuar();
        }

        if (novoSubState == 11 || novoSubState == 22 || novoSubState == 1212 || novoSubState == 3131) {
            botoes.mostrarBotaoVoltar();
        }
        if (novoSubState == 10 || novoSubState == 20 || novoSubState == 32 || novoSubState == 1213) {
            botoes.esconderBotaoVoltar();
        }
    }
    public void resetPlayState() {
        setPlaySubState(0);
    }


    public UI getUi() {
        return ui;
    }
    public Jogador getJogador() {
        return jogador;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public int getFlorestaCardState() {
        return florestaCardState;
    }
    public int getLagoCardState() {
        return lagoCardState;
    }
    public int getMontanhaCardState() {
        return montanhaCardState;
    }
}
