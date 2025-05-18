package UI;

import Ambiente.*;
import Controles.*;
import Entidade.*;
import Main.Painel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class UI {

    private BufferedImage fundoTitulo, chama1, chama2, chama3, imagemClima;

    private Painel painel;
    private Jogador jogador = new Jogador();
    private Botoes botoes;
    private AmbienteFloresta floresta;
    private AmbienteLago lago;
    private AmbienteMontanha montanha;

    private Graphics2D g2;
    private Font pixelsans_30, pixelsans_60B;

    private int tileSize;
    private int larguraTela;
    private int alturaTela;

    private int numComando = 0;
    private int frame = 1;
    private int contadorChama = 1;
    private int telaInicialState = 0;


    public UI(Painel painel, Jogador jogador) {
        this.painel = painel;
        this.jogador = jogador;

        botoes = new Botoes(painel);
        floresta = new AmbienteFloresta(painel, jogador, this);
        lago = new AmbienteLago(painel, jogador, this);
        montanha = new AmbienteMontanha(painel, jogador, this);

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
        fundoTitulo = setupImagens("fundo_mao", "background");
        chama1 = setupImagens("chama-1", "animacao");
        chama2 = setupImagens("chama-2", "animacao");
        chama3 = setupImagens("chama-3", "animacao");
    }

    // Metodo geral de desenho
    public void mostrarInterface(Graphics2D g2) {
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
        if (gameState == playState && painel.getAmbienteAtual() != null) {
            mostrarStatusEAmbiente(g2);
            painel.getAmbienteAtual().playState(g2);
        }
        // Game over
        if (gameState == gameOverState) {
            mostrarGameOverScreen();
            painel.resetAposGameOver();
        }

        // Cards de ambiente
        if (gameState == florestaCardState) {
            floresta.construirCard(g2);
        }
        if (gameState == lagoCardState) {
            lago.construirCard(g2);
        }
        if (gameState == montanhaCardState) {
            montanha.construirCard(g2);
        }
    }

    public void mostrarStatusEAmbiente(Graphics2D g2) {
        tileSize = painel.getTileSize();
        int y = tileSize * 2;
        int x = tileSize + 5;

        // Visualizar tatus do jogador
        String status = "STATUS"; g2.drawString(status, x, y);
        String textovida = jogador.getVida() + "HP"; g2.drawString(textovida, x, y += tileSize);
        String textoataque = jogador.getAtaqueAtual() + " ATK"; g2.drawString(textoataque, x, y += tileSize);

        // Visualizar local e clima atuais
        String textolocal = jogador.getLocalizacao();
        if (textolocal != null) {
            x = coordenadaXParaTextoCentralizado(g2,textolocal);
            g2.drawString(textolocal, x, tileSize);
        }

        String nomeImagem;
        switch (painel.getEventoClimatico().getClima()) {
            case "chuva":
                nomeImagem = "clima_chuva";
                break;
            default:
                nomeImagem = "clima_ameno";
                break;
        }

        imagemClima = setupImagens(nomeImagem, "clima");
        g2.drawImage(imagemClima,painel.getLargura() - painel.getLargura()/8, painel.getAltura()/6, 130, 50, null);
    }

    // Métodos de compactação de código
    public BufferedImage setupImagens(String nomeImagem, String tipo) {
        BufferedImage imagem = null;
        try {
            imagem = ImageIO.read(getClass().getResource("/Imagens_" + tipo +"/" + nomeImagem + ".png"));
            if (imagem == null) {
                System.out.println("Imagem não carregada: " + nomeImagem);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem: " + nomeImagem);
            e.printStackTrace();
        }
        return imagem;
    }

    public void desenharPlanoDeFundo(BufferedImage imagem) {
        g2.drawImage(imagem, 0, 0, larguraTela, alturaTela, null);
    }

    public void desenharOpcoes(String[] opcoes, int yInicial, int numComando) {

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

        int y = yInicial;

        if (opcoes.length == 1) {
            String texto = opcoes[0];
            int x = coordenadaXParaTextoCentralizado(g2, texto);
            g2.setColor(Color.white);
            g2.drawString(texto, x, y);

            switch (contadorChama) {
                case 1: g2.drawImage(chama1, x - 65, y - 30, tileSize, tileSize, null); break;
                case 2: g2.drawImage(chama2, x - 65, y - 30, tileSize, tileSize, null); break;
                case 3: g2.drawImage(chama3, x - 65, y - 30, tileSize, tileSize, null); break;
            }
            g2.setColor(Color.red);
            g2.drawString(texto, x, y);
        }

        else {
            for (int i = 0; i < opcoes.length; i++) {
                String texto = opcoes[i];
                int x = coordenadaXParaTextoCentralizado(g2, texto);

                if (numComando == i) {
                    switch (contadorChama) {
                        case 1: g2.drawImage(chama1, x - 65, y - 30, tileSize, tileSize, null); break;
                        case 2: g2.drawImage(chama2, x - 65, y - 30, tileSize, tileSize, null); break;
                        case 3: g2.drawImage(chama3, x - 65, y - 30, tileSize, tileSize, null); break;
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
        g2.setColor(Color.white);
    }

    public int coordenadaXParaTextoCentralizado(Graphics2D g2, String texto) {
        this.g2 = g2;
        int larguraTela = painel.getLargura();

        int comprimento = (int) g2.getFontMetrics().getStringBounds(texto, g2).getWidth();
        return larguraTela / 2 - comprimento / 2;

    }

    public String escreverTexto(String texto, int y) {
        tileSize = painel.getTileSize();
        int x = coordenadaXParaTextoCentralizado(g2, texto);
        g2.drawString(texto, x, y);
        return texto;
    }

    public void updateFrames() {
        frame++;
        if (frame % 20 == 0) { // muda a cada 10 frames
            contadorChama++;
            if (contadorChama > 3) {
                contadorChama = 1;
            }
        }
    }


    // Métodos para telas especiais
    public void mostrarInventario() {
        painel.getInvent().abrir();
        painel.repaint();
    }

    public void mostrarClima() {
        painel.getClima().verSituacaoClimatica();
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
            desenharPlanoDeFundo(fundoTitulo);
            // Título (com sombra)
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
            int y = tileSize * 5;
            int x = coordenadaXParaTextoCentralizado(g2, "O MUNDO FUNESTO");

            g2.setColor((Color.black));
            g2.drawString("O MUNDO FUNESTO", x + 5, y + 5);
            g2.setColor(Color.white);
            escreverTexto("O MUNDO FUNESTO", y);

            //MENU
            desenharOpcoes(new String[]{"NOVO JOGO", "CONTROLES", "SAIR"}, y += tileSize * 2, numComando);
        }


        // Tela inicial 2 (seleção de personagem)
        else if(getTelaInicialState() == 1) {
            desenharPlanoDeFundo(fundoTitulo);
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            int y;
            escreverTexto("Escolha seu personagem.", y = tileSize*3);

            desenharOpcoes(new String[]{"A GUERREIRA", "O SOBREVIVENTE", "O MÉDICO", "A CAÇADORA", "Voltar ao início"}, y += tileSize * 3, numComando);
        }
    }

    public void mostrarTutorialControles() {
        tileSize = painel.getTileSize();

        g2.setColor(new Color(20, 0, 10));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        int y = tileSize * 3;
        g2.setColor(Color.white);
        escreverTexto("CONTROLES", y);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));;
        escreverTexto(" ", y += tileSize);
        escreverTexto("Enquanto estiver no menu de seleção de opção:", y += tileSize);
        escreverTexto("- Pressione [W] ou [UP] para subir o cursor.", y += tileSize);
        escreverTexto("- Pressione [S] ou [DOWN] para descer o cursor.", y += tileSize);
        escreverTexto("- Pressione [ENTER] para escolher sua opção.", y += tileSize);
        escreverTexto("Pressione [ESC] para sair do inventário.", y += tileSize);
        escreverTexto("Para clicar em botões, como [VOLTAR], use o mouse ou touchpad.", y += tileSize);
    }

    public void mostrarGameOverScreen() {

        tileSize = painel.getTileSize();

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));;
        int y = tileSize * 4;
        g2.setColor(Color.red);

        escreverTexto("FIM DE JOGO", y += tileSize);
        escreverTexto("VOCÊ MORREU.", y += tileSize);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 10F));;
        g2.setColor(Color.lightGray);
        escreverTexto("Em outra vida, talvez, você soubesse qual o sentido de tudo isso...", y += tileSize);

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



    // Getters e setters
    public Painel getPainel() {
        return painel;
    }
    public Jogador getJogador() {
        return jogador;
    }
    public Font getPixelsans_30() {
        return pixelsans_30;
    }

    public int getTelaInicialState() { return telaInicialState; }
    public void setTelaInicialState(int telaInicialState) { this.telaInicialState = telaInicialState; }

    // Metodos de comando
    public void subtrairNumComando(int numOpcoes) {
        numComando--;
        if (numComando < 0) {
            numComando = numOpcoes - 1;
        }
    }
    public void adicionarNumComando(int numOpcoes) {
        numComando++;
        if (numComando > numOpcoes - 1) {
            numComando = 0;
        }
    }
    public int getNumComando() { return numComando; }
    public void setNumComando(int numComando) { this.numComando = numComando; }

    public void setGraphics(Graphics2D g2) { this.g2 = g2; }
}