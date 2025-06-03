package UI;

import Ambiente.AmbienteFloresta;
import Ambiente.AmbienteGruta;
import Ambiente.AmbienteLago;
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
    private boolean resistiu = false;
    private boolean critico = false;

    private int ataqueOriginalCriatura;
    private int ataqueFracoCriatura;
    private int ataqueBaseJogador;

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
        ataqueOriginalCriatura = criaturaEmCombate.getAtaqueCriatura();

        if (ataqueBaseJogador == 0) {
            ataqueBaseJogador = getJogador().getAtaqueAtual();
        }
        int ataqueFracoJogador = (int) (ataqueBaseJogador * 0.66f);

        if (painel.getEventoClimatico().getClima().equals("tempestade")
                && !getJogador().getHabilidade().equals("SOBREVIVENCIAL")) {
            criaturaEmCombate.setAtaqueCriatura((int) (ataqueOriginalCriatura * 1.33f));
        } else {
            criaturaEmCombate.setAtaqueCriatura(ataqueOriginalCriatura);
        }
        if (painel.getEventoClimatico().getClima().equals("salgado")
                && !getJogador().getHabilidade().equals("SOBREVIVENCIAL")) {
            getJogador().setAtaqueAtual(Math.max(1, ataqueFracoJogador)); // Garante mínimo 1
        } else {
            getJogador().setAtaqueAtual(ataqueBaseJogador); // Restaura o valor original
        }

        fimDeCombate = false;
        turnoJogador = true;
        numComandoCombate = 0;

        String nomeImagem = painel.getAmbienteAtual().getNomeFundoCombate();

        fundoCombate = setupImagens(
                criaturaEmCombate.isBoss() ? nomeImagem + "boss" : nomeImagem,
                "background"
        );
    }

    // Finaliza o combate e reseta os atributos
    public void finalizarCombate() {
        painel.setFightState(false);
        eventoCriatura.setEventoCriaturaAtivo(false);

        if (!escapou) {
            eventoCriatura.incrementarContador();
            getJogador().setEnergia(getJogador().getEnergia() - 1);
        }

        getJogador().setAtaqueAtual(ataqueBaseJogador);
        ataqueBaseJogador = 0;

        if (criaturaEmCombate != null) {
            criaturaEmCombate.setAtaqueCriatura(criaturaEmCombate.getAtaqueCriatura());
        }

        resetAtributosTransicao();
        resetarCriaturaEmCombate();
        escapou = false;;

        botoes.mostrarBotao("Abrir mochila");
        botoes.mostrarBotao("Clima");

        botoes.mostrarBotao("Local");
        if (painel.getAmbienteAtual().checarSeSubStateFoiVisitado(1)) {
            botoes.mostrarBotao("Voltar à base");
        }

        if (getJogador().getHabilidade().equals("MEDICINAL")) {
            getJogador().setVida(getJogador().getVida() + 2);
        }
    }

    // UI da tela
    public void estruturaTelaCombate(Graphics2D g2) {

        setGraphics(g2);

        updateFrames();

        botoes.esconderBotao("Continuar");
        botoes.esconderBotao("Clima");

        int tileSize = painel.getTileSize();
        int y = tileSize * 2;

        desenharPlanoDeFundoCombate(g2);

        // Dentro de combate
        if (!escapou) {
            g2.setColor(Color.white);
            if (criaturaEmCombate != null) {
                imagemInimigo = setupImagens(criaturaEmCombate.getNomeImagem(), "criatura");
                desenharInimigo(g2);
            }

            if (!fimDeCombate) {

                g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 20F));
                g2.setColor(Color.white);

                // Inimigo
                String nome = criaturaEmCombate.getNomeCriatura();
                g2.drawString(nome, tileSize, y);

                String descricao = (criaturaEmCombate.getVidaCriatura()) + "HP / " + criaturaEmCombate.getAtaqueCriatura() + "ATK";
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
                            escreverTexto("Você defende! Seu momentum pega o monstro desprevenido.", y += tileSize * 2);
                            escreverTexto("O ataque dele está temporariamente mais fraco.", y += tileSize);
                        }
                        else {
                            escreverTexto("Inferiu -" + getJogador().getAtaqueAtual() + "HP de dano no oponente!", y += tileSize * 2);
                            g2.setColor(Color.red);
                            escreverTexto("O inimigo ataca!", y += tileSize);
                            escreverTexto("-" + criaturaEmCombate.getAtaqueCriatura() + "HP", y += tileSize);
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
                inimigoSeEsvaindo(g2);

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

    // Sistema de turnos
    public void sistemaTurno() {
        if (!fimDeCombate) {
            if (this.criaturaEmCombate != null) {

                ataqueFracoCriatura = ataqueOriginalCriatura / 2;

                // Turno do jogador
                if (turnoJogador) {

                    critico = false;
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

                        double probabilidade = painel.definirUmaProbabilidade();
                        if (getJogador().getHabilidade().equals("COMBATIVA")) {
                            probabilidade = probabilidade * 0.85;
                        }

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

                        double probabilidade = painel.definirUmaProbabilidade();
                        if (getJogador().getHabilidade().equals("COMBATIVA")) {
                            probabilidade = probabilidade * 0.9;
                        }

                        if (getJogador().getArmaAtual().equals("Escudo")) {
                            probabilidade = probabilidade * 0.9;
                        }

                        if (probabilidade <= 50) {
                            bloqueou = true;

                            criaturaEmCombate.setAtaqueCriatura(ataqueFracoCriatura);

                        } else {
                            defesaFalha = true;
                        }
                        turnoJogador = false;
                    }

                    // FUGIR
                    else if (numComandoCombate == 4) {
                        int percentual = painel.definirUmaProbabilidade();
                        int probabilidade = painel.definirUmaProbabilidade();

                        if (probabilidade <= percentual/2) {
                            escapou = true;
                        } else {
                            fugaFalha = true;
                            turnoJogador = false;
                        }
                    }

                    // Definir criticidade do proximo ataque
                    int ataqueCriaturaCritico = painel.definirUmaProbabilidade();
                    if (ataqueCriaturaCritico <= 20) {
                        int novoAtaque = criaturaEmCombate.getAtaqueCriatura() + criaturaEmCombate.getAtaqueCriatura() / 2;
                        criaturaEmCombate.setAtaqueCriatura(novoAtaque);
                        critico = true;
                    }
                }

                // Turno do inimigo
                else {
                    if (!bloqueou && !esquivou) {
                        // Cálculo de vida jogador
                        getJogador().setVida(getJogador().getVida() - criaturaEmCombate.getAtaqueCriatura());

                        // Cálculo de morte do jogador/troca de turno
                        if (getJogador().getVida() <= 0) {

                            double probabilidade = painel.definirUmaProbabilidade();
                            if (probabilidade <= 20 && getJogador().getHabilidade().equals("COMBATIVA")
                                    || probabilidade <= 15 && getJogador().getHabilidade().equals("SOBREVIVENCIAL")) {
                                getJogador().setVida(getJogador().getVidaMax() / 4);
                                turnoJogador = true;
                                resistiu = true;
                            } else {
                                fimDeCombate = true;
                                finalizarCombate();
                                painel.setGameState(painel.getGameOverState());
                            }
                        } else {
                            turnoJogador = true;
                            criaturaEmCombate.setAtaqueCriatura(ataqueOriginalCriatura);
                        }
                    } else {
                        turnoJogador = true;
                    }
                }
                numComandoCombate = 0;
            }
        }
    }

    public void desenharInimigo(Graphics2D g2) {

        int tileSize = painel.getTileSize();

        Composite composite = g2.getComposite();

        if (transicaoIniciada) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - alphaFade));
            g2.drawImage(imagemInimigo,
                    painel.getLargura()/2 - (criaturaEmCombate.getLarguraImagemEscala() * tileSize)/2,
                    painel.getAltura()/2 - (criaturaEmCombate.getAlturaImagemEscala() * tileSize)/2,
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
            if (criaturaEmCombate.isBoss()
            && !criaturaEmCombate.getNomeCriatura().equals("Corvo Espectral")){
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

    public void setResistiu(boolean resistiu) { this.resistiu = resistiu; }

    public boolean isCombateFinalizado() { return fimDeCombate; }

    public Criatura getCriaturaEmCombate() { return criaturaEmCombate; }
    public void resetarCriaturaEmCombate() { this.criaturaEmCombate = null; }
}