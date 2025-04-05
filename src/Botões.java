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

    public void continuar() {

    //Painel de local do botão continuar
        setLayout(null);
        painelBotaoContinuar = new JPanel();
        painelBotaoContinuar.setBounds(600, 560, 250, 100);
        painelBotaoContinuar.setBackground(Color.BLACK);
    // Botão continuar
        botaoContinuar = new JButton("Continuar");
        botaoContinuar.setBackground(Color.BLACK);
        botaoContinuar.setForeground(Color.WHITE);
        botaoContinuar.setFont(pixelsans_20);

        this.add(painelBotaoContinuar);
        painelBotaoContinuar.add(botaoContinuar);
        this.repaint();
    }
}
