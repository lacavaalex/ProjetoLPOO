import javax.swing.*;
import java.awt.*;

public class Painel extends JPanel implements Runnable {

// Construtor de classes externas
    Thread gameThread;
    JPanel jPanel = new JPanel();

// Chamada de classe
    UI ui = new UI(this);

// Definição da tela
    private final int originalTileSize = 16;
    private final int escala = 3; // tornar 48x48
    private int tileSize = originalTileSize * escala;

    private int larguraTela = 900;
    private int alturaTela = 700;

// Game State
    private int gameState;
    private final int titleState = 0;

    public Painel(){
     //Estabelecimento dos dados da tela
        jPanel.setBounds(0, 0, getLargura(), getAltura());
        this.setPreferredSize(new Dimension(larguraTela, alturaTela));
        this.setBackground(Color.black);
    }

// Inicialização do jogo e aplicação da Thread
    public void setupJogo() {
        gameState = titleState;
    }

    public void iniciarGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void run() {
    }

// Visualização (paint component)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Tela inicial
        if (gameState == titleState) {
            ui.mostrar(g2);
        }

        g2.dispose();
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
    }
    public int getTitleState() {
        return titleState;
    }


    public UI getUi() {
        return ui;
    }
    public void setUi(UI ui) {
        this.ui = ui;
    }
}
