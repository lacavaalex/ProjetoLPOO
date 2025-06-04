package Main;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Main {

 public static void main(String[] args) {

  JFrame janela = new JFrame();

  janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  janela.setTitle("O MUNDO FUNESTO");

  // Integração das funcionalidades do painel na janela
  Painel painel = new Painel();
  janela.add(painel);

  BufferedImage icon = painel.getUi().setupImagens("game_icon", "icones");
  janela.setIconImage(icon);

  // Tamanho e localização da janela na tela (central)
  janela.pack();
  janela.setLocationRelativeTo(null);
  janela.setVisible(true);
  janela.setResizable(false);

  painel.setupJogo();
  painel.iniciarGameThread();
 }
}