package Evento;

import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;
import java.util.Random;

public class EventoClimatico extends Evento {

    private Random rand;

    private int probabilidade;
    private int executavel;
    private boolean mensagemVisivel = false;

    public EventoClimatico(Painel painel, UI ui, Jogador jogador) {
        super(painel, ui, jogador);

        rand = new Random();
    }

    @Override
    public void executar(Graphics2D g2, int tipo) {
        if (executavel == 1) {
            mensagemVisivel = true;
            getPainel().getEvento().setEventoClimaticoAtivo(true);
            if (tipo == 1) {
                chuva(g2);
            }
        }
    }

    @Override
    public void chance(Graphics2D g2, int tipo) {

        probabilidade = rand.nextInt(100) + 1;

        if (tipo == 1) { // Chuva
            executavel = (probabilidade <= 40) ? 1 : 0;

        }

        System.out.println("PROBABILIDADE: " + getProbabilidade());
    }

    @Override
    public void setSurpresa(boolean surpresa) {}

    // Descrição dos eventos
    public void chuva(Graphics2D g2) {
        if (mensagemVisivel) {
            int tileSize = getPainel().getTileSize();
            int larguraTela = getPainel().getLargura();
            int alturaTela = getPainel().getAltura();

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, larguraTela, alturaTela);

            int y = tileSize * 3;

            g2.setColor(Color.white);
            getUi().escreverTexto("Uma chuva leve se inicia.", y);
            getUi().escreverTexto("Isso pode afetar sua visibilidade", y += tileSize);
            g2.setColor(Color.gray);
            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
            getUi().escreverTexto("(Chances de encontro com criaturas aumentadas.)", y += tileSize);
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
            getUi().escreverTexto("Aperte ENTER para continuar.", y += tileSize);
        }
    }

    @Override
    public int getExecutavel() { return executavel; }

    public int getProbabilidade() { return probabilidade; }

    @Override
    public void removerMensagemVisivel() { mensagemVisivel = false; }
}