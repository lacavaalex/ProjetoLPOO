package Ambiente;

import Entidade.*;
import Controles.*;
import Main.Painel;
import UI.*;
import Evento.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AmbienteFloresta extends Ambiente {

    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;
    private Criatura criatura;

    private EventoCriatura eventoVibora;
    private EventoCriatura eventoUrso;
    private EventoClimatico eventoChuva;

    private BufferedImage fundoFloresta, fundoFloresta2;

    private final int vibora = 1001;
    private final int urso = 1002;

    public AmbienteFloresta(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();
        this.criatura = new Criatura();

        this.eventoVibora = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoUrso = new EventoCriatura(painel, ui, jogador, criatura);

        this.eventoChuva = painel.getEventoClimatico();

        descreverAmbiente();
        fundoFloresta = ui.setupImagens("floresta_macabra", "background");
        fundoFloresta2 = ui.setupImagens("floresta_macabra-2", "background");
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("FLORESTA MACABRA");
        this.setDescricao("Escura, densa, barulhenta.");
        this.setDificuldade("medianas.");
        this.setRecursos("frutas, água, madeira, pedras.");
        this.setFrequenciaEventos("muitas criaturas, esconderijos, riscos à saúde.");
        this.setClimaAmbiente("levemente frio.");
        this.setNomeFundoCombate("floresta_macabra_combate");
    }

    @Override
    public void construirCard(Graphics2D g2) {
        if (isCardVisivel()) {

            int tileSize = painel.getTileSize();
            int y = tileSize * 3;

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

            ui.desenharPlanoDeFundo(fundoFloresta);

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

        g2.fillRect(0, 0, larguraTela, alturaTela);
        ui.desenharPlanoDeFundo(fundoFloresta2);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize * 2;

        switch (subState) {
            // STATES DE EVENTO
            case vibora:
                definirOcorrenciaDeEventoCriatura(g2, eventoVibora, 11);
                break;
            case urso:
                definirOcorrenciaDeEventoCriatura(g2, eventoUrso, 12);
                break;

            // PONTO INICIAL
            case 0:
                ui.escreverTexto("A luz misteriosa brilha à distância, floresta adentro.", y);
                ui.escreverTexto("Espalhadas pelo chão, há coisas que parecem ser úteis.", y += tileSize);
                ui.escreverTexto("Uma brecha por entre as árvores revela uma montanha próxima.", y += tileSize);
                ui.escreverTexto("Altitude pode ser uma vantagem.", y += tileSize);
                ui.escreverTexto("Um chiado estranho parece se aproximar...", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Seguir luz", "Ficar e coletar recursos", "Ir até montanha"}, y += tileSize * 2, numComando);
                break;

            // ACAMPAMENTO/BASE
            case 1:
                botoes.esconderBotao("Voltar à base");
                ui.mostrarAcampamento();
                break;

            // BRANCH DA LUZ
            case 100:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você deixa a luz te guiar...", y);
                ui.escreverTexto(". . .", y += tileSize);
                ui.escreverTexto("Encontrou no caminho: 2 pedras.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Pedra", "recurso", 2);
                    setRecursosColetados(true);
                }
                definirOcorrenciaDeEventoClimatico(g2, eventoChuva, 1);
                break;

            case 101:
                ui.escreverTexto("Arbustos chacoalham ao seu redor enquanto anda.", y);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Você está perto o suficiente da luz... é uma fogueira.", y += tileSize);
                ui.escreverTexto("Nínguem a vista, mas há um cantil com água e um pouco de carne...", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);
                ui.escreverTexto("", y += tileSize);

                ui.desenharOpcoes(new String[]{"Saquear acampamento", "Explorar arredores"}, y += tileSize * 2, numComando);
                break;

            case 102:
                definirTelaDeBotao("voltar");

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Cantil", "consumo", 1);
                    painel.getInvent().adicionarItem("Carne", "consumo", 2);
                    painel.getInvent().adicionarItem("Carvão", "recurso", 1);
                    setRecursosColetados(true);
                }

                ui.escreverTexto("Você pega o cantil, a carne, e um resto de carvão.", y);
                break;

            case 103:
               definirTelaDeBotao("continuar");

                ui.escreverTexto("Melhor não mexer com o que não é seu.", y);
                ui.escreverTexto("(e, afinal, quem sabe de quem pode ser...)", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("Em torno dessa fogueira há vegetação baixa.", y += tileSize);
                ui.escreverTexto("Há um cervo à distância, mas você não tem equipamento para caça.", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("Mais adiante, o barulho animador aumenta: água corrente!", y += tileSize);
                ui.escreverTexto("Apressando o passo, em minutos você chega ao lago.", y += tileSize);
                break;

            case 104:
                ui.escreverTexto("Você retorna a atenção à fogueira.", y);
                ui.escreverTexto("O fogo parece estar muito mais fraco desde que você chegou...", y +=tileSize);
                ui.escreverTexto("Você ouve um barulho de água à distância. Pode ser um lago.", y += tileSize);

                ui.desenharOpcoes(new String[]{"Ir ao lago"}, y += tileSize, numComando);
                break;

            // BRANCH DA VÍBORA
            case 200:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você busca por recursos.", y);
                ui.escreverTexto("Encontrou: 7 madeiras, 2 pedras, 1 galho pontiagudo.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Madeira", "recurso", 7);
                    painel.getInvent().adicionarItem("Pedra", "recurso", 2);
                    painel.getInvent().adicionarItem("Galho pontiagudo", "combate", 1);
                    setRecursosColetados(true);
                }
                break;

            case 201:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Aquele chiado... parece estar tão perto...", y);
                break;

            case 202:
                ui.escreverTexto("Já é hora de pensar no que fazer para se sustentar nessa mata.", y);
                ui.escreverTexto("Você já tem madeira para fogueira, mas é importante ter", y += tileSize);
                ui.escreverTexto("remédios e alguma arma melhor para caça.", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Buscar frutas e medicação natural", "Buscar por arma de caça", "Montar fogueira"}, y += tileSize * 2, numComando);
                break;

            case 203:
                //frutas e mediccao
                break;

            case 204:
                ui.escreverTexto("Você procura por algo melhor para caça.", y);
                ui.escreverTexto("...", y += tileSize);
                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Madeira", "recurso", 2);
                    setRecursosColetados(true);
                }
                break;

            case 205:
                botoes.mostrarBotao("Continuar");

                ui.escreverTexto("(Você usa as madeiras que encontrou para montar uma fogueira.)", y);
                ui.escreverTexto("Agora, hora de tentar fazer fogo.", y += tileSize);
                ui.escreverTexto("Você tenta gerar fricção com um graveto", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                break;

            case 208:
                botoes.mostrarBotao("Continuar");

                if (!isChanceTirada()) {
                    int probabilidade = painel.definirUmaProbabilidade();
                    boolean fogoAceso = probabilidade <= 50 || jogador.getHabilidade().equals("SOBREVIVENCIAL");
                    setBaseFogoAceso(fogoAceso);
                    setChanceTirada(true);
                }

                if (isBaseFogoAceso()) {
                    ui.escreverTexto("Poxa, deu certo! Uma fonte de calor!", y += tileSize);
                    if (!isRecursosGastos()) {
                        painel.getInvent().removerItem("Madeira", 2);
                        setRecursosGastos(true);
                    }
                    ui.escreverTexto("Dificilmente alguma criatura tentará te atacar aqui.", y += tileSize);
                    ui.escreverTexto("É uma boa ideia tornar esse lugar sua base.", y += tileSize);
                }
                else {
                    ui.escreverTexto("Isso está demorando...", y += tileSize);
                    ui.escreverTexto("Só mais um pouco, não é seguro ficar parado...", y += tileSize);
                }
                break;

            case 211:
                ui.escreverTexto("Você já tem uma fogueira, mas precisa de", y += tileSize);
                ui.escreverTexto("remédio para o veneno e alguma arma melhor para caça.", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Buscar frutas e medicação natural", "Buscar por arma de caça"}, y += tileSize * 2, numComando);
                break;

            // BRANCH DA MONTANHA
            case 300:
               definirTelaDeBotao("continuar");

                ui.escreverTexto("Você se direciona até a montanha", y);
                ui.escreverTexto(". . .", y += tileSize);
                ui.escreverTexto("Algo não parce certo.", y += tileSize);
                ui.escreverTexto("Um movimento suspeito te acompanha.", y += tileSize);
                ui.escreverTexto("Nada se revela, mas você não está sozinho.", y += tileSize);
                ui.escreverTexto(". . .", y += tileSize);
                break;

            case 301:
                ui.escreverTexto("Atingido o pé da montanha, você enxerga um trecho de subida.", y);
                ui.escreverTexto("O que quer que estava te seguindo aparenta ter parado...", y += tileSize);
                ui.escreverTexto("Não há nada de destaque neste local, além de uma rocha firme.", y += tileSize);
                ui.escreverTexto("Sua formação pode conceder um abrigo com boa visibilidade.", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);
                ui.escreverTexto("", y += tileSize);

                ui.desenharOpcoes(new String[]{"Subir pelo trecho", "Descansar até o amanhecer"}, y += tileSize * 2, numComando);
                break;

            case 302:
               definirTelaDeBotao("continuar");

                ui.escreverTexto("Você sobe o trecho semi-íngreme.", y);
                ui.escreverTexto("Olhando pra trás, há um olhar a espreita.", y += tileSize);
                ui.escreverTexto("(Ainda bem que não fiquei).", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("Você chega num paredão.", y += tileSize);
                break;

            case 303:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você se dirige àquela rocha para um abrigo temporário.", y);
                ui.escreverTexto("Vai ter que servir. Você já se afastou demais da clareira.", y += tileSize);
                ui.escreverTexto("A energia aqui não é ideal, mas não parece ter mais nada à espreita", y += tileSize);
                ui.escreverTexto("De olhos entreabertos, você deita para descansar...", y += tileSize);
                break;

            case 304:
                ui.escreverTexto("Você descansa os olhos...", y);
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}
