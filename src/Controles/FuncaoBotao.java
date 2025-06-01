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
                case 16:
                    painel.setPlaySubState(15);
                    break;

                case 4:
                    painel.setPlaySubState(1004);
                    break;

                case 10:
                    painel.setPlaySubState(12);
                    break;

                case 17:
                    painel.setPlaySubState(1001);
                    painel.getAmbienteAtual().setSubStateParaRetornar(15);
                    break;

                case 105:
                    painel.setPlaySubState(2001);
                    painel.getAmbienteAtual().setSubStateParaRetornar(102);
                    break;
                case 108:
                    painel.setPlaySubState(2001);
                    painel.getAmbienteAtual().setSubStateParaRetornar(109);
                    break;

                case 113:
                    painel.setPlaySubState(111);
                    break;

                case 115:
                case 116:
                    painel.setPlaySubState(114);
                    break;

                case 122:
                    painel.trocarAmbiente("floresta", 12);
                    break;

                case 300:
                    painel.trocarAmbiente("floresta", 36);
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
                case 11:
                    painel.trocarAmbiente("lago", 100);
                    break;
                case 31:
                    painel.trocarAmbiente("gruta", 200);
                    break;
                case 35:
                    painel.trocarAmbiente("montanha", 300);
                    break;

                // EVENTOS DE CRIATURA
                case 14:
                    painel.setPlaySubState(1001);
                    break;

                case 20:
                case 22:
                case 23:
                    int stateRetornar;
                    if (subState == 20) { stateRetornar = 21; }
                    else { stateRetornar = painel.getPlaySubState() + 2; }
                    painel.getAmbienteAtual().setSubStateParaRetornar(stateRetornar);

                    int probabilidade = painel.definirUmaProbabilidade();

                    int criatura;
                    if (probabilidade <= 50) {criatura = 1001; }
                    else { criatura = 1003; }
                    painel.setPlaySubState(criatura);
                    break;

                case 36:
                    painel.setPlaySubState(1002);
                    break;
                case 103:
                    painel.getAmbienteAtual().setSubStateParaRetornar(110);
                    painel.setPlaySubState(2001);
                    break;
                case 118:
                    painel.setPlaySubState(2002);
                    break;

                // FLORESTA
                case 18:
                    painel.setPlaySubState(19);
                    break;
                case 19:
                    painel.getAmbienteAtual().setSubStateParaRetornar(15);
                    if (painel.getAmbienteAtual().isBaseFogoAceso()) {
                        painel.setPlaySubState(1);
                    } else {
                        painel.setPlaySubState(1001);
                    }
                    break;

                // LAGO
                case 101:
                    painel.getAmbienteAtual().setSubStateParaRetornar(102);
                    painel.setPlaySubState(1);
                    break;
                case 104:
                    painel.setPlaySubState(106);
                    break;

                // GRUTA
                case 200:
                    painel.getAmbienteAtual().setSubStateParaRetornar(201);
                    painel.getAmbienteAtual().setBaseFogoAceso(false);
                    painel.getAmbienteAtual().setBaseFonteDeAlimento(false);
                    painel.setPlaySubState(1);
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