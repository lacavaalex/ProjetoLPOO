package UI;

import Entidade.*;
import Main.Painel;
import Itens.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CombateUI extends UI {

    private BufferedImage imagemInimigo;

    private ItemCombate item;
    private Criatura criaturaEmCombate;

    private Font pixelsans_30;
    private boolean turnoJogador = true;
    private boolean fimDeCombate = false;

    public CombateUI(Painel painel, Jogador jogador) {
        super(painel, jogador);

        item = new ItemCombate(painel);

        this.pixelsans_30 = painel.getUi().getPixelsans_30();
    }

    // Configurações da tela de combate
    public void iniciarCombate(Criatura criatura) {
        this.criaturaEmCombate = criatura;
        fimDeCombate = false;
        turnoJogador = true;
        numComando = 0;

        for (Item itemNoInventario : getPainel().getInvent().getInvent().values()) {
            if (itemNoInventario instanceof ItemCombate) {
                item = (ItemCombate) itemNoInventario;
                item.setNome(itemNoInventario.getNome());
                item.definirArma(itemNoInventario.getNome());
                break;
            }
        }
    }

    // UI da tela
    public void telaCombate(Graphics2D g2, UI ui) {

        int tileSize = getPainel().getTileSize();
        int y = tileSize;

        // Titulo
        g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 25F));
        int x = coordenadaXParaTextoCentralizado(g2, "COMBATE");

        g2.setColor(Color.red);
        g2.drawString("COMBATE", x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString("COMBATE", x, y);

        // Dentro de combate
        if (!fimDeCombate) {

            if (criaturaEmCombate != null) {
                imagemInimigo = setup("/Imagens/" + criaturaEmCombate.getNomeImagem());
                desenharInimigo(g2);

                g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 22F));
                g2.setColor(Color.red);
                escreverTexto(criaturaEmCombate.getDescricao(), y += tileSize * 3);
                g2.setColor(Color.white);
                escreverTexto("Você: " + getJogador().getVida() + "HP", y += tileSize * 2);
            }

            g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 15F));
            if (turnoJogador) {
                escreverTexto("Aja enquanto pode.", y += tileSize * 2);
                desenharOpcoes(new String[]{"ATACAR", "FUGIR"}, y += tileSize);
            } else {
                escreverTexto("Você inferiu " + getJogador().getAtaqueAtual() + "HP de dano ao oponente.", y += tileSize * 2);
                escreverTexto("O inimigo ataca!", y += tileSize);
                escreverTexto("-" + criaturaEmCombate.getAtaqueCriatura() + "HP", y += tileSize);
                desenharOpcoes(new String[]{"Continuar"}, y += tileSize);
            }
        }

        // Fim de combate
        else {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
            escreverTexto("Seu oponente foi derrotado, FIM DE COMBATE!", y += tileSize * 2);
            g2.setColor(Color.yellow);
            escreverTexto("DESFECHO", y += tileSize * 2);
            escreverTexto("- Vida: " + getJogador().getVida() + "HP", y += tileSize);
            escreverTexto("- Consequências: ", y += tileSize);
            g2.setColor(Color.white);
            escreverTexto("Pressione ENTER para continuar.", y += tileSize * 2);

            // * ENTER DEVE SETEVENTOATIVO-FALSE. TALVEZ SETGAMESTATE PRO SUBSTATE ANTERIOR, SE NAO FUNCIONAR NO SISTEMATURNO
        }
    }

    // Sistema de turnos
    public void sistemaTurno() {
        if (fimDeCombate) {
            getPainel().setGameState(getPainel().getPlayState());
            return;
        }

            // Turno do jogador
            if (turnoJogador) {
                // ATACAR
                if (numComando == 0) {
                    // Cálculo de vida inimigo
                    criaturaEmCombate.setVidaCriatura(criaturaEmCombate.getVidaCriatura() - getJogador().getAtaqueAtual());//item.getPoder()); // A IMPLEMENTAR DANO POR ITEM

                    // Cálculo de morte do inimigo/troca de turno
                    if (criaturaEmCombate.getVidaCriatura() <= 0) {
                        fimDeCombate = true;
                    } else {
                        turnoJogador = false;
                    }

                    // FUGIR
                } else if (numComando == 1) {
                    // fuga
                }

                // Turno do inimigo
            } else {
                // Cálculo de vida jogador
                getJogador().setVida(getJogador().getVida() - criaturaEmCombate.getAtaqueCriatura());

                // Cálculo de morte do jogador/troca de turno
                if (getJogador().getVida() <= 0) {
                    fimDeCombate = true;
                    getPainel().setFightState(false);
                    getPainel().getEvento().setEventoCriaturaAtivo(false);
                    getPainel().setGameState(getPainel().getGameOverState());
                } else {
                    turnoJogador = true;
                }
            }
            numComando = 0;
        }

    // Desenha imagem do inimigo
    public void desenharInimigo(Graphics2D g2) {

        int tileSize = getPainel().getTileSize();

        g2.drawImage(imagemInimigo,
                getPainel().getLargura() - tileSize * criaturaEmCombate.getDistanciaBordaEscala(),
                getPainel().getAltura() - tileSize * criaturaEmCombate.getDistanciaBordaEscala(),
                tileSize * criaturaEmCombate.getLarguraImagemEscala(),
                tileSize * criaturaEmCombate.getAlturaImagemEscala(),
                null);
    }
}
