package UI;

import Entidade.Jogador;
import Itens.*;
import Main.*;
import Controles.*;
import Sistema.InventarioSistema;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InventarioUI extends UI {

    private Painel painel;
    private Botoes botoes;
    private InventarioSistema inventSystem;

    private BufferedImage fundoInventario, imagemDaArma;
    private boolean fechado = true;
    private String armaAtual;

    public InventarioUI(Painel painel, Jogador jogador, Botoes botoes, InventarioSistema inventSystem) {
        super(painel, jogador);
        this.painel = painel;
        this.botoes = botoes;
        this.inventSystem = inventSystem;
        fundoInventario = setupImagens("fundo_inventario", "background");
    }

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

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));

            inventSystem.atualizarListaItens();

            ui.desenharOpcoes(inventSystem.getListaItens(), tileSize*2, inventSystem.getNumComando());

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));

            estruturaArmaEquipada(g2);
            estruturaAlimentoSelecionado(g2);
            estruturaRecursoSelecionado(g2);
        }
    }

    // Metodos de vizualização das funções dos itens
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
        if (inventSystem.getListaItens().length > 0) {

            String nomeSelecionado = inventSystem.getListaItens()[inventSystem.getNumComando()].split(" x")[0];
            Item itemSelecionado = inventSystem.getInventario().get(nomeSelecionado);

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
                    case "energia":
                        nivelAtual = "Sua energia: " + getJogador().getEnergia() + "/" + getJogador().getEnergiaMax();
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

        if (inventSystem.getListaItens().length > 0) {

            String nomeSelecionado = inventSystem.getListaItens()[inventSystem.getNumComando()].split(" x")[0];
            Item itemSelecionado = inventSystem.getInventario().get(nomeSelecionado);

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
                    g2.drawString("Utilizar? (Aperte [ENTER])", x, y += tileSize / 2);
                }
            }
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

        g2.setColor(new Color(30, 0, 0));
        g2.fillRect(xCaixinha, yCaixinha, larguraCaixinha, alturaCaixinha);

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

        Item equipado = inventSystem.getInventario().get(armaAtual);

        if (inventSystem.getListaItens().length > 0) {

            String nomeSelecionado = inventSystem.getListaItens()[inventSystem.getNumComando()].split(" x")[0];
            Item itemSelecionado = inventSystem.getInventario().get(nomeSelecionado);

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
        inventSystem.setNumComando(0);
        if (!painel.getFightState()) {
            botoes.mostrarBotao("Abrir mochila");
        }
    }

    public boolean isFechado() { return fechado; }
}