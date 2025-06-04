package Sistema;

import Entidade.Jogador;
import Itens.*;
import Main.Painel;

import java.util.HashMap;

public class InventarioSistema {

    private Painel painel;
    private Jogador jogador;

    private HashMap<String, Item> invent = new HashMap<>();
    private String[] listaItens;

    private int numComandoInvent;
    private String itemEscolhido;

    public InventarioSistema(Painel painel, Jogador jogador) {
        this.painel = painel;
        this.jogador = jogador;
    }

    public void adicionarItem(String nome, String tipo, int quantidade) {
        if (acharItem(nome)) {
            if (!tipo.equals("combate")) {
                Item itemExistente = invent.get(nome);
                itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            }
        }
        else {
            Item novoItem;

            switch (tipo) {
                case "consumo":
                    novoItem = new ItemConsumo(painel, jogador);
                    break;
                case "recurso":
                    novoItem = new ItemRecurso(painel);
                    break;
                case "combate":
                    novoItem = new ItemCombate(painel);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de item desconhecido: " + tipo);
            }

            novoItem.setNome(nome);
            novoItem.setQuantidade(quantidade);
            invent.put(nome, novoItem);
        }
    }

    public void removerItem(String nome, int quantidadeParaRemover) {
        try {
            Item item = invent.get(nome);
            int novaQuantidade = item.getQuantidade() - quantidadeParaRemover;

            if (novaQuantidade > 0) {
                item.setQuantidade(novaQuantidade);
            } else {
                invent.remove(nome);
                numComandoInvent = Math.max(0, numComandoInvent - 1);
            }
        } catch (NullPointerException e) {
            System.out.println("Erro: o item '" + nome + "' não foi encontrado no inventário.");
        }

    }

    public boolean acharItem(String nome) {
        return invent.containsKey(nome);
    }

    public void atualizarListaItens() {
        listaItens = new String[invent.size()];
        int i = 0;
        for (String nome : invent.keySet()) {
            Item item = invent.get(nome);
            listaItens[i++] = nome + " x" + item.getQuantidade();
        }
    }

    public void esvazearInventario() { invent.clear(); }

    public void selecionouItem() {
        if (!invent.isEmpty()) {
            itemEscolhido = listaItens[numComandoInvent];
            String nomeReal = itemEscolhido.substring(0, itemEscolhido.lastIndexOf(" x"));
            Item item = invent.get(nomeReal);

            if (item != null) {
                item.usar(nomeReal);
            } else {
                System.out.println("Item " + nomeReal + " não encontrado no inventário.");
            }
        }
    }

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

    // Getters e setters
    public int getNumComando() { return numComandoInvent; }
    public void setNumComando(int numComando) { this.numComandoInvent = numComando; }

    public HashMap<String, Item> getInventario() { return invent; }

    public String[] getListaItens() { return listaItens; }
}
