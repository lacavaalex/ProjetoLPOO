package Evento;

import Entidade.Jogador;
import Main.*;
import UI.UI;

import java.awt.*;

public abstract class Evento {

    private Painel painel;
    private UI ui;
    private Jogador jogador;

    private boolean eventoCriaturaAtivo = false;
    private boolean eventoClimaticoAtivo = false;

    public Evento(Painel painel, UI ui, Jogador jogador) {
        this.painel = painel;
        this.ui = ui;
        this.jogador = jogador;
    }

    // Base para o polimorfismo da superclasse
    public abstract void executar(Graphics2D g2, int tipo);

    // Base polimórfica a probabilidade de evento
    public abstract void chance(Graphics2D g2, int tipo);

    // Getters e setters
    public abstract int getExecutavel();

    public boolean isEventoCriaturaAtivo() { return eventoCriaturaAtivo; }
    public void setEventoCriaturaAtivo(boolean eventoCriaturaAtivo) { this.eventoCriaturaAtivo = eventoCriaturaAtivo; }

    public boolean isEventoClimaticoAtivo() { return eventoClimaticoAtivo; }
    public void setEventoClimaticoAtivo(boolean eventoClimaticoAtivo) { this.eventoClimaticoAtivo = eventoClimaticoAtivo; }

    public Painel getPainel() { return painel; }
    public UI getUi() { return ui; }
    public Jogador getJogador() { return jogador; }
}