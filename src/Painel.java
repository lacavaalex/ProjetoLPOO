import javax.swing.*;
import java.awt.*;

public class Painel extends JPanel implements Runnable {

// Construtor de classes externas
    Thread gameThread;

// Definição de FPS
    private final int fps = 60;

// Chamada de classe
    private Jogador jogador = new Jogador();
    private UI ui = new UI(this);
    private Teclado teclado = new Teclado(this);
    private Botões botoes = new Botões();

// Definição da tela
    private final int originalTileSize = 16;
    private final int escala = 3; // tornar 48x48
    private int tileSize = originalTileSize * escala;

    private int larguraTela = 900;
    private int alturaTela = 700;

// Game State
    private int gameState;
    private final int titleState = 0;
    private final int playState = 1;

    public Painel(){
     // Estabelecimento dos dados da tela
        this.setPreferredSize(new Dimension(larguraTela, alturaTela));
        this.setBackground(Color.black);
        botoes.configurarComPainel(this);
        this.add(botoes);
        botoes.setVisible(true);
        botoes.mostrarBotao();

    // Dica recebida: Buffering mais preciso
        this.setDoubleBuffered(true);

    // Traz os controles e "foca" a classe em receber o input
        this.addKeyListener(teclado);
        this.setFocusable(true);
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
        if (gameState == playState) {
            botoes.setVisible(true);
        } else {
            botoes.setVisible(false);
        }
    }

// Visualização (paint component)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Tela inicial
        if (gameState == titleState) {
            ui.mostrar(g2);
            g2.dispose();
        }
        if (gameState == playState) {
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

        if (gameState == playState) {
            botoes.setVisible(true);
        } else {
            botoes.setVisible(false);
        }
    }
    public int getTitleState() {
        return titleState;
    }
    public int getPlayState() {
        return playState;
    }


    public UI getUi() {
        return ui;
    }
    public void setUi(UI ui) {
        this.ui = ui;
    }
}
