package Evento;

import Entidade.Jogador;
import Main.*;
import UI.UI;

import java.awt.*;

public abstract class Evento {

    Painel painel;
    UI ui;
    Jogador jogador;

    private boolean eventoCriaturaAtivo = false;

    public Evento(Painel painel, UI ui, Jogador jogador) {
        this.painel = painel;
        this.ui = ui;
        this.jogador = jogador;
    }

// Metodo-base para o polimorfismo da superclasse
    public abstract void executar(Graphics2D g2, int tipo);

// Menu de opcoes no combate
    public void opcoesCombate(Graphics2D g2, int y) {

        int tileSize = painel.getTileSize();
        ui.desenharOpcoes(new String[]{"Atacar", "Fugir"},  y += tileSize * 2);
    }

// Getters e setters
    public boolean isEventoCriaturaAtivo() { return eventoCriaturaAtivo; }
    public void setEventoCriaturaAtivo(boolean eventoCriaturaAtivo) { this.eventoCriaturaAtivo = eventoCriaturaAtivo; }

    public Painel getPainel() { return painel; }
    public UI getUi() { return ui; }
}