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
                definirOcorrenciaDeEventoCriatura(g2, eventoVibora, 21);
                break;
            case goblin:
                definirOcorrenciaDeEventoCriatura(g2, eventoGoblin, 22);
                break;
            case boss:
                definirOcorrenciaDeEventoCriatura(g2, eventoGolem, 23);
                break;

            case 200:
                definirOcorrenciaDeEventoClimatico(g2, eventoCavernoso, 4);

                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você se encontra dentro de uma caverna.", y);
                ui.escreverTexto("Um local diferente de qualquer outro que já tenha visto,", y += tileSize);
                ui.escreverTexto("paredes e teto compostas por minerais ricos em sódio,", y += tileSize);
                ui.escreverTexto("concedendo um gradiente branco, cinza e rosado. É quase etéreo.", y += tileSize);
                ui.escreverTexto("Infelizmente, não há tempo para apreciar formações geológicas,", y += tileSize);
                ui.escreverTexto("sua queda o fez tombar em um buraco que agora está", y += tileSize);
                ui.escreverTexto("alto demais para escalar de volta à floresta.", y += tileSize);
                ui.escreverTexto("Não deu para enxergar como era a figura que te empurrou...", y += tileSize);
                ui.escreverTexto("Enfim... é hora de montar uma base aqui", y += tileSize);
                ui.escreverTexto("E avançar atrás de uma saída.", y += tileSize);
                break;

            case 201:
                ui.escreverTexto("Como prosseguir agora?", y);

                boolean achouAgua = painel.getAmbienteAtual().checarSeSubStateFoiVisitado();
                boolean podeMinerar = painel.getInvent().acharItem("Picareta");

                String opcaoAgua = achouAgua ? "Tentar pegar mais água" : "Achar fonte de água";
                String opcaoMinerio = podeMinerar ? "Minerar" : "Checar minérios";

                ui.desenharOpcoes(new String[] {opcaoAgua, opcaoMinerio, "Descobrir como escapar"}, y += tileSize * 2, numComando);
                break;

            case 202:
                break;

            case 203:
                break;

            case 204:
                break;

            case 205:
                break;

            case 206:
                break;

            case 207:
                break;

            case 208:
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}
