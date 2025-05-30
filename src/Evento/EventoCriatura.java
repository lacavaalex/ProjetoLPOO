package Evento;

import Entidade.Criatura;
import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class EventoCriatura extends Evento {

    private Criatura criatura;

    private double probabilidade;
    private int executavel;
    private boolean encontroSurpresa;
    private boolean ataqueSurpresaExecutado = false;

    private int contadorDeEncontros = 0;


    public EventoCriatura(Painel painel, UI ui, Jogador jogador, Criatura criatura) {
        super(painel, ui, jogador);
        this.criatura = criatura;
    }

    @Override
    public void executar(Graphics2D g2, int tipo) {
        int tileSize = getPainel().getTileSize();
        int y = tileSize * 2;

        // Execução com probabilidade bem sucedida
        if (executavel == 1) {

            getPainel().getEventoCriatura().setEventoCriaturaAtivo(true);
            getPainel().setGameState(getPainel().getPlayState());
            criatura.definirCriatura(tipo);
            getPainel().getCombate().iniciarCombate(criatura);

            if (tipo == 11) {
                ataqueSurpresa();

                g2.setColor(Color.red);
                getUi().escreverTexto("ATAQUE SURPRESA! -" + criatura.getAtaqueCriatura()/2 +"HP", y += tileSize * 4);
                g2.setColor(Color.white);
                getUi().escreverTexto("O que diabos!?... é uma VÍBORA-RUBRO!", y += tileSize);
                getJogador().setEnvenenado(true);
            }

            else if (tipo == 12) {
                g2.setColor(Color.red);
                getUi().escreverTexto("*GROAAAAAR*", y += tileSize * 4);
                g2.setColor(Color.white);
                getUi().escreverTexto("O RUGIDO ESTREMECE TODA A FLORESTA. VOCÊ PULA EM DESESPERO.", y += tileSize);
                getUi().escreverTexto("É... minha nossa... um urso negro gigante!", y += tileSize);
            }

            else if (tipo == 13) {
                ataqueSurpresa();

                g2.setColor(Color.red);
                getUi().escreverTexto("*ÁUUUUUUU*", y += tileSize * 4);
                getUi().escreverTexto("ATAQUE SURPRESA! -" + criatura.getAtaqueCriatura()/2 +"HP", y += tileSize);
                g2.setColor(Color.white);
                getUi().escreverTexto("Antes que você pudesse reagir, a criatura se atira contra você.", y += tileSize);
                getUi().escreverTexto("Um lobo de mandíbula extraterrestre te escolhe como presa.", y += tileSize);
            }

            else if (tipo == 14) {
                g2.setColor(Color.white);
                getUi().escreverTexto("De repente, um vulto passa rasante.", y += tileSize * 4);
                getUi().escreverTexto("O que é aquilo? É um corvo... ou... parte de um...", y += tileSize);
            }

            else if (tipo == 21) {
                g2.setColor(Color.white);
                getUi().escreverTexto("Espere, há algo vindo, o que é... nossa!", y += tileSize * 4);
                getUi().escreverTexto("Parece um caranguejo com... três olhos!?", y += tileSize);
            }

            else if (tipo == 22) {
                criatura.definirCriatura(22);
                getPainel().getBotoes().esconderBotao("Clima");
                getPainel().setFightState(true);
            }

        // Probabilidade mal sucedida
        } else if (executavel == 0) {
            encontroSurpresa = false;
        }
    }

    @Override
    public void chance(Graphics2D g2, int tipo) {

        probabilidade = getPainel().definirUmaProbabilidade();

        if (getJogador().getHabilidade() != null &&
                !getJogador().getHabilidade().equals("SOBREVIVENCIAL")) {

            if (getPainel().getEventoClimatico().getClima().equals("chuva")) {
                probabilidade = probabilidade * 0.85;
            }

            else if (getPainel().getEventoClimatico().getClima().equals("tempestade")) {
                probabilidade = probabilidade * 0.75;
            }

            else if (getPainel().getEventoClimatico().getClima().equals("tornado")) {
                probabilidade = probabilidade * 0.70;
            }
        }

        if (tipo == 11) { // Víbora
            executavel = (probabilidade <= 65) ? 1 : 0;

        } else if (tipo == 12) { // Urso Pai
            executavel = (probabilidade <= 50) ? 1 : 0;
        }
        else if (tipo == 13) { // Lobo Famélico
            executavel = (probabilidade <= 50) ? 1 : 0;
         }
        else if (tipo == 14) { // Corvo Espectral
            executavel = (probabilidade <= 50) ? 1 : 0;
        }
        else if (tipo == 21) { // Crustaceo Triclope
            executavel = (probabilidade <= 40) ? 1 : 0;
        }
        else if (tipo == 22) { // Crustoso Cruel
            executavel = 1;
        }

        System.out.println("PROBABILIDADE: " + getProbabilidade());
    }

    public void incrementarContador() { contadorDeEncontros++; }
    public void resetContador() { contadorDeEncontros = 0; }

    public void ataqueSurpresa() {
        if (isSurpresa()) {
            if (!ataqueSurpresaExecutado) {
                getJogador().setVida(getJogador().getVida() - criatura.getAtaqueCriatura()/2);
                ataqueSurpresaExecutado = true;
                setSurpresa(false);
            }
        }
    }

    // Getters e setters
    @Override
    public int getExecutavel() { return executavel; }

    public int getContadorDeEncontros() { return contadorDeEncontros; }

    public boolean isSurpresa() { return encontroSurpresa; }
    public void setSurpresa(boolean encontroSurpresa) { this.encontroSurpresa = encontroSurpresa; }

    public double getProbabilidade() { return probabilidade; }
}