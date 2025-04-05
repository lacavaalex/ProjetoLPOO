import javax.swing.*;
import java.awt.*;

public class Botões extends JPanel {

    JPanel painelBotaoContinuar;
    JButton botaoContinuar;
    Font pixelsans_20 = new Font("Pixel Sans Serif", Font.PLAIN, 20);

    public Botões() {
        setLayout(null);
        setOpaque(false);
        continuar();
    }
    public void configurarComPainel(Painel painel) {
        setBounds(0, 0, painel.getLargura(), painel.getAltura());
    }


    public void continuar() {

    //Painel de local do botão continuar
        painelBotaoContinuar = new JPanel();
        painelBotaoContinuar.setBounds(600, 560, 250, 75);
        painelBotaoContinuar.setBackground(Color.BLACK);
        painelBotaoContinuar.setLayout(new BorderLayout());

    // Botão continuar
        botaoContinuar = new JButton("Continuar");
        botaoContinuar.setBackground(Color.BLACK);
        botaoContinuar.setForeground(Color.WHITE);
        botaoContinuar.setFont(pixelsans_20);

        this.add(painelBotaoContinuar);
        painelBotaoContinuar.add(botaoContinuar, BorderLayout.CENTER);

        painelBotaoContinuar.setVisible(false);
    }

    public void mostrarBotao() {
        painelBotaoContinuar.setVisible(true);
    }
    public void esconderBotao() {
        painelBotaoContinuar.setVisible(false);
    }

    public JButton getBotaoContinuar() {
        return botaoContinuar;
    }
}
