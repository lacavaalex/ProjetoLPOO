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

            if (telaInicialState == 1) {

                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    ui.subtrairNumComando(3);
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    ui.adicionarNumComando(3);
                }
                if (code == KeyEvent.VK_ENTER) {
                    // Novo jogo
                    if (ui.getNumComando() == 0) {
                        telaInicialState = 2;
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

            } else if (telaInicialState == 2) {

                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    ui.subtrairNumComando(5);
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    ui.adicionarNumComando(5);
                }

                if (code == KeyEvent.VK_ENTER) {
                    switch (ui.getNumComando()) {
                        case 0:
                            painel.getJogador().setNome("Coleen, a guerreira");
                            painel.setGameState(openingState);
                            telaInicialState = 0;
                            break;
                        case 1:
                            painel.getJogador().setNome("Ben, o sobrevivente");
                            painel.setGameState(openingState);
                            telaInicialState = 0;
                            break;
                        case 2:
                            painel.getJogador().setNome("Dr. Murphy, o médico");
                            painel.setGameState(openingState);
                            telaInicialState = 0;
                            break;
                        case 3:
                            painel.getJogador().setNome("Alice, a caçadora");
                            painel.setGameState(openingState);
                            telaInicialState = 0;
                            break;
                        case 4:
                            telaInicialState = 0;
                            ui.setNumComando(0);
                            break;
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

            // Configuração das telas de situação climática e card de ambiente
            else if (painel.getClima().isAnalisandoClima()) {
                if (code == KeyEvent.VK_ESCAPE) {
                    painel.getClima().sair();
                }
            }
            else if (painel.getAmbienteAtual().isCardVisivel()) {
                if (code == KeyEvent.VK_ESCAPE) {
                    painel.getAmbienteAtual().sair();
                }
            }

            else {
                if (!painel.getFightState()) {
                    // States com 3 opcoes
                    if (subState == 0 || subState == 202 || subState == 403 || subState == 413 || subState == 416) {
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            painel.getUi().subtrairNumComando(3);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            painel.getUi().adicionarNumComando(3);
                        }

                        if (code == KeyEvent.VK_ENTER) {
                            int opcao = painel.getUi().getNumComando();

                            if (subState == 0) {
                                painel.setPlaySubState((opcao + 1) * 100);
                                painel.getUi().setNumComando(0);
                            }

                            else if (subState == 413) {
                                if (opcao == 0) { painel.setPlaySubState(414); }
                                else if (opcao == 1) { painel.setPlaySubState(411); }
                                else if (opcao == 2) {
                                    if (painel.getInvent().acharItem("Vara de pesca")) {
                                        painel.setPlaySubState(416);
                                    }
                                    else {
                                        painel.setPlaySubState(415);
                                    }
                                }
                            }

                            // Pescaria
                            else if (subState == 416) {
                                if (opcao == 0 || opcao == 1) {
                                    if (painel.getInvent().acharItem("Tridente") || painel.getInvent().acharItem("Cimitarra")) {
                                        painel.setPlaySubState(419);
                                    }
                                    else {
                                        painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
                                    }
                                }

                                else if (opcao == 2) {
                                    painel.getAmbienteAtual().setSubStateParaRetornar(413);
                                    painel.setPlaySubState(2001);
                                }
                            }

                            else {
                                painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
                            }
                            painel.getUi().setNumComando(0);
                        }
                    }


                    // States com 2 opcoes
                    if (subState == 1 || subState == 101 || subState == 211||
                            subState == 301 || subState == 400 || subState == 411) {
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            painel.getUi().subtrairNumComando(2);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            painel.getUi().adicionarNumComando(2);
                        }
                        if (code == KeyEvent.VK_ENTER) {
                            int opcao = painel.getUi().getNumComando();

                            // State da Base/Acampamento
                            if (subState == 1) {
                                if (opcao == 0) {
                                    if (jogador.getEnergia() <= jogador.getEnergiaMax()/2) {
                                        jogador.setEnergia(jogador.getEnergiaMax());
                                    }
                                }
                                else if (opcao == 1) {
                                    painel.getUi().setNumComando(0);
                                    int stateRetornar = painel.getAmbienteAtual().getSubStateParaRetornar();
                                    if (stateRetornar == 211 || stateRetornar == 403 ) {
                                        painel.setPlaySubState(painel.getAmbienteAtual().getSubStateParaRetornar());
                                    }
                                    else {
                                        painel.getAmbienteAtual().voltarStateAnterior();
                                    }
                                    painel.getBotoes().mostrarBotaoBase();
                                }
                            }

                            else if (subState == 211) {
                                if (opcao == 0) { painel.setPlaySubState(203); }
                                else if (opcao == 1) { painel.setPlaySubState(204); }
                            }

                            else if (subState == 400) {
                                if (opcao == 0) { painel.setPlaySubState(401); }
                                else if (opcao == 1) {
                                    painel.trocarAmbiente("floresta", 104);
                                }
                            }

                            else if (subState == 411) {
                                if (opcao == 0) { painel.setPlaySubState(2001); }
                                else if (opcao == 1) { painel.setPlaySubState(406);}
                            }

                            else {
                                painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
                                painel.getUi().setNumComando(0);
                            }
                        }
                    }

                    // States com 1 opcao
                    if (subState == 104 || subState == 419) {
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            painel.getUi().subtrairNumComando(1);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            painel.getUi().adicionarNumComando(1);
                        }
                        if (code == KeyEvent.VK_ENTER) {
                            if (ui.getNumComando() == 0) {
                                if (subState == 104) {
                                    painel.trocarAmbiente("lago", 400);
                                }
                                else if (subState == 419) {
                                    painel.getAmbienteAtual().setTransicaoIniciada(true);
                                }
                            }
                        }
                    }
                }

                // Configuração própria da tela de combate
                if (painel.getFightState()) {
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                        painel.getCombate().subtrairNumComandoCombate(3);
                    }
                    if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                        painel.getCombate().adicionarNumComandoCombate(3);
                    }
                    if (code == KeyEvent.VK_ENTER) {
                        int opcao = painel.getCombate().getNumComando();
                        if (opcao == 0) {
                            painel.getCombate().sistemaTurno();
                        }
                        else if (opcao == 1) {

                        }
                        else if (opcao == 2) {
                            painel.getUi().mostrarInventario();
                        }
                    }

                    if (painel.getCombate().isCombateFinalizado()) {
                        if (code == KeyEvent.VK_ESCAPE) {
                            painel.getCombate().finalizarCombate();
                            painel.setPlaySubState(painel.getAmbienteAtual().getSubStateParaRetornar());
                        }
                    }
                }
            }
        }
    }

    // Métodos obrigatórios da classe KeyEvent, porém sem uso para este projeto
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}