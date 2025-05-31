package Ambiente;

import Controles.Botoes;
import Entidade.Jogador;
import Evento.EventoClimatico;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class AmbienteMontanha extends Ambiente {

    private Graphics2D g2;
    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;

    private EventoClimatico eventoNevasca;

    public AmbienteMontanha(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();

        descreverAmbiente();
        eventoNevasca = new EventoClimatico(painel, ui, jogador);
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("MONTANHA EPOPEICA");
        this.setDescricao("Imponente, desafiadora, majestosa.");
        this.setDificuldade("perigosas.");
        this.setRecursos("indefinido.");
        this.setFrequenciaEventos("desastres naturais, perigos escondidos.");
        this.setClimaAmbiente("altamente frio, piora com altitude.");
        //this.setNomeFundoCard("montanha_epopeica");
        //this.setNomeFundoCombate("montanha_epopeica_combate");
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

        if (subState != 600) {
            definirOcorrenciaDeEventoClimatico(g2, eventoNevasca, 4);
        }

        switch (subState) {

            case 1:
                botoes.esconderBotao("Voltar à base");
                ui.mostrarAcampamento();
                break;

            case 600:
                definirTelaDeBotao("voltar");

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
