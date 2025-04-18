package Controles;

import Main.*;

import javax.swing.*;
import java.awt.*;

public class Botões extends JPanel {

    JPanel painelBotaoContinuar, painelBotaoVoltar, painelBotaoInicio, painelMochila, painelBotaoSair;
    JButton botaoContinuar, botaoVoltar, botaoInicio, botaoMochila, botaoSair;
    Font pixelsans_15 = new Font("Pixel Sans Serif", Font.PLAIN, 15);

    Painel painel;
    FunçãoBotão fB;

    public Botões(Painel painel) {
        this.painel = painel;
        this.fB = new FunçãoBotão(painel, this);

        setLayout(null);
        setOpaque(false);
        continuar();
        voltar();
        botaoInventario();
        sair();
        voltarAoInicio();
    }
    public void configurarComPainel(Painel painel) {
        setBounds(0, 0, painel.getLargura(), painel.getAltura());
    }

    public void continuar() {
    // Main.Painel de local do botão continuar
        painelBotaoContinuar = new JPanel();
        painelBotaoContinuar.setBounds(1020, 600, 200, 50);
        painelBotaoContinuar.setBackground(Color.BLACK);
        painelBotaoContinuar.setLayout(new BorderLayout());

    // Botão continuar
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

    public void voltar() {
    // Main.Painel de local do botão voltar
        painelBotaoVoltar = new JPanel();
        painelBotaoVoltar.setBounds(100, 600, 200, 50);
        painelBotaoVoltar.setBackground(Color.BLACK);
        painelBotaoVoltar.setLayout(new BorderLayout());

    // Botão voltar
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setBackground(Color.BLACK);
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFont(pixelsans_15);

   // Atribui a ação do action listener ao botao
        botaoVoltar.addActionListener(fB);

        this.add(painelBotaoVoltar);
        painelBotaoVoltar.add(botaoVoltar, BorderLayout.CENTER);

        painelBotaoVoltar.setVisible(false);
    }

    public void botaoInventario() {
        painelMochila = new JPanel();
        painelMochila.setBounds(painel.getLargura()/2 - 140/2, 600, 140, 50);
        painelMochila.setBackground(Color.BLACK);
        painelMochila.setLayout(new BorderLayout());

        botaoMochila = new JButton("Mochila");
        botaoMochila.setBackground(Color.BLACK);
        botaoMochila.setForeground(Color.WHITE);
        botaoMochila.setFont(pixelsans_15);

    // Atribui a ação do action listener ao botao
        botaoMochila.addActionListener(fB);

        this.add(painelMochila);
        painelMochila.add(botaoMochila, BorderLayout.CENTER);

        painelMochila.setVisible(false);
    }

    public void sair() {
        painelBotaoSair = new JPanel();
        painelBotaoSair.setBounds(100, 600, 150, 50);
        painelBotaoSair.setBackground(Color.BLACK);
        painelBotaoSair.setLayout(new BorderLayout());

        botaoSair = new JButton("Sair");
        botaoSair.setBackground(Color.BLACK);
        botaoSair.setForeground(Color.WHITE);
        botaoSair.setFont(pixelsans_15);

        botaoSair.addActionListener(fB);

        this.add(painelBotaoSair);
        painelBotaoSair.add(botaoSair, BorderLayout.CENTER);

        painelBotaoSair.setVisible(false);
    }


    public void voltarAoInicio() {
    // Main.Painel de local do botão
        painelBotaoInicio = new JPanel();
        painelBotaoInicio.setBounds(100, 600, 300, 50);
        painelBotaoInicio.setBackground(Color.BLACK);
        painelBotaoInicio.setLayout(new BorderLayout());

    // Botão
        botaoInicio = new JButton("Voltar ao início");
        botaoInicio.setBackground(Color.BLACK);
        botaoInicio.setForeground(Color.WHITE);
        botaoInicio.setFont(pixelsans_15);

    // Atribui a ação do action listener ao botao
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

    public void mostrarBotaoSair() { painelBotaoSair.setVisible(true); }
    public void esconderBotaoSair() { painelBotaoSair.setVisible(false); }

    public void mostrarBotaoInicio() { painelBotaoInicio.setVisible(true); }
    public void esconderBotaoInicio() { painelBotaoInicio.setVisible(false); }

    public JButton getBotaoContinuar() { return botaoContinuar; }
    public JButton getBotaoVoltar() { return botaoVoltar; }
    public JButton getBotaoInicio() { return botaoInicio; }
    public JButton getBotaoMochila() { return botaoMochila; }
    public JButton getBotaoSair() { return botaoSair; }

}
