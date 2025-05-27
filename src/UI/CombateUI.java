package UI;

import Controles.Botoes;
import Entidade.*;
import Evento.EventoCriatura;
import Main.Painel;
import Itens.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CombateUI extends UI {

    private BufferedImage imagemInimigo, fundoCombate;

    private Painel painel;
    private ItemCombate item;
    private Criatura criaturaEmCombate;
    private Botoes botoes;
    private EventoCriatura eventoCriatura;

    private Font pixelsans_30;
    private boolean turnoJogador = true;
    private boolean fimDeCombate = false;

    private boolean esquivou = false;
    private boolean bloqueou = false;
    private boolean defesaFalha = false;
    private boolean escapou = false;
    private boolean fugaFalha = false;

    private int ataqueOriginal;
    private int ataqueFraco;

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
        ataqueOriginal = criaturaEmCombate.getAtaqueCriatura();

        fimDeCombate = false;
        turnoJogador = true;
        numComandoCombate = 0;

        String nomeImagem = painel.getAmbienteAtual().getNomeFundoCombate();

        if (criaturaEmCombate.isBoss()) {
            fundoCombate = setupImagens(nomeImagem + "boss", "background");
        } else {
            fundoCombate = setupImagens(nomeImagem, "background");
        }
    }

    // Finaliza o combate e reseta os atributos
    public void finalizarCombate() {
        painel.setFightState(false);

        eventoCriatura.setEventoCriaturaAtivo(false);

        if (!escapou) {
            eventoCriatura.incrementarContador();
            getJogador().setEnergia(getJogador().getEnergia() - 1);
        }

        resetAtributosTransicao();

        botoes.mostrarBotaoMochila();
        botoes.mostrarBotaoClima();

        botoes.mostrarBotaoCardAmbiente();
        if (painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
            botoes.mostrarBotaoBase();
        }

        resetarCriaturaEmCombate();

        escapou = false;
    }

    // UI da tela
    public void estruturaTelaCombate(Graphics2D g2) {

        updateFrames();

        botoes.esconderBotaoContinuar();
        botoes.esconderBotaoClima();

        int tileSize = painel.getTileSize();
        int y = tileSize * 2;

        desenharPlanoDeFundoCombate(g2);

        // Titulo
        desenharTitulo(g2);

        // Dentro de combate
        if (!escapou) {
            g2.setColor(Color.white);
            if (criaturaEmCombate != null) {
                imagemInimigo = setupImagens(criaturaEmCombate.getNomeImagem(), "criatura");
                desenharInimigo(g2);
            }

            if (!fimDeCombate) {

                g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 20F));

                g2.setColor(criaturaEmCombate.isBoss() ? Color.white : Color.red);

                String nome = criaturaEmCombate.getNomeCriatura();
                String descricao = (criaturaEmCombate.getVidaCriatura()) + "HP / " + (criaturaEmCombate.getAtaqueCriatura()) + "ATK";

                int comprimentoNome = (int) g2.getFontMetrics().getStringBounds(nome, g2).getWidth();
                int comprimentoDescricao = (int) g2.getFontMetrics().getStringBounds(descricao, g2).getWidth();

                g2.drawString(nome, painel.getLargura() - tileSize - comprimentoNome, y);
                g2.drawString(descricao, painel.getLargura() - tileSize - comprimentoDescricao, y += tileSize);

                g2.setColor(Color.white);
                String vida = "Sua vida: " + getJogador().getVida() + "HP";
                g2.drawString(vida, tileSize, y);

                String atk = "Seu ataque: " + getJogador().getAtaqueAtual() + "ATK";
                g2.drawString(atk, tileSize, y += tileSize);

                g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 15F));

                // Turno do jogador
                if (turnoJogador) {
                    escreverTexto("Aja enquanto pode.", y += tileSize * 2);
                    if (painel.getCombate().getCriaturaEmCombate() != null
                            && painel.getCombate().getCriaturaEmCombate().isBoss()) {
                        desenharOpcoes(new String[]{"ATACAR", "ESQUIVAR", "DEFENDER", "MOCHILA"}, y += tileSize, numComandoCombate);
                    } else {
                        desenharOpcoes(new String[]{"ATACAR", "ESQUIVAR", "DEFENDER", "MOCHILA", "FUGIR"}, y += tileSize, numComandoCombate);
                    }

                } else {
                    if (fugaFalha || defesaFalha) {
                        escreverTexto("ARGH, DROGA! Não há como escapar desse golpe!", y += tileSize * 2);
                        escreverTexto("-" + criaturaEmCombate.getAtaqueCriatura() + "HP", y += tileSize);
                    }
                    else {
                        if (esquivou) {
                            escreverTexto("Você desvia, e aplica um poderoso golpe surpresa!", y += tileSize * 2);
                            escreverTexto("Oponente perde -" + getJogador().getAtaqueAtual() * 2 + "HP.", y += tileSize * 2);
                        }
                        else if (bloqueou) {
                            escreverTexto("Você defendeu! Seu momentum pega o inimigo de surpresa.", y += tileSize * 2);
                            escreverTexto("O ataque dele está temporariamente mais fraco.", y += tileSize);
                        }
                        else {
                            escreverTexto("Você infere -" + getJogador().getAtaqueAtual() + "HP de dano no oponente.", y += tileSize * 2);
                            g2.setColor(Color.red);
                            escreverTexto("O inimigo ataca!", y += tileSize);
                            escreverTexto("-" + criaturaEmCombate.getAtaqueCriatura() + "HP", y += tileSize);
                            g2.setColor(Color.white);
                        }

                    }
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
                    escreverTexto("Pressione [esc] para continuar.", y += tileSize * 3);
                }
            }
        }

        // Fuga bem sucedida
        else {
            botoes.esconderBotaoMochila();
            botoes.esconderBotaoClima();

            g2.setColor(Color.yellow);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));

            escreverTexto("A tensão se esvaiu como fumaça... o silêncio agora parece até confortável.", y += tileSize * 2);
            escreverTexto("Você conseguiu despistar a criatura...", y += tileSize * 2);
            escreverTexto("Pressione [esc] para continuar.", y += tileSize * 3);
        }
    }

    // Sistema de turnos
    public void sistemaTurno() {

        if (this.criaturaEmCombate != null) {

            ataqueFraco = ataqueOriginal / 2;

            // Turno do jogador
            if (turnoJogador) {

                fugaFalha = false;
                defesaFalha = false;
                esquivou = false;
                bloqueou = false;

                // ATACAR
                if (numComandoCombate == 0) {

                    // Cálculo de vida inimigo
                    criaturaEmCombate.setVidaCriatura(criaturaEmCombate.getVidaCriatura() - getJogador().getAtaqueAtual());

                    // Cálculo de morte do inimigo/troca de turno
                    if (criaturaEmCombate.getVidaCriatura() <= 0) {
                        fimDeCombate = true;
                        transicaoIniciada = true;
                    } else {
                        turnoJogador = false;
                    }
                }

                // ESQUIVAR
                else if (numComandoCombate == 1) {

                    int probabilidade = painel.definirUmaProbabilidade();

                    if (probabilidade <= 30) {
                        esquivou = true;

                        criaturaEmCombate.setVidaCriatura(criaturaEmCombate.getVidaCriatura() - getJogador().getAtaqueAtual());

                        if (criaturaEmCombate.getVidaCriatura() <= 0) {
                            fimDeCombate = true;
                            transicaoIniciada = true;
                        } else {
                            turnoJogador = false;
                        }
                    } else {
                        defesaFalha = true;
                        turnoJogador = false;
                    }
                }

                // BLOQUEAR
                else if (numComandoCombate == 2) {

                    int probabilidade = painel.definirUmaProbabilidade();

                    if (probabilidade <= 50) {
                        bloqueou = true;

                        criaturaEmCombate.setAtaqueCriatura(ataqueFraco);

                        turnoJogador = false;
                    } else {
                        defesaFalha = true;
                        turnoJogador = false;
                    }
                }

                // FUGIR
                else if (numComandoCombate == 4) {
                    int percentual = painel.definirUmaProbabilidade();
                    int probabilidade = painel.definirUmaProbabilidade();

                    if (probabilidade <= percentual) {
                        escapou = true;
                    } else {
                        fugaFalha = true;
                        turnoJogador = false;
                    }
                }
            }

            // Turno do inimigo
            else {
                if (!bloqueou && !esquivou) {
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
                    criaturaEmCombate.setAtaqueCriatura(ataqueOriginal);
                } else {
                    turnoJogador = true;
                }
            }
            numComandoCombate = 0;
        }
    }

    public void desenharTitulo(Graphics2D g2) {
        g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 30F));
        String combate = "COMBATE";
        int y = painel.getTileSize();
        int x = coordenadaXParaTextoCentralizado(g2, painel.getLargura(),"COMBATE");


        Color[] tonsVermelho = {
                new Color(50, 0, 0),
                new Color(75, 0, 0),
                new Color(100, 0, 0),
                new Color(125, 0, 0),
        };

        Color[] tonsAzul = {
                new Color(0, 0, 50),
                new Color(0, 0, 75),
                new Color(0, 0, 100),
                new Color(0, 0, 125),
        };

        Color[] tonsAmarelo = {
                new Color(100, 60, 0),
                new Color(120, 70, 0),
                new Color(150, 100, 0),
                new Color(190, 120, 0),
                new Color(250, 200, 0)
        };

        Color[] tonsBranco = {
                new Color(95, 95, 95),
                new Color(115, 115, 115),
                new Color(165, 165, 165),
                new Color(215, 215, 215),
                new Color(255, 255, 255)
        };


        for (int i = 8; i >= 5; i--) {
            g2.setColor((criaturaEmCombate.isBoss()? tonsAzul[8 - i] : tonsVermelho[8 - i]));
            g2.drawString(combate, x + i, y + i);
        }

        for (int i = 4; i >= 0; i--) {
            g2.setColor((criaturaEmCombate.isBoss()? tonsBranco[4 - i] : tonsAmarelo[4 - i]));
            g2.drawString(combate, x + i, y + i);
        }
    }

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

    public void desenharPlanoDeFundoCombate(Graphics2D g2) {
        g2.drawImage(fundoCombate, 0, 0, painel.getLargura(), painel.getAltura(), null);
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

    public boolean isTurnoJogador() { return turnoJogador; }

    public boolean conseguiuEscapar() { return escapou; }

    public boolean isCombateFinalizado() { return fimDeCombate; }

    public Criatura getCriaturaEmCombate() { return criaturaEmCombate; }
    public void resetarCriaturaEmCombate() { this.criaturaEmCombate = null; }
}