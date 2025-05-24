package Ambiente;

import Controles.Botoes;
import Entidade.Criatura;
import Entidade.Jogador;
import Evento.EventoCriatura;
import Main.Painel;
import UI.UI;

import java.awt.*;

public class AmbienteLago extends Ambiente {

    private Graphics2D g2;
    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;
    private Criatura criatura;

    private EventoCriatura eventoTriclope;

    private final int caranguejo = 2001;

    public AmbienteLago(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();
        this.criatura = new Criatura();

        this.eventoTriclope = new EventoCriatura(painel, ui, jogador, criatura);

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
        if (isCardVisivel()) {

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

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
            String textoEsc = ("Aperte [esc] para sair");
            g2.drawString(textoEsc, painel.getLargura() - tileSize * 6,painel.getAltura() - tileSize);
        }
    }

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

        int numComando = ui.getNumComando();

        g2.setColor(Color.black);
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize * 2;

        switch (subState) {

            case caranguejo:
                definirOcorrenciaDeEventoCriatura(g2, eventoTriclope, 21);
                break;

            case 1:
                botoes.esconderBotaoBase();
                ui.mostrarAcampamento();
                break;

            case 400:
                ui.escreverTexto("Este lago parece bom para descanso.", y);
                ui.escreverTexto("Você pode buscar um bom lugar acampamento aqui,", y += tileSize);
                ui.escreverTexto("ou retornar para a floresta.", y += tileSize);

                ui.desenharOpcoes(new String[] {"Explorar lago", "Retornar à floresta"}, y += tileSize * 2, numComando);
                break;

            case 401:
                definirTelaDeTransicao("continuar");

                ui.escreverTexto("O lago é extenso, cobrindo quase todo o horizonte.", y);
                ui.escreverTexto("Não parece haver muitas criaturas por aqui.", y += tileSize);
                ui.escreverTexto("Entretanto, há algumas sementes. Vale a pena pegar.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Punhado de sementes", "recurso", 1);
                    setRecursosColetados(true);
                }

                ui.escreverTexto("Aqui parece ser um bom lugar para montar acampamento.", y+= tileSize);
                break;

            case 403:
                ui.escreverTexto("O que fazer agora?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Checar o lago", "Observar ecossistema", "Buscar recursos"}, y += tileSize, numComando);
                break;

            case 404:
                definirTelaDeTransicao("continuar");
                ui.escreverTexto("Você se aproxima do lago...", y += tileSize);
                break;

            case 405:
                ui.escreverTexto("Parece que há uma população de crustáceos aqui.", y += tileSize);
                ui.escreverTexto("A maioria não parece realmente hostil, já outros...", y += tileSize);
                ui.escreverTexto("Há uma placa próxima à margem do lago.", y += tileSize);
                break;

            case 406:
                ui.escreverTexto("Você busca por recursos.", y += tileSize);
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}