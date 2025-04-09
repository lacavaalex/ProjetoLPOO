package Ambiente;

import Entidade.Criatura;
import Entidade.Jogador;
import Main.*;

import java.awt.*;
import java.util.Random;

public class Evento {

    Painel painel;
    UI ui;
    Jogador jogador;
    Criatura criatura;

    private int tipo;
    private int probabilidade;
    private Random random = new Random();



    public Evento(Painel painel, UI ui, Jogador jogador, Criatura criatura) {
        this.painel = painel;
        this.ui = ui;
        this.jogador = jogador;
        this.criatura = criatura;
    }

    public void consumoPodre() {
        //A editar
    }

    // Evento de encontro surpresa com a víbora
    public void viboraRubroFloresta(Graphics2D g2) {

        int tileSize = painel.getTileSize();
        int y = tileSize * 3;

        g2.setColor(Color.red);
        ui.escreverTexto("ATAQUE SURPRESA! -1 DE VIDA", y);
        jogador.setVida(jogador.getVida() - 1);
        g2.setColor(Color.white);
        ui.escreverTexto("O que diabos!?... é uma VÍBORA-RUBRO!", y += tileSize);
        ui.escreverTexto("", y += tileSize);
        g2.setColor(Color.red);
        criatura.setViboraRubro();
        ui.escreverTexto(criatura.getDescricao(), y += tileSize);
        g2.setColor(Color.white);
        ui.escreverTexto("O que fazer?", y += tileSize);

        String[] opcoes = {"Atacar", "Fugir"};

        opcoesCombate(g2, y);
    }

    // Evento de encontro surpresa com o urso
    public void ursoPai(Graphics2D g2) {

        int tileSize = painel.getTileSize();
        int y = tileSize * 3;

        g2.setColor(Color.red);
        ui.escreverTexto("*GROAAAAAR*", y);
        g2.setColor(Color.white);
        ui.escreverTexto("O RUGIDO ESTREMECE TODA A FLORESTA. VOCÊ PULA EM DESESPERO.", y += tileSize);
        ui.escreverTexto("É... minha nossa... um urso negro gigante!", y += tileSize);
        ui.escreverTexto("", y += tileSize);
        g2.setColor(Color.red);
        criatura.setUrsoPai();
        ui.escreverTexto(criatura.getDescricao(), y += tileSize);
        g2.setColor(Color.white);
        ui.escreverTexto("O que fazer?", y += tileSize);

        opcoesCombate(g2, y);
        if (painel.getPlaySubState() == 34) {
            jogador.setVida(jogador.getVida() - jogador.getVidaMax());
        }
    }



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
}