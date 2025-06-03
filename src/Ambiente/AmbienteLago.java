package Ambiente;

import Controles.Botoes;
import Entidade.Criatura;
import Entidade.Jogador;
import Evento.EventoCriatura;
import Main.Painel;
import UI.UI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AmbienteLago extends Ambiente {

    private Graphics2D g2;
    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;
    private Criatura criatura;

    private EventoCriatura eventoTriclope;
    private EventoCriatura eventoCrustoso;

    private BufferedImage fundoLago2;
    private BufferedImage placaFrente, placaVerso, crustoso, joia;

    private int contadorEspera = 0;
    private int contadorMovimento = 0;

    private final int caranguejo = 2001;
    private final int boss = 2002;

    public AmbienteLago(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();
        this.criatura = new Criatura();

        this.eventoTriclope = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoCrustoso = new EventoCriatura(painel, ui, jogador, criatura);

        descreverAmbiente();

        fundoLago2 = ui.setupImagens("lago_sereno-2", "background");
        placaFrente = ui.setupImagens("placa_lago_frente", "zoom");
        placaVerso = ui.setupImagens("placa_lago_verso", "zoom");
        crustoso = ui.setupImagens("crustoso_cruel", "criatura");
        joia = ui.setupImagens("joia_carcaca", "zoom");
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("LAGO SERENO");
        this.setDescricao("Limpo, vasto, estranhamente silencioso.");
        this.setDificuldade("tranquilas.");
        this.setRecursos("peixe, madeira, água.");
        this.setFrequenciaEventos("quieto, uma espécie predominante.");
        this.setClimaAmbiente("levemente frio");
        this.setNomeFundoCard("lago_sereno");
        this.setNomeFundoCombate("lago_sereno_combate");
    }

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

        int numComando = ui.getNumComando();

        g2.fillRect(0, 0, larguraTela, alturaTela);
        ui.desenharPlanoDeFundo(fundoLago2);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize * 2;

        switch (subState) {
            case caranguejo:
                definirOcorrenciaDeEventoCriatura(g2, eventoTriclope, 11);
                break;
            case boss:
                definirOcorrenciaDeEventoCriatura(g2, eventoCrustoso, 12);
                break;

            case 1:
                botoes.esconderBotao("Voltar à base");;
                ui.mostrarAcampamento();
                break;

            case 100:
                ui.escreverTexto("Este lago parece bom para descanso.", y);
                ui.escreverTexto("Você pode buscar um bom lugar para acampar", y += tileSize);
                ui.escreverTexto("aqui, ou retornar para a floresta.", y += tileSize);

                ui.desenharOpcoes(new String[] {"Explorar lago", "Retornar à floresta"}, y += tileSize * 2, numComando);
                break;

            case 101:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("O lago é extenso, cobrindo quase todo o horizonte.", y);
                ui.escreverTexto("Não parece haver muitas criaturas por aqui.", y += tileSize);
                ui.escreverTexto("Entretanto, há algumas sementes. Vale a pena pegar.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Punhado de sementes", "recurso", 1);
                    setRecursosColetados(true);
                }

                ui.escreverTexto("Aqui parece ser um bom lugar para montar acampamento.", y+= tileSize);
                break;

            case 102:
                botoes.mostrarBotao("Voltar à base");
                ui.escreverTexto("O que fazer agora?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Checar o lago de perto", "Analisar terras do mangue", "Buscar recursos"}, y += tileSize * 2, numComando);
                break;

            case 103:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Você se aproxima do lago...", y += tileSize);
                break;

            case 104:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Há uma população de crustáceos bizarros aqui.", y += tileSize);
                ui.escreverTexto("Eles marcham ao redor do lago quase que com coordenação,", y += tileSize);
                ui.escreverTexto("até parece que o fazem com algum propósito.", y += tileSize);
                ui.escreverTexto("A maioria não parece realmente hostil, já outros...", y += tileSize);
                ui.escreverTexto("Há uma placa meio apagada próxima à margem do lago.", y += tileSize);;
                ui.escreverTexto("Ilegível, mas há algum tipo de gravura...", y += tileSize * 2);
                break;

            case 105:
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
                            if (probabilidade <= 50) {
                                if (!painel.getInvent().acharItem("Corda")) {
                                    painel.getInvent().adicionarItem("Corda", "recurso", 1);
                                }
                            }

                            if (painel.getInvent().getInvent().size() < 7) {
                                painel.getInvent().adicionarItem("Galho pontiagudo", "combate", 1);
                                if (probabilidade <= 65 && probabilidade >= 15) {
                                    painel.getInvent().adicionarItem("Pedra", "recurso", 2);
                                    if (probabilidade <= 45) {
                                        painel.getInvent().adicionarItem("Madeira", "recurso", 1);
                                            if (probabilidade <= 25) {
                                                painel.getInvent().adicionarItem("Fruta", "consumo", 2);

                                            }
                                        }
                                    }

                                if (probabilidade <= 20) {
                                    painel.getInvent().adicionarItem("Cantil", "consumo", 1);
                                }
                                if (probabilidade <= 10) {
                                    painel.getInvent().adicionarItem("Lâmina metálica", "recurso", 1);
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

            case 106:
                definirTelaDeBotao("continuar");

                desenharImagemZoom(g2, placaFrente);
                ui.escreverTexto("Estranho. Talvez haja mais no verso.", painel.getAltura() - tileSize);
                break;

            case 107:
                definirTelaDeBotao("continuar");

                desenharImagemZoom(g2, placaVerso);
                ui.escreverTexto("...", painel.getAltura() - tileSize);
                break;

            case 108:
                definirTelaDeBotao("voltar");
                ui.escreverTexto("Esse lago esconde algo...", y);
                ui.escreverTexto("É vital medir os próximos passos.", y += tileSize);
                break;

            case 109:
                botoes.mostrarBotao("Voltar à base");
                ui.escreverTexto("O que fazer?", y);

                ui.desenharOpcoes(new String[]{"Inspecionar o lago", "Buscar recursos", "Retornar à floresta"}, y += tileSize * 2, numComando);
                break;

            case 110:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Esses bichos parecem não gostar da sua presença.", y);
                ui.escreverTexto("Enfim, há terra firme e sem muita atividade em uma margem próxima", y += tileSize);
                break;

            case 111:
                botoes.esconderBotao("Voltar à base");

                setContadorEspera(0);
                setContadorMovimento(0);

                ui.escreverTexto("Será que há algo produtivo para se fazer aqui?", y);

                if (painel.getInvent().acharItem("Vara de pesca")) {
                    ui.desenharOpcoes(new String[] {"Observar brilho estranho", "Sair da margem", "Pescar"}, y += tileSize * 2, numComando);
                } else {
                    ui.desenharOpcoes(new String[] {"Observar brilho estranho", "Sair da margem", "Chutar um crustáceo no lago"}, y += tileSize * 2, numComando);
                }
                break;

            case 112:
                botoes.esconderBotao("Voltar à base");
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Uma luz está visível, ainda que trêmula, bem no fundo do lago", y);
                ui.escreverTexto("Curioso. Fora dela, o fundo é um breu por completo.", y += tileSize);
                ui.escreverTexto("E absolutamente sem movimentação na água.", y += tileSize);
                ui.escreverTexto("Louco como algo tão calmo pode ser tão... desconfortável.", y += tileSize);
                ui.escreverTexto("(Ao encarar demais, você sente um calafrio subir sua espinha).", y += tileSize);
                break;

            case 113:
                definirTelaDeBotao("voltar");

                ui.escreverTexto("Você chuta um dos bichos no lago", y);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("É difícil enxergar a utilidade disso.", y += tileSize);

                if (!isChanceTirada()) {
                    double probabilidade = painel.definirUmaProbabilidade();
                    if (jogador.getHabilidade().equals("RASTREADORA")) {
                        probabilidade = probabilidade * 0.9;
                    }
                    boolean recursoEncontrado = probabilidade <= 5;

                    if (recursoEncontrado) {
                        if (!isRecursosColetados()) {
                            painel.getInvent().adicionarItem("Faca", "combate", 1);
                            setRecursosColetados(true);
                        }
                    }
                    setChanceTirada(true);
                }

                if (painel.getInvent().acharItem("Faca")) {
                    ui.escreverTexto("Espere, ele deixou cair alguma coisa?...", y += tileSize);
                }
                break;

            case 114:
                botoes.esconderBotao("Voltar à base");

                iniciarEspera();
                g2.setColor(new Color (0, 70, 100));
                ui.escreverTexto("PESCANDO", y);
                g2.setColor(Color.white);
                ui.escreverTexto("Você encaixa pequenas iscas na vara.", y += tileSize);

                ui.desenharOpcoes(new String[] {"Esperar", "Chacoalhar vara", "Sair"}, y += tileSize * 2, numComando);

                break;

            case 115:
                definirTelaDeBotao("voltar");

                if (isAguardando()) {
                    g2.setColor(Color.red);
                    ui.escreverTexto("Não há pressa... Aguarde...", y += tileSize * 2);
                    g2.setColor(Color.white);

                    setContadorTimer(getContadorTimer() + 1);
                    if (getContadorTimer() >= getTempoDeEsperaEmFPS()) {
                        setAguardando(false);

                        if (!isChanceTirada()) {
                            jogador.setContadorDaSede(0);

                            double probabilidade = painel.definirUmaProbabilidade();
                            if (jogador.getHabilidade().equals("RASTREADORA")) {
                                probabilidade = probabilidade * 0.9;
                            }
                            if (painel.getInvent().acharItem("Jóia azul")) {
                                probabilidade = probabilidade * 0.80;
                            }
                            boolean pescouPeixe = probabilidade <= 60;
                            boolean pescouItem = probabilidade >= 80;

                            if (contadorEspera == 5 && contadorEspera > contadorMovimento) {
                                if (probabilidade >= 50) {
                                    if (!isRecursosColetados()) {
                                        painel.getInvent().adicionarItem("Tridente", "combate", 1);
                                        setRecursosColetados(true);
                                    }
                                } else {
                                    if (!isRecursosColetados()) {
                                        painel.getInvent().adicionarItem("Cimitarra", "combate", 1);
                                        setRecursosColetados(true);
                                    }
                                }
                            }

                            if (pescouPeixe) {
                                if (!isRecursosColetados()) {
                                    painel.getInvent().adicionarItem("Peixe", "consumo", 1);
                                    setRecursosColetados(true);
                                }
                            } else if (pescouItem) {
                                if (!isRecursosColetados()) {
                                    if (probabilidade < 90) {
                                        painel.getInvent().adicionarItem("Planta medicinal", "recurso", 1);
                                    } else {
                                        if (!painel.getInvent().acharItem("Lâmina metálica")) {
                                            painel.getInvent().adicionarItem("Lâmina metálica", "recurso", 1);
                                        }
                                    }
                                    setRecursosColetados(true);
                                }
                            }

                            contadorEspera++;
                            setChanceTirada(true);
                        }
                    }
                } else {
                    ui.escreverTexto("Confira se pegou algo.", y += tileSize * 2);
                }

                break;

            case 116:
                definirTelaDeBotao("voltar");

                if (isAguardando()) {
                    g2.setColor(Color.red);
                    ui.escreverTexto("Vamos ver se há sinal de vida nesse lago... Aguarde...", y += tileSize * 2);
                    g2.setColor(Color.white);

                    setContadorTimer(getContadorTimer() + 1);
                    if (getContadorTimer() >= getTempoDeEsperaEmFPS()/2) {
                        setAguardando(false);

                        if (!isChanceTirada()) {
                            jogador.setContadorDaSede(0);

                            double probabilidade = painel.definirUmaProbabilidade();
                            if (jogador.getHabilidade().equals("RASTREADORA")) {
                                probabilidade = probabilidade * 0.9;
                            }
                            if (painel.getInvent().acharItem("Jóia azul")) {
                                probabilidade = probabilidade * 0.80;
                            }
                            boolean pescouPeixe = probabilidade <= 50;

                            if (contadorMovimento == 4 && contadorMovimento > contadorEspera) {
                                if (probabilidade >= 85) {
                                    if (!isRecursosColetados()) {
                                        painel.getInvent().adicionarItem("Tridente", "combate", 1);
                                        setRecursosColetados(true);
                                    }
                                } else {
                                    if (!isRecursosColetados()) {
                                        painel.getInvent().adicionarItem("Cimitarra", "combate", 1);
                                        setRecursosColetados(true);
                                    }
                                }
                            }

                            if (pescouPeixe) {
                                if (!isRecursosColetados()) {
                                    painel.getInvent().adicionarItem("Peixe", "consumo", 1);
                                    setRecursosColetados(true);
                                }
                            }

                            contadorMovimento++;
                            setChanceTirada(true);
                        }
                    }
                } else {
                    ui.escreverTexto("Confira se pegou algo.", y += tileSize * 2);
                }
                break;

            case 117:
                botoes.esconderBotao("Abrir mochila");

                if (isAguardando()) {
                    ui.escreverTexto("Ainda sem sinal de sol...", y);
                    ui.escreverTexto("Sem sinal de um caminho para fora desse lugar...", y += tileSize);
                    ui.escreverTexto("Sem um pingo de sentido sobre como veio parar aqui....", y += tileSize);
                    ui.escreverTexto("''O que fiz para merecer parar neste inferno?''", y += tileSize);
                    ui.escreverTexto("Por ora, o que lhe resta é esta pescaria...", y += tileSize * 2);
                    g2.setColor(Color.red);
                    ui.escreverTexto("Aguarde...", y += tileSize);
                    g2.setColor(Color.white);

                    setContadorTimer(getContadorTimer() + 1);
                    if (getContadorTimer() >= getTempoDeEsperaEmFPS()) {
                        setAguardando(false);
                    }
                } else {
                    Composite composite = g2.getComposite();
                    if (!isTransicaoFinalizada()) {
                        transicaoDeTela(g2);

                        ui.escreverTexto("Você sente que fisgou algo. Algo grande.", painel.getAltura() / 2 - tileSize * 2);
                        ui.escreverTexto("...", painel.getAltura() / 2 - tileSize);
                        ui.escreverTexto(jogador.getNome() + ", solte essa vara de pesca...", painel.getAltura() / 2);

                        ui.desenharOpcoes(new String[]{"Soltar vara"}, painel.getAltura() / 2 + tileSize * 2, numComando);
                    }
                    g2.setComposite(composite);

                }
                if (isTransicaoFinalizada()) {
                    painel.setPlaySubState(118);
                }

                break;

            case 118:
                resetAtributosTransicao();

                if (!isRecursosGastos()) {
                    painel.getInvent().removerItem("Vara de pesca", 1);
                    setRecursosGastos(true);
                }

                definirTelaDeBotao("continuar");
                desenharImagemZoom(g2, crustoso);
                ui.escreverTexto("Uma fera incrustada emerge do lago.", painel.getAltura() - tileSize);
                break;

            case 119:
                definirTelaDeBotao("continuar");

                if (!isRecursosColetados()) {
                    jogador.setVidaMax(jogador.getVidaMax() + 5);
                    jogador.setVida(jogador.getVidaMax());
                    painel.getInvent().adicionarItem("Espeto crustáceo", "combate", 1);
                    painel.getInvent().adicionarItem("Jóia azul", "recurso", 1);
                    setRecursosColetados(true);
                }

                ui.escreverTexto("...", y);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Essa... foi uma experiência desagradável.", y += tileSize);
                ui.escreverTexto("Não é como se as criaturas deste lugar fossem normais, mas aquilo?", y += tileSize);
                ui.escreverTexto("Absurdamente extraterreste.", y += tileSize);
                ui.escreverTexto("Este embate te fortalece, mas não parece ter sido o último do tipo.", y += tileSize * 2);
                ui.escreverTexto("Que outros monstros essa terra maldita ainda esconde?", y += tileSize);
                break;

            case 120:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Seu golpe fatal foi tão poderoso", y);
                ui.escreverTexto("que arrancou uma das pernas da criatura.", y += tileSize);
                ui.escreverTexto("É uma crosta assustadoramente rígida,", y += tileSize);
                ui.escreverTexto( "além de ser afiada (Você que o diga).", y += tileSize);
                ui.escreverTexto("Pode vir a ser útil.", y+= tileSize);
                ui.escreverTexto("Espere... há algo mais.", y += tileSize * 2);
                ui.escreverTexto("Há uma iluminação estranha vindo de dentro da carcaça do crustáceo.", y += tileSize);
                break;

            case 121:
                definirTelaDeBotao("continuar");
                desenharImagemZoom(g2, joia);
                ui.escreverTexto("Isso... é melhor guardar.", painel.getAltura() - tileSize);
                break;

            case 122:
                definirTelaDeBotao("voltar");
                ui.escreverTexto("O lago não era tão sereno quanto aparentava.", y += tileSize);
                ui.escreverTexto("Ainda assim, esses crustáceos não causam tanto problema.", y += tileSize);
                ui.escreverTexto("O acampamento pode ser movido, mas nada impede você de voltar", y += tileSize);
                ui.escreverTexto("e pescar um pouco. Dificilmente o lago é lar de algo maior que aquilo...", y += tileSize);
                ui.escreverTexto("De volta àquela floresta, por ora.", y += tileSize);
                break;

            case 123:
                ui.escreverTexto("Este é o lago. O que fazer?", y);

                ui.desenharOpcoes(new String[] {"Pescar", "Retornar à floresta"}, y += tileSize * 2, numComando);
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }

    public void setContadorEspera(int contadorEspera) { this.contadorEspera = contadorEspera; }
    public void setContadorMovimento(int contadorMovimento) { this.contadorMovimento = contadorMovimento; }
}