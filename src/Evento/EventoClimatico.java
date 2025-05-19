package Evento;

import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class EventoClimatico extends Evento {

    private Painel painel;

    private int probabilidade;
    private int executavel;

    private String clima = "ameno";

    public EventoClimatico(Painel painel, UI ui, Jogador jogador) {
        super(painel, ui, jogador);
        this.painel = painel;
    }

    @Override
    public void executar(Graphics2D g2, int tipo) {
        if (executavel == 1) {
            painel.getEventoClimatico().setEventoClimaticoAtivo(true);
            if (tipo == 1) {
                setClima("chuva");
            }
        }
    }

    @Override
    public void chance(Graphics2D g2, int tipo) {

        probabilidade = painel.definirUmaProbabilidade();

        if (tipo == 1) { // Chuva
            executavel = (probabilidade <= 100) ? 1 : 0;
        }

        System.out.println("PROBABILIDADE: " + getProbabilidade());
    }

    public void finalizarEventoClimatico() {
        setEventoClimaticoAtivo(false);
        setClima("ameno");
    }

    // Getters e setters
    @Override
    public int getExecutavel() { return executavel; }

    public int getProbabilidade() { return probabilidade; }

    public String getClima() { return clima; }
    public void setClima(String clima) { this.clima = clima; }
}