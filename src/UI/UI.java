package UI;

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
    private Jogador jogador;
    private Botoes botoes;

    private Graphics2D g2;
    private Font pixelsans_30, pixelsans_60B;

    private int tileSize;
    private int larguraTela;
    private int alturaTela;

    private int numComando = 0;
    private int frame = 1;
    private int contadorChama = 1;
    private int telaInicialState = 0;

    private String recadoAutor;
    private int indiceChar = 0;
    private int frameCounterRecado = 0;
    private int framesPorLetra = 10;
    private float alphaFade = 0f;
    private boolean transicaoIniciada = false;
    private boolean transicaoFinalizada = false;


    public UI(Painel painel, Jogador jogador) {
        this.painel = painel;
        this.jogador = jogador;

        botoes = new Botoes(painel);

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

        // Title state
        if (gameState == titleState) {
            if (!transicaoFinalizada) {
                mostrarTextoDoAutor(g2);
            } else {
                mostrarTelaInicial();
            }
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
            mostrarStatus(g2);
            painel.getAmbienteAtual().playState(g2);
        }
        // Game over
        if (gameState == gameOverState) {
            mostrarGameOverScreen();
            painel.resetAposGameOver();
        }
    }

    public void mostrarStatus(Graphics2D g2) {
        tileSize = painel.getTileSize();
        int y = tileSize;
        int x = tileSize * 2/3;

        // Visualizar tatus do jogador
        String statusTxt = "STATUS"; g2.drawString(statusTxt, x, y);

        desenharStatus(x, y += tileSize/2);

        y = tileSize * 5/2;
        g2.setColor(Color.white);

        // Vida e ataque
        String vidaTxt = jogador.getVida() + "HP";
        String atkTxt = jogador.getAtaqueAtual() + " ATK";

        int xCentralHP = coordenadaXParaTextoCentralizado(g2, tileSize * 4,vidaTxt);
        int xCentralATK = coordenadaXParaTextoCentralizado(g2, tileSize * 4, atkTxt);

        g2.drawRect(x, y += tileSize/2, tileSize * 4, tileSize*2/3);
        g2.drawString(vidaTxt, xCentralHP, y += tileSize/2);

        g2.drawRect(x, y += tileSize/2, tileSize * 4, tileSize*2/3);
        g2.drawString(atkTxt, xCentralATK, y += tileSize/2);

        // Visualizar local e clima
        String textoLocal = painel.getPlaySubState() != 1 ? null : "ACAMPAMENTO";

        if (textoLocal != null) {
            x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(),textoLocal);
            g2.drawString(textoLocal, x, tileSize);
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
            int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(), texto);
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
                int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(), texto);

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

    public void desenharStatus(int x, int y) {
        BufferedImage fome = null;
        BufferedImage sede = null;
        BufferedImage energia = null;

        // Fome
        if (jogador.getFome() <= jogador.getFomeMax()) {
            fome = setupImagens("fome_cheia", "status");

            if (jogador.getFome() <= jogador.getFomeMax() * 9 / 10) {
                fome = setupImagens("fome_decente", "status");

                if (jogador.getFome() <= jogador.getFomeMax() * 6 / 10) {
                    fome = setupImagens("fome_mediana", "status");

                    if (jogador.getFome() <= jogador.getFomeMax() * 3 / 10) {
                        fome = setupImagens("fome_perigosa", "status");

                        if (jogador.getFome() <= jogador.getFomeMax() / 10) {
                            fome = setupImagens("fome_zerada", "status");
                        }
                    }
                }
            }
        }

        // Sede
        String nivelSede = jogador.getNivelSede();

        if (nivelSede.equals("alto")) {
            sede = setupImagens("sede_false", "status");
        } else if (nivelSede.equals("media")) {
            sede = setupImagens("sede_mid", "status");
        } else if (nivelSede.equals("baixa")) {
            sede = setupImagens("sede_true", "status");
        }

        // Energia
        if (jogador.getEnergia() >= jogador.getEnergiaMax()*3/4) {
            energia = setupImagens("energia_cheia", "status");
        } else if (jogador.getEnergia() > 0 && jogador.getEnergia() < jogador.getEnergiaMax()*3/4) {
            energia = setupImagens("energia_metade", "status");
        } else if (jogador.getEnergia() == 0) {
            energia = setupImagens("energia_vazia", "status");
        }

        // Desenho das imagens
        int borda = tileSize/8;
        int espacamento = 8;
        int rectLargura = tileSize + 2 * borda;

        g2.drawRect(x, y, rectLargura, rectLargura);
        g2.drawImage(fome, x + borda, y + borda, tileSize, tileSize, null);

        int x2 = x + rectLargura + espacamento;

        g2.drawRect(x2, y, rectLargura, rectLargura);
        g2.drawImage(sede, x2 + borda, y + borda, tileSize, tileSize, null);

        int x3 = x2 + rectLargura + espacamento;

        g2.drawRect(x3, y, rectLargura, rectLargura);
        g2.drawImage(energia, x3 + borda, y + borda, tileSize, tileSize, null);

    }

    public int coordenadaXParaTextoCentralizado(Graphics2D g2, int largura, String texto) {
        this.g2 = g2;

        int comprimento = (int) g2.getFontMetrics().getStringBounds(texto, g2).getWidth();
        return largura / 2 - comprimento / 2;

    }

    public String escreverTexto(String texto, int y) {
        tileSize = painel.getTileSize();
        int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(), texto);
        g2.drawString(texto, x, y);
        return texto;
    }

    public void updateFrames() {
        frame++;
        if (frame % 20 == 0) {
            contadorChama++;
            if (contadorChama > 3) {
                contadorChama = 1;
            }
        }
        if (this.recadoAutor != null) {
            frameCounterRecado++;
            if (frameCounterRecado >= framesPorLetra && indiceChar < recadoAutor.length()) {
                indiceChar++;
                frameCounterRecado = 0;
            }
            if (transicaoIniciada && alphaFade < 1.0f) {
                alphaFade += 0.005f;
                if (alphaFade >= 1.0f) {
                    transicaoFinalizada = true;
                    painel.setGameState(painel.getTitleState());
                }
            }
        }
    }


    // Métodos para telas especiais
    public void mostrarTextoDoAutor(Graphics2D g2) {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        recadoAutor = "Um projeto de Alex Lacava";

        int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(), recadoAutor);

        String textoVisivel = recadoAutor.substring(0, Math.min(indiceChar, recadoAutor.length()));
        g2.drawString(textoVisivel, x, painel.getAltura()/2);

        if (indiceChar >= recadoAutor.length() && !transicaoIniciada) {
            transicaoIniciada = true;
        }

        if (transicaoIniciada) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFade));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    public void mostrarAcampamento() {

        tileSize = painel.getTileSize();
        int y = tileSize * 3;

        escreverTexto("Você está em sua base.", y += tileSize);

        g2.setColor(Color.yellow);
        boolean fonteAlimento = painel.getAmbienteAtual().isBaseFonteDeAlimento();
        String plantio = fonteAlimento ? "sementes plantadas." : "não há fonte de alimento.";
        escreverTexto("Fonte de alimento: " + plantio, y += tileSize);

        boolean fogoAceso = painel.getAmbienteAtual().isBaseFogoAceso();
        String fogo = fogoAceso ? "aceso e forte." : "não há fogueira acesa na base.";
        escreverTexto("Fogo: " + fogo, y += tileSize);

        int fortificacao = painel.getAmbienteAtual().getBaseFortificacao();
        escreverTexto("Fortificação: " + fortificacao + " / 20", y += tileSize);

        g2.setColor(Color.white);
        desenharOpcoes(new String[]{"Descansar", "Continuar a aventura"}, y += tileSize * 2, numComando);
    }

    public void mostrarInventario() {
        painel.getInvent().abrir();
        painel.repaint();
    }

    public void mostrarCardAmbiente() {
        painel.getAmbienteAtual().verCard();
        painel.repaint();
    }

    public void mostrarClima() {
        painel.getClima().verSituacaoClimatica();
        painel.repaint();
    }

    public void mostrarTelaInicial() {

        botoes.esconderBotaoContinuar();

        // Tela inicial 1 (autor)
        if (getTelaInicialState() == 0) {
            if (transicaoFinalizada) {
                setTelaInicialState(1);
            }
        }

        // Tela inicial 2 (entrada)
        if (getTelaInicialState() == 1) {

            tileSize = painel.getTileSize();
            larguraTela = painel.getLargura();
            alturaTela = painel.getAltura();

            // Fundo
            desenharPlanoDeFundo(fundoTitulo);
            // Título (com sombra)
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
            int y = tileSize * 5;
            int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(), "O MUNDO FUNESTO");

            g2.setColor((Color.black));
            g2.drawString("O MUNDO FUNESTO", x + 5, y + 5);
            g2.setColor(Color.white);
            escreverTexto("O MUNDO FUNESTO", y);

            //MENU
            desenharOpcoes(new String[]{"NOVO JOGO", "CONTROLES", "SAIR"}, y += tileSize * 2, numComando);
        }


        // Tela inicial 3 (seleção de personagem)
        else if(getTelaInicialState() == 2) {
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
        escreverTexto("Pressione [ESC] para sair de certas telas.", y += tileSize);
        escreverTexto("Para clicar em botões (como [VOLTAR]), use o mouse ou touchpad.", y += tileSize);
        escreverTexto("No inventário, aperte [ENTER] para selecionar armas ou alimento.", y += tileSize);
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