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
    private final int escala = 3; // tornar 48x48
    private int tileSize = originalTileSize * escala;

    private int larguraTela = 900;
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
    private final int openingState = 1;
    private final int florestaCardState = 2;
    private final int playState = 3;
    private int playSubState = 0;

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

        if (gameState == titleState || gameState == openingState || gameState == florestaCardState || gameState == playState) {
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

        if (gameState == openingState) {
            botoes.setVisible(true);

        } else {
            botoes.setVisible(false);
        }

        if (gameState == florestaCardState) {
            botoes.setVisible(true);
            ambiente.ambienteFloresta();
        }

        if (gameState == playState) {
            botoes.setVisible(true);
            botoes.esconderBotaoContinuar();
        }
    }
    public int getTitleState() {
        return titleState;
    }
    public int getOpeningState() {
        return openingState;
    }
    public int getPlayState() {
        return playState;
    }
    public int getPlaySubState() {
        return playSubState;
    }
    public void setPlaySubState(int novoSubState) {
        this.playSubState = novoSubState;

        if (novoSubState > 0 && novoSubState < 10) {
            botoes.mostrarBotaoContinuar();
        } else {
            botoes.esconderBotaoContinuar();
        }
        if (novoSubState == 20) {
            botoes.esconderBotaoContinuar();
        }
        if (novoSubState == 22) {
            botoes.mostrarBotaoVoltar();
        }
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
}
