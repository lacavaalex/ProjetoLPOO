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
            }
            else {
                painel.setGameState(painel.getGameState() + 1);
            }


        } else if (fonte == botoes.getBotaoVoltar()) {
            painel.setPlaySubState(painel.getPlaySubState() - 2);
            System.out.println("Clicou em VOLTAR: \n" + painel.getGameState());
            botoes.esconderBotaoVoltar();
        }

        System.out.println(gameState);
    }
}
