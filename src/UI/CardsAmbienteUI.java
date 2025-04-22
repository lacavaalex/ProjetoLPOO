package UI;

import Ambiente.Ambiente;
import Entidade.Jogador;
import Main.Painel;

import java.awt.*;

public class CardsAmbienteUI extends UI {

    public CardsAmbienteUI(Painel painel, Jogador jogador) {
        super(painel, jogador);
    }

// Estrutura dos cards
    public void construirCard(Ambiente ambiente) {
        ambiente = painel.getAmbiente();

        int tileSize = painel.getTileSize();
        int y = tileSize * 3;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.white);
        escreverTexto(ambiente.getNome(), y += tileSize);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
        y = tileSize * 5;

        escreverTexto(ambiente.getDescricao(), y += tileSize);
        escreverTexto(" ", y += tileSize);
        escreverTexto("Condições de exploração: " + ambiente.getDificuldade(), y += tileSize);
        escreverTexto("Recursos possíveis: " + ambiente.getRecursos(), y += tileSize);
        escreverTexto("Ecossistema: " + ambiente.getFrequenciaEventos(), y += tileSize);
        escreverTexto("Clima: " + ambiente.getClima(), y += tileSize);

        botoes.mostrarBotaoContinuar();
    }

// Criação dos cards
    public void cardFloresta() {
        g2.setColor(new Color(5, 20, 5));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        Ambiente floresta = painel.getAmbiente();
        construirCard(floresta);
    }
    public void cardLago() {
        g2.setColor(new Color(115, 155, 255));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        Ambiente lago = painel.getAmbiente();
        construirCard(lago);
    }
    public void cardMontanha() {
        g2.setColor(new Color(180, 180, 180));
        g2.fillRect(0, 0, painel.getLargura(), painel.getAltura());

        Ambiente montanha = painel.getAmbiente();
        construirCard(montanha);
    }
}
