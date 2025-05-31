package Controles;

import Main.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FuncaoBotao implements ActionListener {

    private Painel painel;
    private Botoes botoes;

    public FuncaoBotao(Painel painel, Botoes botoes) {
        this.painel = painel;
        this.botoes = botoes;
    }

    // Ação que cada botão executa
    public void actionPerformed(ActionEvent e) {

        Object fonte = e.getSource();

        // CONTINUAR
        if (fonte == botoes.getBotao("Continuar")) {
            actionPerformedContinuar();
        }

        // VOLTAR
        else if (fonte == botoes.getBotao("Voltar")) {
            actionPerformedVoltar();
        }

        // MOCHILA/INVENTÁRIO
        else if (fonte == botoes.getBotao("Abrir mochila")) {
            painel.getUi().mostrarInventario();
        }

        // VOLTAR À BASE
        else if (fonte == botoes.getBotao("Voltar à base")) {
            painel.setPlaySubState(1);
        }

        // CARD DE AMBIENTE
        else if (fonte == botoes.getBotao("Local")) {
            painel.getUi().mostrarCardAmbiente();
        }

        // CLIMA
        else if (fonte == botoes.getBotao("Clima")) {
            painel.getUi().mostrarClima();
        }

        // DE VOLTA AO INÍCIO
        else if (fonte == botoes.getBotao("Voltar ao início")) {
            painel.setGameState(painel.getTitleState());
        }

    }

    public void actionPerformedVoltar() {

        botoes.esconderBotao("Voltar");

        if (painel.getGameState() == painel.getTutorialControles()) {
            painel.setGameState(painel.getTitleState());

        } else {
            botoes.mostrarBotao("Abrir mochila");

            int subState = painel.getPlaySubState();

            switch (subState) {
                case 3:
                case 203:
                    painel.setPlaySubState(202);
                    break;

                case 4:
                    painel.setPlaySubState(1004);
                    break;

                case 102:
                    painel.setPlaySubState(104);
                    break;

                case 204:
                    painel.setPlaySubState(1001);
                    painel.getAmbienteAtual().setSubStateParaRetornar(202);
                    break;

                case 406:
                    painel.setPlaySubState(2001);
                    painel.getAmbienteAtual().setSubStateParaRetornar(403);
                    break;
                case 410:
                    painel.setPlaySubState(2001);
                    painel.getAmbienteAtual().setSubStateParaRetornar(411);
                    break;

                case 415:
                    painel.setPlaySubState(413);
                    break;

                case 417:
                case 418:
                    painel.setPlaySubState(416);
                    break;

                case 424:
                    painel.trocarAmbiente("floresta", 104);
                    break;

                case 600:
                    painel.trocarAmbiente("floresta", 303);
                    break;

                default:
                    painel.setPlaySubState(painel.getPlaySubState() - 1);
                    break;
            }
        }
    }

    public void actionPerformedContinuar() {
        int gameState = painel.getGameState();

        botoes.esconderBotao("Continuar");

        // FightState
        if (painel.getEventoCriatura().isEventoCriaturaAtivo()) {
            botoes.esconderBotao("Continuar");
            painel.setFightState(true);
        }

        // PlayState
        if (gameState == painel.getPlayState()) {
            botoes.esconderBotao("Continuar");

            if (!painel.getFightState()) {
                botoes.mostrarBotao("Abrir mochila");;
            }

            int subState = painel.getPlaySubState();

            switch (subState) {
                // GERAR CARDS
                case 103:
                    painel.trocarAmbiente("lago", 400);
                    break;
                case 220:
                    painel.trocarAmbiente("gruta", 500);
                    break;
                case 302:
                    painel.trocarAmbiente("montanha", 600);
                    break;

                // EVENTOS DE CRIATURA
                case 201:
                    painel.setPlaySubState(1001);
                    break;

                case 209:
                case 211:
                case 212:
                    int stateRetornar;
                    if (subState == 209) { stateRetornar = 210; }
                    else { stateRetornar = painel.getPlaySubState() + 2; }
                    painel.getAmbienteAtual().setSubStateParaRetornar(stateRetornar);

                    int probabilidade = painel.definirUmaProbabilidade();

                    int criatura;
                    if (probabilidade <= 50) {criatura = 1001; }
                    else { criatura = 1003; }
                    painel.setPlaySubState(criatura);
                    break;

                case 303:
                    painel.setPlaySubState(1002);
                    break;
                case 404:
                    painel.getAmbienteAtual().setSubStateParaRetornar(412);
                    painel.setPlaySubState(2001);
                    break;
                case 420:
                    painel.setPlaySubState(2002);
                    break;

                // FLORESTA
                case 205:
                    painel.setPlaySubState(208);
                    break;
                case 208:
                    painel.getAmbienteAtual().setSubStateParaRetornar(202);
                    if (painel.getAmbienteAtual().isBaseFogoAceso()) {
                        painel.setPlaySubState(1);
                    } else {
                        painel.setPlaySubState(1001);
                    }
                    break;


                // LAGO
                case 401:
                    painel.getAmbienteAtual().setSubStateParaRetornar(403);
                    painel.setPlaySubState(1);
                    break;
                case 405:
                    painel.setPlaySubState(408);
                    break;

                // MONTANHA
                default:
                    if (painel.getPlaySubState() < 1000) {
                        painel.setPlaySubState(painel.getPlaySubState() + 1);
                    } else {
                        System.out.println("Combate ativo");
                    }
                    break;
            }
        }

        // Outras telas
        else if (gameState == painel.getOpeningState()) {
            painel.trocarAmbiente("floresta", 0);
            painel.setGameState(painel.getPlayState());
        }
    }
}