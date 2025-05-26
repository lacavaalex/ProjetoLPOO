package UI;

import Controles.Botoes;
import Entidade.*;
import Evento.EventoCriatura;
import Main.Painel;
import Itens.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CombateUI extends UI {

    private BufferedImage imagemInimigo;

    private Painel painel;
    private ItemCombate item;
    private Criatura criaturaEmCombate;
    private Botoes botoes;
    private EventoCriatura eventoCriatura;

    private Font pixelsans_30;
    private boolean turnoJogador = true;
    private boolean fimDeCombate = false;

    private int numComandoCombate;

    private float alphaFade = 0f;
    private boolean transicaoIniciada = false;
    private boolean transicaoFinalizada = false;

    public CombateUI(Painel painel, Jogador jogador, Botoes botoes, EventoCriatura eventoCriatura) {
        super(painel, jogador);
        this.painel = painel;
        this.botoes = botoes;
        this.eventoCriatura = eventoCriatura;

        item = new ItemCombate(painel);

        this.pixelsans_30 = painel.getUi().getPixelsans_30();
    }

    // Configurações da tela de combate
    public void iniciarCombate(Criatura criatura) {
        this.criaturaEmCombate = criatura;
        fimDeCombate = false;
        turnoJogador = true;
        numComandoCombate = 0;
    }

    // UI da tela
    public void estruturaTelaCombate(Graphics2D g2, UI ui) {

        updateFrames();

        botoes.esconderBotaoContinuar();
        botoes.esconderBotaoClima();

        int tileSize = painel.getTileSize();
        int y = tileSize * 2;

        // Titulo
        desenharTitulo(g2);

        g2.setColor(Color.white);
        if (criaturaEmCombate != null) {
            imagemInimigo = setupImagens(criaturaEmCombate.getNomeImagem(), "criatura");
            desenharInimigo(g2);
        }


        // Dentro de combate
        if (!fimDeCombate) {

            g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 20F));
            g2.setColor(Color.red);
            escreverTexto(criaturaEmCombate.getDescricao(), y += tileSize * 3);
            g2.setColor(Color.white);
            escreverTexto("Sua vida: " + getJogador().getVida() + "HP", y += tileSize * 2);
            escreverTexto("Seu ataque: " + getJogador().getAtaqueAtual() + "ATK", y += tileSize);

            g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 15F));
            if (turnoJogador) {
                escreverTexto("Aja enquanto pode.", y += tileSize * 2);
                desenharOpcoes(new String[]{"ATACAR", "FUGIR", "MOCHILA"}, y += tileSize, numComandoCombate);
            } else {
                escreverTexto("Você inferiu " + getJogador().getAtaqueAtual() + "HP de dano ao oponente.", y += tileSize * 2);
                escreverTexto("O inimigo ataca!", y += tileSize);
                escreverTexto("-" + criaturaEmCombate.getAtaqueCriatura() + "HP", y += tileSize);
                desenharOpcoes(new String[]{"Continuar"}, y += tileSize, numComandoCombate);
            }
        }

        // Fim de combate
        else {
            inimigoSeEsvaindo(g2);

            if (transicaoFinalizada) {
                botoes.esconderBotaoMochila();
                botoes.esconderBotaoClima();
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
                escreverTexto("Seu oponente foi derrotado, FIM DE COMBATE!", y += tileSize * 2);
                g2.setColor(Color.yellow);
                escreverTexto("DESFECHO", y += tileSize * 2);
                escreverTexto("- Vida: " + getJogador().getVida() + "HP", y += tileSize);
                g2.setColor(Color.white);
                escreverTexto("Pressione [esc] para continuar.", y += tileSize * 2);
            }
        }
    }

    public void desenharTitulo(Graphics2D g2) {
        g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 25F));
        String combate = "COMBATE";
        int y = painel.getTileSize() * 2;
        int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(),"COMBATE");

        g2.setColor(new Color(200,150,0));
        g2.drawString(combate, x + 5, y + 5);

        g2.setColor(new Color(50,0,0));
        g2.drawString(combate, x + 4, y + 4);

        g2.setColor(new Color(75,0,0));
        g2.drawString(combate, x + 3, y + 3);

        g2.setColor(new Color(100, 0, 0));
        g2.drawString(combate, x + 2, y + 2);

        g2.setColor(new Color(150, 0, 0));
        g2.drawString(combate, x, y);
    }

    // Desenha imagem do inimigo
    public void desenharInimigo(Graphics2D g2) {

        int tileSize = painel.getTileSize();

        Composite composite = g2.getComposite();

        if (transicaoIniciada) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - alphaFade));
            g2.drawImage(imagemInimigo,
                    painel.getLargura()/2 - (criaturaEmCombate.getLarguraImagemEscala() * tileSize)/2,
                    painel.getAltura()/2 - criaturaEmCombate.getAlturaImagemEscala(),
                    tileSize * criaturaEmCombate.getLarguraImagemEscala(),
                    tileSize * criaturaEmCombate.getAlturaImagemEscala(),
                    null);
        } else {
            g2.drawImage(imagemInimigo,
                    painel.getLargura() - tileSize * criaturaEmCombate.getDistanciaBordaEscala(),
                    painel.getAltura() - tileSize * criaturaEmCombate.getDistanciaBordaEscala(),
                    tileSize * criaturaEmCombate.getLarguraImagemEscala(),
                    tileSize * criaturaEmCombate.getAlturaImagemEscala(),
                    null);
        }

        g2.setComposite(composite);
    }

    public void inimigoSeEsvaindo(Graphics2D g2) {
        if (transicaoIniciada && alphaFade < 1.0f) {
            alphaFade += 0.008f;
            if (alphaFade >= 1.0f) {
                alphaFade = 1.0f;
                transicaoFinalizada = true;
            }
        }
    }

    public void resetAtributosTransicao() {
        alphaFade = 0f;
        transicaoIniciada = false;
        transicaoFinalizada = false;
    }

    // Sistema de turnos
    public void sistemaTurno() {

        // Turno do jogador
        if (turnoJogador) {
            // ATACAR
            if (numComandoCombate == 0) {
                // Cálculo de vida inimigo
                criaturaEmCombate.setVidaCriatura(criaturaEmCombate.getVidaCriatura() - getJogador().getAtaqueAtual());//item.getPoder()); // A IMPLEMENTAR DANO POR ITEM

                // Cálculo de morte do inimigo/troca de turno
                if (criaturaEmCombate.getVidaCriatura() <= 0) {
                    fimDeCombate = true;
                    transicaoIniciada = true;
                } else {
                    turnoJogador = false;
                }

                // FUGIR
            } else if (numComandoCombate == 1) {
                // fuga
            }

            // Turno do inimigo
        } else {
            // Cálculo de vida jogador
            getJogador().setVida(getJogador().getVida() - criaturaEmCombate.getAtaqueCriatura());

            // Cálculo de morte do jogador/troca de turno
            if (getJogador().getVida() <= 0) {
                fimDeCombate = true;
                finalizarCombate();
                painel.setGameState(painel.getGameOverState());
            } else {
                turnoJogador = true;
            }
        }
        numComandoCombate = 0;
    }

    // Finaliza o combate e reseta os atributos
    public void finalizarCombate() {
        painel.setFightState(false);

        eventoCriatura.setEventoCriaturaAtivo(false);
        eventoCriatura.incrementarContador();

        getJogador().setEnergia(getJogador().getEnergia() - 1);

        resetAtributosTransicao();

        botoes.mostrarBotaoMochila();
        botoes.mostrarBotaoClima();

        botoes.mostrarBotaoCardAmbiente();
        if (painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
            botoes.mostrarBotaoBase();
        }
    }

    // Metodos de comando
    public void subtrairNumComandoCombate(int numOpcoes) {
        numComandoCombate--;
        if (numComandoCombate < 0) {
            numComandoCombate = numOpcoes - 1;
        }
    }
    public void adicionarNumComandoCombate(int numOpcoes) {
        numComandoCombate++;
        if (numComandoCombate > numOpcoes - 1) {
            numComandoCombate = 0;
        }
    }

    // Getters e setters
    public int getNumComando() { return numComandoCombate; }

    public boolean isCombateFinalizado() { return fimDeCombate; }

    public Criatura getCriaturaEmCombate() { return criaturaEmCombate; }
}