package Controles;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Botoes extends JPanel {

    private Font pixelsans_15 = new Font("Pixel Sans Serif", Font.PLAIN, 15);

    private Painel painel;
    private FuncaoBotao fB;

    private final Map<String, JButton> botoes = new HashMap<>();
    private final Map<String, JPanel> paineisBotao = new HashMap<>();

    public Botoes(Painel painel) {
        this.painel = painel;
        this.fB = new FuncaoBotao(painel, this);

        setLayout(null);
        setOpaque(false);
        gerarTodosOsBotoes();
    }

    // Adequação ao painel
    public void configurarComPainel(Painel painel) {
        setBounds(0, 0, painel.getLargura(), painel.getAltura());
    }

    public void gerarTodosOsBotoes() {
        criarBotao("Continuar",1020, 600, 200, 50);
        criarBotao("Voltar", 100, 600, 200, 50);
        criarBotao("Abrir mochila", painel.getLargura()/2 - 210, 600, 220, 50);
        criarBotao("Voltar à base", painel.getLargura()/2 + 10, 600, 220, 50);
        criarBotao("Clima", painel.getLargura() - painel.getLargura()/8, painel.getTileSize(), 130, 50);
        criarBotao("Local", painel.getLargura() - painel.getLargura()/8, painel.getTileSize() * 4, 130, 50);
        criarBotao("Voltar ao início", 100, 600, 300, 50);
    }

    public void criarBotao(String titulo, int x, int y, int w, int h) {
        JPanel painelBotao = new JPanel();
        painelBotao.setBounds(x, y, w, h);
        painelBotao.setBackground(Color.BLACK);
        painelBotao.setLayout(new BorderLayout());

        JButton botao = new JButton(titulo);
        botao.setBackground(Color.BLACK);
        botao.setForeground(Color.WHITE);
        botao.setFont(pixelsans_15);

        // Atribui a ação do action listener ao botao
        botao.addActionListener(fB);

        this.add(painelBotao);
        painelBotao.add(botao, BorderLayout.CENTER);
        painelBotao.setVisible(false);

        botoes.put(titulo, botao);
        paineisBotao.put(titulo, painelBotao);
    }

    // Visibilidade/uso dos botões
    public void mostrarBotao(String nome) {
        JPanel painel = paineisBotao.get(nome);
        if (painel != null) painel.setVisible(true);
    }

    public void esconderBotao(String nome) {
        JPanel painel = paineisBotao.get(nome);
        if (painel != null) painel.setVisible(false);
    }

    public JButton getBotao(String nome) { return botoes.get(nome); }
}