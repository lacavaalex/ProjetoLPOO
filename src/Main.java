import javax.swing.*;

public class Main {

 public static void main(String[] args) {

  //Chamada de classes externas de swing (JFrame para a janela e JPanel para recurso visual)
  JFrame janela = new JFrame();

  //Abertura e título da janela
  janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  janela.setTitle("O MUNDO FUNESTO");

  //Integração das funcionalidades do painel na janela
  Painel painel = new Painel();
  janela.add(painel);

  // Tamanho e localização da janela na tela (central)
  janela.pack();
  janela.setLocationRelativeTo(null);
  janela.setVisible(true);
  janela.setResizable(false);

  //Rodar o jogo
  painel.setupJogo();
  painel.iniciarGameThread();
 }
}