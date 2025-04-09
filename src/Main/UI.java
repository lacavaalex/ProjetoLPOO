package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class UI {

    BufferedImage titleBackground;

    Painel painel;
    Jogador jogador = new Jogador();
    Botões botoes;
    Criatura criatura;
    Evento evento;

    Graphics2D g2;
    Font pixelsans_30, pixelsans_60B;

    private int tileSize;
    private int larguraTela;
    private int alturaTela;

    public int numComando = 0;
    private int telaInicialState = 0;

    public UI(Painel painel, Jogador jogador) {
        this.painel = painel;
        this.jogador = jogador;

        pixelsans_30 = new Font("Pixel Sans Serif", Font.PLAIN, 30);
        pixelsans_60B = new Font("Pixel Sans Serif", Font.BOLD, 60);

        botoes = new Botões(painel);
        criatura = new Criatura();
        evento = new Evento(painel, this, jogador, criatura);

        try {
            titleBackground = ImageIO.read(getClass().getResource("/Imagens/fundo_mao_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrar(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(pixelsans_30);
        g2.setColor(Color.white);

        int gameState = painel.getGameState();
        int tutorialControles = painel.getTutorialControles();
        int titleState = painel.getTitleState();
        int openingState = painel.getOpeningState();
        int playState = painel.getPlayState();
        int gameOverState = painel.getGameOverState();
        int florestaCardState = painel.getFlorestaCardState();
        int lagoCardState = painel.getLagoCardState();
        int montanhaCardState = painel.getMontanhaCardState();

    // Informações do jogador
        if (gameState != titleState && gameState!= openingState && gameState!=florestaCardState) {
        }

    // Title state
        if (gameState == titleState) {
            mostrarTelaInicial();
        }
    // Tela de controles
        if (gameState == tutorialControles) {
            mostrarTutorialControles();
        }
    // Opening state
        if (gameState == openingState) {
            mostrarAbertura();
        }
    // Play state
        if (gameState == playState) {
            mostrarPlayState();
        }
    // Game over
        if (gameState == gameOverState) {
            mostrarGameOverScreen();
            painel.resetPlayState();
            jogador.resetVida();
        }
    // Cards de ambiente
        if (gameState == florestaCardState) {
            mostrarCardFloresta();
        }
        if (gameState == lagoCardState) {
            mostrarCardLago();
        }
        if (gameState == montanhaCardState) {
            mostrarCardMontanha();
        }
    }

    public void mostrarTelaInicial() {

        botoes.esconderBotaoContinuar();

    // Tela inicial 1 (entrada)
        if (getTelaInicialState() == 0) {

            tileSize = painel.getTileSize();
            larguraTela = painel.getLargura();
            alturaTela = painel.getAltura();

            // Título
            desenharFundoMao();
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
            int y = tileSize * 5;
            int x = coordenadaXParaTextoCentralizado("O MUNDO FUNESTO");

            //Sombra
            g2.setColor((Color.black));
            g2.drawString("O MUNDO FUNESTO", x + 5, y + 5);

            g2.setColor(Color.white);
            escreverTexto("O MUNDO FUNESTO", y);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            String[] opcoes = {"NOVO JOGO", "CONTROLES", "SAIR"};

            y += tileSize * 2;
            for (int i = 0; i < opcoes.length; i++) {
                String texto = opcoes[i];
                x = coordenadaXParaTextoCentralizado(texto);

                if (numComando == i) {
                    g2.setColor(new Color(120, 0, 40));
                    g2.drawString("->", x + 3 - tileSize, y + 3);
                    g2.setColor((Color.white));
                    g2.drawString("->", x - tileSize, y);
                }
                g2.drawString(texto, x, y);
                y += tileSize;
            }
        }


    // Tela inicial 2 (seleção de personagem)
        else if(getTelaInicialState() == 1) {
            desenharFundoMao();
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            int y;
            escreverTexto("Escolha seu personagem.", y = tileSize*3);

            String[] opcoes = {"A GUERREIRA", "O SOBREVIVENTE", "O MÉDICO", "A CAÇADORA", "Voltar ao início"};

            y += tileSize * 3;
            for (int i = 0; i < opcoes.length; i++) {
                String texto = opcoes[i];
                int x = coordenadaXParaTextoCentralizado(texto);

                if (numComando == i) {
                    g2.drawString(">", x - tileSize, y);
                }
                g2.drawString(texto, x, y);
                y += tileSize;
            }
        }
    }

    public void mostrarTutorialControles() {
        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(20, 0, 10));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        int y = tileSize * 4;
        g2.setColor(Color.white);
        escreverTexto("CONTROLES", y);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));;
        escreverTexto(" ", y += tileSize);
        escreverTexto("- A tecla [W] sobe a seleção no painel de opções", y += tileSize);
        escreverTexto("- A tecla [S] desce a seleção no painel de opções.", y += tileSize);
        escreverTexto("- Pressione [ENTER] no painel para escolher sua opção.", y += tileSize);
        escreverTexto("- Clique em botões ([CONTINUAR] / [VOLTAR]) com o cursor do mouse.", y += tileSize);
    }

    public void mostrarGameOverScreen() {

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));;
        int y = tileSize * 4;
        g2.setColor(Color.red);

        escreverTexto("FIM DE JOGO", y += tileSize);
        escreverTexto("VOCÊ MORREU", y += tileSize);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 10F));;
        g2.setColor(Color.lightGray);
        escreverTexto("Em outra vida, talvez, você soubesse qual o sentido de tudo isso...", y += tileSize);


        botoes.mostrarBotaoContinuar();
    }

    public void mostrarAbertura() {

        Teclado teclado = new Teclado(painel);

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(20, 0, 10));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));;
        int y = tileSize * 2;
        g2.setColor(Color.white);

        escreverTexto("Você é: " + painel.getJogador().getNome(), y += tileSize);
        escreverTexto("Você acorda em um mundo desconhecido e inóspito.", y += tileSize);
        escreverTexto("É noite. O chão treme ao andar sobre ele,", y += tileSize);
        escreverTexto("e o céu vasto aparenta ter ânsia em te engolir.", y += tileSize);
        escreverTexto("Seus arredores lembram uma clareira. Há uma luz à distância.", y += tileSize);
        escreverTexto("Sem memória de como chegou aqui, ou idéia de como escapar,", y += tileSize);
        escreverTexto("sua única opção é descobrir explorando.", y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Você adentra a FLORESTA MACABRA...", y += tileSize);

        botoes.mostrarBotaoContinuar();
    }

    public void mostrarPlayState() {

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(Color.black);
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize;

        switch (subState) {
            case 0:
                escreverTexto("A luz misteriosa brilha à distância, floresta adentro.", y += tileSize);
                escreverTexto("Espalhadas pelo chão, há coisas que parecem ser úteis.", y += tileSize);
                escreverTexto("Uma brecha por entre as árvores revela uma montanha próxima.", y += tileSize);
                escreverTexto("Altitude pode ser uma vantagem.", y += tileSize);
                escreverTexto("Um chiado estranho parece se aproximar...", y += tileSize);
                escreverTexto("O que fazer?", y += tileSize);


                String[] opcoes0 = {"Seguir luz", "Ficar e coletar recursos", "Ir até montanha"};

                y += tileSize * 2;
                for (int i = 0; i < opcoes0.length; i++) {
                    String texto = opcoes0[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;

            case 1:
                escreverTexto("Você deixa a luz te guiar...", y += tileSize);
                escreverTexto(". . .", y += tileSize);
                escreverTexto("Encontrou no caminho: 1 pedra.", y += tileSize);
                painel.getJogador().adicionarItem("Pedra", 1);
                break;

            case 2:
                escreverTexto("Você busca por recursos.", y += tileSize);
                escreverTexto("Encontrou: 2 madeiras e 1 pedra.", y += tileSize);

                painel.getJogador().adicionarItem("Madeira", 2);
                painel.getJogador().adicionarItem("Pedra", 1);
                break;

            case 3:
                escreverTexto("Você se direciona até a montanha", y += tileSize);
                escreverTexto(". . .", y += tileSize);
                escreverTexto("Algo não parce certo.", y += tileSize);
                escreverTexto("Um movimento suspeito te acompanha.", y += tileSize);
                escreverTexto("Nada se revela, mas você não está sozinho.", y += tileSize);
                escreverTexto(". . .", y += tileSize);
                break;

            case 10:
                escreverTexto("Arbustos chacoalham ao seu redor enquanto anda.", y += tileSize);
                escreverTexto("...", y += tileSize);
                escreverTexto("Você está perto o suficiente da luz... é uma fogueira", y += tileSize);
                escreverTexto("Nínguem a vista, mas há uma tigela com água...", y += tileSize);
                escreverTexto("O que fazer?", y += tileSize);
                escreverTexto("", y += tileSize);


                String[] opcoes10 = {"Beber água", "Explorar arredores"};

                y += tileSize * 2;
                for (int i = 0; i < opcoes10.length; i++) {
                    String texto = opcoes10[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;

            case 11:
                escreverTexto("Você bebe água.", y += tileSize);
                jogador.setSede(false);
                escreverTexto("Hidratação no máximo.", y += tileSize);
                break;

            case 12:
                escreverTexto("Melhor não mexer com o que não é seu.",y += tileSize);
                escreverTexto("(e, afinal, quem sabe de quem pode ser...)",tileSize * 3);
                escreverTexto("",tileSize * 4);
                escreverTexto("Em torno dessa fogueira há vegetação baixa.",tileSize * 5);
                escreverTexto("Há um cervo à distância, mas você não tem equipamento para caça.",tileSize * 6);
                escreverTexto("",tileSize * 7);
                escreverTexto("Mais adiante, um barulho animador: água corrente!",tileSize * 8);
                escreverTexto("Apressando o passo, em minutos você chega ao lago.",tileSize * 9);
                break;
            case 1212:
                escreverTexto("Este é o lago.",tileSize * 2);
                escreverTexto("Você pode ficar e descansar, ou retornar à fogueira.",tileSize * 3);
                break;

            case 1213:
                escreverTexto("Você retorna a atenção à fogueira.", y += tileSize);

                String[] opcoes1213 = {"Beber a água", "Ir ao lago"};

                y += tileSize;
                for (int i = 0; i < opcoes1213.length; i++) {
                    String texto = opcoes1213[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;

            case 20:
                escreverTexto("Aquele chiado... parece estar tão perto...", tileSize * 2);
                evento.viboraRubroFloresta(g2);
                // NÃO DEFINI PROBABILIDADE POIS SEMPRE QUERO QUE ESTE EVENTO ACONTECA NESSE CASO
                break;

            case 21:
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F)); escreverTexto("COMBATE", y += tileSize);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F)); escreverTexto("Você a atacou!", y += tileSize);
                g2.setColor(Color.red); escreverTexto("-1HP", y += tileSize);
                g2.setColor(Color.red); escreverTexto("Víbora-Rubro: 2HP", y += tileSize);
                g2.setColor(Color.white); escreverTexto("-Após notar sua investida, ela foge! HA!", y += tileSize);
                escreverTexto("", y += tileSize);
                escreverTexto("FIM DE COMBATE.", y += tileSize);
                escreverTexto("", y += tileSize);
                escreverTexto("DESFECHO: Você está levemente ENVENENADO.", y += tileSize);
                break;

            case 22:
                escreverTexto("Você pensa em fugir, mas se sente meio tonto...",tileSize * 2);
                escreverTexto("Porcaria, a mordida da víbora o deixou", y += tileSize * 2);
                g2.setColor(Color.red); escreverTexto("envenenado.", y += tileSize);
                g2.setColor(Color.white); escreverTexto("", y += tileSize);
                escreverTexto("Ela é rápida, e você está abatido(a). É melhor não arriscar.", y += tileSize);

                botoes.mostrarBotaoVoltar();
                break;


            case 30:
                escreverTexto("Atingido o pé da montanha, você enxerga um trecho de subida.", y += tileSize);
                escreverTexto("O que quer que estava te seguindo aparenta ter parado...", y += tileSize);
                escreverTexto("Não há nada de destaque neste local, além de uma rocha firme.", y += tileSize);
                escreverTexto("Sua formação pode conceder um abrigo com boa visibilidade.", y += tileSize);
                escreverTexto("O que fazer?", y += tileSize);
                escreverTexto("", y += tileSize);


                String[] opcoes30 = {"Subir pelo trecho", "Descansar até o amanhecer"};

                y += tileSize * 2;
                for (int i = 0; i < opcoes30.length; i++) {
                    String texto = opcoes30[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;

            case 31:
                escreverTexto("Você sobe o trecho semi-íngreme.", y += tileSize);
                escreverTexto("Olhando pra trás, há um olhar a espreita.", y += tileSize);
                escreverTexto("(Ainda bem que não fiquei).", y += tileSize);
                escreverTexto("", y += tileSize);
                escreverTexto("Você chega num paredão.", y += tileSize);
                break;
            case 3131:
                escreverTexto("O trecho acabou. O único caminho para além daqui...",tileSize * 2);
                escreverTexto("é para cima. A montanha sussura seu nome...",tileSize * 3);
                escreverTexto("",tileSize * 4);
                escreverTexto("Seria fatal escalar o paredão sem equipamentos e preparação.",tileSize * 5);
                escreverTexto("E ficar congelando aqui não é uma opção.",tileSize * 6);
                escreverTexto("Isso é um beco sem saída. Melhor retornar.",tileSize * 7);
                break;

            case 32:
                escreverTexto("Você se dirige àquela rocha para um abrigo temporário.", y += tileSize);
                escreverTexto("Vai ter que servir. Você já se afastou demais da clareira.", y += tileSize);
                escreverTexto("A energia aqui não é ideal, mas não parece ter mais nada à espreita", y += tileSize);
                escreverTexto("De olhos entreabertos, você deita para descansar...", y += tileSize);
                break;

            case 33:
                // Deixei de lado a questão de probabilidade por enquanto, mas será implementada aqui
                    evento.ursoPai(g2);
                break;
            case 34:
                escreverTexto("Você tenta--", y += tileSize);
                g2.setColor(Color.red); escreverTexto("O urso irrompe um golpe fatal. -10HP", y += tileSize);
                break;



            default: System.out.println("default"); System.out.println(painel.getPlaySubState()); break;
        }
    }


    public void mostrarCardFloresta() {

        Ambiente ambienteFloresta = painel.getAmbiente();

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(5, 20, 5));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));

        int y = tileSize * 3;
        g2.setColor(Color.white);
        escreverTexto(ambienteFloresta.getNome(), y += tileSize);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        y = tileSize * 5;

        escreverTexto(ambienteFloresta.getDescricao(), y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Condições de exploração: " + ambienteFloresta.getDificuldade(), y += tileSize);
        escreverTexto("Recursos possíveis: " + ambienteFloresta.getRecursos(), y += tileSize);
        escreverTexto("Ecossistema: " + ambienteFloresta.getFrequenciaEventos(), y += tileSize);
        escreverTexto("Clima: " + ambienteFloresta.getClima(), y += tileSize);

        botoes.mostrarBotaoContinuar();
    }

    public void mostrarCardLago() {

        Ambiente ambienteLago = painel.getAmbiente();

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(115, 155, 255));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));

        int y = tileSize * 3;
        g2.setColor(Color.white);
        escreverTexto(ambienteLago.getNome(), y += tileSize);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        y = tileSize * 5;

        escreverTexto(ambienteLago.getDescricao(), y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Condições de exploração: " + ambienteLago.getDificuldade(), y += tileSize);
        escreverTexto("Recursos possíveis: " + ambienteLago.getRecursos(), y += tileSize);
        escreverTexto("Ecossistema: " + ambienteLago.getFrequenciaEventos(), y += tileSize);
        escreverTexto("Clima: " + ambienteLago.getClima(), y += tileSize);

        botoes.mostrarBotaoContinuar();
    }

    public void mostrarCardMontanha() {

        Ambiente ambienteMontanha = painel.getAmbiente();

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(180, 180, 180));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));

        int y = tileSize * 3;
        g2.setColor(Color.white);
        escreverTexto(ambienteMontanha.getNome(), y += tileSize);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        y = tileSize * 5;

        escreverTexto(ambienteMontanha.getDescricao(), y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Condições de exploração: " + ambienteMontanha.getDificuldade(), y += tileSize);
        escreverTexto("Recursos possíveis: " + ambienteMontanha.getRecursos(), y += tileSize);
        escreverTexto("Ecossistema: " + ambienteMontanha.getFrequenciaEventos(), y += tileSize);
        escreverTexto("Clima: " + ambienteMontanha.getClima(), y += tileSize);

        botoes.mostrarBotaoContinuar();
    }


    public int coordenadaXParaTextoCentralizado(String texto) {

        int larguraTela = painel.getLargura();

        int comprimento = (int) g2.getFontMetrics().getStringBounds(texto, g2).getWidth();
        int x = larguraTela / 2 - comprimento / 2;
        return x;

    }

    public String escreverTexto(String texto, int y) {
        tileSize = painel.getTileSize();
        int x = coordenadaXParaTextoCentralizado(texto);
        g2.drawString(texto, x, y);
        return texto;
    }

    public void desenharFundoMao() {
        g2.drawImage(titleBackground, 0, 0, larguraTela, alturaTela, null);
    }

// Getters e setters
    public int getTelaInicialState() {
        return telaInicialState;
    }
    public void setTelaInicialState(int telaInicialState) {
        this.telaInicialState = telaInicialState;
    }

    public int getNumComando() {
        return numComando;
    }
    public void setNumComando(int numComando) {
        this.numComando = numComando;
    }

    public Botões getBotoes() {
        return botoes;
    }
}