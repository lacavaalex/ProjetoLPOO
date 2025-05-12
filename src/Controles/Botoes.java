package Controles;

import Main.*;

import javax.swing.*;
import java.awt.*;

public class Botoes extends JPanel {

    private JPanel painelBotaoContinuar, painelBotaoVoltar, painelBotaoInicio, painelMochila;
    private JButton botaoContinuar, botaoVoltar, botaoInicio, botaoMochila;
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
        estruturaBInicio();
    }

    // Adequação ao painel
    public void configurarComPainel(Painel painel) {
        setBounds(0, 0, painel.getLargura(), painel.getAltura());
    }

    // Construção de cada botão

    public void estruturaBContinuar() {
        // Painel do botão
        painelBotaoContinuar = new JPanel();
        painelBotaoContinuar.setBounds(1020, 600, 200, 50);
        painelBotaoContinuar.setBackground(Color.BLACK);
        painelBotaoContinuar.setLayout(new BorderLayout());

         // Botão
        botaoContinuar = new JButton("Continuar");
        botaoContinuar.setBackground(Color.BLACK);
        botaoContinuar.setForeground(Color.WHITE);
        botaoContinuar.setFont(pixelsans_15);

        // Atribui a ação do action listener ao botao
        botaoContinuar.addActionListener(fB);
        this.add(painelBotaoContinuar);
        painelBotaoContinuar.add(botaoContinuar, BorderLayout.CENTER);
        painelBotaoContinuar.setVisible(false);
    }

    public void estruturaBVoltar() {
        painelBotaoVoltar = new JPanel();
        painelBotaoVoltar.setBounds(100, 600, 200, 50);
        painelBotaoVoltar.setBackground(Color.BLACK);
        painelBotaoVoltar.setLayout(new BorderLayout());

        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBackground(Color.BLACK);
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFont(pixelsans_15);

        botaoVoltar.addActionListener(fB);
        this.add(painelBotaoVoltar);
        painelBotaoVoltar.add(botaoVoltar, BorderLayout.CENTER);
        painelBotaoVoltar.setVisible(false);
    }

    public void estruturaBInventario() {
        painelMochila = new JPanel();
        painelMochila.setBounds(painel.getLargura()/2 - 140/2, 600, 140, 50);
        painelMochila.setBackground(Color.BLACK);
        painelMochila.setLayout(new BorderLayout());

        botaoMochila = new JButton("Mochila");
        botaoMochila.setBackground(Color.BLACK);
        botaoMochila.setForeground(Color.WHITE);
        botaoMochila.setFont(pixelsans_15);

        botaoMochila.addActionListener(fB);
        this.add(painelMochila);
        painelMochila.add(botaoMochila, BorderLayout.CENTER);
        painelMochila.setVisible(false);
    }

    public void estruturaBInicio() {
        painelBotaoInicio = new JPanel();
        painelBotaoInicio.setBounds(100, 600, 300, 50);
        painelBotaoInicio.setBackground(Color.BLACK);
        painelBotaoInicio.setLayout(new BorderLayout());

        botaoInicio = new JButton("Voltar ao início");
        botaoInicio.setBackground(Color.BLACK);
        botaoInicio.setForeground(Color.WHITE);
        botaoInicio.setFont(pixelsans_15);

        botaoInicio.addActionListener(fB);
        this.add(painelBotaoInicio);
        painelBotaoInicio.add(botaoInicio, BorderLayout.CENTER);
        painelBotaoInicio.setVisible(false);
    }


    // Visibilidade/uso dos botões
    public void mostrarBotaoContinuar() { painelBotaoContinuar.setVisible(true); }
    public void esconderBotaoContinuar() { painelBotaoContinuar.setVisible(false); }

    public void mostrarBotaoVoltar() { painelBotaoVoltar.setVisible(true); }
    public void esconderBotaoVoltar() { painelBotaoVoltar.setVisible(false); }

    public void mostrarBotaoMochila() { painelMochila.setVisible(true); }
    public void esconderBotaoMochila() { painelMochila.setVisible(false); }

    public void mostrarBotaoInicio() { painelBotaoInicio.setVisible(true); }
    public void esconderBotaoInicio() { painelBotaoInicio.setVisible(false); }


    // Getters e setters
    public JButton getBotaoContinuar() { return botaoContinuar; }
    public JButton getBotaoVoltar() { return botaoVoltar; }
    public JButton getBotaoInicio() { return botaoInicio; }
    public JButton getBotaoMochila() { return botaoMochila; }

}
