package UI;

import Ambiente.Ambiente;
import Controles.*;
import Entidade.*;
import Main.Painel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class UI {

    BufferedImage titleBackground, chama1, chama2, chama3;

    Painel painel;
    Jogador jogador = new Jogador();
    Botões botoes;
    Criatura criatura;
    Ambiente ambiente;
    CardsEspeciaisUI cardsEspeciaisUI;

    Graphics2D g2;
    Font pixelsans_30, pixelsans_60B;

    private int tileSize;
    private int larguraTela;
    private int alturaTela;

    public int numComando = 0;
    private int frame = 1;
    private int contadorChama = 1;
    private int telaInicialState = 0;

    public UI(Painel painel, Jogador jogador) {
        this.painel = painel;
        this.jogador = jogador;

        botoes = new Botões(painel);
        criatura = new Criatura();
        ambiente = new Ambiente();

        // Atribuição da fonte
        try {
            // Carrega a fonte customizada a partir dos resources
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Fonte/PixelSansSerif.ttf"));

            pixelsans_30 = pixelFont.deriveFont(Font.PLAIN, 30f);
            pixelsans_60B = pixelFont.deriveFont(Font.BOLD, 60f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            pixelsans_30 = new Font("Pixel Sans Serif", Font.PLAIN, 30);
            pixelsans_60B = new Font("Pixel Sans Serif", Font.BOLD, 60);
        }

    // Atribuição de imagens
        titleBackground = setup("/Imagens/fundo_mao_2");
        chama1 = setup("/Imagens/chama-1");
        chama2 = setup("/Imagens/chama-2");
        chama3 = setup("/Imagens/chama-3");
    }

// Metodo geral de desenho
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
            // A acrescentar
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
            ambiente.playState(g2);
        }
        // Game over
        if (gameState == gameOverState) {
            mostrarGameOverScreen();
            painel.resetPlayState();
            jogador.resetVida();
        }

        // Cards de ambiente
        if (gameState == florestaCardState) {
            cardsEspeciaisUI.g2 = this.g2;
            cardsEspeciaisUI.cardFloresta();
        }
        if (gameState == lagoCardState) {
            cardsEspeciaisUI.g2 = this.g2;
            cardsEspeciaisUI.cardLago();
        }
        if (gameState == montanhaCardState) {
            cardsEspeciaisUI.g2 = this.g2;
            cardsEspeciaisUI.cardMontanha();
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

            // Fundo
            desenharFundoMao();
            // Título (com sombra)
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
            int y = tileSize * 5;
            int x = coordenadaXParaTextoCentralizado("O MUNDO FUNESTO");

            g2.setColor((Color.black));
            g2.drawString("O MUNDO FUNESTO", x + 5, y + 5);
            g2.setColor(Color.white);
            escreverTexto("O MUNDO FUNESTO", y);

            //MENU
            desenharOpcoes(new String[]{"NOVO JOGO", "CONTROLES", "SAIR"}, y += tileSize * 2);
        }


        // Tela inicial 2 (seleção de personagem)
        else if(getTelaInicialState() == 1) {
            desenharFundoMao();
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            int y;
            escreverTexto("Escolha seu personagem.", y = tileSize*3);

            desenharOpcoes(new String[]{"A GUERREIRA", "O SOBREVIVENTE", "O MÉDICO", "A CAÇADORA", "Voltar ao início"}, y += tileSize * 3);
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
        escreverTexto("Enquanto estiver no menu de seleção de opção:", y += tileSize);
        escreverTexto("- Pressione [W] ou [UP] para subir o cursor.", y += tileSize);
        escreverTexto("- Pressione [S] ou [DOWN] para descer o cursor.", y += tileSize);
        escreverTexto("- Pressione [ENTER] para escolher sua opção.", y += tileSize);
        escreverTexto("Para clicar em botões, como [MOCHILA], use o mouse ou touchpad.", y += tileSize);
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
        escreverTexto("Você acorda em um mundo desconhecido e inóspito. Está de noite.", y += tileSize);
        escreverTexto("O chão treme ao andar sobre ele, e o céu vasto aparenta ter", y += tileSize);
        escreverTexto("ânsia em te engolir. Tudo que você tem é uma mochila vazia.", y += tileSize);
        escreverTexto("Seus arredores lembram uma clareira. Há uma luz à distância.", y += tileSize);
        escreverTexto("Sem memória de como chegou aqui, ou idéia de como escapar,", y += tileSize);
        escreverTexto("sua única opção é descobrir explorando.", y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Você adentra a FLORESTA MACABRA...", y += tileSize);

        botoes.mostrarBotaoContinuar();
    }

// Métodos de compactação de código
    public BufferedImage setup(String caminhoImagem) {
        BufferedImage imagem = null;

        try {
            imagem = ImageIO.read(getClass().getResource(caminhoImagem + ".png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
        return imagem;
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

    public void desenharOpcoes(String[] opcoes, int yInicial) {

        update();

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

        int y = yInicial;

        for (int i = 0; i < opcoes.length; i++) {
            String texto = opcoes[i];
            int x = coordenadaXParaTextoCentralizado(texto);

            if (numComando == i) {
                switch (contadorChama) {
                    case 1:
                        g2.drawImage(chama1, x - 65, y - 30, tileSize, tileSize, null);
                        break;
                    case 2:
                        g2.drawImage(chama2, x - 65, y - 30, tileSize, tileSize, null);
                        break;
                    case 3:
                        g2.drawImage(chama3, x - 65, y - 30, tileSize, tileSize, null);
                        break;
                }
            }
            if (numComando == i) {
                g2.setColor(Color.red);
            } else {
                g2.setColor(Color.white);
            }
            g2.drawString(texto, x, y);
            y += tileSize;
        }
    }

    public void desenharFundoMao() {
        g2.drawImage(titleBackground, 0, 0, larguraTela, alturaTela, null);
    }

    public void update() {
        frame++;
        if (frame % 20 == 0) { // muda a cada 10 frames
            contadorChama++;
            if (contadorChama > 3) {
                contadorChama = 1;
            }
        }
    }


// Getters e setters
    public int getTelaInicialState() { return telaInicialState; }
    public void setTelaInicialState(int telaInicialState) { this.telaInicialState = telaInicialState; }

    public void setAmbiente(Ambiente ambiente) { this.ambiente = ambiente; }
    public void setCardsEspeciaisUI(CardsEspeciaisUI cardsEspeciaisUI) { this.cardsEspeciaisUI = cardsEspeciaisUI; }

    public int getNumComando() { return numComando; }
    public void setNumComando(int numComando) { this.numComando = numComando; }
}