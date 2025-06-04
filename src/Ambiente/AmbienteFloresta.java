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
    private EventoCriatura eventoLobo;
    private EventoCriatura eventoCorvo;

    private EventoClimatico eventoChuva;
    private EventoClimatico eventoTempestade;
    private EventoClimatico eventoTornado;

    private BufferedImage fundoFloresta2;
    private BufferedImage figura;
    private BufferedImage ursoFace1;

    private final int vibora = 1001;
    private final int urso = 1002;
    private final int lobo = 1003;
    private final int corvo = 1004;

    public AmbienteFloresta(Painel painel, Jogador jogador, UI ui) {
        super(painel);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();
        this.criatura = new Criatura();

        this.eventoVibora = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoUrso = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoLobo = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoCorvo = new EventoCriatura(painel, ui, jogador, criatura);

        this.eventoChuva = painel.getEventoClimatico();
        this.eventoTempestade = painel.getEventoClimatico();
        this.eventoTornado = painel.getEventoClimatico();

        descreverAmbiente();
        fundoFloresta2 = ui.setupImagens("floresta_macabra-2", "background");
        figura = ui.setupImagens("figura_misteriosa", "zoom");
        ursoFace1 = ui.setupImagens("urso_face-1", "animacao");
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("FLORESTA MACABRA");
        this.setDescricao("Escura, densa, barulhenta.");
        this.setDificuldade("medianas.");
        this.setRecursos("frutas, água, madeira, pedras.");
        this.setFrequenciaEventos("muitas criaturas, esconderijos, riscos à saúde.");
        this.setClimaAmbiente("levemente frio.");
        this.setNomeFundoCard("floresta_macabra");
        this.setNomeFundoCombate("floresta_macabra_combate");
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

        boolean podeCurar = painel.getInventSystem().acharItem("Planta medicinal");
        boolean podeCacar = painel.getInventSystem().acharItem("Machado") || painel.getInventSystem().acharItem("Faca");

        switch (subState) {
            // STATES DE EVENTO
            case vibora:
                definirOcorrenciaDeEventoCriatura(g2, eventoVibora, 1);
                break;
            case urso:
                definirOcorrenciaDeEventoCriatura(g2, eventoUrso, 2);
                break;
            case lobo:
                definirOcorrenciaDeEventoCriatura(g2, eventoLobo, 3);
                break;
            case corvo:
                definirOcorrenciaDeEventoCriatura(g2, eventoCorvo, 4);
                break;

            // PONTO INICIAL
            case 0:
                ui.escreverTexto("A luz misteriosa brilha à distância, floresta adentro.", y);
                ui.escreverTexto("No chão ao seu redor, há coisas que parecem ser úteis.", y += tileSize);
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

            case 2:
                iniciarEspera();

                ui.escreverTexto("O que fazer?", y += tileSize);

                boolean derrotouBossLago = painel.getInventSystem().acharItem("Jóia azul");
                boolean derrotouBossCaverna = painel.getInventSystem().acharItem("Jóia vermelha");

                String opcaoLago = derrotouBossLago ? "Ir ao lago" : "Seguir luz";
                String opcaoRecursos = derrotouBossCaverna ? "Caçar" : "Coletar recursos";

                ui.desenharOpcoes(new String[]{opcaoLago, opcaoRecursos, "Ir até montanha"}, y += tileSize * 2, numComando);

                break;

            case 3:
                definirTelaDeBotao("voltar");
                ui.escreverTexto("O ar sopra uivos e arrastar de folhas.", y);
                ui.escreverTexto("Há luzes distantes que somem e reacendem,", y += tileSize);
                ui.escreverTexto("fagulhas de movimentos por toda parte.", y += tileSize);
                ui.escreverTexto("A sensação de se sentir observado é sufocante,", y += tileSize);
                ui.escreverTexto("mas, por esse olhos, você é apenas um intruso,", y += tileSize);
                ui.escreverTexto("ou talvez a próxima refeição?", y += tileSize);
                ui.escreverTexto("Essa floresta macabra não é como qualquer outra mata que já pisou.", y += tileSize);
                break;

            // CAÇA
            case 4:
                definirTelaDeBotao("voltar");

                if (isAguardando()) {
                    g2.setColor(Color.red);
                    ui.escreverTexto("CAÇANDO", y);
                    g2.setColor(Color.white);
                    ui.escreverTexto("Não há muitas criaturas para caçar aqui.", y += tileSize);
                    ui.escreverTexto("Quer dizer, não muitas que esteja disposto a comer...", y += tileSize);
                    ui.escreverTexto("Ainda assim, é crucial ter alimento.", y += tileSize);
                    ui.escreverTexto("A qualidade de sua arma equipada influencia na caça.", y += tileSize * 2);
                    ui.escreverTexto("Quanto mais forte, melhor suas chances de pegar uma boa presa.", y += tileSize);
                    ui.escreverTexto("Aguarde...", y += tileSize);

                    setContadorTimer(getContadorTimer() + 1);
                    if (getContadorTimer() >= getTempoDeEsperaEmFPS() / 2) {
                        setAguardando(false);

                        if (!isChanceTirada()) {
                            double probabilidade = painel.definirUmaProbabilidade();
                            if (jogador.getHabilidade().equals("RASTREADORA")) {
                                probabilidade = probabilidade * 0.9;
                            }
                            boolean cacaBemSucedida = probabilidade <= 60;
                            probabilidade = probabilidade - jogador.getAtaqueAtual();

                            if (cacaBemSucedida) {
                                if (!isRecursosColetados()) {
                                    if (probabilidade <= 10) {
                                        painel.getInventSystem().adicionarItem("Carne suculenta", "consumo", 1);
                                    } else {
                                        painel.getInventSystem().adicionarItem("Carne magra", "consumo", 1);
                                    }
                                    setRecursosColetados(true);
                                }
                            }
                            setChanceTirada(true);
                        }
                    }
                } else {
                    ui.escreverTexto("Pronto, vamos ver se deu certo...", y += tileSize * 2);
                }

                break;

            case 5:
                botoes.mostrarBotao("Voltar à base");

                iniciarEspera();

                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Caçar", "Adentrar floresta", "Voltar à área desmatada"}, y += tileSize * 2, numComando);
                break;


            case 6:
                iniciarEspera();

                ui.escreverTexto("O que fazer?", y += tileSize);

                derrotouBossLago = painel.getInventSystem().acharItem("Jóia azul");
                derrotouBossCaverna = painel.getInventSystem().acharItem("Jóia vermelha");

                String opcaoLago2 = derrotouBossLago ? "Ir ao lago" : "Seguir luz";
                String opcaoRecursos2 = derrotouBossCaverna ? "Caçar" : "Coletar recursos";

                ui.desenharOpcoes(new String[]{opcaoLago2, opcaoRecursos2, "Ir para a grande árvore"}, y += tileSize * 2, numComando);
                break;

            case 7:
                definirTelaDeBotao("voltar");

                if (painel.getInventSystem().acharItem("Pedra")) {
                    ui.escreverTexto("Você atira uma pedra num pássaro voando próximo.", y += tileSize * 2);
                    if (!isRecursosGastos()) {
                        painel.getInventSystem().removerItem("Pedra", 1);
                        setRecursosGastos(true);
                    }
                    ui.escreverTexto("Parece que ele deixou cair algo...", y += tileSize);
                    ui.escreverTexto("Você buscou e colocou na mochila.", y += tileSize);

                    if (!isRecursosColetados()) {
                        painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                        painel.getInventSystem().adicionarItem("Carne suculenta", "consumo", 1);

                        int probabilidade = painel.definirUmaProbabilidade();
                        if (probabilidade <= 5) {
                            painel.getInventSystem().adicionarItem("Carvão estranho", "recurso", 1);
                        }
                        else if (probabilidade <= 15) {
                            painel.getInventSystem().adicionarItem("Rocha regenerativa", "recurso", 1);
                        }
                        else if (probabilidade <= 50) {
                            painel.getInventSystem().adicionarItem("Fruta", "consumo", 1);
                        }
                        else if (probabilidade <= 100) {
                            painel.getInventSystem().adicionarItem("Pedra", "recurso", 1);
                        }
                        setRecursosColetados(true);
                    }
                }
                else {
                    ui.escreverTexto("Você não tem pedras para jogar...", y += tileSize * 2);
                }
                break;

            // BRANCH DA LUZ
            case 8:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você deixa a luz te guiar...", y);

                definirOcorrenciaDeEventoClimatico(g2, eventoChuva, 1);
                break;

            case 9:
                ui.escreverTexto("Arbustos chacoalham ao seu redor enquanto anda.", y);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Você está perto o suficiente da luz... é uma fogueira.", y += tileSize);
                ui.escreverTexto("Nínguem a vista, mas há um cantil com água e um pouco de carne...", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Saquear acampamento", "Explorar arredores"}, y += tileSize * 2, numComando);
                break;

            case 10:
                definirTelaDeBotao("voltar");

                if (!painel.getAmbienteAtual().checarSeSubStateFoiVisitado(12)) {
                    if (!isRecursosColetados()) {
                        painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                        painel.getInventSystem().adicionarItem("Carne suculenta", "consumo", 1);

                        int probabilidade = painel.definirUmaProbabilidade();
                        if (probabilidade <= 5) {
                            painel.getInventSystem().adicionarItem("Carvão estranho", "recurso", 1);
                        } else {
                            painel.getInventSystem().adicionarItem("Carvão", "recurso", 1);
                        }
                        setRecursosColetados(true);
                    }

                    ui.escreverTexto("Você pega o cantil, a carne, e um resto de carvão.", y);
                } else {
                    ui.escreverTexto("Não há mais nada para pegar aqui.", y);
                }
                break;

            case 11:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Melhor não mexer com o que não é seu.", y);
                ui.escreverTexto("(e, afinal, quem sabe de quem pode ser...)", y += tileSize);
                ui.escreverTexto("Em torno dessa fogueira há vegetação baixa.", y += tileSize);
                ui.escreverTexto("Mais adiante, o barulho animador aumenta: água corrente!", y += tileSize);
                ui.escreverTexto("Apressando o passo, em minutos você chega ao lago.", y += tileSize);
                break;

            case 12:
                ui.escreverTexto("Você retorna a atenção à fogueira.", y);
                ui.escreverTexto("O fogo parece estar muito mais fraco desde que você chegou...", y += tileSize);
                ui.escreverTexto("Você ouve um barulho de água à distância. Pode ser um lago.", y += tileSize);

                ui.desenharOpcoes(new String[]{"Ir ao lago", "Continuar explorando aqui"}, y += tileSize, numComando);
                break;

            // BRANCH DA VÍBORA
            case 13:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você busca por recursos.", y);
                ui.escreverTexto("Encontrou: 3 madeiras, 1 pedras, 1 galho pontiagudo.", y += tileSize);
                ui.escreverTexto("Verifique a mochila para usar os itens encontrados.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInventSystem().adicionarItem("Madeira", "recurso", 3);
                    painel.getInventSystem().adicionarItem("Pedra", "recurso", 1);
                    painel.getInventSystem().adicionarItem("Galho pontiagudo", "combate", 1);
                    setRecursosColetados(true);
                }
                break;

            case 14:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Aquele chiado... parece estar tão perto...", y);
                break;

            case 15:
                iniciarEspera();

                if (checarSeSubStateFoiVisitado(1)) {
                    botoes.mostrarBotao("Voltar à base");
                }

                ui.escreverTexto("Já é hora de pensar no que fazer para se sustentar nessa mata.", y);
                ui.escreverTexto("O que fazer?", y += tileSize * 2);


                String opcaoFrutas = "Buscar frutas e medicação natural";
                if (podeCurar && checarSeSubStateFoiVisitado(1)) {
                    opcaoFrutas = "Buscar sinal de civilização";
                }
                String opcaoCaca = podeCacar ? "Caçar" : "Buscar por arma de caça";
                String opcaoFogueira = checarSeSubStateFoiVisitado(1) ? "Analisar ambiente" : "Montar fogueira";

                ui.desenharOpcoes(new String[]{opcaoFrutas, opcaoCaca, opcaoFogueira}, y += tileSize * 2, numComando);
                break;

            case 16:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Deve haver algum vegetal com propriedades de cura", y);
                ui.escreverTexto("por essas matas. E, por Deus, algo bom pra comer", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Tudo o que tinha por aqui já está na mochila.", y += tileSize);
                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }
                    boolean recursoEncontrado = probabilidade <= 70;

                    if (recursoEncontrado) {
                        if (!isRecursosColetados()) {
                            if (probabilidade <= 10) {
                                painel.getInventSystem().adicionarItem("Fruta", "consumo", 4);
                                painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 4);
                            } else if (probabilidade > 10 || probabilidade <= 20) {
                                painel.getInventSystem().adicionarItem("Fruta", "consumo", 2);
                                painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 3);
                            } else if (probabilidade > 20 || probabilidade <= 30) {
                                painel.getInventSystem().adicionarItem("Fruta", "consumo", 2);
                                painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 3);
                            } else if (probabilidade > 30 || probabilidade <= 50) {
                                painel.getInventSystem().adicionarItem("Fruta", "consumo", 1);
                                painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 2);
                            } else {
                                painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 2);
                            }
                            setRecursosColetados(true);
                        }
                    }
                    setChanceTirada(true);
                }
                definirOcorrenciaDeEventoClimatico(g2, eventoChuva, 1);
                break;

            case 17:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Você procura por algo que sirva para caçar.", y);
                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }
                    boolean recursoEncontrado = probabilidade <= 70;

                    if (recursoEncontrado) {
                        if (!isRecursosColetados()) {
                            if (probabilidade <= 15) {
                                painel.getInventSystem().adicionarItem("Machado", "combate", 1);
                            } else {
                                painel.getInventSystem().adicionarItem("Faca", "combate", 1);
                            }
                            setRecursosColetados(true);
                        }
                    }
                    setChanceTirada(true);
                }
                if (painel.getInventSystem().acharItem("Machado") || painel.getInventSystem().acharItem("Faca")) {
                    ui.escreverTexto("Olha só... acho que isso pode servir.", y += tileSize);
                }
                definirOcorrenciaDeEventoClimatico(g2, eventoTempestade, 2);
                break;

            case 18:
                botoes.mostrarBotao("Continuar");

                ui.escreverTexto("(Você usa as madeiras que encontrou para montar uma fogueira.)", y);
                ui.escreverTexto("Agora, hora de tentar fazer fogo.", y += tileSize);
                ui.escreverTexto("Você tenta gerar fricção com um graveto", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                break;

            case 19:
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
                        painel.getInventSystem().removerItem("Madeira", 2);
                        setRecursosGastos(true);
                    }
                    ui.escreverTexto("Dificilmente alguma criatura tentará te atacar aqui.", y += tileSize);
                    ui.escreverTexto("É uma boa ideia tornar esse lugar sua base.", y += tileSize);
                } else {
                    ui.escreverTexto("Isso está demorando...", y += tileSize);
                    ui.escreverTexto("Só mais um pouco, não é seguro ficar parado...", y += tileSize);
                }
                break;

            case 20:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("O que seria este lugar? Independente, não", y);
                ui.escreverTexto("é possível que aqui só haja você e os monstros.", y += tileSize);
                ui.escreverTexto("Você adentra cautelosamente a mata, em busca de respostas.", y += tileSize);
                ui.escreverTexto("Durante o caminho, recolheu alguns recursos úteis.", y += tileSize);

                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }

                    if (!isRecursosColetados()) {
                        painel.getInventSystem().adicionarItem("Madeira", "recurso", 1);

                        boolean recursoEncontrado = probabilidade <= 70;
                        if (recursoEncontrado) {
                            painel.getInventSystem().adicionarItem("Mel", "consumo", 2);
                            if (probabilidade >= 35) {
                                painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 1);
                                painel.getInventSystem().adicionarItem("Punhado de sementes", "recurso", 1);
                            } else {
                                painel.getInventSystem().adicionarItem("Fruta", "consumo", 1);
                                painel.getInventSystem().adicionarItem("Corda", "recurso", 1);
                            }
                            setRecursosColetados(true);
                        }
                    }
                    setChanceTirada(true);
                }
                break;

            case 21:
                ui.escreverTexto("Você segue caminhando. Há uma bifurcação á frente.", y);
                ui.escreverTexto("À esquerda há planícies, à direita, mais mata.", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize * 2);

                ui.desenharOpcoes(new String[]{"Seguir pela esquerda", "Seguir pela direita"}, y += tileSize * 2, numComando);
                break;

            case 22:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Aqui o céu jorra sua escuridão imensa.", y);
                ui.escreverTexto("Não havia tido uma vista tão boa dele desde aquela clareira", y += tileSize);
                ui.escreverTexto("Há muitos troncos de árvores cortadas aqui, por isso o espaço.", y += tileSize);
                ui.escreverTexto("Pelo visto algo, ou alguém, 'limpou' essa parte da floresta.", y += tileSize);
                ui.escreverTexto("Você coleta o que encontra, e segue adiante.", y += tileSize);

                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }

                    if (!isRecursosColetados()) {
                        painel.getInventSystem().adicionarItem("Lâmina metálica", "recurso", 1);

                        boolean recursoEncontrado = probabilidade <= 90;
                        if (recursoEncontrado) {
                            painel.getInventSystem().adicionarItem("Carvão", "recurso", 1);
                            if (probabilidade >= 40) {
                                painel.getInventSystem().adicionarItem("Pedra", "recurso", 3);
                            } else {
                                painel.getInventSystem().adicionarItem("Escudo", "combate", 1);
                            }
                            setRecursosColetados(true);
                        }
                    }
                    setChanceTirada(true);
                }
                definirOcorrenciaDeEventoClimatico(g2, eventoTornado, 3);
                break;

            case 23:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Por ora, é bom verificar se a mata é muito extensa.", y);
                ui.escreverTexto("É só não se afastar demais.", y += tileSize);
                ui.escreverTexto("Os ruídos e criaturas de sempre, e ainda sem visão de algo útil.", y += tileSize);
                ui.escreverTexto("Você coleta o que encontra, e segue adiante.", y += tileSize);

                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }

                    if (!isRecursosColetados()) {
                        painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 1);

                        boolean recursoEncontrado = probabilidade <= 90;
                        if (recursoEncontrado) {
                            painel.getInventSystem().adicionarItem("Mel", "consumo", 1);
                            if (probabilidade >= 50) {
                                painel.getInventSystem().adicionarItem("Fruta", "consumo", 2);
                                painel.getInventSystem().adicionarItem("Punhado de sementes", "recurso", 1);
                            } else {
                                painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                            }
                            setRecursosColetados(true);
                        }
                    }
                    setChanceTirada(true);
                }
                definirOcorrenciaDeEventoClimatico(g2, eventoTempestade, 2);
                break;

            case 24:
                botoes.esconderBotao("Voltar à base");

                ui.escreverTexto("Após mais uma longa caminhada... nada", y);
                ui.escreverTexto("Pouca floresta, mas sem sinal de algo civilizado.", y += tileSize);
                ui.escreverTexto("E, mesmo depois de horas, o sol continua tímido em surgir", y += tileSize);
                ui.escreverTexto("De onde você está, realmente, apenas a montanha é visível.", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize * 2);

                ui.desenharOpcoes(new String[]{"Ir à montanha", "Retornar à floresta"}, y += tileSize * 2, numComando);
                break;

            case 25:
                botoes.esconderBotao("Voltar à base");

                ui.escreverTexto("Foram-se horas vagando, e ainda nenhum sinal de sentido.", y);
                ui.escreverTexto("'Como posso me livrar dessa prisão?'", y += tileSize);
                ui.escreverTexto("A essa altura, as escolhas são: permanecer e se virar,", y += tileSize);
                ui.escreverTexto("ou botar fé na caminhada até achar algum rumo real.", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize * 2);

                ui.desenharOpcoes(new String[]{"Seguir adiante", "Voltar para perto do abrigo"}, y += tileSize * 2, numComando);
                break;

            case 26:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Quanto mais caminha, mais perdido se sente.", y);
                ui.escreverTexto("Seus pés doem, sua energia se esgota. As árvores", y += tileSize);
                ui.escreverTexto("se fecham, seus galhos tortuosos cada vez mais assombrosos.", y += tileSize);
                ui.escreverTexto("Está ficando progressivamente mais escuro.", y += tileSize);
                ui.escreverTexto("Talvez esse não tenha sido o melhor caminho...", y += tileSize);
                ui.escreverTexto("Espere.", y += tileSize);
                ui.escreverTexto("Perto de uma vala, há algo brilhando...", y += tileSize);
                ui.escreverTexto("Uma espada. Melhor não imaginar como veio parar aqui.", y += tileSize);
                ui.escreverTexto("Não há sangue na lâmina, mas há no cabo.", y += tileSize);
                ui.escreverTexto("Pelo jeito, o dono não vai mais precisar dela", y += tileSize);
                ui.escreverTexto("Você se abaixa para pegar a espada.", y += tileSize);

                if (!isChanceTirada()) {
                    if (!isRecursosColetados()) {
                        painel.getInventSystem().adicionarItem("Espada Basilar", "combate", 1);

                        int probabilidade = painel.definirUmaProbabilidade();
                        boolean carvaoEstranho = probabilidade <= 5;

                        if (carvaoEstranho) {
                            painel.getInventSystem().adicionarItem("Carvão estranho", "recurso", 1);
                        }
                        else {
                            painel.getInventSystem().adicionarItem("Carvão", "recurso", 1);
                        }
                        setRecursosColetados(true);
                    }
                    setChanceTirada(true);
                }
                break;

            case 27:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("...", painel.getAltura()/2);
                break;

            case 28:
                botoes.esconderBotao("Abrir mochila");
                Composite composite = g2.getComposite();
                if (!isTransicaoFinalizada()) {
                    transicaoDeTela(g2);

                    ui.escreverTexto("Você sente que está sendo observado.", painel.getAltura() / 2 - tileSize);
                    ui.desenharOpcoes(new String[]{"Se virar"},painel.getAltura() / 2, numComando);
                }
                g2.setComposite(composite);

                if (isTransicaoFinalizada()) {
                    painel.setPlaySubState(29);
                }
                break;

            case 29:
                resetAtributosTransicao();

                definirTelaDeBotao("continuar");
                desenharImagemZoom(g2, figura);
                ui.escreverTexto("...", painel.getAltura() - tileSize);
                break;

            case 30:
                definirTelaDeBotao("continuar");

                g2.setColor(Color.red);
                ui.escreverTexto("*PUSH*", painel.getAltura()/2 - tileSize);
                ui.escreverTexto(jogador.getNome() + " foi empurrado da vala.", painel.getAltura()/2);
                g2.setColor(Color.white);
                break;

            case 31:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Você passou alguns minutos desacordado.", y += tileSize);
                ui.escreverTexto("Aparentemente, a queda foi feia. E maior do que aparentava.", y += tileSize);
                ui.escreverTexto("E, evidentemente, você não está mais na floresta...", y += tileSize);
                break;

            case 32:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("...",painel.getAltura()/2 - tileSize * 3);
                ui.escreverTexto("Entre tu e este mundo funesto, há uma coisa em comum:", painel.getAltura()/2 - tileSize * 2);
                ui.escreverTexto("sua morte soa melhor do que permanecer nele.", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("...", painel.getAltura()/2);
                ui.escreverTexto("Caminhando apenas mais um pouco, você avista algo familiar...", painel.getAltura()/2 + tileSize);
                ui.escreverTexto("Aquela clareira. Pelo jeito, por todo esse tempo, você andou em círculo.", painel.getAltura()/2 + tileSize * 2);
                ui.escreverTexto("Retornando a ela, você avalia o seu próximo passo...", painel.getAltura()/2 + tileSize * 3);

                definirOcorrenciaDeEventoClimatico(g2, eventoTempestade, 2);
                break;

            // BRANCH DA MONTANHA
            case 33:
               definirTelaDeBotao("continuar");

                ui.escreverTexto("Você se direciona até a montanha", y);
                ui.escreverTexto(". . .", y += tileSize);
                ui.escreverTexto("Algo não parce certo.", y += tileSize);
                ui.escreverTexto("Um movimento suspeito te acompanha.", y += tileSize);
                ui.escreverTexto("Nada se revela, mas você não está sozinho.", y += tileSize);
                ui.escreverTexto(". . .", y += tileSize);
                break;

            case 34:
                ui.escreverTexto("Atingido o pé da montanha, você enxerga um trecho de subida.", y);
                ui.escreverTexto("O que quer que estava te seguindo aparenta ter parado...", y += tileSize);
                ui.escreverTexto("Não há nada de destaque neste local, além de uma rocha firme.", y += tileSize);
                ui.escreverTexto("Sua formação pode conceder um abrigo com boa visibilidade.", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Subir pelo trecho", "Descansar até o amanhecer"}, y += tileSize * 2, numComando);
                break;

            case 35:
               definirTelaDeBotao("continuar");

                ui.escreverTexto("Subindo o trecho semi-íngreme.", y);
                ui.escreverTexto("você atinge um paredão.", y += tileSize);
                break;

            case 36:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você se dirige àquela rocha para um abrigo temporário.", y);
                ui.escreverTexto("Vai ter que servir. Você já se afastou demais da clareira.", y += tileSize);
                ui.escreverTexto("A energia aqui não é ideal, mas não parece ter mais nada à espreita", y += tileSize);
                ui.escreverTexto("De olhos entreabertos, você deita para descansar...", y += tileSize);

                definirOcorrenciaDeEventoClimatico(g2, eventoChuva, 1);
                break;

            case 37:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("... Mas é impossível. Há algo nessa floresta...", y);
                ui.escreverTexto("Você monta um pequeno acampamento perto da rocha.", y += tileSize);
                ui.escreverTexto("Seria bom descobrir a natureza deste lugar, porém,", y += tileSize);
                ui.escreverTexto("fazer isso sem um porto seguro não é viável.", y += tileSize);
                break;

            case 38:
                iniciarEspera();

                botoes.mostrarBotao("Voltar à base");
                ui.escreverTexto("Essa rocha parece um local bom o suficiente por agora.", y);
                ui.escreverTexto("Mas algo chama a atenção... uma árvore gigante.", y += tileSize);
                ui.escreverTexto("Lá do alto, talvez consiga alguma noção de direção.", y += tileSize);
                ui.escreverTexto("E evita ter que subir naquela montanha", y += tileSize);
                ui.escreverTexto("Mas há o que fazer aqui embaixo, também.", y += tileSize);

                String opcaoFrutas2 = podeCurar ? "Analisar ambiente" : "Buscar frutas e medicação natural";
                String opcaoCaca2 = podeCacar ? "Caçar" : "Buscar por arma de caça";

                ui.desenharOpcoes(new String[]{opcaoFrutas2, opcaoCaca2, "Subir na árvore"}, y += tileSize * 2, numComando);
                break;

            case 39:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Você começa a escalar a árvore...", y);

                definirOcorrenciaDeEventoClimatico(g2, eventoTempestade, 2);
                break;

            case 40:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Metade do caminho já foi...", y);
                ui.escreverTexto("Há um pouco de mel misturado com seiva. Açúcar é bom.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInventSystem().adicionarItem("Mel", "consumo", 1);
                    setRecursosColetados(true);
                }

                ui.escreverTexto("Daqui, o que dá para ver não é promissor...", y += tileSize * 2);
                ui.escreverTexto("Mas agora já está alto. Melhor continuar.", y += tileSize);
                ui.escreverTexto("Em pouco tempo, você atinge o topo da árvore.", y += tileSize);
                ui.escreverTexto("E a vista é... perturbadora.", y += tileSize);
                break;

            case 41:
                ui.escreverTexto("A floresta é INFINITA. O horizonte é mata pura.", y);
                ui.escreverTexto("É possível ver algumas áreas sem cobertura,", y += tileSize);
                ui.escreverTexto("desmatamento ou corpos aquáticos, provavelmente.", y += tileSize);
                ui.escreverTexto("Há uma pequena caverna, em um pico.", y += tileSize);
                ui.escreverTexto("Mas, fora isso, apenas a imponente montanha...", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Transferir base para a caverna", "Retornar à clareira", "Jogar pedras em pássaros"}, y += tileSize * 2, numComando);
                break;

            case 42:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você se dirige ao pico.", y);
                ui.escreverTexto("Nada de especial ao longo do caminho.", y += tileSize);
                ui.escreverTexto("Ainda há criaturas aqui, mas, quanto mais se aproxima,", y += tileSize);
                ui.escreverTexto("menos delas você vê. Esse pico pode ser seguro mesmo.", y += tileSize);

                definirOcorrenciaDeEventoClimatico(g2, eventoTornado, 3);
                break;

            case 43:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Finalmente, você chega ao pé do pico.", y);
                ui.escreverTexto("Daqui, enxerga a pequena caverna. Basta subir.", y += tileSize);
                ui.escreverTexto("Verdadeiramente, é um lugar ordinário.", y += tileSize);
                ui.escreverTexto("Porém, bem localizado. Vai servir para a nova base", y += tileSize);
                break;

            case 44:
                iniciarEspera();
                botoes.mostrarBotao("continuar");

                ui.escreverTexto("A sobrevivência deve ser prioridade.", y);
                ui.escreverTexto("Armas, comida, e materiais são vitais.", y += tileSize);
                ui.escreverTexto("Essa caverna é boa até mesmo para dormir...", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Buscar recursos", "Caçar", "Dormir por algumas horas"}, y += tileSize * 2, numComando);
                break;

            case 45:
                definirTelaDeBotao("voltar");
                ui.escreverTexto("Você busca por recursos.", y);

                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }
                    boolean recursoEncontrado = probabilidade <= 80;

                    if (recursoEncontrado) {
                        if (!isRecursosColetados()) {
                            if (!painel.getInventSystem().acharItem("Escudo") && !painel.getInventSystem().acharItem("Foice")) {
                                if (probabilidade <= 50) {
                                    painel.getInventSystem().adicionarItem("Escudo", "combate", 1);
                                }
                                else {
                                    painel.getInventSystem().adicionarItem("Foice", "combate", 1);
                                }
                            }

                            if (painel.getInventSystem().getInventario().size() < 7) {
                                if (probabilidade <= 65 && probabilidade >= 15) {
                                    painel.getInventSystem().adicionarItem("Fruta", "consumo", 2);
                                    painel.getInventSystem().adicionarItem("Planta medicinal", "recurso", 1);
                                    if (probabilidade <= 45) {
                                        painel.getInventSystem().adicionarItem("Madeira", "recurso", 1);
                                    }
                                }

                                if (probabilidade <= 30) {
                                    painel.getInventSystem().adicionarItem("Lâmina metálica", "recurso", 2);
                                }
                                if (probabilidade <= 20) {
                                    painel.getInventSystem().adicionarItem("Cantil", "consumo", 1);
                                }
                            }
                            setRecursosColetados(true);
                        }
                    }
                    setChanceTirada(true);
                }

                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Parece que você já pegou tudo de útil por aqui.", y += tileSize);
                ui.escreverTexto("Há bons recursos em sua mochila.", y += tileSize);
                break;

            case 46:
                botoes.esconderBotao("Abrir mochila");
                botoes.esconderBotao("Voltar à base");
                Composite composite2 = g2.getComposite();
                if (!isTransicaoFinalizada()) {
                    transicaoDeTela(g2);

                    ui.escreverTexto("Finalmente, você encontra um tempo para repouso.", y);
                    ui.escreverTexto("Tudo o que está havendo é obscuro, mas, com fé,", y += tileSize);
                    ui.escreverTexto("o dia de amanhã será melhor.", y += tileSize);

                    ui.desenharOpcoes(new String[]{"Dormir"},painel.getAltura() / 2, numComando);
                }
                g2.setComposite(composite2);

                if (isTransicaoFinalizada()) {
                    painel.setPlaySubState(47);
                }
                break;

            case 47:
                resetAtributosTransicao();

                definirTelaDeBotao("continuar");
                desenharImagemZoom(g2, ursoFace1);
                ui.escreverTexto("...", painel.getAltura() - tileSize);
                break;

            case 48:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Você acorda com companhia.", painel.getAltura()/2);
                break;

            case 49:
                definirTelaDeBotao("continuar");

                ui.desenharAnimacaoUrso();

                int dS = painel.getDialogueState();

                String comentario;
                if (painel.getInventSystem().acharItem("Jóia vermelha")
                        && !painel.getInventSystem().acharItem("Jóia azul")) {
                    comentario = "``O genocídio na gruta?``";
                }
                else if (painel.getInventSystem().acharItem("Jóia azul")
                        && !painel.getInventSystem().acharItem("Jóia vermelha")) {
                   comentario = "``A invasão ao lago?``";
                }
                else if (painel.getInventSystem().acharItem("Jóia vermelha")
                        && painel.getInventSystem().acharItem("Jóia azul")) {
                   comentario = "``Toda a violência contra nós?``";
                }
                else {
                    comentario = "``Seu atentado a nossa paz?``";
                }

                String dialogo = switch (dS) {
                    case 0 -> "``Você é previsível.``";
                    case 1 -> "``Até sua expressão de surpresa é sempre idêntica.``";
                    case 2 -> "``Valeu a pena?``";
                    case 3 -> comentario;
                    case 4 -> "``...``";
                    case 5 -> "``Não irei me extender dessa vez.``";
                    case 6 -> "``Seus pecados serão pagos.``";
                    case 7 -> "``Seja nesta vida, ou na próxima.``";
                    case 8 -> "``E hoje...``";
                    case 9 -> "``Sua carcaça será minha ceia.``";
                    default -> throw new IllegalStateException("Diálogo desconhecido: " + dS);
                };

                g2.setColor(Color.red);
                ui.escreverTexto(dialogo, painel.getAltura() - tileSize);
                g2.setColor(Color.white);
                break;

            case 50:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("...", y);
                ui.escreverTexto("Esse encontro o deixa reflexivo.", y += tileSize);
                ui.escreverTexto("As coisas que aquele monstro disse...", y += tileSize * 2);
                ui.escreverTexto("O que ele quis dizer?", y += tileSize);
                ui.escreverTexto(jogador.getNome() + ", há mais nessa história do que parece.", y += tileSize);
                ui.escreverTexto("A floresta grita, a morte do urso parece", y += tileSize * 2);
                ui.escreverTexto("ter causado um impacto no ambiente.", y += tileSize);
                ui.escreverTexto("A luta te enche de vigor, mas deve haver mais por vir.", y += tileSize);
                break;

            case 51:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Aquela espada que estava encravada na criatura...", y);
                ui.escreverTexto("É melhor não desperdiçar uma boa arma.", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Ela tem uma energia estranha.", y += tileSize);
                if (!isRecursosColetados()) {
                    jogador.setVidaMax(jogador.getVidaMax() + 10);
                    jogador.setVida(jogador.getVidaMax());
                    painel.getInventSystem().adicionarItem("Espada Insigne", "combate", 1);
                    setRecursosColetados(true);
                }
                break;

            case 52:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("É inviável permanecer aqui.", y);
                ui.escreverTexto("Retorne à clareira para se estabelecer em outro local.", y += tileSize);
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}