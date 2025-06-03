package Ambiente;

import Controles.Botoes;
import Entidade.Jogador;
import Evento.EventoClimatico;
import Main.Painel;
import UI.UI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AmbienteMontanha extends Ambiente {

    private Graphics2D g2;
    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;

    private EventoClimatico eventoNevasca;
    private BufferedImage fundoMontanha2;

    public AmbienteMontanha(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();

        this.eventoNevasca = painel.getEventoClimatico();

        descreverAmbiente();
        fundoMontanha2 = ui.setupImagens("montanha_epopeica-2", "background");
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("MONTANHA EPOPEICA");
        this.setDescricao("Imponente, desafiadora, majestosa.");
        this.setDificuldade("perigosas.");
        this.setRecursos("indefinido.");
        this.setFrequenciaEventos("desastres naturais, perigos escondidos.");
        this.setClimaAmbiente("altamente frio, piora com altitude.");
        this.setNomeFundoCard("montanha_epopeica");
        this.setNomeFundoCombate("montanha_epopeica_combate");
    }

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

        int numComando = ui.getNumComando();

        g2.fillRect(0, 0, larguraTela, alturaTela);
        ui.desenharPlanoDeFundo(fundoMontanha2);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize * 2;

        if (subState >= 303) {
            definirOcorrenciaDeEventoClimatico(g2, eventoNevasca, 4);
        }

        switch (subState) {

            case 1:
                botoes.esconderBotao("Voltar à base");
                ui.mostrarAcampamento();
                break;

            case 300:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("O trecho da floresta até a montanha acabou.", y);
                ui.escreverTexto("O único caminho para além daqui é para cima.", y += tileSize);
                ui.escreverTexto("(A montanha sussurra seu nome...)", y += tileSize);
                ui.escreverTexto("Seria fatal escalar o paredão sem equipamentos e preparação.", y += tileSize);
                ui.escreverTexto("E ficar congelando aqui não é uma opção.", y += tileSize);
                ui.escreverTexto("Isso é um beco sem saída. Melhor retornar.", y += tileSize);
                break;

            case 301:
                definirTelaDeBotao("continuar");
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                g2.setColor(Color.gray);
                ui.escreverTexto(jogador.getNome(), painel.getAltura()/2 - tileSize * 2);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
                g2.setColor(Color.WHITE);
                ui.escreverTexto("Há um movimento estranho na sua mochila.", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("As jóias... brilham forte aqui.", painel.getAltura()/2);
                ui.escreverTexto("O segredo desse lugar só pode estar lá em cima.",painel.getAltura()/2 + tileSize);
                ui.escreverTexto("Se ao menos houvesse uma forma de escalar...",painel.getAltura()/2 + tileSize * 2);
                break;

            case 302:
                if (painel.getInvent().acharItem("Corda de escalada")) {
                    ui.escreverTexto("Pelo jeito você tem uma corda de escalada...", y);
                    ui.escreverTexto("Seria agora o momento de enfrentar este desafio?", y += tileSize);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Escalar é uma decisão definitiva.)", y += tileSize);

                    ui.desenharOpcoes(new String[] {"Escalar", "Agora não"}, y += tileSize, numComando);
                }
                else {
                    if (!painel.getInvent().acharItem("Corda")) {
                        if (!isRecursosColetados()) {
                            painel.getInvent().adicionarItem("Corda", "recurso", 1);
                            setRecursosColetados(true);
                        }
                    } else if (painel.getInvent().acharItem("Corda")) {
                        ui.escreverTexto("Algo na sua mochila pode permitir que você crie um", y += tileSize);
                        ui.escreverTexto("equipamento de escalada. Uma corda com algo firme, pontudo.", y += tileSize);
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}
