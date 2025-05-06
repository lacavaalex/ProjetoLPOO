package UI;

import Entidade.Jogador;
import Itens.Item;
import Main.*;
import Controles.*;

import java.awt.*;
import java.util.HashMap;

public class InventarioUI extends UI {

    private Painel painel;
    private Botões botoes;

    private boolean fechado = true;
    private HashMap<String, Item> invent = new HashMap<>();

    private int numComandoInvent;

    public InventarioUI(Painel painel, Jogador jogador, Botões botoes) {
        super(painel, jogador);
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
            ui.escreverTexto("Inventario", tileSize);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));

            // Lista de itens
            String[] listaItens = new String[invent.size()];
            int i = 0;

            for (String nome : invent.keySet()) {
                Item item = invent.get(nome);
                listaItens[i++] = i + "- " + nome + " x" + item.getQuantidade();
            }
            ui.desenharOpcoes(listaItens, tileSize*2, numComandoInvent);
        }
    }

    public void abrir() {
        fechado = false;
        botoes.mostrarBotaoSair();
    }
    public void fechar() {
        fechado = true;
        botoes.esconderBotaoSair();
    }

    // Getters e setters
    public HashMap<String, Item> getInvent() { return invent; }
    public boolean isFechado() { return fechado; }


    // Metodos de comando
    public void subtrairNumComandoInvent() {
        numComandoInvent--;
        if (numComandoInvent < 0) {
            numComandoInvent = invent.size() - 1;
            if (invent.isEmpty()) numComandoInvent = 0;
        }
    }

    public void adicionarNumComandoInvent() {
        numComandoInvent++;
        if (numComandoInvent > invent.size() - 1) {
            numComandoInvent = 0;
        }
    }
    public int getNumComando() { return numComandoInvent; }
}