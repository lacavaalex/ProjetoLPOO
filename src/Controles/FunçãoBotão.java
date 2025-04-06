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
            painel.setGameState(painel.getGameState() + 1);
            System.out.println("Clicou em CONTINUAR: \n" + painel.getGameState());
        } else if (fonte == botoes.getBotaoVoltar()) {
            painel.setPlaySubState(painel.getPlaySubState() - 2);
            System.out.println("Clicou em VOLTAR: \n" + painel.getGameState());
            botoes.esconderBotaoVoltar();
        }

        System.out.println(gameState);
    }
}
