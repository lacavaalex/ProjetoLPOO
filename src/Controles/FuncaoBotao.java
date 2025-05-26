package Controles;

import Main.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FuncaoBotao implements ActionListener {

    private Painel painel;
    private Botoes botoes;

    public FuncaoBotao(Painel painel, Botoes botoes) {
        this.painel = painel;
        this.botoes = botoes;
    }

    // Ação que cada botão executa
    public void actionPerformed(ActionEvent e) {

        Object fonte = e.getSource();

        // CONTINUAR
        if (fonte == botoes.getBotaoContinuar()) {
            actionPerformedContinuar();
        }

        // VOLTAR
        else if (fonte == botoes.getBotaoVoltar()) {

            botoes.esconderBotaoVoltar();

            if (painel.getGameState() == painel.getTutorialControles()) {
                painel.setGameState(painel.getTitleState());

            } else {
                botoes.mostrarBotaoMochila();

                int subState = painel.getPlaySubState();

                switch (subState) {
                    case 102:
                        painel.setPlaySubState(104);
                        break;

                    case 406:
                    case 410:
                        painel.setPlaySubState(2001);
                        painel.getAmbienteAtual().setSubStateParaRetornar(411);
                        break;

                    case 415:
                        painel.setPlaySubState(413);
                        break;

                    case 500:
                        painel.trocarAmbiente("floresta", 303);
                        break;

                    default:
                        painel.setPlaySubState(painel.getPlaySubState() - 1);
                        break;
                }
            }
        }

        // MOCHILA/INVENTÁRIO
        else if (fonte == botoes.getBotaoMochila()) {
            painel.getUi().mostrarInventario();
        }

        // VOLTAR À BASE
        else if (fonte == botoes.getBotaoBase()) {
            painel.setPlaySubState(1);
        }

        // CARD DE AMBIENTE
        else if (fonte == botoes.getBotaoCardAmbiente()) {
            painel.getUi().mostrarCardAmbiente();
        }

        // CLIMA
        else if (fonte == botoes.getBotaoClima()) {
            painel.getUi().mostrarClima();
        }

        // DE VOLTA AO INÍCIO
        else if (fonte == botoes.getBotaoInicio()) {
            painel.setGameState(painel.getTitleState());
        }

    }

    public void actionPerformedContinuar() {
        int gameState = painel.getGameState();

        botoes.esconderBotaoContinuar();

        // FightState
        if (painel.getEventoCriatura().isEventoCriaturaAtivo()) {
            painel.setFightState(true);
        }

        // PlayState
        if (gameState == painel.getPlayState()) {
            botoes.esconderBotaoContinuar();

            if (!painel.getFightState()) {
                botoes.mostrarBotaoMochila();
            }

            int subState = painel.getPlaySubState();

            switch (subState) {
                // GERAR CARDS
                case 103:
                    painel.trocarAmbiente("lago", 400);
                    break;
                case 302:
                    painel.trocarAmbiente("montanha", 500);
                    break;

                // EVENTOS DE CRIATURA
                case 201:
                    painel.setPlaySubState(1001);
                    break;
                case 303:
                    painel.setPlaySubState(1002);
                    break;
                case 404:
                    painel.setPlaySubState(2001);
                    break;

                // FLORESTA
                case 205:
                    painel.setPlaySubState(208);
                    break;
                case 208:
                    if (painel.getAmbienteAtual().isBaseFogoAceso()) {
                        painel.getAmbienteAtual().setSubStateParaRetornar(211);
                        painel.setPlaySubState(1);
                    } else {
                        painel.getAmbienteAtual().setSubStateParaRetornar(202);
                        painel.setPlaySubState(1001);
                    }
                    break;


                // LAGO
                case 401:
                    painel.getAmbienteAtual().setSubStateParaRetornar(403);
                    painel.setPlaySubState(1);
                    break;
                case 405:
                    painel.setPlaySubState(408);
                    break;

                // MONTANHA
                default:
                    if (painel.getPlaySubState() < 1000) {
                        painel.setPlaySubState(painel.getPlaySubState() + 1);
                    } else {
                        System.out.println("Combate ativo");
                    }
                    break;
            }
        }

        // Outras telas
        else if (gameState == painel.getOpeningState()) {
            painel.trocarAmbiente("floresta", 0);
            painel.setGameState(painel.getPlayState());
        }
    }
}