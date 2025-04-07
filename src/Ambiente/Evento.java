package Ambiente;

import Entidade.Criatura;
import Entidade.Jogador;
import Main.*;

import java.awt.*;
import java.util.Random;

public class Evento {

    Painel painel;
    UI ui;
    Jogador jogador;
    Criatura criatura;
    Graphics2D g2;

    Random random = new Random();

    private int tipo;
    private int probabilidade;

    public Evento(Painel painel, UI ui, Jogador jogador, Criatura criatura) {
       this.painel = painel;
       this.ui = ui;
       this.jogador = jogador;
       this.criatura = criatura;
    }

    // Evento de encontro surpresa com a víbora
    public void viboraRubroFloresta (Graphics2D g2) {

        probabilidade = getProbabilidade();

        // Garante o evento durante o substate 20, mas tem apenas 50% de chance em outros casos
        if (probabilidade <= 50 || painel.getPlaySubState() == 20) {

            int tileSize = painel.getTileSize();
            int y = tileSize * 3;

            g2.setColor(Color.red);
            ui.escreverTexto("ATAQUE SURPRESA! -1 DE VIDA", y);
            jogador.setVida(jogador.getVida() - 1);
            g2.setColor(Color.white);
            ui.escreverTexto("O que diabos!?... é uma VÍBORA-RUBRO!", y += tileSize);
            ui.escreverTexto("", y += tileSize);
            g2.setColor(Color.red);
            criatura.setViboraRubro();
            ui.escreverTexto(criatura.getDescricao(), y += tileSize);
            g2.setColor(Color.white);
            ui.escreverTexto("O que fazer?", y += tileSize);

            String[] opcoes = {"Atacar", "Fugir"};

            y += tileSize * 2;
            for (int i = 0; i < opcoes.length; i++) {
                String texto = opcoes[i];
                int x = ui.coordenadaXParaTextoCentralizado(texto);

                if (ui.numComando == i) {
                    g2.drawString(">", x - tileSize, y);
                }
                g2.drawString(texto, x, y);
                y += tileSize;
            }
        } else {
            // A acrescentar
        }
    }

    public int getProbabilidade() {
        return probabilidade;
    }
    public void setProbabilidade() {
        this.probabilidade = random.nextInt(101);
    }
}
