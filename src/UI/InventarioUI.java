package UI;

import Entidade.Jogador;
import Itens.*;
import Main.*;
import Controles.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class InventarioUI extends UI {

    private Painel painel;
    private Botoes botoes;

    private BufferedImage fundoInventario, imagemDaArma;

    private boolean fechado = true;
    private HashMap<String, Item> invent = new HashMap<>();
    private String[] listaItens;

    private int numComandoInvent;
    private String itemEscolhido;
    private String armaAtual;

    public InventarioUI(Painel painel, Jogador jogador, Botoes botoes) {
        super(painel, jogador);
        this.painel = painel;
        this.botoes = botoes;

        fundoInventario = setupImagens("fundo_inventario", "background");
    }

    public void adicionarItem(String nome, String tipo, int quantidade) {
        if (acharItem(nome)) {
            Item itemExistente = invent.get(nome);
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
        }
        else {
            Item novoItem = null;

            switch (tipo) {
                case "consumo":
                    novoItem = new ItemConsumo(painel);
                    break;
                case "recurso":
                    novoItem = new ItemRecurso(painel);
                    break;
                case "combate":
                    novoItem = new ItemCombate(painel);
                    break;
                default:
                    System.out.println("Tipo de item desconhecido: " + tipo);
                    break;
            }

            if (novoItem != null) {
                novoItem.setNome(nome);
                novoItem.setQuantidade(quantidade);
                invent.put(nome, novoItem);
            }
            System.out.println("Quantidade " + nome + " é " + novoItem.getQuantidade());
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

    public void estruturaTelaDeInventario(Graphics2D g2, UI ui) {
        if (!fechado) {

            // Definições gerais (visual, atriutos)
            ui.setGraphics(g2);

            int tileSize = painel.getTileSize();
            int larguraTela = painel.getLargura();
            int alturaTela = painel.getAltura();

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, larguraTela, alturaTela);

            ui.desenharPlanoDeFundo(fundoInventario);

            // Definições de texto
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
            g2.setColor(Color.white);
            ui.escreverTexto("Inventário", tileSize);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
            String textoEsc = ("Aperte [esc] para sair");
            g2.drawString(textoEsc, painel.getLargura() - tileSize * 6,painel.getAltura() - tileSize);

            // Lista de itens
            listaItens = new String[invent.size()];
            int i = 0;

            for (String nome : invent.keySet()) {
                Item item = invent.get(nome);
                listaItens[i++] = nome + " x" + item.getQuantidade();
            }
            ui.desenharOpcoes(listaItens, tileSize*2, numComandoInvent);

            // Vizualização da arma equipada
            int x = painel.getLargura() - tileSize * 7;
            int y = tileSize * 2;

            int xCaixinha = x - 15;
            int yCaixinha = y - tileSize;
            int larguraCaixinha = tileSize * 7;
            int alturaCaixinha = tileSize * 7;

            g2.setColor(Color.white);
            g2.drawRect(xCaixinha, yCaixinha, larguraCaixinha, alturaCaixinha);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            String textoArma = ("Arma atual: ");
            g2.drawString(textoArma, x, y);

            armaAtual = painel.getJogador().getArmaAtual();
            String nomeArma = (armaAtual);
            g2.drawString(nomeArma, x, y += tileSize);

            Item equipado = invent.get(armaAtual);

            if (equipado instanceof ItemCombate arma) {

                if (arma.getNomeImagem() != null) {
                    imagemDaArma = setupImagens(arma.getNomeImagem(), "arma");
                    desenharArma(g2, x + tileSize * 2, y += tileSize);

                    String poderArma = ("Dano: " + arma.getPoder());
                    g2.drawString(poderArma, x + tileSize * 2, y + tileSize * 3);
                }
            }
        }
    }

    // Define qual ítem foi selecionado
    public void selecionouItem() {
        itemEscolhido = listaItens[numComandoInvent];
        String nomeReal = itemEscolhido.substring(0, itemEscolhido.lastIndexOf(" x"));
        Item item = invent.get(nomeReal);

        if (item != null) {
            item.usar(nomeReal);
        } else {
            System.out.println("Item " + nomeReal + " não encontrado no inventário.");
        }
    }

    // Metodo que desenha a arma equipada
    public void desenharArma(Graphics2D g2, int x, int y) {
        int tileSize = getPainel().getTileSize();
        g2.drawImage(imagemDaArma, x, y, tileSize * 2, tileSize * 2, null);
    }

    // Controle da aparição da tela
    public void abrir() {
        fechado = false;
    }
    public void fechar() {
        fechado = true;
        numComandoInvent = 0;
        botoes.mostrarBotaoMochila();
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