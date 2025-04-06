import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FunçãoBotão implements ActionListener {

    Painel painel;

    public FunçãoBotão(Painel painel) {
        this.painel = painel;
    }

    public void actionPerformed(ActionEvent e) {

        int gameState = painel.getGameState();
        gameState = gameState + 1;
        painel.setGameState(gameState++);
        System.out.println(gameState);
    }
}
