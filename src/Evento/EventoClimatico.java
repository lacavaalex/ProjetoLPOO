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
            else if (tipo == 2) {
                setClima("tempestade");
            }
            else if (tipo == 3) {
                setClima("tornado");
            }
            else if (tipo == 4) {
                setClima("cavernoso");
            }
            else if (tipo == 5) {
                setClima("salgado");
            }
            else if (tipo == 6) {
                setClima("nevasca");
            }
        }
    }

    @Override
    public void chance(Graphics2D g2, int tipo) {

        probabilidade = painel.definirUmaProbabilidade();

        if (tipo == 1) { // Chuva
            executavel = (probabilidade <= 45) ? 1 : 0;
        }
        else if (tipo == 2) { // Tempestade
            executavel = (probabilidade <= 20) ? 1 : 0;
        }
        else if (tipo == 3) { // Tornado
            executavel = (probabilidade <= 25) ? 1 : 0;
        }
        else if (tipo == 4) { // Cavernoso
            executavel = 1;
        }
        else if (tipo == 5) { // Salgado
            executavel = (probabilidade <= 50) ? 1 : 0;
        }
        else if (tipo == 6) { // Nevasca
            executavel = 1;
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