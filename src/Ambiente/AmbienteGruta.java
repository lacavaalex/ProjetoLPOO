package Ambiente;

import Controles.Botoes;
import Entidade.Criatura;
import Entidade.Jogador;
import Evento.EventoClimatico;
import Evento.EventoCriatura;
import Main.Painel;
import UI.UI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AmbienteGruta extends Ambiente {

    private Graphics2D g2;
    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;
    private Criatura criatura;

    private EventoCriatura eventoVibora;
    private EventoCriatura eventoGoblin;
    private EventoCriatura eventoGolem;

    private EventoClimatico eventoCavernoso;
    private EventoClimatico eventoSalgado;

    private BufferedImage fundoGruta2;

    private final int vibora = 3001;
    private final int goblin = 3002;
    private final int boss = 3003;

    public AmbienteGruta(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();
        this.criatura = new Criatura();

        this.eventoVibora = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoGoblin = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoGolem = new EventoCriatura(painel, ui, jogador, criatura);

        this.eventoCavernoso = painel.getEventoClimatico();
        this.eventoSalgado = painel.getEventoClimatico();

        descreverAmbiente();

        fundoGruta2 = ui.setupImagens("gruta_sal-2", "background");
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("GRUTA DE SAL");
        this.setDescricao("Escura, escondida, e salgada.");
        this.setDificuldade("precárias.");
        this.setRecursos("minerais, água. E sal.");
        this.setFrequenciaEventos("movimentado, sua presença é invasiva.");
        this.setClimaAmbiente("húmido. E salgado.");
        this.setNomeFundoCard("gruta_sal");
        this.setNomeFundoCombate("gruta_sal_combate");
    }

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

        int numComando = ui.getNumComando();

        g2.fillRect(0, 0, larguraTela, alturaTela);
        ui.desenharPlanoDeFundo(fundoGruta2);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize * 2;

        switch (subState) {
            // STATES DE EVENTO
            case vibora:
                definirOcorrenciaDeEventoCriatura(g2, eventoVibora, 31);
                break;
            case goblin:
                definirOcorrenciaDeEventoCriatura(g2, eventoGoblin, 32);
                break;
            case boss:
                definirOcorrenciaDeEventoCriatura(g2, eventoGolem, 33);
                break;

            case 500:
                definirOcorrenciaDeEventoClimatico(g2, eventoCavernoso, 4);
                ui.escreverTexto("Esta é a gruta.", y);
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}
