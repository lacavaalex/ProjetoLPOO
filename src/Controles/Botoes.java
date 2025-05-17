package Controles;

import Main.*;

import javax.swing.*;
import java.awt.*;

public class Botoes extends JPanel {

    private JPanel painelBotaoContinuar, painelBotaoVoltar, painelBotaoInicio, painelMochila, painelBotaoClima;
    private JButton botaoContinuar, botaoVoltar, botaoInicio, botaoMochila, botaoClima;
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
        painelMochila = new JPanel();
        painelMochila.setBounds(painel.getLargura()/2 - 140/2, 600, 140, 50);
        definicoesPainelBotao(painelMochila);

        botaoMochila = new JButton("Mochila");
        definicoesBotao(botaoMochila);

        definicoesIntegracaoDeBotao(painelMochila, botaoMochila);
    }

    public void estruturaBClima() {
        painelBotaoClima = new JPanel();
        painelBotaoClima.setBounds(painel.getLargura() - painel.getLargura()/8, painel.getAltura()/12, 130, 50);
        definicoesPainelBotao(painelBotaoClima);

        botaoClima = new JButton("Clima");
        definicoesBotao(botaoClima);

        definicoesIntegracaoDeBotao(painelBotaoClima, botaoClima);
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

    public void mostrarBotaoMochila() { painelMochila.setVisible(true); }
    public void esconderBotaoMochila() { painelMochila.setVisible(false); }

    public void mostrarBotaoClima() { painelBotaoClima.setVisible(true); }
    public void esconderBotaoClima() { painelBotaoClima.setVisible(false); }

    public void mostrarBotaoInicio() { painelBotaoInicio.setVisible(true); }
    public void esconderBotaoInicio() { painelBotaoInicio.setVisible(false); }


    // Getters e setters
    public JButton getBotaoContinuar() { return botaoContinuar; }
    public JButton getBotaoVoltar() { return botaoVoltar; }
    public JButton getBotaoInicio() { return botaoInicio; }
    public JButton getBotaoMochila() { return botaoMochila; }
    public JButton getBotaoClima() { return botaoClima; }

}
