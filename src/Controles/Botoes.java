package Controles;

import Main.*;

import javax.swing.*;
import java.awt.*;

public class Botoes extends JPanel {

    private JPanel painelBotaoContinuar, painelBotaoVoltar, painelBotaoInicio;
    private JPanel painelBotaoMochila, painelBotaoBase, painelBotaoCardAmbiente, painelBotaoClima;
    private JButton botaoContinuar, botaoVoltar, botaoInicio;
    private JButton botaoMochila, botaoBase, botaoCardAmbiente, botaoClima;

    private Font pixelsans_15 = new Font("Pixel Sans Serif", Font.PLAIN, 15);

    private Painel painel;
    private FuncaoBotao fB;

    public Botoes(Painel painel) {
        this.painel = painel;
        this.fB = new FuncaoBotao(painel, this);

        setLayout(null);
        setOpaque(false);
        estruturaBContinuar();
        estruturaBVoltar();
        estruturaBInventario();
        estruturaBBase();
        estruturaBCardAmbiente();
        estruturaBClima();
        estruturaBInicio();
    }

    // Adequação ao painel
    public void configurarComPainel(Painel painel) {
        setBounds(0, 0, painel.getLargura(), painel.getAltura());
    }

    // Definicoes genericas do painel por trás do potao
    public void definicoesPainelBotao (JPanel painelBotao) {
        painelBotao.setBackground(Color.BLACK);
        painelBotao.setLayout(new BorderLayout());
    }

    // Definiçoes genericas do botao
    public void definicoesBotao(JButton botao) {
        botao.setBackground(Color.BLACK);
        botao.setForeground(Color.WHITE);
        botao.setFont(pixelsans_15);

        // Atribui a ação do action listener ao botao
        botao.addActionListener(fB);
    }

    // Definições genericas de integração painelBotao + botao
    public void definicoesIntegracaoDeBotao(JPanel painelBotao, JButton botao) {
        this.add(painelBotao);
        painelBotao.add(botao, BorderLayout.CENTER);
        painelBotao.setVisible(false);
    }

    // Construção de cada botão
    public void estruturaBContinuar() {
        painelBotaoContinuar = new JPanel();
        painelBotaoContinuar.setBounds(1020, 600, 200, 50);
        definicoesPainelBotao(painelBotaoContinuar);

        botaoContinuar = new JButton("Continuar");
        definicoesBotao(botaoContinuar);

        definicoesIntegracaoDeBotao(painelBotaoContinuar, botaoContinuar);
    }

    public void estruturaBVoltar() {
        painelBotaoVoltar = new JPanel();
        painelBotaoVoltar.setBounds(100, 600, 200, 50);
        definicoesPainelBotao(painelBotaoVoltar);

        botaoVoltar = new JButton("Voltar");
        definicoesBotao(botaoVoltar);

        definicoesIntegracaoDeBotao(painelBotaoVoltar, botaoVoltar);
    }

    public void estruturaBInventario() {
        painelBotaoMochila = new JPanel();
        int largura = 220;
        painelBotaoMochila.setBounds(painel.getLargura()/2 - largura - 10, 600, largura, 50);
        definicoesPainelBotao(painelBotaoMochila);

        botaoMochila = new JButton("Abrir mochila");
        definicoesBotao(botaoMochila);

        definicoesIntegracaoDeBotao(painelBotaoMochila, botaoMochila);
    }

    public void estruturaBBase() {
        painelBotaoBase = new JPanel();
        painelBotaoBase.setBounds(painel.getLargura()/2 + 10, 600, 220, 50);
        definicoesPainelBotao(painelBotaoBase);

        botaoBase = new JButton("Voltar à base");
        definicoesBotao(botaoBase);

        definicoesIntegracaoDeBotao(painelBotaoBase, botaoBase);
    }

    public void estruturaBClima() {
        painelBotaoClima = new JPanel();
        painelBotaoClima.setBounds(painel.getLargura() - painel.getLargura()/8, painel.getTileSize(), 130, 50);
        definicoesPainelBotao(painelBotaoClima);

        botaoClima = new JButton("Clima");
        definicoesBotao(botaoClima);

        definicoesIntegracaoDeBotao(painelBotaoClima, botaoClima);
    }

    public void estruturaBCardAmbiente() {
        painelBotaoCardAmbiente = new JPanel();
        painelBotaoCardAmbiente.setBounds(painel.getLargura() - painel.getLargura()/8, painel.getTileSize() * 4, 130, 50);
        definicoesPainelBotao(painelBotaoCardAmbiente);

        botaoCardAmbiente = new JButton("Local");
        definicoesBotao(botaoCardAmbiente);

        definicoesIntegracaoDeBotao(painelBotaoCardAmbiente, botaoCardAmbiente);
    }

    public void estruturaBInicio() {
        painelBotaoInicio = new JPanel();
        painelBotaoInicio.setBounds(100, 600, 300, 50);
        definicoesPainelBotao(painelBotaoInicio);

        botaoInicio = new JButton("Voltar ao início");
        definicoesBotao(botaoInicio);

        definicoesIntegracaoDeBotao(painelBotaoInicio, botaoInicio);
    }

    // Visibilidade/uso dos botões
    public void mostrarBotaoContinuar() { painelBotaoContinuar.setVisible(true); }
    public void esconderBotaoContinuar() { painelBotaoContinuar.setVisible(false); }

    public void mostrarBotaoVoltar() { painelBotaoVoltar.setVisible(true); }
    public void esconderBotaoVoltar() { painelBotaoVoltar.setVisible(false); }

    public void mostrarBotaoMochila() { painelBotaoMochila.setVisible(true); }
    public void esconderBotaoMochila() { painelBotaoMochila.setVisible(false); }

    public void mostrarBotaoBase() { painelBotaoBase.setVisible(true); }
    public void esconderBotaoBase() { painelBotaoBase.setVisible(false); }

    public void mostrarBotaoCardAmbiente() { painelBotaoCardAmbiente.setVisible(true); }
    public void esconderBotaoCardAmbiente() { painelBotaoCardAmbiente.setVisible(false); }

    public void mostrarBotaoClima() { painelBotaoClima.setVisible(true); }
    public void esconderBotaoClima() { painelBotaoClima.setVisible(false); }

    public void mostrarBotaoInicio() { painelBotaoInicio.setVisible(true); }
    public void esconderBotaoInicio() { painelBotaoInicio.setVisible(false); }


    // Getters e setters
    public JButton getBotaoContinuar() { return botaoContinuar; }
    public JButton getBotaoVoltar() { return botaoVoltar; }
    public JButton getBotaoInicio() { return botaoInicio; }
    public JButton getBotaoMochila() { return botaoMochila; }
    public JButton getBotaoBase() { return botaoBase; }
    public JButton getBotaoCardAmbiente() { return botaoCardAmbiente; }
    public JButton getBotaoClima() { return botaoClima; }

}
