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
    private BufferedImage pintura;

    private final int vibora = 3001;
    private final int goblin = 3002;
    private final int golem = 3003;

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
        pintura = ui.setupImagens("rupestre", "zoom");
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
                if (!isEventoEspecialDefinido()) {
                    definirOcorrenciaDeEventoClimatico(g2, eventoSalgado, 5);
                    setEventoEspecialDefinido(true);
                }
                break;
            case golem:
                definirOcorrenciaDeEventoCriatura(g2, eventoGolem, 23);
                break;

            // ACAMPAMENTO/BASE
            case 1:
                botoes.esconderBotao("Voltar à base");
                ui.mostrarAcampamento();
                break;

            case 200:
                definirOcorrenciaDeEventoClimatico(g2, eventoCavernoso, 4);

                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você se encontra dentro de uma caverna.", y);
                ui.escreverTexto("Um local diferente de qualquer outro que já tenha visto,", y += tileSize);
                ui.escreverTexto("paredes e teto compostas por minerais ricos em sódio,", y += tileSize);
                ui.escreverTexto("concedendo um gradiente branco, cinza e rosado. É quase etéreo.", y += tileSize);
                ui.escreverTexto("Infelizmente, não há tempo para apreciar formações geológicas,", y += tileSize * 2);
                ui.escreverTexto("sua queda o fez tombar em um buraco que agora está", y += tileSize);
                ui.escreverTexto("alto demais para escalar de volta à floresta.", y += tileSize);
                ui.escreverTexto("Não deu para enxergar como era a figura que te empurrou...", y += tileSize);
                ui.escreverTexto("Enfim... é hora de montar uma base aqui,", y += tileSize);
                ui.escreverTexto("e avançar atrás de uma saída.", y += tileSize);
                break;

            case 201:
                botoes.mostrarBotao("Voltar à base");
                ui.escreverTexto("Como prosseguir agora?", y);

                boolean achouAgua = painel.getAmbienteAtual().checarSeSubStateFoiVisitado(205);
                boolean podeMinerar = painel.getInventSystem().acharItem("Picareta");

                String opcaoAgua = achouAgua ? "Pegar mais água" : "Achar fonte de água";
                String opcaoMinerio = podeMinerar ? "Minerar" : "Buscar minérios";

                ui.desenharOpcoes(new String[] {opcaoAgua, opcaoMinerio, "Descobrir como escapar"}, y += tileSize * 2, numComando);
                break;

            case 202:
                ui.escreverTexto("As estalactites pingam.", y);
                ui.escreverTexto("Um amontoado grande o suficiente pode prover", y += tileSize);
                ui.escreverTexto("uma quantia significativa para saciar sua sede.", y += tileSize);
                ui.escreverTexto("Dois túneis parecem ser cheios deles.", y += tileSize);

                ui.desenharOpcoes(new String[] {"Seguir pelo da esquerda", "Seguir pelo da direita"}, y += tileSize * 2, numComando);
                break;

            case 203:
                ui.escreverTexto("Apesar da abundância de sal, há outras rochas únicas.", y);
                ui.escreverTexto("Algum desses minerais pode ser útil para algo.", y += tileSize);
                ui.escreverTexto("Espere... há um trilho de mineração mais a frente.", y += tileSize);
                ui.escreverTexto("O oposto de uma saída, mas pode levar a bons minérios.", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Você segue o trilho até uma bifurcação.", y += tileSize);
                ui.escreverTexto("Havia iluminação até aqui, mas agora... o que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[] {"Seguir pelo da esquerda", "Seguir pelo da direita"}, y += tileSize * 2, numComando);
                break;

            case 204:
            case 214:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Você atinge o final do túnel...", y);
                ui.escreverTexto("Isso... ah, não! Esse lugar é um ninho de víboras!", y+= tileSize);
                ui.escreverTexto("Saia daqui antes que uma delas ataque!", y += tileSize);
                break;

            case 205:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Esse foi o caminho certo!", y);
                ui.escreverTexto("Há uma gigante estalactite suanado incessantemente.", y += tileSize);
                ui.escreverTexto("Uma pequena poça se forma debaixo dela.", y += tileSize);
                ui.escreverTexto("Você pode encher o cantil velho na sua mochila.", y += tileSize);

                if (!isRecursosColetados()) {
                    if (painel.getInventSystem().acharItem("Cantil")) {
                        painel.getInventSystem().removerItem("Cantil", 1);
                        painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                    } else {
                        painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                    }
                    setRecursosColetados(true);
                }
                break;

            case 206:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("O caminho revela... mais túneis?", y);
                ui.escreverTexto("Mas aqui, é diferente: um sítio de minerais!", y += tileSize);
                ui.escreverTexto("Podem haver riquezas. Ou, ao menos, minerais de cura.", y += tileSize);
                ui.escreverTexto("Há uma única picareta a vista. Usada, mas vai servir.", y += tileSize);
                ui.escreverTexto("Melhor voltar ao ponto inicial para marcar este caminho.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInventSystem().adicionarItem("Picareta", "combate", 1);
                    setRecursosColetados(true);
                }
                break;

            case 207:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Você enche seu cantil na poça.", y);
                ui.escreverTexto("Seria ótimo se toda a gruta fosse recursiva assim...", y += tileSize);

                if (!isRecursosColetados()) {
                    if (painel.getInventSystem().acharItem("Cantil")) {
                        painel.getInventSystem().removerItem("Cantil", 1);
                        painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                    } else {
                        painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                    }
                    setRecursosColetados(true);
                }
                break;

            case 208:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Picareta em mãos.", y);
                ui.escreverTexto("Você minera até achar algo.", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Infelizmente, essa tarefa é cansativa.", y += tileSize);
                ui.escreverTexto("Melhor recolher o que pegou, ou esgotará sua energia.", y += tileSize);

                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }

                    if (probabilidade <= 70) {
                        if (!isRecursosColetados()) {
                            if (probabilidade >= 55) {
                                painel.getInventSystem().adicionarItem("Carvão", "recurso", 1);
                            }
                            if (probabilidade >= 30) {
                                painel.getInventSystem().adicionarItem("Rocha intensa", "recurso", 1);
                            } else if (probabilidade > 5) {
                                painel.getInventSystem().adicionarItem("Rocha regenerativa", "recurso", 1);
                            } else if (probabilidade <= 5) {
                                painel.getInventSystem().adicionarItem("Carvão estranho", "recurso", 1);
                            }
                            setRecursosColetados(true);
                        }
                    }
                    if (jogador.getEnergia() < 0) {
                        jogador.setEnergia(jogador.getEnergia() - 2);
                    }
                    setChanceTirada(true);
                }
                break;

            case 209:
                ui.escreverTexto("O sistema de túneis parece ser extenso.", y);
                ui.escreverTexto("Entretanto, um deles produz mais eco.", y += tileSize);
                ui.escreverTexto("Se for o coração da gruta, é conectado a outras entradas", y += tileSize);
                ui.escreverTexto("Deseja seguir em frente de vez?", y += tileSize);

                ui.desenharOpcoes(new String[] {"Invadir coração da gruta", "Voltar e se preparar"}, y += tileSize * 2, numComando);
                break;

            case 210:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Há uma longa caminhada pela frente.", y);
                ui.escreverTexto("Restos de rochas espalhados pelo chão.", y += tileSize);
                ui.escreverTexto("O que for interessante, vai na mochila.", y += tileSize);
                ui.escreverTexto("Por ora, mais caminho...", y += tileSize);

                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }

                    if (probabilidade <= 60) {
                        if (!isRecursosColetados()) {
                            painel.getInventSystem().adicionarItem("Rocha regenerativa", "recurso", 1);

                            boolean carvaoEstranho = probabilidade <= 5;

                            if (carvaoEstranho) {
                                painel.getInventSystem().adicionarItem("Carvão estranho", "recurso", 1);
                            }
                            setRecursosColetados(true);
                        }
                        setChanceTirada(true);
                    }
                }
                break;

            case 211:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Sair desse lugar o quanto antes é imperativo.", y);
                ui.escreverTexto("Parece que as criaturas daqui são, de alguma forma,", y += tileSize);
                ui.escreverTexto("mais hostis em relação a sua presença.", y += tileSize);
                ui.escreverTexto("Caminhando mais, você avista uma pintura nas paredes...", y += tileSize * 2);
                break;

            case 212:
                definirTelaDeBotao("continuar");
                desenharImagemZoom(g2, pintura);
                ui.escreverTexto("...um tanto descritivo.", painel.getAltura() - tileSize);
                break;

            case 213:
                botoes.esconderBotao("Voltar à base");

                ui.escreverTexto("São voltas e voltas aqui dentro.", y);
                ui.escreverTexto("Outra bifurcação rege a frente.", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize * 2);

                ui.desenharOpcoes(new String[] {"Pegar caminho da esquerda", "Pegar caminho da direita"}, y += tileSize * 2, numComando);
                break;

            case 215:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Os ecos ficam cada vez mais fortes...", y);
                ui.escreverTexto("A estrutura da caverna se afunila.", y += tileSize);
                ui.escreverTexto("Você chega a um ponto que só oferece um rumo:", y += tileSize);
                ui.escreverTexto("uma grande descida escorregadia.", y += tileSize);
                ui.escreverTexto("O coração da caverna deve estar próximo.", y += tileSize);
                break;

            case 216:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você desliza rapidamente pela rampa...", y);
                g2.setColor(Color.red);
                ui.escreverTexto("*PUFF*", y += tileSize);
                g2.setColor(Color.white);
                ui.escreverTexto("A queda dói. Mas, bem, você já teve piores...", y += tileSize);
                ui.escreverTexto("Aqui é tão barulhento quanto a floresta.", y += tileSize * 2);
                ui.escreverTexto("Mais a frente, você avista... goblins!", y += tileSize);
                ui.escreverTexto("Eles estão guardando uma montanha de pedras preciosas.", y += tileSize);
                ui.escreverTexto("O local é uma enorme câmara, cujo tesouro é radiante.", y += tileSize);
                break;

            case 217:
                ui.escreverTexto("Uma das pedras, uma vermelha...", y );
                ui.escreverTexto("Brilha mais que todo o resto.", y += tileSize);
                if (painel.getInventSystem().acharItem("Jóia azul")) {
                    ui.escreverTexto("Parece com aquela em sua bolsa...", y += tileSize);
                }
                ui.escreverTexto("E, no fundo da câmara, o que você queria: uma enorme subida.", y += tileSize * 2);
                ui.escreverTexto("Se há uma saída, só pode estar no fim daquele túnel.", y += tileSize);
                ui.escreverTexto("Sua liberdade está do outro lado dessa câmara cheia de monstros.", y += tileSize);
                ui.escreverTexto("Qual é o próximo passo?", y += tileSize);

                ui.desenharOpcoes(new String[] {"Se esgueirar lentamente", "Mostrar para os goblins quem é " + jogador.getNome()}, y+=tileSize*2, numComando);
                break;

            case 218:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Lentamente, você segue", y);
                ui.escreverTexto("Passando por vãos, se escondendo atrás de pilhas rochosas.", y += tileSize);
                ui.escreverTexto("Há umas rochas de cura no chão, então você coleta.", y += tileSize);
                ui.escreverTexto("O útil supera o fútil, as riquezas ficam para trás.", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Você atinge o fim da caverna", y += tileSize);

                if (!isChanceTirada()) {
                    if (!isRecursosColetados()) {
                        painel.getInventSystem().adicionarItem("Rocha regenerativa", "recurso", 1);

                        double probabilidade = painel.definirUmaProbabilidade();
                        if (jogador.getHabilidade().equals("RASTREADORA")) {
                            probabilidade = probabilidade * 0.9;
                        }
                        boolean carvaoEstranho = probabilidade <= 5;

                        if (carvaoEstranho) {
                            painel.getInventSystem().adicionarItem("Carvão estranho", "recurso", 1);
                        }
                        setRecursosColetados(true);
                    }
                    setChanceTirada(true);
                }
                break;

            case 219:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Sua coragem é admirável.", y);
                ui.escreverTexto("Arma em mãos, você avança contra a horda de goblins.", y += tileSize);
                break;

            case 220:
                botoes.esconderBotao("Abrir mochila");
                botoes.esconderBotao("Voltar à base");
                Composite composite = g2.getComposite();
                if (!isTransicaoFinalizada()) {
                    transicaoDeTela(g2);

                    ui.escreverTexto("No pé do túnel de saída, você começa a apressar o passo...", y);
                    ui.escreverTexto("Entretanto...", y += tileSize);
                    g2.setColor(Color.red);
                    ui.escreverTexto(jogador.getNome().toUpperCase(), y += tileSize * 2);
                    g2.setColor(Color.white);
                    ui.escreverTexto("Aquela jóia vermelha grita seu nome com todas as letras", y += tileSize * 2);
                    ui.escreverTexto("Você se vira. Ela está encostada em um pilar.", y += tileSize);
                    ui.escreverTexto("Todos os goblins se afastaram após o grito.", y += tileSize);
                    ui.escreverTexto("...O que isso significa?", y += tileSize);

                    ui.desenharOpcoes(new String[]{"Pegar jóia"},y += tileSize * 2, numComando);
                }
                g2.setComposite(composite);

                if (isTransicaoFinalizada()) {
                    painel.setPlaySubState(224);
                }
                break;

            case 221:
                definirTelaDeBotao("continuar");

                if (!isRecursosColetados()) {
                    int novaVida = (jogador.getVida() + jogador.getVidaMax()/2);
                    if (novaVida <= jogador.getVidaMax()) {
                        jogador.setVida(novaVida);
                    } else {
                        jogador.setVida(painel.getJogador().getVidaMax());
                    }
                    setRecursosColetados(true);
                }

                ui.escreverTexto("O trabalho não acabou!", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("Seu sangue borbulha pela batalha!", painel.getAltura()/2);
                break;

            case 223:
                definirTelaDeBotao("continuar");

                if (!isRecursosColetados()) {
                    int novaVida = (jogador.getVida() + jogador.getVidaMax()/2);
                    if (novaVida <= jogador.getVidaMax()) {
                        jogador.setVida(novaVida);
                    } else {
                        jogador.setVida(painel.getJogador().getVidaMax());
                    }
                    setRecursosColetados(true);
                }

                ui.escreverTexto("Avante!", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("A saída está logo à frente!", painel.getAltura()/2);
                break;

            case 224:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Se aproximando do pilar, rapidamente, você se abaixa", painel.getAltura()/2 - tileSize * 2);
                ui.escreverTexto("para pegar a jóia e dar o fora da câmara.", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("...", painel.getAltura()/2);
                ui.escreverTexto("Seus arredores tremem... Meu Deus.", painel.getAltura()/2 + tileSize);
                ui.escreverTexto("Não é um pilar...", painel.getAltura()/2 + tileSize * 2);
                break;

            case 225:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("O titã se despedaça devagar.", y);
                ui.escreverTexto("Os goblins entram em pânico.", y += tileSize);
                ui.escreverTexto("O chão começa a tremer.", y += tileSize);
                ui.escreverTexto("Parece que a existência desse guardião estava", y += tileSize * 2);
                ui.escreverTexto("conectada à estabilidade da caverna.", y += tileSize);
                ui.escreverTexto("Tudo está em ruínas.", y += tileSize);
                ui.escreverTexto("Você agarra aquela jóia e parte para o túnel.", y += tileSize);

                if (!isRecursosColetados()) {
                    jogador.setVidaMax(jogador.getVidaMax() + 10);
                    jogador.setVida(jogador.getVidaMax());
                    painel.getInventSystem().adicionarItem("Jóia vermelha", "recurso", 1);
                    setRecursosColetados(true);
                }
                break;

            case 226:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você corre, corre, corre...", y);
                ui.escreverTexto("A gruta e tudo que há nela está desmoronando.", y += tileSize);
                ui.escreverTexto("Toda essa destruição... será que foi necessária?", y += tileSize);
                ui.escreverTexto("A luz ao final fica cada vez mais tangível...", y += tileSize * 2);
                ui.escreverTexto("Até que aquele céu medonho finalmente se revela.", y += tileSize);
                ui.escreverTexto("A floresta.", y += tileSize);
                ui.escreverTexto("Apressando o passo, você abandona a gruta e suas criaturas...", y += tileSize);
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}
