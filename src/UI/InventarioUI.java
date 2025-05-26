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
            if (!tipo.equals("combate") || nome.equals("Galho pontiagudo")) {
                Item itemExistente = invent.get(nome);
                itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            }
        }
        else {
            Item novoItem;

            switch (tipo) {
                case "consumo":
                    novoItem = new ItemConsumo(painel, getJogador());
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

    public void esvazearInventario() { invent.clear(); }

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

    // Metodos de construção e vizualização do layout da tela
    public void estruturaTelaDeInventario(Graphics2D g2, UI ui) {
        if (!fechado) {

            // Definições gerais (visual, atributos)
            ui.setGraphics(g2);

            int larguraTela = painel.getLargura();
            int alturaTela = painel.getAltura();
            int tileSize = painel.getTileSize();

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

            estruturaArmaEquipada(g2);
            estruturaAlimentoSelecionado(g2);
            estruturaRecursoSelecionado(g2);
        }
    }

    public void estruturaArmaEquipada(Graphics2D g2) {

        // Definições visuais
        int tileSize = painel.getTileSize();
        int x = painel.getLargura() - tileSize * 7;
        int y = tileSize * 3/2;

        int xCaixinha = x - 15;
        int yCaixinha = y - tileSize;
        int larguraCaixinha = tileSize * 7;
        int alturaCaixinha = tileSize * 7;

        g2.setColor(Color.white);
        g2.drawRect(xCaixinha, yCaixinha, larguraCaixinha, alturaCaixinha);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        g2.setColor(Color.yellow);

        // Conteúdo
        String textoArma = ("Arma atual: ");
        g2.drawString(textoArma, x, y);
        g2.setColor(Color.white);

        armaAtual = painel.getJogador().getArmaAtual();
        String nomeArma = (armaAtual);
        g2.drawString(nomeArma, x, y += tileSize);

        Item equipado = invent.get(armaAtual);

        if (listaItens.length > 0) {

            String nomeSelecionado = listaItens[numComandoInvent].split(" x")[0];
            Item itemSelecionado = invent.get(nomeSelecionado);

            if (itemSelecionado instanceof ItemCombate) {

                int x2 = painel.getLargura() - tileSize * 7;
                int y2 = tileSize * 17 / 2;

                g2.setColor(Color.red);
                g2.drawString(itemSelecionado.getNome(), x2, y2);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));

                g2.drawString("Equipar? (Aperte [ENTER])", x2, y2 += tileSize / 2);
            }

        }

        if (equipado instanceof ItemCombate arma) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            if (arma.getNomeImagem() != null) {
                imagemDaArma = setupImagens(arma.getNomeImagem(), "arma");
                desenharArma(g2, x + tileSize * 2, y += tileSize);

                String poderArma = ("Dano: " + arma.getPoder());
                g2.drawString(poderArma, x + tileSize * 2, y + tileSize * 3);
            }
        }
    }

    public void estruturaAlimentoSelecionado(Graphics2D g2) {

        // Definições visuais
        int tileSize = painel.getTileSize();
        int x = painel.getLargura() - tileSize * 7;
        int y = tileSize * 17/2;
        int xCaixinha = x - 15;
        int larguraCaixinha = tileSize * 7;
        int alturaCaixinha = tileSize * 11/2;

        g2.setColor(Color.white);
        g2.drawRect(xCaixinha, y - tileSize, larguraCaixinha, alturaCaixinha);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));


        // Conteúdo
        if (listaItens.length > 0) {

            String nomeSelecionado = listaItens[numComandoInvent].split(" x")[0];
            Item itemSelecionado = invent.get(nomeSelecionado);

            if (itemSelecionado instanceof ItemConsumo alimento) {

                g2.setColor(Color.yellow);
                String textoAlimento = ("Alimento selecionado: ");
                g2.drawString(textoAlimento, x, y);
                g2.setColor(Color.white);

                String nomeAlimento = alimento.getNome();
                alimento.definirConsumo(nomeAlimento);

                int quantidade = itemSelecionado.getQuantidade();
                int durabilidade = alimento.getDurabilidade();
                int duraMax = alimento.getDurabilidadeMax();
                int sustancia = alimento.getSustancia();

                g2.setColor(Color.red);
                g2.drawString(nomeAlimento, x, y += tileSize);

                g2.setColor(Color.white);
                if (alimento.getEfeito().equals("fome")) {
                    g2.drawString("Sustância: " + sustancia, x, y += tileSize/2);
                }
                g2.drawString("Resta: " + (durabilidade + (quantidade - 1) * duraMax) + "/" + duraMax * quantidade, x, y += tileSize/2);

                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));

                String nivelAtual;
                switch (alimento.getEfeito()) {
                    case "fome":
                        nivelAtual = "Seu nível de fome: " + getJogador().getFome() + "/" + getJogador().getFomeMax();
                        break;
                    case "sede":
                        String hidratacao = getJogador().estaComSede() ? "DESIDRATADO" : "Hidratado";
                        nivelAtual = "Sua sede: " + hidratacao;
                        break;
                    default: nivelAtual = null; break;
                }
                g2.drawString(nivelAtual, x, y += tileSize);


                g2.drawString("Consumir? (Aperte [ENTER])", x, y += tileSize/2);
            }
        }
    }

    public void estruturaRecursoSelecionado(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int x = painel.getLargura() - tileSize * 7;
        int y = tileSize * 17 / 2;

        if (listaItens.length > 0) {

            String nomeSelecionado = listaItens[numComandoInvent].split(" x")[0];
            Item itemSelecionado = invent.get(nomeSelecionado);

            if (itemSelecionado instanceof ItemRecurso recurso) {

                g2.setColor(Color.yellow);
                String textoRecurso = ("Recurso selecionado: ");
                g2.drawString(textoRecurso, x, y);
                g2.setColor(Color.white);

                String nomeRecurso = recurso.getNome();
                recurso.definirRecurso(nomeRecurso);

                g2.setColor(Color.red);
                g2.drawString(nomeRecurso, x, y += tileSize);

                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));

                g2.setColor(Color.yellow);
                String utilidade = "Utilidades: ";
                g2.drawString(utilidade, x, y += tileSize);

                g2.setColor(Color.white);
                String opcoesCrafting = recurso.getOpcaoCrafting();
                g2.drawString(opcoesCrafting, x, y += tileSize / 2);

                if (!recurso.getOpcaoCrafting().equals("...")) {
                    g2.drawString("Construir? (Aperte [ENTER])", x, y += tileSize / 2);
                }
            }
        }
    }

    // Metodo que desenha a arma equipada
    public void desenharArma(Graphics2D g2, int x, int y) {
        int tileSize = painel.getTileSize();
        g2.drawImage(imagemDaArma, x, y, tileSize * 2, tileSize * 2, null);
    }

    // Controle da aparição da tela
    public void abrir() {
        fechado = false;
    }
    public void fechar() {
        fechado = true;
        numComandoInvent = 0;
        if (!painel.getFightState()) {
            botoes.mostrarBotaoMochila();
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

    public HashMap<String, Item> getInvent() { return invent; }

    public boolean isFechado() { return fechado; }
}