package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;
import Sistema.*;
import UI.*;
import Evento.EventoClimatico;
import Evento.EventoCriatura;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Painel extends JPanel implements Runnable {

    // Definição da tela
    private final int originalTileSize = 16;
    private final int escala = 3;
    private int tileSize = originalTileSize * escala;

    private int larguraTela = 1300;
    private int alturaTela = 700;

    // Construtor de classes externas
    private Thread gameThread;
    private Random rand;

    // Construtor de classes internas
    private Ambiente ambienteAtual;
    private CombateSistema combateSistema;
    private CombateUI combateUi;
    private Jogador jogador = new Jogador(this);
    private Criatura criatura = new Criatura();
    private Botoes botoes = new Botoes(this);
    private UI ui = new UI(this, jogador);
    private EventoCriatura eventoCriatura = new EventoCriatura(this, ui, jogador, criatura);
    private EventoClimatico eventoClimatico = new EventoClimatico(this, ui, jogador);
    private ClimaUI clima = new ClimaUI(this, jogador, botoes, eventoClimatico);
    private InventarioSistema inventSystem = new InventarioSistema(this, jogador);
    private InventarioUI inventUi = new InventarioUI(this, jogador, botoes, inventSystem);
    private Teclado teclado = new Teclado(this, inventSystem, inventUi);

    private final int fps = 60;

    // Game States
    private int gameState;
    private final int titleState = 0;
    private final int gameOverState = 1;
    private final int openingState = 2;
    private final int playState = 3;
    private final int victoryState = 4;
    private final int tutorialControles = 5;
    private int dialogueState = 0;
    private boolean fightState = false;

    // Atributos do sistema de substate
    private Map<String, Ambiente> ambientes = new HashMap<>();
    private Set<Integer> subStatesVisitadosTemporario = new HashSet<>();


    public Painel(){
        // Estabelecimento dos dados da tela
        this.setPreferredSize(new Dimension(larguraTela, alturaTela));
        this.setBackground(Color.black);
        this.setLayout(null);

        botoes.configurarComPainel(this);
        botoes.setVisible(true);
        this.add(botoes);

        // Buffering mais preciso
        this.setDoubleBuffered(true);

        // Faz a classe priorizar o input do teclado mesmo na presença de botões
        this.addKeyListener(teclado);
        this.setFocusable(true);


        ambienteAtual = new AmbienteFloresta(this, jogador, ui);
        inicializarSistemaCombate();
        rand = new Random();
    }

    public void setupJogo() {
        gameState = titleState;
        jogador.setAtaqueAtual(jogador.getAtaqueInicial());

        this.setLayout(null);
        botoes.setBounds(0, 0, larguraTela, alturaTela);
        this.add(botoes);

        this.requestFocusInWindow();
    }

    public void iniciarGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Atualizações contínuas de mecânicas do jogo
    public void update() {
        ui.updateFrames();

        updateClima();

        ambienteAtual.definirSubStateParaRetornar();

        int numSubStatesAtualizacao = 7;
        jogador.atualizarCondicaoJogador(numSubStatesAtualizacao);

        updateBotoes();
    }

    public void updateBotoes() {
        if (gameState == titleState || !inventUi.isFechado()
                || clima.isAnalisandoClima() || ambienteAtual.isCardVisivel()) {
            botoes.setVisible(false);
        } else {
            botoes.setVisible(true);
        }
    }

    public void updateClima() {
        String climaAtual = eventoClimatico.getClima();
        switch (climaAtual) {
            case "chuva":
            case "tornado":
                if (eventoCriatura.getContadorDeEncontros() >= 5) {
                    eventoCriatura.resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;

            case "tempestade":
                if (eventoCriatura.getContadorDeEncontros() >= 6) {
                    eventoCriatura.resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;

            case "cavernoso":
                if (!jogador.getLocalizacao().equals("GRUTA DE SAL")) {
                    eventoCriatura.resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;

            case "salgado":
                if (!jogador.getLocalizacao().equals("GRUTA DE SAL")) {
                    eventoCriatura.resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                } else {
                    if (eventoCriatura.getContadorDeEncontros() >= 4) {
                        eventoCriatura.resetContador();
                        eventoClimatico.setClima("cavernoso");
                    }
                }
                break;

            case "nevasca":
                if (!jogador.getLocalizacao().equals("MONTANHA EPOPEICA")) {
                    eventoCriatura.resetContador();
                    eventoClimatico.finalizarEventoClimatico();
                }
                break;

            default:
                break;
        }

        if (jogador.getLocalizacao() != null) {
            if (jogador.getLocalizacao().equals("MONTANHA EPOPEICA")) {
                eventoClimatico.setClima("nevasca");
            }
        }
    }

    // Implementação do game loop
    public void run() {

        double intervalo = 1000000000 / fps;
        double delta = 0; long lastTime = System.nanoTime();
        long currentTime; long timer = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / intervalo;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    // Visualização (paint component)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (!fightState) {
            if (gameState == titleState || gameState == openingState || gameState == playState ||
                    gameState == gameOverState || gameState == tutorialControles || gameState == victoryState) {
                ui.mostrarInterface(g2);
                if (gameState == playState) {
                    ui.mostrarStatus(g2);
                }
            }
        } else {
            combateUi.estruturaTelaCombate(g2);
        }

        // Mostra a situacao climatica
        if (clima.isAnalisandoClima()) {
            clima.estruturaTelaDeClima(g2, ui);
        }

        // Mostra o card do ambiente atual
        if (ambienteAtual.isCardVisivel()) {
            ambienteAtual.construirCard(g2, ambienteAtual.getNomeFundoCard());
        }

        // Desenha a tela de inventário à frente do resto
        if (!inventUi.isFechado()) {
            inventUi.estruturaTelaDeInventario(g2, ui);
        }
    }

    private void inicializarSistemaCombate() {
        this.combateUi = new CombateUI(this, jogador, botoes, null);
        this.combateSistema = new CombateSistema(this, jogador, eventoCriatura, combateUi);
        this.combateUi.setCombateSistema(combateSistema);
    }

    public void trocarAmbiente(String nome, int subStateNoNovoAmbiente) {
        if (!ambientes.containsKey(nome)) {

            Ambiente novoAmbiente = switch (nome) {
                case "floresta" -> new AmbienteFloresta(this, jogador, ui);
                case "lago" -> new AmbienteLago(this, jogador, ui);
                case "gruta" -> new AmbienteGruta(this, jogador, ui);
                case "montanha" -> new AmbienteMontanha(this, jogador, ui);
                default -> throw new IllegalArgumentException("Ambiente desconhecido: " + nome);
            };

            ambientes.put(nome, novoAmbiente);
        }
        ui.setNumComando(0);
        ambienteAtual = ambientes.get(nome);
        jogador.setLocalizacao(ambienteAtual.getNome());

        if (!ambienteAtual.checarSeSubStateFoiVisitado(subStateNoNovoAmbiente)) {
            ambienteAtual.verCard();
        }
        setPlaySubState(subStateNoNovoAmbiente);
    }

    // Gerador de número aleatório entre 1 e 100 (para probabilidades)
    public int definirUmaProbabilidade() {
        int probabilidade = (rand.nextInt(100) + 1);
        return probabilidade;
    }

    // Reinicialização
    public void resetAposGameOver() {
        botoes.esconderBotao("Voltar à base");

        resetPlayState();

        combateSistema.resetarCriaturaEmCombate();

        inventSystem.esvazearInventario();

        eventoClimatico.setClima("ameno");

        jogador.resetarTodosOsAtributos();

        eventoCriatura.resetContador();
        ambienteAtual.resetarSubStatesVisitadosTotal();

        ambienteAtual.setBaseFogoAceso(false);
        ambienteAtual.setBaseFonteDeAlimento(false);
    }

    // Getters e setters
    public int getQuantidadeSubStatesVisitadosTemporario() { return subStatesVisitadosTemporario.size(); }

    public Set<Integer> getSubStatesVisitadosTemporario() { return subStatesVisitadosTemporario; }

    public void resetarSubStatesVisitadosTemporario() { subStatesVisitadosTemporario.clear(); }

    // G/S dos game states principais
    public int getGameState() { return gameState; }
    public void setGameState(int gameState) {
        this.gameState = gameState;

        botoes.setVisible(false);
        botoes.esconderBotao("Voltar ao início");
        botoes.esconderBotao("Clima");
        botoes.esconderBotao("Local");
        botoes.esconderBotao("Abrir mochila");
        botoes.esconderBotao("Voltar");
        botoes.esconderBotao("Continuar");
        botoes.esconderBotao("Voltar à base");

        // Não-play states
        if (gameState == openingState) {
            botoes.setVisible(true);
            botoes.mostrarBotao("Continuar");
        }
        if (gameState == tutorialControles) {
            botoes.setVisible(true);
            botoes.mostrarBotao("Voltar");
        }
        if (gameState == gameOverState || gameState == victoryState) {
            botoes.setVisible(true);
            botoes.esconderBotao("Clima");
            botoes.mostrarBotao("Voltar ao início");
        }

        // Play state
        if (gameState == playState) {
            botoes.setVisible(true);
            botoes.mostrarBotao("Abrir mochila");
            botoes.mostrarBotao("Clima");
            botoes.mostrarBotao("Local");
            if (ambienteAtual.checarSeSubStateFoiVisitado(1)) {
                botoes.mostrarBotao("Voltar à base");
            }
        }

        // Fight state
        if (eventoCriatura.isEventoCriaturaAtivo()) {
            botoes.esconderBotao("Voltar à base");
            botoes.esconderBotao("Abrir mochila");
            botoes.esconderBotao("Local");
            botoes.mostrarBotao("Continuar");

            if (fightState) {
                botoes.esconderBotao("Local");
                botoes.esconderBotao("Abrir mochila");
                botoes.esconderBotao("Clima");
                botoes.esconderBotao("Continuar");
            }
        }
    }

    public int getPlaySubState() {
        return (ambienteAtual != null ? ambienteAtual.getSubState() : -1);
    }
    public void setPlaySubState(int novoSubState) {
        if (ambienteAtual != null) {
            ambienteAtual.setSubState(novoSubState);
        }
    }

    public boolean getFightState() { return fightState; }
    public void setFightState(boolean fightState) { this.fightState = fightState; }
    public int getPlayState() { return playState; }
    public void resetPlayState() { setPlaySubState(0); }

    // G/S dos game states diversos
    public int getDialogueState() { return dialogueState; }
    public void setDialogueState(int dialogueState) { this.dialogueState = dialogueState; }
    public int getTitleState() { return titleState; }
    public int getTutorialControles() { return tutorialControles; }
    public int getOpeningState() { return openingState; }
    public int getGameOverState() { return gameOverState; }
    public int getVictoryState() { return victoryState; }

    // G/S das classes intermediadas pelo Painel
    public UI getUi() { return ui; }
    public CombateSistema getCombateSistema() { return combateSistema; }
    public CombateUI getCombateUi() { return combateUi; }
    public Jogador getJogador() { return jogador; }
    public Evento.EventoCriatura getEventoCriatura() { return eventoCriatura; }
    public Evento.EventoClimatico getEventoClimatico() { return eventoClimatico; }
    public InventarioSistema getInventSystem() { return inventSystem; }
    public InventarioUI getInventUI() { return inventUi; }
    public ClimaUI getClima() { return clima; }
    public Botoes getBotoes() { return botoes; }
    public Ambiente getAmbienteAtual() { return ambienteAtual; }

    // G/S das dimensões da tela
    public int getTileSize() { return tileSize; }
    public int getLargura() { return larguraTela; }
    public int getAltura() { return alturaTela; }
}