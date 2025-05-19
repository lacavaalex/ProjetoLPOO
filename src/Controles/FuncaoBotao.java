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

        int gameState = painel.getGameState();
        Object fonte = e.getSource();

        // CONTINUAR
        if (fonte == botoes.getBotaoContinuar()) {
            botoes.esconderBotaoContinuar();

            // FightState
            if (painel.getEventoCriatura().isEventoCriaturaAtivo()) {
                painel.setFightState(true);
            }

            // PlayState
            if (gameState == painel.getPlayState()) {
                botoes.mostrarBotaoMochila();
                botoes.esconderBotaoContinuar();

                int subState = painel.getPlaySubState();

                switch (subState) {
                    // FLORESTA
                    case 205: painel.setPlaySubState(208); break;

                    // Cards
                    case 103: painel.trocarAmbiente("lago"); painel.setGameState(painel.getLagoCardState()); break;
                    case 302: painel.trocarAmbiente("montanha"); painel.setGameState(painel.getMontanhaCardState()); break;

                    // Eventos de criatura
                    case 201: painel.setPlaySubState(1001); break;
                    case 303: painel.setPlaySubState(1002); break;

                    // LAGO
                    case 99992: painel.setGameState(400); break;

                    // MONTANHA
                    case 99993: painel.setGameState(500); break;

                    default: painel.setPlaySubState(painel.getPlaySubState() + 1); break;
                }
            }

            // Outras telas
            else if (gameState == painel.getOpeningState()) {
                painel.setGameState(painel.getFlorestaCardState());
            }
            else if (gameState == painel.getFlorestaCardState()) {
                painel.setGameState(painel.getPlayState());
            }
            else if (gameState == painel.getLagoCardState()) {
                painel.setGameState(painel.getPlayState());
                painel.setPlaySubState(400);
            }
            else if (gameState == painel.getMontanhaCardState()) {
                painel.setGameState(painel.getPlayState());
                painel.setPlaySubState(500);
            }
            System.out.println("Substate atual: " + painel.getPlaySubState());
        }


        // VOLTAR
        else if (fonte == botoes.getBotaoVoltar()) {
            botoes.esconderBotaoVoltar();
            if (painel.getGameState() == painel.getTutorialControles()) {
                painel.setGameState(painel.getTitleState());
            } else {
                botoes.mostrarBotaoMochila();

                if (painel.getPlaySubState() == 400 || painel.getPlaySubState() == 102) {
                    painel.trocarAmbiente("floresta");
                    painel.setPlaySubState(104);
                } else if (painel.getPlaySubState() == 500) {
                    painel.trocarAmbiente("floresta");
                    painel.setPlaySubState(303);
                }
            }
        }

        // MOCHILA/INVENTÁRIO
        else if (fonte == botoes.getBotaoMochila()) {
            painel.getUi().mostrarInventario();
        }

        // CLIMA
        else if (fonte == botoes.getBotaoClima()) {
            painel.getUi().mostrarClima();
        }

        // DE VOLTA AO INÍCIO
        else if (fonte == botoes.getBotaoInicio()) { painel.setGameState(painel.getTitleState()); }

    }
}