package Controles;

import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener {

    Painel painel;
    Jogador jogador;
    UI ui;

    public Teclado(Painel painel) {
        this.painel = painel;
        this.jogador = painel.getJogador();
        this.ui = painel.getUi();
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

                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    numComando--;
                    if (numComando < 0) {
                        numComando = 2;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    numComando++;
                    if (numComando > 2) {
                        numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    // Novo jogo
                    if (numComando == 0) {
                        telaInicialState = 1;
                    }
                    // Controles
                    if (numComando == 1) {
                        painel.setGameState(painel.getTutorialControles());
                    }
                    // Sair
                    if (numComando == 2) {
                        System.exit(0);
                    }
                }

            } else if (telaInicialState == 1) {

                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    numComando--;
                    if (numComando < 0) {
                        numComando = 4;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    numComando++;
                    if (numComando > 4) {
                        numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (numComando == 0) {
                        painel.getJogador().setNome("Coleen, a guerreira");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (numComando == 1) {
                        painel.getJogador().setNome("Ben, o sobrevivente");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (numComando == 2) {
                        painel.getJogador().setNome("Dr. Murphy, o médico");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (numComando == 3) {
                        painel.getJogador().setNome("Alice, a caçadora");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (numComando == 4) {
                        telaInicialState = 0;
                        numComando = 0;
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

            // States com 3 opcoes
            if (subState == 0) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    painel.getUi().numComando--;
                    if (painel.getUi().numComando < 0) {
                        painel.getUi().numComando = 2;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    painel.getUi().numComando++;
                    if (painel.getUi().numComando > 2) {
                        painel.getUi().numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    int opcao = painel.getUi().numComando;
                    painel.setPlaySubState((opcao + 1)*100);
                    painel.getUi().numComando = 0;
                }
            }

            // States com 2 opcoes
            if (subState == 101 || subState == 202 || subState == 301
                    || subState == 304) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    painel.getUi().numComando--;
                    if (painel.getUi().numComando < 0) {
                        painel.getUi().numComando = 1;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    painel.getUi().numComando++;
                    if (painel.getUi().numComando > 1) {
                        painel.getUi().numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (subState == 304){
                        painel.setPlaySubState(305);
                    }
                    else {
                        int opcao = painel.getUi().numComando;
                        painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
                        painel.getUi().numComando = 0;
                    }
                }
            }

            // States com 1 opcao
            if (subState == 104) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    painel.getUi().numComando--;
                    if (painel.getUi().numComando < 0) {
                        painel.getUi().numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    painel.getUi().numComando++;
                    if (painel.getUi().numComando > 0) {
                        painel.getUi().numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (numComando == 0) {
                        painel.setGameState(painel.getLagoCardState());
                    }
                }
            }

            // Configuração própria da tela de combate
            if (painel.getFightState()) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    painel.getCombate().numComando--;
                    if (painel.getCombate().numComando < 0) {
                        painel.getCombate().numComando = 1;
                    }
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    painel.getCombate().numComando++;
                    if (painel.getCombate().numComando > 1) {
                        painel.getCombate().numComando = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    painel.getCombate().sistemaTurno();
                }
            }
        }

    }

    // Métodos obrigatórios da classe KeyEvent, porém sem uso para este projeto
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}