package Controles;

import Entidade.Jogador;
import Main.Painel;
import UI.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener {

    private Painel painel;
    private InventarioUI invent;
    private Jogador jogador;
    private UI ui;

    public Teclado(Painel painel, InventarioUI invent) {
        this.painel = painel;
        this.invent = invent;
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


        // Tela inicial
        if (gameState == titleState) {

            if (telaInicialState == 0) {

                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    ui.subtrairNumComando(3);
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    ui.adicionarNumComando(3);
                }
                if (code == KeyEvent.VK_ENTER) {
                    // Novo jogo
                    if (ui.getNumComando() == 0) {
                        telaInicialState = 1;
                    }
                    // Controles
                    if (ui.getNumComando() == 1) {
                        painel.setGameState(painel.getTutorialControles());
                    }
                    // Sair
                    if (ui.getNumComando() == 2) {
                        System.exit(0);
                    }
                }

            } else if (telaInicialState == 1) {

                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    ui.subtrairNumComando(5);
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    ui.adicionarNumComando(5);
                }

                if (code == KeyEvent.VK_ENTER) {
                    if (ui.getNumComando() == 0) {
                        painel.getJogador().setNome("Coleen, a guerreira");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (ui.getNumComando() == 1) {
                        painel.getJogador().setNome("Ben, o sobrevivente");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (ui.getNumComando() == 2) {
                        painel.getJogador().setNome("Dr. Murphy, o médico");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (ui.getNumComando() == 3) {
                        painel.getJogador().setNome("Alice, a caçadora");
                        painel.setGameState(openingState);
                        telaInicialState = 0;
                    }
                    if (ui.getNumComando() == 4) {
                        telaInicialState = 0;
                        ui.setNumComando(0);
                    }
                    if (painel.getJogador().getNome() != null) {
                        ui.setNumComando(0);
                    }
                }
            }
        }

        // Joga a atualização dos valores dessas duas variáveis à sua classe natal
        ui.setTelaInicialState(telaInicialState);

        // Atualização gráfica
        painel.repaint();

        // Play state
        if (gameState == playState) {

            // Configuração própria da tela de inventário
            if (!invent.isFechado()) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    invent.subtrairNumComandoInvent();
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    invent.adicionarNumComandoInvent();
                }
                if (code == KeyEvent.VK_ENTER) {
                    invent.selecionouItem();
                }
                if (code == KeyEvent.VK_ESCAPE) {
                    invent.fechar();
                }
            }
            else {
                    if (!painel.getFightState()) {
                        // States com 3 opcoes
                        if (subState == 0) {
                            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                                painel.getUi().subtrairNumComando(3);
                            }
                            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                                painel.getUi().adicionarNumComando(3);
                            }
                            if (code == KeyEvent.VK_ENTER) {
                                int opcao = painel.getUi().getNumComando();
                                painel.setPlaySubState((opcao + 1) * 100);
                                painel.getUi().setNumComando(0);
                            }
                        }

                        // States com 2 opcoes
                        if (subState == 101 || subState == 202 || subState == 301
                                || subState == 304) {
                            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                                painel.getUi().subtrairNumComando(2);
                            }
                            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                                painel.getUi().adicionarNumComando(2);
                            }
                            if (code == KeyEvent.VK_ENTER) {
                                if (subState == 304) {
                                    painel.setPlaySubState(305);
                                } else {
                                    int opcao = painel.getUi().getNumComando();
                                    painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
                                    painel.getUi().setNumComando(0);
                                }
                            }
                        }

                        // States com 1 opcao
                        if (subState == 104) {
                            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                                painel.getUi().subtrairNumComando(1);
                            }
                            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                                painel.getUi().adicionarNumComando(1);
                            }
                            if (code == KeyEvent.VK_ENTER) {
                                if (ui.getNumComando() == 0) {
                                    painel.setGameState(painel.getLagoCardState());
                                }
                            }
                        }
                    }

                    // Configuração própria da tela de combate
                    if (painel.getFightState()) {
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            painel.getCombate().subtrairNumComandoCombate(2);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            painel.getCombate().adicionarNumComandoCombate(2);
                        }
                        if (code == KeyEvent.VK_ENTER) {
                            painel.getCombate().sistemaTurno();
                        }
                    }
            }
        }
    }

    // Métodos obrigatórios da classe KeyEvent, porém sem uso para este projeto
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}