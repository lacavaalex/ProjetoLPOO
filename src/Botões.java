import javax.swing.*;
import java.awt.*;

public class Botões extends JPanel {

    JPanel painelBotaoContinuar;
    JButton botaoContinuar;
    Font pixelsans_15 = new Font("Pixel Sans Serif", Font.PLAIN, 15);

    Painel painel;
    FunçãoBotão fB;

    public Botões(Painel painel) {
        this.painel = painel;
        this.fB = new FunçãoBotão(painel);

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
        painelBotaoContinuar.setBounds(620, 600, 200, 65);
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

    public void mostrarBotaoContinuar() {
        painelBotaoContinuar.setVisible(true);
    }
    public void esconderBotaoContinuar() {
        painelBotaoContinuar.setVisible(false);
    }

    public JButton getBotaoContinuar() {
        return botaoContinuar;
    }
}
