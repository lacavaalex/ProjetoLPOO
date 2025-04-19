package UI;

import Controles.*;
import Entidade.*;
import Evento.Evento;
import Main.Painel;

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
    PlayStateUI playStateUI;
    CardsAmbienteUI cardsAmbienteUI;

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
            playStateUI.g2 = this.g2;
            playStateUI.playState();
        }
        // Game over
        if (gameState == gameOverState) {
            mostrarGameOverScreen();
            painel.resetPlayState();
            jogador.resetVida();
        }

        // Cards de ambiente
        if (gameState == florestaCardState) {
            cardsAmbienteUI.g2 = this.g2;
            cardsAmbienteUI.cardFloresta();
        }
        if (gameState == lagoCardState) {
            cardsAmbienteUI.g2 = this.g2;
            cardsAmbienteUI.cardLago();
        }
        if (gameState == montanhaCardState) {
            cardsAmbienteUI.g2 = this.g2;
            cardsAmbienteUI.cardMontanha();
        }
    }

    // Métodos para telas especiais
    public void mostrarInventario() {
        painel.getInvent().abrir();
        painel.repaint();
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

        g2.setColor(new Color(20, 0, 10));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

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

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

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

        tileSize = painel.getTileSize();

        g2.setColor(new Color(20, 0, 10));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

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
    public void setPlayStateUI(PlayStateUI playStateUI) { this.playStateUI = playStateUI; }
    public void setCardsAmbienteUI(CardsAmbienteUI cardsAmbienteUI) { this.cardsAmbienteUI = cardsAmbienteUI; }

    public int getNumComando() {
        return numComando;
    }
    public void setNumComando(int numComando) {
        this.numComando = numComando;
    }
}