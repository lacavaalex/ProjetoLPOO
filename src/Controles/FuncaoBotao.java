package Controles;

import Main.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FuncaoBotao implements ActionListener {

    private Painel painel;
    private Botoes botoes;

    private final int viboraRubro = 1001;
    private final int urso = 1002;
    private final int lobo = 1003;
    private final int corvo = 1004;
    private final int caranguejo = 2001;
    private final int cruel = 2002;
    private final int viboraMineral = 3001;
    private final int goblin = 3002;
    private final int golem = 3003;
    private final int leopardo = 4001;
    private final int funesto = 4002;

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
            painel.resetAposGameOver();
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
                // FLORESTA
                case 3:
                case 16:
                case 17:
                    painel.setPlaySubState(painel.getAmbienteAtual().getSubStateOrigem());
                    break;

                case 4:
                case 7:
                    painel.setPlaySubState(corvo);
                    if (subState == 7) {
                        painel.getAmbienteAtual().setSubStateParaRetornar(41);
                    }
                    break;

                case 10:
                    painel.setPlaySubState(12);
                    break;

                case 45:
                    painel.setPlaySubState(viboraRubro);
                    painel.getAmbienteAtual().setSubStateParaRetornar(44);
                    break;

                // LAGO
                case 105:
                case 108:
                    painel.setPlaySubState(caranguejo);
                    if (subState == 105) {
                        painel.getAmbienteAtual().setSubStateParaRetornar(102);
                    }
                    break;

                case 113:
                    painel.setPlaySubState(111);
                    break;

                case 116:
                    painel.setPlaySubState(114);
                    break;

                case 122:
                    painel.trocarAmbiente("floresta", 12);
                    break;

                // GRUTA
                case 204:
                case 205:
                case 206:
                case 207:
                case 208:
                    if (subState == 204) {
                        painel.setPlaySubState(viboraMineral);
                        painel.getAmbienteAtual().setSubStateParaRetornar(201);
                    } else { painel.setPlaySubState(201); }
                    break;

                case 214:
                    painel.setPlaySubState(viboraMineral);
                    painel.getAmbienteAtual().setSubStateParaRetornar(213);
                    break;

                // MONTANHA
                case 300:
                    boolean derrotouBossLago = painel.getInventSystem().acharItem("Jóia azul");
                    boolean derrotouBossCaverna = painel.getInventSystem().acharItem("Jóia vermelha");
                    boolean derrotouBossFloresta = painel.getInventSystem().acharItem("Espada Insigne");

                    if (derrotouBossCaverna && derrotouBossLago && derrotouBossFloresta) {
                        painel.setPlaySubState(301);
                    }
                    else {
                        painel.trocarAmbiente("floresta", 36);
                    }
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
                case 226:
                    painel.trocarAmbiente("floresta", 32);
                    break;

                // FLORESTA
                case 14:
                case 33:
                    painel.setPlaySubState(viboraRubro);
                    break;

                case 19:
                    painel.getAmbienteAtual().setSubStateParaRetornar(15);
                    if (painel.getAmbienteAtual().isBaseFogoAceso()) {
                        painel.setPlaySubState(1);
                    } else {
                        painel.setPlaySubState(viboraRubro);
                    }
                    break;

                case 20:
                case 22:
                case 23:
                case 42:
                case 43:
                    int stateRetornar;
                    if (subState == 20) { stateRetornar = 21; }
                    else { stateRetornar = painel.getPlaySubState() + 2; }
                    painel.getAmbienteAtual().setSubStateParaRetornar(stateRetornar);

                    int probabilidade = painel.definirUmaProbabilidade();

                    int criatura;
                    if (probabilidade <= 50) {criatura = viboraRubro; }
                    else { criatura = lobo; }
                    painel.setPlaySubState(criatura);
                    break;

                case 7:
                case 39:
                case 40:
                    if (subState == 7) {
                        painel.getAmbienteAtual().setSubStateParaRetornar(41);
                    }
                    painel.setPlaySubState(corvo);
                    break;

                case 49:
                    painel.setDialogueState(painel.getDialogueState() + 1);

                    if (painel.getDialogueState() == 10) {
                        painel.setPlaySubState(urso);
                        painel.setDialogueState(0);
                    }
                    break;

                case 32:
                case 52:
                    painel.setPlaySubState(2);
                    break;

                case 37:
                    painel.getAmbienteAtual().setSubStateParaRetornar(38);
                    painel.setPlaySubState(1);
                    break;

                // LAGO
                case 101:
                    painel.getAmbienteAtual().setSubStateParaRetornar(102);
                    painel.setPlaySubState(1);
                    break;

                case 103:
                    painel.getAmbienteAtual().setSubStateParaRetornar(110);
                    painel.setPlaySubState(caranguejo);
                    break;

                case 104:
                    painel.setPlaySubState(106);
                    break;

                case 118:
                    painel.setPlaySubState(cruel);
                    break;

                // GRUTA
                case 200:
                    painel.getAmbienteAtual().setSubStateParaRetornar(201);
                    painel.getAmbienteAtual().setBaseFogoAceso(false);
                    painel.getAmbienteAtual().setBaseFonteDeAlimento(false);
                    painel.setPlaySubState(1);
                    break;

                case 210:
                    painel.getAmbienteAtual().setSubStateParaRetornar(211);

                    int probabilidade2 = painel.definirUmaProbabilidade();
                    int criaturaGruta;

                    if (probabilidade2 <= 50) {criaturaGruta = viboraMineral; }
                    else { criaturaGruta = goblin; }
                    painel.setPlaySubState(criaturaGruta);
                    break;

                case 212:
                case 225:
                    painel.setPlaySubState(goblin);
                    break;

                case 218:
                case 219:
                case 221:
                case 223:
                    painel.setPlaySubState(goblin);
                    if (subState == 223) {
                        painel.getAmbienteAtual().setSubStateParaRetornar(220);
                    }
                    else {
                        painel.getAmbienteAtual().setSubStateParaRetornar(subState + 2);
                    }
                    break;

                case 224:
                    painel.setPlaySubState(golem);
                    break;

                // MONTANHA
                case 305:
                case 309:
                case 310:
                case 311:
                    painel.setPlaySubState(leopardo);
                    break;

                case 308:
                case 312:
                    painel.setPlaySubState(leopardo);
                    painel.getAmbienteAtual().setSubStateParaRetornar(313);
                    break;

                case 317:
                    painel.setDialogueState(painel.getDialogueState() + 1);

                    if (painel.getDialogueState() == 28) {
                        painel.setPlaySubState(318);
                        painel.setDialogueState(0);
                    }
                    break;

                case 319:
                    painel.setDialogueState(painel.getDialogueState() + 1);

                    if (painel.getDialogueState() == 10) {
                        painel.setGameState(painel.getGameOverState());
                        painel.setDialogueState(0);
                    }
                    break;

                case 320:
                    painel.setDialogueState(painel.getDialogueState() + 1);

                    if (painel.getDialogueState() == 6) {
                        painel.setPlaySubState(funesto);
                        painel.setDialogueState(0);
                    }
                    break;

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