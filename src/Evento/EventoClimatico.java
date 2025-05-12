package Evento;

import Controles.Botoes;
import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;

public abstract class EventoClimatico extends Evento {

    private Botoes botoes;

    public EventoClimatico(Painel painel, UI ui, Jogador jogador, Botoes botoes) {
        super(painel, ui, jogador);
        this.botoes = botoes;
    }

    @Override
    public void executar(Graphics2D g2, int tipo) {
        if (tipo == 1) {
            chuva(g2);
        }
    }

// Descrição dos eventos
    public void chuva(Graphics2D g2) {
        int tileSize = getPainel().getTileSize();
        int y = tileSize * 3;

        g2.setColor(Color.white);
        getUi().escreverTexto("Uma chuva leve se inicia.", y);
        getUi().escreverTexto("Isso pode afetar sua visibilidade", y += tileSize);
        g2.setColor(Color.gray); g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
        getUi().escreverTexto("(Chances de encontro com criaturas aumentadas.)", y += tileSize);
        g2.setColor(Color.white); g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

        botoes.mostrarBotaoContinuar();
        botoes.esconderBotaoMochila();
    }
}