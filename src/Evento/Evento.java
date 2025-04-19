package Evento;

import Entidade.Criatura;
import Entidade.Jogador;
import Main.*;
import UI.UI;

import java.awt.*;

public abstract class Evento {

    Painel painel;
    UI ui;
    Jogador jogador;
    Criatura criatura;

    public Evento(Painel painel, UI ui, Jogador jogador, Criatura criatura) {
        this.painel = painel;
        this.ui = ui;
        this.jogador = jogador;
        this.criatura = criatura;
    }

    public abstract void executar(Graphics2D g2);

// Opcoes no combate
    public void opcoesCombate(Graphics2D g2, int y) {

        int tileSize = painel.getTileSize();

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
    }

// Getters e setters
    public Painel getPainel() { return painel; }
    public UI getUi() { return ui; }
    public Jogador getJogador() { return jogador; }
    public Criatura getCriatura() { return criatura; }
}