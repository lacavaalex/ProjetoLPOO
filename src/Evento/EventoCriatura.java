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
            getPainel().getCombate().iniciarCombate(criatura);
            criatura.definirCriatura(tipo);

            if (tipo == 11) {
                if (isSurpresa()) {
                    g2.setColor(Color.red);
                    getUi().escreverTexto("ATAQUE SURPRESA! -1 DE VIDA", y += tileSize * 4);
                    g2.setColor(Color.white);
                    getUi().escreverTexto("O que diabos!?... é uma VÍBORA-RUBRO!", y += tileSize);

                    if (!ataqueSurpresaExecutado) {
                        getJogador().setVida(getJogador().getVida() - criatura.getAtaqueCriatura());
                        getJogador().setEnvenenado(true);
                        ataqueSurpresaExecutado = true;
                        setSurpresa(false);
                    }
                }
            }

            else if (tipo == 12) {
                g2.setColor(Color.red);
                getUi().escreverTexto("*GROAAAAAR*", y += tileSize * 4);
                g2.setColor(Color.white);
                getUi().escreverTexto("O RUGIDO ESTREMECE TODA A FLORESTA. VOCÊ PULA EM DESESPERO.", y += tileSize);
                getUi().escreverTexto("É... minha nossa... um urso negro gigante!", y += tileSize);
            }

            else if (tipo == 21) {
                g2.setColor(Color.white);
                getUi().escreverTexto("Espere, há algo vindo, o que é... nossa!", y += tileSize * 4);
                getUi().escreverTexto("Parece um caranguejo com... três olhos!?", y += tileSize);
            }

            else if (tipo == 22) {
                criatura.definirCriatura(22);
                getPainel().getBotoes().esconderBotaoClima();
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
        if (getPainel().getEventoClimatico().getClima().equals("chuva")) {
            probabilidade = probabilidade * 0.85;
        }

        if (tipo == 11) { // Víbora
            executavel = (probabilidade <= 70) ? 1 : 0;

        } else if (tipo == 12) { // Urso Pai
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

    // Getters e setters
    @Override
    public int getExecutavel() { return executavel; }

    public int getContadorDeEncontros() { return contadorDeEncontros; }

    public boolean isSurpresa() { return encontroSurpresa; }
    public void setSurpresa(boolean encontroSurpresa) { this.encontroSurpresa = encontroSurpresa; }

    public double getProbabilidade() { return probabilidade; }
}