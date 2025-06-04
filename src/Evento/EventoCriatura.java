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
            getPainel().getCombateSistema().iniciarCombate(criatura);
            getPainel().getCombateUi().inicializarFundoCombate();

            if ((tipo == 1 || tipo == 3)) {
                ataqueSurpresa();
                setSurpresa(false);
            }

            if (tipo == 1) {
                g2.setColor(Color.red);
                getUi().escreverTexto("ATAQUE SURPRESA! -" + criatura.getAtaqueCriatura()/2 +"HP", y += tileSize * 4);
                g2.setColor(Color.white);
                getUi().escreverTexto("O que diabos!?... é uma VÍBORA-RUBRO!", y += tileSize);
                getJogador().setEnvenenado(true);
            }

            else if (tipo == 3) {
                g2.setColor(Color.red);
                getUi().escreverTexto("*ÁUUUUUUU*", y += tileSize * 4);
                getUi().escreverTexto("ATAQUE SURPRESA! -" + criatura.getAtaqueCriatura()/2 +"HP", y += tileSize);
                g2.setColor(Color.white);
                getUi().escreverTexto("Antes que você pudesse reagir, a criatura se atira contra você.", y += tileSize);
                getUi().escreverTexto("Um lobo de mandíbula extraterrestre te escolhe como presa.", y += tileSize);
            }

            else if (tipo == 4) {
                g2.setColor(Color.white);
                getUi().escreverTexto("De repente, um vulto passa rasante.", y += tileSize * 4);
                getUi().escreverTexto("O que é aquilo? É um corvo... ou... parte de um...", y += tileSize);
            }

            else if (tipo == 11) {
                g2.setColor(Color.white);
                getUi().escreverTexto("Espere, há algo vindo, o que é... nossa!", y += tileSize * 4);
                getUi().escreverTexto("Parece um caranguejo com... três olhos!?", y += tileSize);
            }

            else if (tipo == 21) {
                g2.setColor(Color.white);
                getUi().escreverTexto("Uma víbora coberta de rochas te persegue.", y += tileSize * 4);
                getUi().escreverTexto("Você invadiu seu domínio, e ela não vai parar até te pegar.", y += tileSize);
            }

            else if (tipo == 22) {
                g2.setColor(Color.white);
                getUi().escreverTexto("Há algo seguindo seus movimentos...", y += tileSize * 4);
                g2.setColor(Color.red);
                getUi().escreverTexto("*SWOOP*", y += tileSize);
                g2.setColor(Color.white);
                getUi().escreverTexto("Do teto, um monstro esquisito te observa atentamente.", y += tileSize);
                getUi().escreverTexto("Esse olhar... não é confortável.", y += tileSize);
            }

            else if (tipo == 31) {
                g2.setColor(Color.red);
                getUi().escreverTexto("*RAWWWR*", y += tileSize);
                g2.setColor(Color.white);
                getUi().escreverTexto("Você foi avistado por uma fera cristalina.", y += tileSize * 4);
                getUi().escreverTexto("Um leopardo que mira te espetar com sua cauda gélida.", y += tileSize);
            }

            else if (tipo == 2 || tipo == 12 || tipo == 23 || tipo == 32) {
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

        if (tipo == 1) { // Víbora
            executavel = (probabilidade <= 65) ? 1 : 0;
        }
        else if (tipo == 2) { // Urso Pai
            executavel = 1;
        }
        else if (tipo == 3) { // Lobo Famélico
            executavel = (probabilidade <= 50) ? 1 : 0;
         }
        else if (tipo == 4) { // Corvo Espectral
            executavel = (probabilidade <= 50) ? 1 : 0;
        }
        else if (tipo == 11) { // Crustaceo Triclope
            executavel = (probabilidade <= 40) ? 1 : 0;
        }
        else if (tipo == 12) { // Crustoso Cruel
            executavel = 1;
        }
        else if (tipo == 21) { // Víbora-Mineral
            executavel = (probabilidade <= 65) ? 1 : 0;
        }
        else if (tipo == 22) { // Goblin Salgado
            executavel = 1;
        }
        else if (tipo == 23) { // Golem de Sódio
            executavel = 1;
        }
        else if (tipo == 31) { // Leopardo Glacial
            executavel = 1;
        }
        else if (tipo == 32) { // Funesto
            executavel = 1;
        }
    }

    public void incrementarContador() { contadorDeEncontros++; }
    public void resetContador() { contadorDeEncontros = 0; }

    public void ataqueSurpresa() {
        if (isSurpresa()) {
            getJogador().setVida(getJogador().getVida() - criatura.getAtaqueCriatura()/2);
            setSurpresa(false);
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