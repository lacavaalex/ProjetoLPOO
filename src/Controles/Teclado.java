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
        char caracter = e.getKeyChar();

        int gameState = painel.getGameState();
        int titleState = painel.getTitleState();
        int openingState = painel.getOpeningState();
        int playState = painel.getPlayState();
        int subState = painel.getPlaySubState();

        UI ui = painel.getUi();

        // Tela inicial
        if (gameState == titleState) {

            if (ui.getTelaInicialState() == 1) {

                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    ui.subtrairNumComando(3);
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    ui.adicionarNumComando(3);
                }
                if (code == KeyEvent.VK_ENTER) {
                    // Novo jogo
                    if (ui.getNumComando() == 0) {
                        ui.setDigitandoNome(true);
                        ui.setDigitacaoConfirmada(false);
                        ui.getNomeDigitado().setLength(0);
                        ui.setTelaInicialState(2);
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

            } else if (ui.getTelaInicialState() == 2) {

                // Nome do jogador
                if (ui.isDigitandoNome() && !ui.isDigitacaoConfirmada()) {

                    if (Character.isLetterOrDigit(caracter) || caracter == ' ') {
                        if (ui.getNomeDigitado().length() < 15) {
                            ui.getNomeDigitado().append(caracter);
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !ui.getNomeDigitado().isEmpty()) {
                        ui.getNomeDigitado().deleteCharAt(ui.getNomeDigitado().length() - 1);
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (ui.getNomeDigitado().length() >= 3) {
                            ui.setDigitacaoConfirmada(true);
                            ui.setDigitandoNome(false);

                            String nomeFinal = ui.getNomeDigitado().toString().strip();
                            jogador.setNome(nomeFinal);

                            ui.setNumComando(0);
                            ui.setTelaInicialState(3);
                        }
                    }
                }

            } else if (ui.getTelaInicialState() == 3) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    ui.subtrairNumComando(5);
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    ui.adicionarNumComando(5);
                }

                if (code == KeyEvent.VK_ENTER) {
                    if (ui.getNumComando() != 4) {
                        String[] habilidades = {"COMBATIVA", "SOBREVIVENCIAL", "MEDICINAL", "CAÇADORA"};
                        String habilidadeEscolhida = habilidades[ui.getNumComando()];
                        jogador.setHabilidade(habilidadeEscolhida);

                        painel.setGameState(openingState);
                        ui.setTelaInicialState(0);
                        ui.setNumComando(0);
                    } else {
                        ui.setNumComando(0);
                        ui.setTelaInicialState(1);
                    }
                }
            }
        }

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
                    if (subState == 0 || subState == 202 ||
                            subState == 403 || subState == 411 ||subState == 413 || subState == 416) {
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

                            else if (subState == 411) {
                                if (opcao == 0) { painel.setPlaySubState(2001); }
                                else if (opcao == 1) { painel.setPlaySubState(406); }
                                else if (opcao == 2) { painel.trocarAmbiente("floresta", 104); };
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
                                    if (!painel.getInvent().acharItem("Jóia azul")
                                            && (painel.getInvent().acharItem("Tridente") || painel.getInvent().acharItem("Cimitarra"))) {
                                        painel.setPlaySubState(419);
                                    } else {
                                        painel.setPlaySubState(416 + (opcao + 1));
                                    }
                                }

                                else if (opcao == 2) {
                                    if (!painel.getInvent().acharItem("Jóia azul")) {
                                        painel.getAmbienteAtual().setSubStateParaRetornar(413);
                                    }
                                    else {
                                        painel.getAmbienteAtual().setSubStateParaRetornar(425);
                                    }
                                    painel.setPlaySubState(2001);
                                }
                            }

                            else {
                                painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
                            }
                            painel.getUi().setNumComando(0);
                        }
                    }

                    // Acampamento/base
                    if (subState == 1) {
                        boolean colheitaPronta = painel.getAmbienteAtual().isColheitaPronta();
                        boolean fogoAceso = painel.getAmbienteAtual().isBaseFogoAceso();

                        int numOpcoes = 1;
                        if ((fogoAceso && !colheitaPronta) || (colheitaPronta && !fogoAceso)) { numOpcoes = 2; }
                        if (colheitaPronta && fogoAceso) { numOpcoes = 3; }

                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            painel.getUi().subtrairNumComando(numOpcoes);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            painel.getUi().adicionarNumComando(numOpcoes);
                        }
                        if (code == KeyEvent.VK_ENTER) {
                            int opcao = painel.getUi().getNumComando();

                            if (opcao == 0) {
                                painel.getUi().setNumComando(0);
                                int stateRetornar = painel.getAmbienteAtual().getSubStateParaRetornar();
                                if (stateRetornar == 211 || stateRetornar == 403) {
                                    painel.setPlaySubState(painel.getAmbienteAtual().getSubStateParaRetornar());
                                } else {
                                    painel.getAmbienteAtual().voltarStateAnterior();
                                }
                                painel.getBotoes().mostrarBotao("Voltar à base");
                            } else if (opcao == 1) {
                                if (fogoAceso) {
                                    if (jogador.getEnergia() <= jogador.getEnergiaMax() / 2) {
                                        jogador.setEnergia(jogador.getEnergiaMax());
                                    }
                                } else {
                                    jogador.setFome(jogador.getFome() + 5);
                                    painel.getAmbienteAtual().setColheitaPronta(false);
                                }
                            } else if (opcao == 2) {
                                jogador.setFome(jogador.getFome() + 5);
                                painel.getAmbienteAtual().setColheitaPronta(false);
                            }

                        }
                    }


                    // States com 2 opcoes
                    if (subState == 101 || subState == 211||
                            subState == 301 || subState == 400 || subState == 425) {
                        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                            painel.getUi().subtrairNumComando(2);
                        }
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            painel.getUi().adicionarNumComando(2);
                        }
                        if (code == KeyEvent.VK_ENTER) {
                            int opcao = painel.getUi().getNumComando();
                            if (subState == 211) {
                                if (opcao == 0) { painel.setPlaySubState(203); }
                                else if (opcao == 1) { painel.setPlaySubState(204); }
                            }

                            else if (subState == 400) {
                                if (opcao == 0) {
                                    if (!painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
                                        painel.setPlaySubState(401);
                                    } else {
                                        painel.setPlaySubState(403);
                                    }
                                }
                                else if (opcao == 1) {
                                    painel.trocarAmbiente("floresta", 104);
                                }
                            }

                            else if (subState == 425) {
                                if (opcao == 0) { painel.setPlaySubState(416); }
                                else if (opcao == 1) {  painel.trocarAmbiente("floresta", 104);}
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
                                    if (painel.getInvent().acharItem("Jóia azul")) {
                                        painel.trocarAmbiente("lago", 425);
                                    } else {
                                        painel.trocarAmbiente("lago", 400);
                                    }
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

                    if (painel.getCombate().isTurnoJogador()) {

                        if (painel.getCombate().getCriaturaEmCombate() != null
                                && painel.getCombate().getCriaturaEmCombate().isBoss()) {
                            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                                painel.getCombate().subtrairNumComandoCombate(4);
                            }
                            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                                painel.getCombate().adicionarNumComandoCombate(4);
                            }
                        } else {
                            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                                painel.getCombate().subtrairNumComandoCombate(5);
                            }
                            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                                painel.getCombate().adicionarNumComandoCombate(5);
                            }
                        }

                        if (code == KeyEvent.VK_ENTER) {
                            int opcao = painel.getCombate().getNumComando();
                            if (opcao == 0 || opcao == 1 || opcao == 2 || opcao == 4) {
                                painel.getCombate().sistemaTurno();
                            } else if (opcao == 3) {
                                painel.getUi().mostrarInventario();
                            }
                        }
                    }
                    else {
                        painel.getCombate().setNumComando(0);
                        if (code == KeyEvent.VK_ENTER) {
                            painel.getCombate().setResistiu(false);
                            painel.getCombate().sistemaTurno();
                        }
                    }

                    if (painel.getCombate().isCombateFinalizado() || painel.getCombate().conseguiuEscapar()) {
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