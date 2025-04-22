package Evento;

import Entidade.Jogador;
import Main.*;
import UI.UI;

import java.awt.*;

public abstract class Evento {

    Painel painel;
    UI ui;
    Jogador jogador;

    public Evento(Painel painel, UI ui, Jogador jogador) {
        this.painel = painel;
        this.ui = ui;
        this.jogador = jogador;
    }

// Metodo-base para o polimorfismo da superclasse
    public abstract void executar(Graphics2D g2);

// Menu de opcoes no combate
    public void opcoesCombate(Graphics2D g2, int y) {

        int tileSize = painel.getTileSize();
        ui.desenharOpcoes(new String[]{"Atacar", "Fugir"},  y += tileSize * 2);
    }

// Getters e setters
    public Painel getPainel() { return painel; }
    public UI getUi() { return ui; }
}