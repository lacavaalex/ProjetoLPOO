package Ambiente;

import Controles.Botoes;
import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class AmbienteLago extends Ambiente {

    private Graphics2D g2;
    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;

    public AmbienteLago(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();

        descreverAmbiente();
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("LAGO SERENO");
        this.setDescricao("Limpo, vasto, estranhamente silencioso.");
        this.setDificuldade("tranquilas.");
        this.setRecursos("água.");
        this.setFrequenciaEventos("quieto, um certo ar de misticismo.");
        this.setClimaAmbiente("levemente frio");
    }

    @Override
    public void construirCard(Graphics2D g2) {

        int tileSize = painel.getTileSize();
        int y = tileSize * 3;

        g2.setColor(new Color(115, 155, 255));
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
            case 400:
                botoes.mostrarBotaoVoltar();
                botoes.esconderBotaoMochila();

                ui.escreverTexto("Este é o lago.", y);
                ui.escreverTexto("Você pode ficar e descansar, ou retornar à fogueira.", y += tileSize);
                break;
            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}