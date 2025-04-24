package Ambiente;

import Entidade.Jogador;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class AmbienteLago extends Ambiente {

    Painel painel;
    Jogador jogador;
    UI ui;

    public AmbienteLago(Painel painel, Jogador jogador, UI ui) {
        super();
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;

        descreverAmbiente();
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("LAGO SERENO");
        this.setDescricao("Limpo, vasto, estranhamente silencioso.");
        this.setDificuldade("tranquilas.");
        this.setRecursos("água.");
        this.setFrequenciaEventos("quieto, um certo ar de misticismo.");
        this.setClima("levemente frio");
    }

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();
        //this.numComando = painel.getUi().getNumComando();

        g2.setColor(Color.black);
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize;

        switch (subState) {
            case 1212:
                ui.escreverTexto("Este é o lago.", tileSize * 2);
                ui.escreverTexto("Você pode ficar e descansar, ou retornar à fogueira.", tileSize * 3);
                break;
            default:
                System.out.println("default");
                System.out.println(painel.getPlaySubState());
                break;
        }
    }
}