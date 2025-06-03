package UI;

import Controles.*;
import Entidade.*;
import Main.Painel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;


public class UI {

    private BufferedImage fundoTitulo;
    private BufferedImage chama1, chama2, chama3, azul1, azul2, azul3;
    private BufferedImage ursoFace1, ursoFace2, ursoFace3;
    private BufferedImage imagemClima;

    private Painel painel;
    private Jogador jogador;
    private Botoes botoes;

    private Graphics2D g2;
    private Font pixelsans_30;

    private int tileSize;
    private int larguraTela;
    private int alturaTela;

    private int numComando = 0;
    private int frame = 1;
    private int contadorChama = 1;
    private int contadorUrso = 1;
    private int direcaoUrso = 1;
    private int telaInicialState = 0;

    private StringBuilder nomeDigitado = new StringBuilder();
    private boolean digitandoNome = false;
    private boolean digitacaoConfirmada = false;

    // Atributos do efeito de transição
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

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            pixelsans_30 = new Font("Pixel Sans Serif", Font.PLAIN, 30);
        }

    // Atribuição de imagens
        fundoTitulo = setupImagens("fundo_mao", "background");
        chama1 = setupImagens("chama-1", "animacao");
        chama2 = setupImagens("chama-2", "animacao");
        chama3 = setupImagens("chama-3", "animacao");
        azul1 = setupImagens("chama_azul-1", "animacao");
        azul2 = setupImagens("chama_azul-2", "animacao");
        azul3 = setupImagens("chama_azul-3", "animacao");
        ursoFace1 = setupImagens("urso_face-1", "animacao");
        ursoFace2 = setupImagens("urso_face-2", "animacao");
        ursoFace3 = setupImagens("urso_face-3", "animacao");
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

        int xCentralHP = coordenadaXParaTextoCentralizado(g2, tileSize * 4, vidaTxt);
        int xCentralATK = coordenadaXParaTextoCentralizado(g2, tileSize * 4, atkTxt);

        g2.drawRect(x, y += tileSize/2, tileSize * 4, tileSize*2/3);
        g2.drawString(vidaTxt, xCentralHP, y += tileSize/2);

        g2.drawRect(x, y += tileSize/2, tileSize * 4, tileSize*2/3);
        g2.drawString(atkTxt, xCentralATK, y += tileSize/2);

        if (jogador.estaEnvenenado()) {
            g2.setColor(Color.black);
            g2.drawString("[ ENVENENADO ]", x + 3, y + 3 + tileSize);
            g2.setColor(Color.MAGENTA);
            g2.drawString("[ ENVENENADO ]", x, y += tileSize);
        }
        g2.setColor(Color.white);

        // Visualizar local e clima
        String textoLocal = painel.getPlaySubState() != 1 ? null : "ACAMPAMENTO";

        if (textoLocal != null) {
            x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(),textoLocal);
            g2.drawString(textoLocal, x, tileSize);
        }

        String nomeImagem = switch (painel.getEventoClimatico().getClima()) {
            case "chuva" -> "clima_chuva";
            case "tempestade" -> "clima_tempestade";
            case "tornado" -> "clima_tornado";
            case "cavernoso" -> "clima_cavernoso";
            case "salgado" -> "clima_salgado";
            case "nevasca" -> "clima_nevasca";
            default -> "clima_ameno";
        };

        imagemClima = setupImagens(nomeImagem, "clima");
        g2.drawImage(imagemClima,painel.getLargura() - painel.getLargura()/8, painel.getAltura()/6, 130, 50, null);
    }

    // Métodos para telas especiais
    public void mostrarTextoDoAutor(Graphics2D g2) {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        recadoAutor = "Um projeto por Alex Lacava";

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
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

        if (!jogador.getLocalizacao().equals("MONTANHA EPOPEICA")) {

            escreverTexto("Você está em sua base.", y += tileSize);

            g2.setColor(Color.yellow);

            boolean fonteAlimento = painel.getAmbienteAtual().isBaseFonteDeAlimento();
            boolean colheitaPronta = painel.getAmbienteAtual().isColheitaPronta();
            boolean fogoAceso = painel.getAmbienteAtual().isBaseFogoAceso();

            String plantio = fonteAlimento ? "sementes plantadas. Retorne em breve." : "não há fonte de alimento.";
            if (painel.getAmbienteAtual().isColheitaPronta()) {
                plantio = "pronta para colher.";
            }
            escreverTexto("Fonte de alimento: " + plantio, y += tileSize);

            String fogo = fogoAceso ? "aceso e forte." : "não há fogueira acesa na base.";
            escreverTexto("Fogo: " + fogo, y += tileSize);

            int fortificacao = painel.getAmbienteAtual().getBaseFortificacao();
            escreverTexto("Fortificação: " + fortificacao + " / 20", y += tileSize);

            g2.setColor(Color.white);

            ArrayList<String> opcoes = new ArrayList<>();
            opcoes.add("Continuar a aventura");
            if (fogoAceso) opcoes.add("Descansar");
            if (fonteAlimento && colheitaPronta) opcoes.add("Comer");

            desenharOpcoes(opcoes.toArray(new String[0]), y += tileSize * 2, numComando);
        }

        else {
            escreverTexto("É impossível montar ou acessar base neste clima", painel.getAltura()/2 - tileSize);
            desenharOpcoes(new String[]{"Continuar a aventura"}, painel.getAltura()/2, numComando);
        }
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

        botoes.esconderBotao("Continuar");

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
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 45F));
            int y = tileSize * 2;
            int x = tileSize * 2;

            g2.setColor((Color.black));
            g2.drawString("O MUNDO FUNESTO", x + 5, y + 5);
            g2.setColor(Color.white);
            g2.drawString("O MUNDO FUNESTO", x, y);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            //MENU
            desenharOpcoes(new String[]{"NOVO JOGO", "CONTROLES", "ENCERRAR"}, y += tileSize * 5/2, numComando);
        }


        // Tela inicial 3 (nome do jogador)
        else if (getTelaInicialState() == 2) {
            desenharPlanoDeFundo(fundoTitulo);
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            int y = tileSize * 3;
            escreverTexto("Digite seu nome.", y);
            escreverTexto(nomeDigitado.toString(), y += tileSize * 3);

            mensagemNomeInvalido();
        }

        // Tela inicial 4 (seleção de habilidade)
        else if (getTelaInicialState() == 3) {
            desenharPlanoDeFundo(fundoTitulo);
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            int y = tileSize * 3;
            escreverTexto("Escolha sua especialidade.", y);

            desenharOpcoes(new String[]{"COMBATE", "SOBREVIVÊNCIA", "SAÚDE", "RASTREAMENTO", "Voltar ao início"}, y += tileSize * 3, numComando);
        }
    }

    public void mostrarTutorialControles() {
        tileSize = painel.getTileSize();

        g2.setColor(new Color(20, 0, 10));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
        int y = tileSize * 2;
        g2.setColor(Color.white);
        escreverTexto("CONTROLES", y);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));;
        escreverTexto("Enquanto estiver no menu de seleção de opção:", y += tileSize);
        escreverTexto("- Pressione [W]/[S] ou [UP]/[DOWN] para subir/descer o cursor.", y += tileSize);
        escreverTexto("- Pressione [ENTER] para escolher sua opção.", y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Pressione [ESC] para sair de certas telas.", y += tileSize);
        escreverTexto("Para clicar em botões (como [VOLTAR]), use o mouse ou touchpad.", y += tileSize);
        escreverTexto("Escolha a habilidade do seu personagem para benefícios específicos.", y += tileSize);
        escreverTexto("No inventário, aperte [ENTER] para selecionar armas ou alimento.", y += tileSize);
        escreverTexto("No combate, você pode ATACAR, ESQUIVAR, BLOQUEAR ou FUGIR.", y += tileSize);
    }

    public void mostrarGameOverScreen() {

        botoes.esconderBotao("Clima");

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

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));;
        int y = tileSize * 2;
        g2.setColor(Color.white);

        escreverTexto(painel.getJogador().getNome() + ",", y += tileSize);
        escreverTexto("Você acorda em um mundo desconhecido e inóspito. Está de noite.", y += tileSize);
        escreverTexto("O chão treme ao andar sobre ele, e o céu vasto aparenta ter", y += tileSize);
        escreverTexto("ânsia em te engolir. Tudo que você tem é uma mochila vazia,", y += tileSize);
        escreverTexto("e a vaga memória de alguma experiência " + jogador.getHabilidade().toLowerCase() + " passada...", y += tileSize);
        escreverTexto("Seus arredores lembram uma clareira. Há uma luz à distância.", y += tileSize);
        escreverTexto("Sem memória de como chegou aqui, ou idéia de como escapar,", y += tileSize);
        escreverTexto("sua única opção é descobrir explorando.", y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Você adentra a FLORESTA MACABRA...", y += tileSize);

        botoes.mostrarBotao("Continuar");
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

    public void desenharOpcoes(String[] opcoes, int yInicial, int numComando) {

        int y = yInicial;

        if (opcoes.length == 1) {
            String texto = opcoes[0];
            int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(), texto);

            g2.setColor(Color.white);
            g2.drawString(texto, x, y);

            desenharContadorChama(x, y);

            if (painel.getCombate().getCriaturaEmCombate() != null
                    && painel.getCombate().getCriaturaEmCombate().isBoss()) {
                g2.setColor(Color.blue);
            }
            else { g2.setColor(Color.red); }

            g2.drawString(texto, x, y);
        }

        else {
            for (int i = 0; i < opcoes.length; i++) {
                String texto = opcoes[i];

                int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(), texto);
                if (painel.getGameState() == painel.getTitleState() && opcoes.length == 3) {
                    x = y + tileSize;
                }

                if (numComando == i) {
                    desenharContadorChama(x, y);
                }

                if (numComando == i) {
                    if (painel.getCombate().getCriaturaEmCombate() != null
                            && painel.getCombate().getCriaturaEmCombate().isBoss()) {
                        g2.setColor(Color.blue);
                    }
                    else { g2.setColor(Color.red); }
                }
                else { g2.setColor(Color.white); }

                g2.drawString(texto, x, y);
                y += tileSize;
            }
        }
        g2.setColor(Color.white);
    }

    public void desenharContadorChama(int x, int y) {
        if (painel.getCombate().getCriaturaEmCombate() != null
                && painel.getCombate().getCriaturaEmCombate().isBoss()) {

            switch (contadorChama) {
                case 1: g2.drawImage(azul1, x - 65, y - 30, tileSize, tileSize, null); break;
                case 2: g2.drawImage(azul2, x - 65, y - 30, tileSize, tileSize, null); break;
                case 3: g2.drawImage(azul3, x - 65, y - 30, tileSize, tileSize, null); break;
            }
        }
        else {
            switch (contadorChama) {
                case 1: g2.drawImage(chama1, x - 65, y - 30, tileSize, tileSize, null); break;
                case 2: g2.drawImage(chama2, x - 65, y - 30, tileSize, tileSize, null); break;
                case 3: g2.drawImage(chama3, x - 65, y - 30, tileSize, tileSize, null); break;
            }
        }
    }

    public void desenharAnimacaoUrso() {
        if (painel.getDialogueState() == 4 || painel.getDialogueState() == 9) {
            painel.getAmbienteAtual().desenharImagemZoom(g2, ursoFace1);
        }
        else {
            switch (contadorUrso) {
                case 1: painel.getAmbienteAtual().desenharImagemZoom(g2, ursoFace1); break;
                case 2: painel.getAmbienteAtual().desenharImagemZoom(g2, ursoFace2); break;
                case 3: painel.getAmbienteAtual().desenharImagemZoom(g2, ursoFace3); break;
            }
        }
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

    public void desenharPlanoDeFundo(BufferedImage imagem) {
        g2.drawImage(imagem, 0, 0, larguraTela, alturaTela, null);
    }

    public void desenharStatus(int x, int y) {
        BufferedImage fome = null;
        BufferedImage sede = null;
        BufferedImage energia = null;

        // Fome
        if (jogador.getFome() > jogador.getFomeMax() * 4 / 5) {
            fome = setupImagens("fome_cheia", "status");
        }
        else if (jogador.getFome() <= jogador.getFomeMax() * 4 / 5) {
            fome = setupImagens("fome_decente", "status");
        }
        else if (jogador.getFome() <= jogador.getFomeMax() * 3 / 5) {
            fome = setupImagens("fome_mediana", "status");
        }
        else if (jogador.getFome() <= jogador.getFomeMax() * 2 / 5) {
            fome = setupImagens("fome_perigosa", "status");
        }
        else if (jogador.getFome() <= jogador.getFomeMax() / 5) {
            fome = setupImagens("fome_zerada", "status");
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

    public void mensagemNomeInvalido() {
        if (nomeDigitado.length() < 3 && !nomeDigitado.isEmpty()) {
            int largura = painel.getLargura();
            int y = painel.getAltura() - tileSize;
            String msg = "Nome muito curto!";
            g2.drawString(msg, coordenadaXParaTextoCentralizado(g2, largura, msg), y);
        }
    }

    public void updateFrames() {
        frame++;
        if (frame % 20 == 0) {
            contadorChama++;
            if (contadorChama > 3) {
                contadorChama = 1;
            }
        }

        if (frame % 20 == 0) {
            if (contadorUrso == 1) {
                direcaoUrso = 1;
            }
            else if (contadorUrso == 3) {
                direcaoUrso = -1;
            }
            contadorUrso += direcaoUrso;
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
                    alphaFade = 1.0f;
                    transicaoFinalizada = true;
                    painel.setGameState(painel.getTitleState());
                }
            }
        }
    }

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

    // Getters e setters
    public Jogador getJogador() { return jogador; }
    public Font getPixelsans_30() { return pixelsans_30; }

    public StringBuilder getNomeDigitado() { return nomeDigitado; }

    public boolean isDigitandoNome() { return digitandoNome; }
    public void setDigitandoNome(boolean digitandoNome) { this.digitandoNome = digitandoNome; }

    public boolean isDigitacaoConfirmada() { return digitacaoConfirmada; }
    public void setDigitacaoConfirmada(boolean digitacaoConfirmada) { this.digitacaoConfirmada = digitacaoConfirmada; }

    public int getTelaInicialState() { return telaInicialState; }
    public void setTelaInicialState(int telaInicialState) { this.telaInicialState = telaInicialState; }

    public void setGraphics(Graphics2D g2) { this.g2 = g2; }
}