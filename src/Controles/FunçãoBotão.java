package Controles;

import Main.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FunçãoBotão implements ActionListener {

    Painel painel;
    Botões botoes;

    public FunçãoBotão(Painel painel, Botões botoes) {
        this.painel = painel;
        this.botoes = botoes;
    }

    public void actionPerformed(ActionEvent e) {

        int gameState = painel.getGameState();
        Object fonte = e.getSource();

        if (fonte == botoes.getBotaoContinuar()) {
            if (gameState == painel.getPlayState()) {
                int subState = painel.getPlaySubState();

                switch (subState) {
                    case 1: painel.setPlaySubState(10); break;
                    case 2: painel.setPlaySubState(20); break;
                    case 3: painel.setPlaySubState(30); break;
                    case 12: painel.setGameState(102); break;
                    case 102: painel.setGameState(1212); break;
                    case 31: painel.setGameState(103); break;
                    case 103: painel.setGameState(3131); break;
                    case 32: painel.setPlaySubState(33); break;
                    default: System.out.println("Caso default"); break;
                }
            }

            else if (gameState == painel.getOpeningState()) {
                painel.setGameState(painel.getFlorestaCardState());
            }
            else if (gameState == painel.getFlorestaCardState()) {
                painel.setGameState(painel.getPlayState());
            }
            else if (gameState == painel.getLagoCardState()) {
                painel.setPlaySubState(1212);
                painel.setGameState(painel.getPlayState());
            }
            else if (gameState == painel.getMontanhaCardState()) {
                painel.setPlaySubState(3131);
                painel.setGameState(painel.getPlayState());
            }


        } else if (fonte == botoes.getBotaoVoltar()) {
            if (painel.getPlaySubState() == 1212 || painel.getPlaySubState() == 11) {
                painel.setPlaySubState(1213);
            }
            else if (painel.getPlaySubState() == 3131) {
                painel.setPlaySubState(32);
            }
        }

        System.out.println(gameState);
    }
}
