package Evento;

import Entidade.Criatura;
import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;
import java.util.Random;

public class EventoCriatura extends Evento {

    private Criatura criatura;
    private Random rand;

    private int probabilidade;
    private int executavel;
    private boolean encontroSurpresa;
    private boolean ataqueSurpresaExecutado = false;

    public EventoCriatura(Painel painel, UI ui, Jogador jogador, Criatura criatura) {
        super(painel, ui, jogador);
        this.criatura = criatura;

        rand = new Random();
    }

    @Override
    public void executar(Graphics2D g2, int tipo) {
        int tileSize = getPainel().getTileSize();
        int y = tileSize * 2;

        // Execução com probabilidade bem sucedida
        if (executavel == 1) {
            getPainel().getEvento().setEventoCriaturaAtivo(true);
            getPainel().setGameState(getPainel().getPlayState());
            getPainel().getCombate().iniciarCombate(criatura);

            if (tipo == 1) {
                viboraRubroFloresta(g2);

                if (isSurpresa()) {
                    g2.setColor(Color.red);
                    getUi().escreverTexto("ATAQUE SURPRESA! -1 DE VIDA", y += tileSize * 4);
                    g2.setColor(Color.white);
                    getUi().escreverTexto("O que diabos!?... é uma VÍBORA-RUBRO!", y += tileSize);

                    if (!ataqueSurpresaExecutado) {
                        getJogador().setVida(getJogador().getVida() - criatura.getAtaqueCriatura());
                        ataqueSurpresaExecutado = true;
                        setSurpresa(false);
                    }
                }

            } else if (tipo == 2) {
                ursoPai(g2);

                g2.setColor(Color.red);
                getUi().escreverTexto("*GROAAAAAR*", y += tileSize * 4);
                g2.setColor(Color.white);
                getUi().escreverTexto("O RUGIDO ESTREMECE TODA A FLORESTA. VOCÊ PULA EM DESESPERO.", y += tileSize);
                getUi().escreverTexto("É... minha nossa... um urso negro gigante!", y += tileSize);
                getUi().escreverTexto("", y += tileSize);
            }

        // Probabilidade mal sucedida
        } else if (executavel == 0) {
            encontroSurpresa = false;
        }
    }

    @Override
    public void chance(Graphics2D g2, int tipo) {

        probabilidade = rand.nextInt(100) + 1;

        if (tipo == 1) { // Víbora
            executavel = (probabilidade <= 70) ? 1 : 0;

        } else if (tipo == 2) { // Urso Pai
            executavel = (probabilidade <= 50) ? 1 : 0;
        }

        System.out.println("PROBABILIDADE: " + getProbabilidade());
    }

    // Evento de encontro surpresa com a víbora
    public void viboraRubroFloresta(Graphics2D g2) {
        int tileSize = getPainel().getTileSize();
        int y = tileSize * 8;

        g2.setColor(Color.red);
        criatura.definirCriatura(1);
        getUi().escreverTexto(criatura.getDescricao(), y += tileSize);
        g2.setColor(Color.white);
    }

    // Evento de encontro surpresa com o urso
    public void ursoPai(Graphics2D g2) {
        int tileSize = getPainel().getTileSize();
        int y = tileSize * 8;

        g2.setColor(Color.red);
        criatura.definirCriatura(2);
        getUi().escreverTexto(criatura.getDescricao(), y += tileSize);
        g2.setColor(Color.white);
    }


    // Getters e setters
    @Override
    public int getExecutavel() { return executavel; }

    @Override
    public void setSurpresa(boolean encontroSurpresa) { this.encontroSurpresa = encontroSurpresa; }
    public boolean isSurpresa() { return encontroSurpresa; }

    public int getProbabilidade() { return probabilidade; }

    @Override
    public void removerMensagemVisivel() {}
}