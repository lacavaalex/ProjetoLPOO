package Itens;

import Main.*;
import Controles.*;
import UI.UI;

import java.awt.*;
import java.util.HashMap;

public class Inventario {

    Painel painel;
    UI ui;
    Botões botoes;
    Graphics2D g2;

    private boolean fechado = true;
    private HashMap<String, Item> invent = new HashMap<>();

    public Inventario(Painel painel, Botões botoes) {
        this.painel = painel;
        this.botoes = botoes;
    }

    public void adicionarItem(String nome, int quantidade) {
        if (this.invent.containsKey(nome)) {
            invent.get(nome).setQuantidade(quantidade);
        } else {
            invent.put(nome, new Item(nome, quantidade));
        }
    }

    public void removerItem(String nome, int quantidade) {
        invent.get(nome).setQuantidade(quantidade);
    }

    public void telaDeInventario(Graphics2D g2, UI ui) {
        if (!fechado) {

            this.g2 = g2;
            this.ui = ui;

            int tileSize = painel.getTileSize();
            int larguraTela = painel.getLargura();
            int alturaTela = painel.getAltura();

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, larguraTela, alturaTela);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
            int y = tileSize * 2;
            g2.setColor(Color.white);
            ui.escreverTexto("Inventario", y);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));;
        }
    }

    public HashMap<String, Item> getInvent() {
        return invent;
    }
    public void abrir() {
        fechado = false;
        botoes.mostrarBotaoSair();
    }
    public void fechar() {
        fechado = true;
        botoes.esconderBotaoSair();
    }
    public boolean isFechado() {
        return fechado;
    }
}
