package UI;

import Entidade.*;
import Main.Painel;
import Itens.*;

import java.awt.*;

public class CombateUI extends UI {

    UI ui;
    ItemCombate item;
    private Criatura criaturaEmCombate;

    Font pixelsans_30;
    private boolean turnoJogador = true;
    private boolean fimDeCombate = false;

    public CombateUI(Painel painel, Jogador jogador) {
        super(painel, jogador);

        item = new ItemCombate(painel);

        this.pixelsans_30 = painel.getUi().pixelsans_30;
    }

    // Configurações da tela de combate
    public void iniciarCombate(Criatura criatura) {
        this.criaturaEmCombate = criatura;
        fimDeCombate = false;
        turnoJogador = true;
        numComando = 0;

        for (Item itemNoInventario : painel.getInvent().getInvent().values()) {
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
        this.g2 = g2;
        this.ui = ui;

        int tileSize = painel.getTileSize();
        int y = tileSize;

        // Titulo
        g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 25F));
        int x = coordenadaXParaTextoCentralizado("COMBATE");

        g2.setColor(Color.red);
        g2.drawString("COMBATE", x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString("COMBATE", x, y);

        // Dentro de combate
        if (!fimDeCombate) {
            if (criaturaEmCombate != null) {
                g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 22F));
                g2.setColor(Color.red);
                escreverTexto(criaturaEmCombate.getDescricao(), y += tileSize * 3);
                g2.setColor(Color.white);
                escreverTexto("Você: " + jogador.getVida() + "HP", y += tileSize * 2);
            }
            g2.setFont(pixelsans_30.deriveFont(Font.PLAIN, 15F));
            if (turnoJogador) {
                escreverTexto("Aja enquanto pode.", y += tileSize * 2);
                desenharOpcoes(new String[]{"ATACAR", "FUGIR"}, y += tileSize);
            } else {
                escreverTexto("Você inferiu " + jogador.getAtaqueAtual() + "HP de dano ao oponente.", y += tileSize * 2);
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
            escreverTexto("- Vida: " + jogador.getVida() + "HP", y += tileSize);
            escreverTexto("- Consequências: ", y += tileSize);
            g2.setColor(Color.white);
            escreverTexto("Pressione ENTER para continuar.", y += tileSize * 2);

            // * ENTER DEVE SETEVENTOATIVO-FALSE. TALVEZ SETGAMESTATE PRO SUBSTATE ANTERIOR, SE NAO FUNCIONAR NO SISTEMATURNO
        }
    }

    // Sistema de turnos
    public void sistemaTurno() {
        if (fimDeCombate) {
            painel.setGameState(painel.getPlayState());
            return;
        }

            // Turno do jogador
            if (turnoJogador) {
                // ATACAR
                if (numComando == 0) {
                    // Cálculo de vida inimigo
                    criaturaEmCombate.setVidaCriatura(criaturaEmCombate.getVidaCriatura() - jogador.getAtaqueAtual());//item.getPoder()); // A IMPLEMENTAR DANO POR ITEM

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
                jogador.setVida(jogador.getVida() - criaturaEmCombate.getAtaqueCriatura());

                // Cálculo de morte do jogador/troca de turno
                if (jogador.getVida() <= 0) {
                    fimDeCombate = true;
                    painel.setFightState(false);
                    painel.getEvento().setEventoCriaturaAtivo(false);
                    painel.setGameState(painel.getGameOverState());
                } else {
                    turnoJogador = true;
                }
            }
            numComando = 0;
        }
}
