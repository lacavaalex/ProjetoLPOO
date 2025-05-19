package Ambiente;

import Controles.Botoes;
import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class AmbienteMontanha extends Ambiente {

    private Graphics2D g2;
    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;

    public AmbienteMontanha(Painel painel, Jogador jogador, UI ui) {
        super(painel);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();

        descreverAmbiente();
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("MONTANHA EPOPEICA");
        this.setDescricao("Imponente, desafiadora, majestosa.");
        this.setDificuldade("perigosas.");
        this.setRecursos("indefinido.");
        this.setFrequenciaEventos("desastres naturais, perigos escondidos.");
        this.setClimaAmbiente("altamente frio, piora com altitude.");
    }

    @Override
    public void construirCard(Graphics2D g2) {

        int tileSize = painel.getTileSize();
        int y = tileSize * 3;

        g2.setColor(new Color(180, 180, 180));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.white);
        ui.escreverTexto(getNome(), y += tileSize);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        y = tileSize * 5;

        ui.escreverTexto(getDescricao(), y += tileSize);
        ui.escreverTexto(" ", y += tileSize);
        ui.escreverTexto("Condições de exploração: " + getDificuldade(), y += tileSize);
        ui.escreverTexto("Recursos possíveis: " + getRecursos(), y += tileSize);
        ui.escreverTexto("Ecossistema: " + getFrequenciaEventos(), y += tileSize);
        ui.escreverTexto("Clima: " + getClimaAmbiente(), y += tileSize);

        botoes.mostrarBotaoContinuar();
    }

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

        g2.setColor(Color.black);
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize * 2;

        switch (subState) {

            case 500:
                botoes.mostrarBotaoVoltar();
                botoes.esconderBotaoMochila();

                ui.escreverTexto("O trecho acabou. O único caminho para além daqui...", y);
                ui.escreverTexto("é para cima. A montanha sussura seu nome...", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("Seria fatal escalar o paredão sem equipamentos e preparação.", y += tileSize);
                ui.escreverTexto("E ficar congelando aqui não é uma opção.", y += tileSize);
                ui.escreverTexto("Isso é um beco sem saída. Melhor retornar.", y += tileSize);
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}
