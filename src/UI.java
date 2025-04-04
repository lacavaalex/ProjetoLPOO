import java.awt.*;

public class UI {

    Painel painel;
    Graphics2D g2;
    Font pixelsans_30, pixelsans_60B;

    public int numComando = 0;
    private int telaInicialState = 0;

    public UI(Painel painel) {
        this.painel = painel;

        pixelsans_30 = new Font("Pixel Sans Serif", Font.PLAIN, 30);
        pixelsans_60B = new Font("Pixel Sans Serif", Font.BOLD, 60);
    }

    public void mostrar(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(pixelsans_30);
        g2.setColor(Color.white);

    // Title state
        int gameState = painel.getGameState();
        int titleState = painel.getTitleState();
        if (gameState == titleState) {
            mostrarTelaInicial();
        }

    // Play state
        int playState = painel.getPlayState();
        if (gameState == playState) {

        }
    }

    public void mostrarTelaInicial() {

        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

    // Tela inicial 1 (entrada)
        if (getTelaInicialState() == 0) {

            // Título
            g2.setColor(new Color(20, 0, 10));
            g2.fillRect(0, 0, larguraTela, alturaTela);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
            String texto = "O MUNDO FUNESTO.";
            int x = coordenadaXParaTextoCentralizado(texto);
            int y = tileSize * 5;

            //Sombra
            g2.setColor((Color.darkGray));
            g2.drawString(texto, x + 5, y + 5);

            g2.setColor(Color.white);
            g2.drawString(texto, x, y);

            // MENU (AINDA SEM COMANDOS)
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            texto = "NOVO JOGO";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize * 2;
            g2.drawString(texto, x, y);
            if (getNumComando() == 0) {
                g2.drawString(">", x - tileSize, y);
            }

            texto = "CARREGAR JOGO";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize;
            g2.drawString(texto, x, y);
            if (getNumComando() == 1) {
                g2.drawString(">", x - tileSize, y);
            }

            texto = "SAIR";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize;
            g2.drawString(texto, x, y);
            if (getNumComando() == 2) {
                g2.drawString(">", x - tileSize, y);
            }
        }
    // Tela inicial 2 (seleção de personagem)
        else if(getTelaInicialState() == 1) {
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            String texto = "Escolha seu personagem.";
            int x = coordenadaXParaTextoCentralizado(texto);
            int y = tileSize*3;
            g2.drawString(texto, x, y);

            texto = "A Guerreira";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize*3;
            g2.drawString(texto, x, y);
            if (getNumComando() == 0) {
                g2.drawString(">", x - tileSize, y);
            }

            texto = "O Sobrevivente";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize;
            g2.drawString(texto, x, y);
            if (getNumComando() == 1) {
                g2.drawString(">", x - tileSize, y);
            }

            texto = "O Médico";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize;
            g2.drawString(texto, x, y);
            if (getNumComando() == 2) {
                g2.drawString(">", x - tileSize, y);
            }

            texto = "A Fora da Lei";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize;
            g2.drawString(texto, x, y);
            if (getNumComando() == 3) {
                g2.drawString(">", x - tileSize, y);
            }

            texto = "Voltar";
            x = coordenadaXParaTextoCentralizado(texto);
            y += tileSize*2;
            g2.drawString(texto, x, y);
            if (getNumComando() == 4) {
                g2.drawString(">", x - tileSize, y);
            }
        }
    }

    public int coordenadaXParaTextoCentralizado(String texto) {

        int larguraTela = painel.getLargura();

        int comprimento = (int) g2.getFontMetrics().getStringBounds(texto, g2).getWidth();
        int x = larguraTela / 2 - comprimento / 2;
        return x;

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

}