import javax.swing.*;
import java.awt.*;

public class Painel extends JPanel implements Runnable {

// Construtor de classes externas
    Thread gameThread;
    JPanel jPanel = new JPanel();

// Chamada de classe
    private UI ui = new UI(this);

// Definição da tela
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


// Getters e setters
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


    public UI getUi() {
        return ui;
    }
    public void setUi(UI ui) {
        this.ui = ui;
    }
}
