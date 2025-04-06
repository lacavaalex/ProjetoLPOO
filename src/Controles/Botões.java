package Controles;

import Main.*;

import javax.swing.*;
import java.awt.*;

public class Botões extends JPanel {

    JPanel painelBotaoContinuar, painelBotaoVoltar;
    JButton botaoContinuar, botaoVoltar;
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
    }
    public void configurarComPainel(Painel painel) {
        setBounds(0, 0, painel.getLargura(), painel.getAltura());
    }

    public void continuar() {
    // Main.Painel de local do botão continuar
        painelBotaoContinuar = new JPanel();
        painelBotaoContinuar.setBounds(620, 600, 200, 50);
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
        painelBotaoVoltar.setBounds(120, 600, 200, 50);
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


// Visibilidade/uso dos botões
    public void mostrarBotaoContinuar() {
        painelBotaoContinuar.setVisible(true);
    }
    public void esconderBotaoContinuar() {
        painelBotaoContinuar.setVisible(false);
    }

    public void mostrarBotaoVoltar() {
        painelBotaoVoltar.setVisible(true);
    }
    public void esconderBotaoVoltar() {
        painelBotaoVoltar.setVisible(false);
    }


    public JButton getBotaoContinuar() {
        return botaoContinuar;
    }
    public JButton getBotaoVoltar() {
        return botaoVoltar;
    }
}
