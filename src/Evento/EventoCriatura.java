package Evento;

import Entidade.Criatura;
import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class EventoCriatura extends Evento {

    Criatura criatura;

    private int tipo;

    public EventoCriatura(Painel painel, UI ui, Jogador jogador, Criatura criatura, int tipo) {
        super(painel, ui, jogador);
        this.tipo = tipo;
        this.criatura = criatura;
    }

    @Override
    public void executar(Graphics2D g2) {
        if (tipo == 1) {
            viboraRubroFloresta(g2);
        }
        else if (tipo == 2) {
            ursoPai(g2);
        }
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

// Getters e setters
    public Criatura getCriatura() { return criatura; }
}
