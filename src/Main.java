import javax.swing.JFrame; // Importei a classe JFrame, que me permite criar uma janela
import java.awt.Color;

public class Main {

 public static void main(String[] args) {

  //Abertura e título da janela
  JFrame janela = new JFrame();
  janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  janela.getContentPane().setBackground(Color.black);
  janela.setTitle("O MUNDO FUNESTO");

  // Tamanho e localização da janela na tela (central)
  janela.pack();
  janela.setLocationRelativeTo(null);
  janela.setVisible(true);
  janela.setResizable(false);
  janela.setSize(900, 700);


 }
}