package Itens;

import Main.*;
import Controles.*;
import UI.UI;

import java.awt.*;
import java.util.HashMap;

public class Inventario {

    private Painel painel;
    private Botões botoes;

    private boolean fechado = true;
    private HashMap<String, Item> invent = new HashMap<>();

    public Inventario(Painel painel, Botões botoes) {
        this.painel = painel;
        this.botoes = botoes;
    }

    public void adicionarItem(String nome, int quantidade) {
        if (acharItem(nome)) {
            invent.get(nome).setQuantidade(quantidade);
        } else {
            Item novoItem = new Item();
            novoItem.setNome(nome);
            novoItem.setQuantidade(quantidade);
            invent.put(nome, novoItem);
        }
    }

    public void removerItem(String nome, int quantidadeParaRemover) {
        int total = invent.get(nome).getQuantidade() - quantidadeParaRemover;
        invent.get(nome).setQuantidade(total);

        if (total <= 0) {
            invent.remove(nome);
        }

    }

    public boolean acharItem(String nome) {
        return invent.containsKey(nome);
    }

    public void estruturaTelaDeInventario(Graphics2D g2, UI ui) {
        if (!fechado) {

            int tileSize = painel.getTileSize();
            int larguraTela = painel.getLargura();
            int alturaTela = painel.getAltura();

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, larguraTela, alturaTela);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
            g2.setColor(Color.white);
            ui.escreverTexto("Inventario", tileSize * 2);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
            int y = tileSize * 3;

            // Lista de itens
            for (String nome : invent.keySet()) {
                Item item = invent.get(nome);
                String linha = nome + " x" + item.getQuantidade();
                g2.drawString(linha, tileSize, y);
                y += tileSize;
            }
        }
    }

    public HashMap<String, Item> getInvent() {
        return invent;
    }
    public void abrir() {
        fechado = false;
        painel.setFocusable(false);
        botoes.mostrarBotaoSair();
    }
    public void fechar() {
        fechado = true;
        painel.setFocusable(true);
        botoes.esconderBotaoSair();
    }
    public boolean isFechado() {
        return fechado;
    }
}