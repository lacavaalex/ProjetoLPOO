package Main;

import Controles.*;
import Entidade.*;
import Ambiente.*;

import java.awt.*;

public class UI {

    Painel painel;
    Jogador jogador;
    Botões botoes;

    Graphics2D g2;
    Font pixelsans_30, pixelsans_60B;

    private int tileSize;
    private int larguraTela;
    private int alturaTela;

    public int numComando = 0;
    private int telaInicialState = 0;

    public UI(Painel painel) {
        this.painel = painel;

        pixelsans_30 = new Font("Pixel Sans Serif", Font.PLAIN, 30);
        pixelsans_60B = new Font("Pixel Sans Serif", Font.BOLD, 60);

        botoes = new Botões(painel);
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

    // Opening state
        int openingState = painel.getOpeningState();
        if (gameState == openingState) {
            mostrarAbertura();
        }

    // Play state
        int playState = painel.getPlayState();
        if (gameState == playState) {
            mostrarPlayState();
        }

    // Card da floresta
        int florestaCardState = painel.getFlorestaCardState();
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

    public void mostrarAbertura() {

        tileSize = painel.getTileSize();
        larguraTela = painel.getLargura();
        alturaTela = painel.getAltura();

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));;
        int y = tileSize * 2;
        g2.setColor(Color.white);

        escreverTexto("Você é: ", y += tileSize);
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
            escreverTexto("Espalhaadas pelo chão, há coisas que parecem ser úteis.", y += tileSize);
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
            escreverTexto("Você se aproxima da luz...", tileSize * 4);

        } else if (subState == 2) {
            escreverTexto("Você busca por recursos.", tileSize * 4);
            escreverTexto("Encontrou: 2 madeiras e 1 pedra.", tileSize * 5);

            painel.getJogador().adicionarItem("Madeira", 2);
            painel.getJogador().adicionarItem("Pedra", 1);

            painel.setPlaySubState(20);

        } else if (subState == 3) {
            escreverTexto("Você se direciona até a montanha", tileSize * 4);
        }

        else if (subState == 20) {

            escreverTexto("Aquele chiado... parece estar tão perto...", y += tileSize);
            g2.setColor(Color.red);
            escreverTexto("ATAQUE SURPRESA! -1 DE VIDA", y += tileSize);
            g2.setColor(Color.white);
            escreverTexto("O que diabos!?... é uma VÍBORA-RUBRO!", y += tileSize);
            escreverTexto("", y += tileSize);
            g2.setColor(Color.red);
            escreverTexto("Víbora-Rubro: 3HP / 5 ATK", y += tileSize);
            g2.setColor(Color.white);
            escreverTexto("O que fazer?", y += tileSize);


            String[] opcoes = {"Atacar", "Fugir"};

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