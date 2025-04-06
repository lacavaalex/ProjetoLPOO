import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener {

    Painel painel;
    Jogador jogador = new Jogador();

    public Teclado(Painel painel) {
        this.painel = painel;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        int gameState = painel.getGameState();
        int titleState = painel.getTitleState();
        int openingState = painel.getOpeningState();
        int playState = painel.getPlayState();
        int subState = painel.getPlaySubState();

        UI ui = painel.getUi();
        int telaInicialState = ui.getTelaInicialState();
        int numComando = ui.getNumComando();

        // Tela inicial
        if (gameState == titleState) {

            if (telaInicialState == 0) {

                if (code == KeyEvent.VK_W) {
                    numComando--;
                    if (numComando < 0) {
                        numComando = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    numComando++;
                    if (numComando > 2) {
                        numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (numComando == 0) {
                        telaInicialState = 1;
                    }
                    if (numComando == 1) {

                    }
                    if (numComando == 2) {
                        System.exit(0);
                    }
                }

            } else if (telaInicialState == 1) {

                if (code == KeyEvent.VK_W) {
                    numComando--;
                    if (numComando < 0) {
                        numComando = 4;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    numComando++;
                    if (numComando > 4) {
                        numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (numComando == 0) {
                        jogador.setNome("Colérica, a Guerreira");
                        System.out.println(jogador.getNome());
                        painel.setGameState(openingState);
                    }
                    if (numComando == 1) {
                        System.out.println("O Sobrevivente");
                        painel.setGameState(openingState);
                    }
                    if (numComando == 2) {
                        System.out.println("O Médico");
                        painel.setGameState(openingState);
                    }
                    if (numComando == 3) {
                        System.out.println("A Fora da Lei");
                        painel.setGameState(openingState);
                    }
                    if (numComando == 4) {
                        telaInicialState = 0;
                    }
                }
            }
        }

        // Jogar a atualização dos valores dessas duas variáveis à sua classe natal
        ui.setNumComando(numComando);
        ui.setTelaInicialState(telaInicialState);
        // Atualização gráfica
        painel.repaint();

        // Play state
        if (gameState == playState) {
            if (subState == 0) {
                if (code == KeyEvent.VK_W) {
                    painel.getUi().numComando--;
                    if (painel.getUi().numComando < 0) {
                        painel.getUi().numComando = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    painel.getUi().numComando++;
                    if (painel.getUi().numComando > 2) {
                        painel.getUi().numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    int opcao = painel.getUi().numComando;
                    painel.setPlaySubState(opcao + 1);
                    painel.getUi().numComando = 0;
                }
            }
        }

    }

    public void keyReleased(KeyEvent e) {

    }
}