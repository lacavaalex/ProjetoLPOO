package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;

import java.awt.*;

public class UI {

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
    }

    public void mostrar(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(pixelsans_30);
        g2.setColor(Color.white);

        int gameState = painel.getGameState();
        int titleState = painel.getTitleState();
        int openingState = painel.getOpeningState();
        int playState = painel.getPlayState();
        int florestaCardState = painel.getFlorestaCardState();

    // Informações do jogador
        if (gameState != titleState && gameState!= openingState && gameState!=florestaCardState) {
        }

    // Title state
        if (gameState == titleState) {
            mostrarTelaInicial();
        }

    // Opening state
        if (gameState == openingState) {
            mostrarAbertura();
        }

    // Play state
        if (gameState == playState) {
            mostrarPlayState();
        }

    // Card da floresta
        if (gameState == florestaCardState) {
            mostrarCardFloresta();
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
            g2.setColor(new Color(20, 0, 10));
            g2.fillRect(0, 0, larguraTela, alturaTela);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
            int y = tileSize * 5;
            int x = coordenadaXParaTextoCentralizado("O MUNDO FUNESTO");

            //Sombra
            g2.setColor((Color.darkGray));
            g2.drawString("O MUNDO FUNESTO", x + 5, y + 5);

            g2.setColor(Color.white);
            escreverTexto("O MUNDO FUNESTO", y);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            String[] opcoes = {"NOVO JOGO", "CARREGAR JOGO", "SAIR"};

            y += tileSize * 2;
            for (int i = 0; i < opcoes.length; i++) {
                String texto = opcoes[i];
                x = coordenadaXParaTextoCentralizado(texto);

                if (numComando == i) {
                    g2.drawString(">", x - tileSize, y);
                }
                g2.drawString(texto, x, y);
                y += tileSize;
            }
        }


    // Tela inicial 2 (seleção de personagem)
        else if(getTelaInicialState() == 1) {
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            int y;
            escreverTexto("Escolha seu personagem.", y = tileSize*3);

            String[] opcoes = {"A Guerreira", "O Sobrevivente", "O Médico", "A Fora da Lei", "Voltar"};

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

    public void mostrarAbertura() {

        Teclado teclado = new Teclado(painel);

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));;
        int y = tileSize * 2;
        g2.setColor(Color.white);

        escreverTexto("Você é: " + painel.getJogador().getNome(), y += tileSize);
        escreverTexto("Você acorda em um mundo desconhecido e inóspito.", y += tileSize);
        escreverTexto("O chão treme ao andar sobre ele,", y += tileSize);
        escreverTexto("e o céu vasto aparenta ter ânsia em te engolir.", y += tileSize);
        escreverTexto(" ", y += tileSize);
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
        int y = tileSize;
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();

        if (subState == 0) {

            escreverTexto("A luz misteriosa brilha à distância, floresta adentro.", y += tileSize);
            escreverTexto("Espalhwadas pelo chão, há coisas que parecem ser úteis.", y += tileSize);
            escreverTexto("Uma brecha por entre as árvores revela uma montanha próxima.", y += tileSize);
            escreverTexto("Altitude pode ser uma vantagem.", y += tileSize);
            escreverTexto("Um chiado estranho parece se aproximar...", y += tileSize);
            escreverTexto("O que fazer?", y += tileSize);


            String[] opcoes = {"Seguir luz", "Ficar e coletar recursos", "Ir até montanha"};

            y += tileSize * 2;
            for (int i = 0; i < opcoes.length; i++) {
                String texto = opcoes[i];
                int x = coordenadaXParaTextoCentralizado(texto);

                if (numComando == i) {
                    g2.drawString(">", x - tileSize, y);
                }
                g2.drawString(texto, x, y);
                y += tileSize;
            }

        } else if (subState == 1) {
            escreverTexto("Você deixa a luz te guiar...", tileSize * 4);
            escreverTexto(". . .", tileSize * 5);
            escreverTexto("Encontrou no caminho: 1 pedra.", tileSize * 6);
            painel.getJogador().adicionarItem("Pedra", 1);


        } else if (subState == 2) {
            escreverTexto("Você busca por recursos.", tileSize * 4);
            escreverTexto("Encontrou: 2 madeiras e 1 pedra.", tileSize * 5);

            painel.getJogador().adicionarItem("Madeira", 2);
            painel.getJogador().adicionarItem("Pedra", 1);


        } else if (subState == 3) {
            escreverTexto("Você se direciona até a montanha", tileSize * 4);
        }

        else if (subState == 10) {
            escreverTexto("Arbustos chacoalham ao seu redor enquanto anda.", y += tileSize);
            escreverTexto("...", y += tileSize);
            escreverTexto("Você está perto o suficiente da luz... é uma fogueira", y += tileSize);
            escreverTexto("Nínguem a vista, só uma tigela com água.", y += tileSize);
            escreverTexto("Talvez haja algum lago próximo.", y += tileSize);
            escreverTexto("O que fazer?", y += tileSize);
            escreverTexto("", y += tileSize);


            String[] opcoes = {"Beber água", "Explorar arredores"};

            y += tileSize;
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
        else if (subState == 11) {
            escreverTexto("Você bebe água.", tileSize * 2);
            jogador.setSede(false);
            escreverTexto("Hidratação no máximo.",tileSize * 3);
        }

        else if (subState == 20) {
            escreverTexto("Aquele chiado... parece estar tão perto...", tileSize * 2);
            evento.viboraRubroFloresta(g2);
        }

        else if (subState == 21) {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F)); escreverTexto("COMBATE", tileSize * 2);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F)); escreverTexto("Você a atacou!", tileSize * 3);
            g2.setColor(Color.red); escreverTexto("-1HP", y += tileSize * 3);
            g2.setColor(Color.red); escreverTexto("Víbora-Rubro: 2HP", y += tileSize);
            g2.setColor(Color.white); escreverTexto("-Após notar sua investida, ela foge! HA!", y += tileSize);
            escreverTexto("", y += tileSize);
            escreverTexto("FIM DE COMBATE.", y += tileSize);
            escreverTexto("", y += tileSize);
            escreverTexto("DESFECHO: Você está levemente ENVENENADO.", y += tileSize);
        }
        else if (subState == 22) {
            escreverTexto("Você pensa em fugir, mas se sente meio tonto...",tileSize * 2);
            escreverTexto("Porcaria, a mordida da víbora o deixou", y += tileSize * 2);
            g2.setColor(Color.red); escreverTexto("envenenado.", y += tileSize);
            g2.setColor(Color.white); escreverTexto("", y += tileSize);
            escreverTexto("Ela é rápida, e você está abatido(a). É melhor não arriscar.", y += tileSize);

            botoes.mostrarBotaoVoltar();
        }
    }

    public void mostrarCardFloresta() {

        Ambiente ambienteFloresta = painel.getAmbiente();

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(5, 10, 5));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));

        int y = tileSize * 4;
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