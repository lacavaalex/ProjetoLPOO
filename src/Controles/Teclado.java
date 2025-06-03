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

    private long ultimaInteracao = 0;
    private final long cooldown = 70;

    public Teclado(Painel painel, InventarioUI invent) {
        this.painel = painel;
        this.invent = invent;
        this.jogador = painel.getJogador();
        this.ui = painel.getUi();
    }

    public void keyPressed(KeyEvent e) {

        long agora = System.currentTimeMillis();

        if (agora - ultimaInteracao < cooldown) {
            return;
        }

        ultimaInteracao = agora;

        int subState = painel.getPlaySubState();

        // Tela inicial
        if (painel.getGameState() == painel.getTitleState()) {
            kPTelaInicial(e);
        }

        // Atualização gráfica
        painel.repaint();

        // Play state
        if (painel.getGameState() == painel.getPlayState()) {

            if (!invent.isFechado() || painel.getClima().isAnalisandoClima()
            || painel.getAmbienteAtual().isCardVisivel()) {
                kPTelasPopUp(e);
            }
            else {
                if (!painel.getFightState()) {

                    // States com 3 opcoes
                    if (subState == 0 || subState == 2 || subState == 5 || subState == 6
                            || subState == 15  || subState == 38 || subState == 41 || subState == 44
                            || subState == 102 || subState == 109 || subState == 111
                            || subState == 114 || subState == 201 ) {
                        kPTresOpcoesPlayState(e);
                    }

                    // Acampamento/base
                    if (subState == 1) {
                        kPAcampamento(e);
                    }

                    // States com 2 opcoes
                    if (subState == 9 || subState == 12 || subState == 21
                            || subState == 24 || subState == 25 || subState == 34
                            || subState == 100 || subState == 123
                            || subState == 202 || subState == 203 || subState == 209
                            || subState == 213 || subState == 217 || subState == 302
                            || subState == 304 || subState == 318) {
                        kPDuasOpcoesPlayState(e);
                    }

                    // States com 1 opcao
                    if (subState == 28 || subState == 46|| subState == 117
                            || subState == 220 || subState == 322) {
                        kPUmaOpcaoPlayState(e);
                    }
                }

                // Configuração própria da tela de combate
                if (painel.getFightState()) {
                    kPCombate(e);
                }
            }

        }
    }

    public void kPTelaInicial(KeyEvent e) {
        int code = e.getKeyCode();
        char character = e.getKeyChar();

        // Jogar/Controles/Sair
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

        }
        // Nome
        else if (ui.getTelaInicialState() == 2) {

            // Nome do jogador
            if (ui.isDigitandoNome() && !ui.isDigitacaoConfirmada()) {

                if (Character.isLetterOrDigit(character) || character == ' ') {
                    if (ui.getNomeDigitado().length() < 15) {
                        ui.getNomeDigitado().append(character);
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
        }
        // Habilidade
        else if (ui.getTelaInicialState() == 3) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                ui.subtrairNumComando(5);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                ui.adicionarNumComando(5);
            }

            if (code == KeyEvent.VK_ENTER) {
                if (ui.getNumComando() != 4) {
                    String[] habilidades = {"COMBATIVA", "SOBREVIVENCIAL", "MEDICINAL", "RASTREADORA"};
                    String habilidadeEscolhida = habilidades[ui.getNumComando()];
                    jogador.setHabilidade(habilidadeEscolhida);

                    painel.setGameState(painel.getOpeningState());
                    ui.setTelaInicialState(0);
                    ui.setNumComando(0);
                } else {
                    ui.setNumComando(0);
                    ui.setTelaInicialState(1);
                }
            }
        }
    }

    public void kPTelasPopUp(KeyEvent e) {
        int code = e.getKeyCode();

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
    }

    public void kPCombate(KeyEvent e) {
        int code = e.getKeyCode();

        if (painel.getCombate().isTurnoJogador()) {

            if ((painel.getCombate().getCriaturaEmCombate() != null
                    && painel.getCombate().getCriaturaEmCombate().isBoss())
                    || jogador.getLocalizacao().equals("MONTANHA EPOPEICA")) {

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

    public void kPAcampamento(KeyEvent e) {
        int code = e.getKeyCode();
        int opcao = ui.getNumComando();

        boolean colheitaPronta = painel.getAmbienteAtual().isColheitaPronta();
        boolean fogoAceso = painel.getAmbienteAtual().isBaseFogoAceso();

        int numOpcoes = 1;
        if ((fogoAceso && !colheitaPronta) || (colheitaPronta && !fogoAceso)) {
            numOpcoes = 2;
        }
        if (colheitaPronta && fogoAceso) {
            numOpcoes = 3;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            ui.subtrairNumComando(numOpcoes);
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            ui.adicionarNumComando(numOpcoes);
        }
        if (code == KeyEvent.VK_ENTER) {

            if (opcao == 0) {
                ui.setNumComando(0);
                int stateRetornar = painel.getAmbienteAtual().getSubStateParaRetornar();
                if (stateRetornar == 15 || stateRetornar == 38
                        || stateRetornar == 102 || stateRetornar == 201) {
                    painel.setPlaySubState(stateRetornar);
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

    public void kPTresOpcoesPlayState(KeyEvent e) {
        int code = e.getKeyCode();
        int opcao = ui.getNumComando();
        int subState = painel.getPlaySubState();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            ui.subtrairNumComando(3);
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            ui.adicionarNumComando(3);
        }

        if (code == KeyEvent.VK_ENTER) {

            if (subState == 0) {
                if (opcao == 0) {
                    painel.setPlaySubState(8);
                } else if (opcao == 1) {
                    painel.setPlaySubState(13);
                } else if (opcao == 2) {
                    painel.setPlaySubState(33);
                }
            }
            else if (subState == 2 || subState == 6) {
                boolean derrotouBossLago = painel.getInvent().acharItem("Jóia azul");
                boolean derrotouBossCaverna = painel.getInvent().acharItem("Jóia vermelha");
                boolean derrotouBossFloresta = painel.getInvent().acharItem("Espada Insigne");

                if (opcao == 0) {
                    if (derrotouBossLago) {
                        painel.trocarAmbiente("lago", 123);
                    } else {
                        painel.setPlaySubState(9);
                    }
                } else if (opcao == 1) {
                    if (derrotouBossCaverna) {
                        painel.getAmbienteAtual().setSubStateParaRetornar(2);
                        painel.setPlaySubState(4);
                    } else {
                        painel.setPlaySubState(13);
                    }
                } else if (opcao == 2) {
                    if (subState == 2) {
                        if (derrotouBossFloresta) {
                            painel.trocarAmbiente("montanha", 300);
                        } else {
                            painel.setPlaySubState(24);
                        }
                    }
                    else {
                        painel.setPlaySubState(41);
                    }
                }
            }
            else if (subState == 5) {
                if (opcao == 0) {
                    painel.getAmbienteAtual().setSubStateParaRetornar(5);
                    painel.setPlaySubState(4);
                } else if (opcao == 1) {
                    painel.setPlaySubState(25);
                } else if (opcao == 2) {
                    painel.setPlaySubState(24);
                }
            }
            else if (subState == 15 || subState == 38) {
                painel.getAmbienteAtual().setSubStateOrigem(subState);

                boolean podeCurar = painel.getInvent().acharItem("Planta medicinal");
                boolean podeCacar = painel.getInvent().acharItem("Machado") || painel.getInvent().acharItem("Faca");
                boolean opcaoFogueira = painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1);

                if (opcao == 0) {
                    if (subState == 15) {
                        if (podeCurar && painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
                            painel.setPlaySubState(20);
                        } else { painel.setPlaySubState(16); }
                    }
                    else {
                        if (podeCurar) { painel.setPlaySubState(3); }
                        else { painel.setPlaySubState(16); }
                    }
                }
                else if (opcao == 1) {
                    if (!podeCacar) {
                        painel.setPlaySubState(17);
                    } else {
                        painel.getAmbienteAtual().setSubStateParaRetornar(subState);
                        painel.setPlaySubState(4);
                    }
                }
                else if (opcao == 2) {
                    if (subState == 15) {
                        if (!opcaoFogueira) {
                            painel.setPlaySubState(18);
                        } else {
                            painel.setPlaySubState(3);
                        }
                    }
                    else {
                        painel.setPlaySubState(39);
                    }
                }
            }
            else if (subState == 41) {
                if (opcao == 0) { painel.setPlaySubState(42); }
                if (opcao == 1) { painel.setPlaySubState(6); }
                if (opcao == 2) {painel.setPlaySubState(7); }
            }
            else if (subState == 44) {
                if (opcao == 0) { painel.setPlaySubState(45); }
                if (opcao == 1) {
                    painel.getAmbienteAtual().setSubStateParaRetornar(44);
                    painel.setPlaySubState(4);
                }
                if (opcao == 2) { painel.setPlaySubState(46); }
            }
            else if (subState == 109) {
                if (opcao == 0) {
                    painel.setPlaySubState(2001);
                } else if (opcao == 1) {
                    painel.setPlaySubState(105);
                } else if (opcao == 2) {
                    painel.trocarAmbiente("floresta", 12);
                }
                ;
            }
            else if (subState == 111) {
                if (opcao == 0) {
                    painel.setPlaySubState(112);
                } else if (opcao == 1) {
                    painel.setPlaySubState(109);
                } else if (opcao == 2) {
                    if (painel.getInvent().acharItem("Vara de pesca")) {
                        painel.setPlaySubState(114);
                    } else {
                        painel.setPlaySubState(113);
                    }
                }
            }

            // Pescaria
            else if (subState == 114) {
                stateDePescaria();
            }

            else if (subState == 201) {
                boolean achouAgua = painel.getAmbienteAtual().checarSeSubStateFoiVisitado(205);
                boolean podeMinerar = painel.getInvent().acharItem("Picareta");
                if (opcao == 0) {
                   if (!achouAgua) { painel.setPlaySubState(202); }
                   else { painel.setPlaySubState(207); }
                }
                else if (opcao == 1) {
                    if (!podeMinerar) { painel.setPlaySubState(203); }
                    else { painel.setPlaySubState(208); }
                }
                else if (opcao == 2) { painel.setPlaySubState(209); }
            }

            else {
                painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
            }

            ui.setNumComando(0);
        }
    }

    public void kPDuasOpcoesPlayState(KeyEvent e) {
        int code = e.getKeyCode();
        int opcao = ui.getNumComando();
        int subState = painel.getPlaySubState();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            ui.subtrairNumComando(2);
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            ui.adicionarNumComando(2);
        }
        if (code == KeyEvent.VK_ENTER) {

            if (subState == 12) {
                if (opcao == 0) {
                    if (painel.getInvent().acharItem("Jóia azul")) {
                        painel.trocarAmbiente("lago", 123);
                    } else {
                        painel.trocarAmbiente("lago", 100);
                    }
                } else if (opcao == 1) {
                    painel.setPlaySubState(2);
                }
            }
            else if (subState == 25) {
                if (opcao == 0) {
                    painel.setPlaySubState(26);
                }
                else if (opcao == 1) {
                    painel.setPlaySubState(5);
                }
            }
            else if (subState == 24) {
                if (opcao == 0) {
                    painel.setPlaySubState(33);
                } else if (opcao == 1) {
                    painel.setPlaySubState(5);
                }
            }
            else if (subState == 100) {
                if (opcao == 0) {
                    if (!painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
                        painel.setPlaySubState(101);
                    } else {
                        painel.setPlaySubState(102);
                    }
                } else if (opcao == 1) {
                    painel.trocarAmbiente("floresta", 12);
                }
            }
            else if (subState == 123) {
                if (opcao == 0) {
                    painel.setPlaySubState(114);
                } else if (opcao == 1) {
                    painel.trocarAmbiente("floresta", 12);
                }
            }
            else if (subState == 202 || subState == 203) {
                if (opcao == 0 || opcao == 1) {
                   int probabilidade = painel.definirUmaProbabilidade();
                   boolean caminhoCerto = probabilidade <= 50;
                   if (caminhoCerto) {
                       painel.setPlaySubState(subState + 3);
                   }
                   else {
                       painel.setPlaySubState(204);
                   }
                }
            }
            else if (subState == 209) {
                    if (opcao == 0) { painel.setPlaySubState(210); }
                    else if (opcao == 1) { painel.setPlaySubState(201); }
            }
            else if (subState == 213) {
                if (opcao == 0 || opcao == 1) {
                    int probabilidade = painel.definirUmaProbabilidade();
                    boolean caminhoCerto = probabilidade <= 50;
                    if (caminhoCerto) {
                        painel.setPlaySubState(215);
                    }
                    else {
                        painel.setPlaySubState(214);
                    }
                }
            }
            else if (subState == 302) {
                if (painel.getInvent().acharItem("Corda de escalada")) {
                    if (opcao == 0) { painel.setPlaySubState(303); }
                    else if (opcao == 1) {
                        painel.trocarAmbiente("floresta", 2); }
                }
            }
            else if (subState == 304) {
                if (opcao == 0) { painel.setPlaySubState(305);}
                else if (opcao == 1) { painel.setPlaySubState(309); }
            }
            else {
                painel.setPlaySubState(painel.getPlaySubState() + (opcao + 1));
            }
            ui.setNumComando(0);
        }
    }

    public void kPUmaOpcaoPlayState(KeyEvent e) {
        int code = e.getKeyCode();
        int subState = painel.getPlaySubState();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            ui.subtrairNumComando(1);
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            ui.adicionarNumComando(1);
        }
        if (code == KeyEvent.VK_ENTER) {
            if (ui.getNumComando() == 0) {
                if (subState == 28 || subState == 46 || subState == 117
                        || subState == 220 || subState == 322) {
                    painel.getAmbienteAtual().setTransicaoIniciada(true);
                }
            }
        }
    }

    public void stateDePescaria() {
        int opcao = ui.getNumComando();

        if (opcao == 0 || opcao == 1) {
            if (!painel.getInvent().acharItem("Jóia azul")
                    && (painel.getInvent().acharItem("Tridente") || painel.getInvent().acharItem("Cimitarra"))) {
                painel.setPlaySubState(117);
            } else {
                painel.setPlaySubState(114 + (opcao + 1));
            }
        }

        else if (opcao == 2) {
            if (!painel.getInvent().acharItem("Jóia azul")) {
                painel.getAmbienteAtual().setSubStateParaRetornar(111);
            }
            else {
                painel.getAmbienteAtual().setSubStateParaRetornar(123);
            }
            painel.setPlaySubState(2001);
        }
    }

    // Métodos obrigatórios da classe KeyEvent, porém sem uso para este projeto
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}