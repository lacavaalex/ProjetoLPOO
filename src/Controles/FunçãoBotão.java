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
                if (subState == 1) {
                    painel.setPlaySubState(10);
                }
                else if (subState == 2) {
                    painel.setPlaySubState(20);
                }
                else if (subState == 12) {
                    painel.setGameState(102);
                }
                else if (subState == 102) {
                    painel.setGameState(1212);
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


        } else if (fonte == botoes.getBotaoVoltar()) {
            if (painel.getPlaySubState() == 1212) {
                painel.setPlaySubState(1213);
            }
            else {
                painel.setPlaySubState(painel.getPlaySubState() - 2);
                System.out.println("Clicou em VOLTAR: \n" + painel.getGameState());
                botoes.esconderBotaoVoltar();
            }
        }

        System.out.println(gameState);
    }
}
