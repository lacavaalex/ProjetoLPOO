package UI;

import Controles.Botoes;
import Entidade.*;
import Main.Painel;
import Sistema.CombateSistema;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CombateUI extends UI {

    private BufferedImage imagemInimigo, fundoCombate;

    private Painel painel;
    private Botoes botoes;
    private CombateSistema combateSistema;

    private Font pixelsans_30;

    private float alphaFade = 0f;
    private boolean transicaoIniciada = false;
    private boolean transicaoFinalizada = false;

    public CombateUI(Painel painel, Jogador jogador, Botoes botoes, CombateSistema combateSistema) {
        super(painel, jogador);
        this.painel = painel;
        this.botoes = botoes;
        this.combateSistema = combateSistema;

        this.pixelsans_30 = painel.getUi().getPixelsans_30();
    }

    public void estruturaTelaCombate(Graphics2D g2) {
        setGraphics(g2);

        updateFrames();

        botoes.esconderBotao("Continuar");
        botoes.esconderBotao("Clima");

        desenharFundoCombate(g2);

        int tileSize = painel.getTileSize();
        int y = tileSize * 2;

        boolean turnoJogador = combateSistema.isTurnoJogador();
        boolean fimDeCombate = combateSistema.isCombateFinalizado();
        boolean escapou = combateSistema.conseguiuEscapar();
        boolean esquivou = combateSistema.isAtaqueEsquivado();
        boolean bloqueou = combateSistema.isAtaqueBloqueado();
        boolean resistiu = combateSistema.isAtaqueResistido();
        boolean critico = combateSistema.isAtaqueCritico();
        boolean fugaFalha = combateSistema.isFugaFalha();
        boolean defesaFalha = combateSistema.isDefesaFalha();
        int numComandoCombate = combateSistema.getNumComando();

        // Dentro de combate
        if (!escapou) {
            g2.setColor(Color.white);
            if (combateSistema.getCriaturaEmCombate() != null) {
                imagemInimigo = setupImagens(combateSistema.getCriaturaEmCombate().getNomeImagem(), "criatura");
                desenharInimigo(g2);
            }

            if (!fimDeCombate) {

                g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 20F));
                g2.setColor(Color.white);

                // Inimigo
                String nome = combateSistema.getCriaturaEmCombate().getNomeCriatura();
                g2.drawString(nome, tileSize, y);

                String descricao = (combateSistema.getCriaturaEmCombate().getVidaCriatura()) + "HP / " + combateSistema.getCriaturaEmCombate().getAtaqueCriatura() + "ATK";
                g2.drawString(descricao, tileSize, y += tileSize);

                // Jogador
                String vida = "Sua vida: " + getJogador().getVida() + "HP";
                g2.drawString(vida, tileSize, painel.getAltura() - tileSize * 2);

                String atk = "Seu ataque: " + getJogador().getAtaqueAtual() + "ATK";
                g2.drawString(atk, tileSize, painel.getAltura() - tileSize);

                g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 15F));

                // Turno do jogador
                if (turnoJogador) {

                    if (resistiu) {
                        escreverTexto("Após aquele último golpe.. você ainda se ergue.", y);
                        escreverTexto("Essa luta não acabou.", y += tileSize);
                    }
                    escreverTexto("Aja enquanto pode.", y += tileSize * 3);
                    if (combateSistema.getCriaturaEmCombate() != null
                            && combateSistema.getCriaturaEmCombate().isBoss()) {
                        desenharOpcoes(new String[]{"ATACAR", "ESQUIVAR", "DEFENDER", "MOCHILA"}, y += tileSize, numComandoCombate);
                    } else {
                        desenharOpcoes(new String[]{"ATACAR", "ESQUIVAR", "DEFENDER", "MOCHILA", "FUGIR"}, y += tileSize, numComandoCombate);
                    }

                } else {
                    if (fugaFalha || defesaFalha) {
                        escreverTexto("ARGH, DROGA! Não há como escapar desse golpe!", y += tileSize * 2);
                        escreverTexto("-" + combateSistema.getCriaturaEmCombate().getAtaqueCriatura() + "HP", y += tileSize);
                    }
                    else {
                        if (esquivou) {
                            escreverTexto("Você desvia, e aplica um poderoso golpe surpresa!", y += tileSize * 2);
                            escreverTexto("Oponente perde -" + getJogador().getAtaqueAtual() * 2 + "HP.", y += tileSize * 2);
                        }
                        else if (bloqueou) {
                            escreverTexto("Você defende! Seu momentum pega o monstro desprevenido.", y += tileSize * 2);
                            escreverTexto("O ataque dele está temporariamente mais fraco.", y += tileSize);
                        }
                        else {
                            escreverTexto("Inferiu -" + getJogador().getAtaqueAtual() + "HP de dano no oponente!", y += tileSize * 2);
                            g2.setColor(Color.red);
                            escreverTexto("O inimigo ataca!", y += tileSize);
                            escreverTexto("-" + combateSistema.getCriaturaEmCombate().getAtaqueCriatura() + "HP", y += tileSize);
                            if (critico) {
                                escreverTexto("Ataque crítico!", y += tileSize);
                            }
                            g2.setColor(Color.white);
                        }

                    }
                    desenharOpcoes(new String[]{"Continuar"}, y += tileSize * 2, numComandoCombate);
                }
            }

            // Fim de combate
            else {
                inimigoSeEsvaindo();

                if (transicaoFinalizada) {
                    botoes.esconderBotao("Abrir mochila");
                    botoes.esconderBotao("Clima");

                    g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 15F));

                    escreverTexto("FIM DE COMBATE", y += tileSize * 2);
                    escreverTexto("SEU OPONENTE FOI DERROTADO!", y += tileSize);
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
            botoes.esconderBotao("Abrir mochila");
            botoes.esconderBotao("Clima");

            g2.setColor(Color.yellow);
            g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 15F));

            escreverTexto("A tensão se esvaiu como fumaça... o silêncio agora parece até confortável.", y += tileSize * 2);
            escreverTexto("Você conseguiu despistar a criatura...", y += tileSize * 2);
            escreverTexto("Pressione [esc] para continuar.", y += tileSize * 3);
        }
    }

    public void inicializarFundoCombate() {
        String nomeImagem = painel.getAmbienteAtual().getNomeFundoCombate();
        fundoCombate = setupImagens(
                combateSistema.getCriaturaEmCombate().isBoss() ? nomeImagem + "boss" : nomeImagem,
                "background"
        );
    }

    public void desenharFundoCombate(Graphics2D g2) {
        g2.drawImage(fundoCombate, 0, 0, painel.getLargura(), painel.getAltura(), null);
    }

    public void desenharInimigo(Graphics2D g2) {

        int tileSize = painel.getTileSize();
        int larguraImagem = tileSize * combateSistema.getCriaturaEmCombate().getLarguraImagemEscala();
        int alturaImagem = tileSize * combateSistema.getCriaturaEmCombate().getAlturaImagemEscala();

        Composite composite = g2.getComposite();

        if (transicaoIniciada) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - alphaFade));
            g2.drawImage(imagemInimigo,
                    painel.getLargura()/2 - (combateSistema.getCriaturaEmCombate().getLarguraImagemEscala() * tileSize)/2,
                    painel.getAltura()/2 - (combateSistema.getCriaturaEmCombate().getAlturaImagemEscala() * tileSize)/2,
                    larguraImagem, alturaImagem, null);
        } else {
            g2.drawImage(imagemInimigo,
                    painel.getLargura() - tileSize * combateSistema.getCriaturaEmCombate().getDistanciaBordaEscala(),
                    painel.getAltura() - tileSize * combateSistema.getCriaturaEmCombate().getDistanciaBordaEscala(),
                    larguraImagem, alturaImagem, null);
        }

        g2.setComposite(composite);
    }

    // Metodos da transição
    public void inimigoSeEsvaindo() {
        if (transicaoIniciada && alphaFade < 1.0f) {
            if (combateSistema.getCriaturaEmCombate().isBoss()
            && !combateSistema.getCriaturaEmCombate().getNomeCriatura().equals("Corvo Espectral")){
                alphaFade += 0.002f;
            }
            else {
                alphaFade += 0.008f;
            }
            if (alphaFade >= 1.0f) {
                alphaFade = 1.0f;
                transicaoFinalizada = true;
            }
        }
    }

    public void iniciarTransicao() { transicaoIniciada = true; }

    public void resetAtributosTransicao() {
        alphaFade = 0f;
        transicaoIniciada = false;
        transicaoFinalizada = false;
    }


    public void retornarLayoutBotoes() {
        resetAtributosTransicao();

        botoes.mostrarBotao("Abrir mochila");
        botoes.mostrarBotao("Clima");

        botoes.mostrarBotao("Local");
        if (painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
            botoes.mostrarBotao("Voltar à base");
        }
    }

    public void setCombateSistema(CombateSistema combateSistema) { this.combateSistema = combateSistema; }
}