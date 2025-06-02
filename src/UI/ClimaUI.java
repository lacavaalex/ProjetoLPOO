package UI;

import Controles.Botoes;
import Entidade.Jogador;
import Evento.EventoClimatico;
import Main.Painel;

import java.awt.*;

public class ClimaUI extends UI {

    private Painel painel;
    private Botoes botoes;
    private EventoClimatico eventoClimatico;

    private boolean analisandoClima = false;

    public ClimaUI(Painel painel, Jogador jogador, Botoes botoes, EventoClimatico eventoClimatico) {
        super(painel, jogador);
        this.painel = painel;
        this.botoes = botoes;
        this.eventoClimatico = eventoClimatico;
    }

    public void verSituacaoClimatica() {
        analisandoClima = true;
    }
    public void sair() {
        analisandoClima = false;
        botoes.mostrarBotao("Clima");
    }

    // Descrição dos climas
    public void estruturaTelaDeClima(Graphics2D g2, UI ui) {
        if (analisandoClima) {

            ui.setGraphics(g2);

            int tileSize = painel.getTileSize();
            int larguraTela = painel.getLargura();
            int alturaTela = painel.getAltura();

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, larguraTela, alturaTela);

            int y = tileSize * 3;
            g2.setColor(Color.white);

            String clima = eventoClimatico.getClima();

            switch (clima) {
                case "chuva":
                    ui.escreverTexto("Está chovendo.", y);
                    ui.escreverTexto("Isso pode afetar sua visibilidade.", y += tileSize);
                    g2.setColor(Color.gray);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Chances de encontro com criaturas aumentadas.)", y += tileSize);
                    g2.setColor(Color.white);
                    break;

                case "tempestade":
                    ui.escreverTexto("Uma tempestade furiosa se inicia.", y);
                    ui.escreverTexto("Nesse caos relampejante, parece que seus inimigos se fortalecem", y += tileSize);
                    g2.setColor(Color.gray);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Chances de encontro com criaturas aumentadas.)", y += tileSize);
                    ui.escreverTexto("(Ataque de criatura aumentado.)", y += tileSize);
                    g2.setColor(Color.white);
                    break;

                case "tornado":
                    ui.escreverTexto("Um tornado se formou de repente!", y);
                    ui.escreverTexto("Ele assola tudo que toca, você está em perigo!", y += tileSize);
                    g2.setColor(Color.gray);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Chances de encontro com criaturas aumentadas.)", y += tileSize);
                    ui.escreverTexto("(Chances de dano aleatório aumentadas.)", y += tileSize);
                    g2.setColor(Color.white);
                    break;

                case "cavernoso":
                    ui.escreverTexto("Essa caverna não demonstra instabilidade.", y);
                    ui.escreverTexto("Porém, a humidade é estranha. O ambiente parece drenar você.", y += tileSize);
                    g2.setColor(Color.gray);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Sua sede será gasta mais rapidamente.)", y += tileSize);
                    g2.setColor(Color.white);
                    break;

                case "salgado":
                    ui.escreverTexto("O goblin maldito fez algo com as pedras... uma poeira de sódio", y);
                    ui.escreverTexto("se espalha no ar, e seu organismo não está adaptado para respirá-la.", y += tileSize);
                    g2.setColor(Color.gray);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Sua sede será gasta mais rapidamente.)", y += tileSize);
                    ui.escreverTexto("(Você está mais fraco contra as criaturas da caverna.)", y += tileSize);
                    g2.setColor(Color.white);
                    break;

                case "montanha":
                    ui.escreverTexto("Aqui, não há como escapar.", y);
                    ui.escreverTexto("Essa nevasca te envolverá até o último suspiro", y += tileSize);
                    g2.setColor(Color.gray);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Você não pode montar base aqui)", y += tileSize);
                    ui.escreverTexto("(Você não pode fugir de batalhas)", y += tileSize);
                    g2.setColor(Color.white);
                    break;

                case "ameno":
                    ui.escreverTexto("O clima está ameno.", y);
                    ui.escreverTexto("Sem sinais de mudanças, por agora...", y += tileSize);
                    break;

                default:
                    throw new IllegalArgumentException("Clima desconhecido: " + clima);
            }

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
            String textoEsc = ("Aperte [esc] para sair");
            g2.drawString(textoEsc, painel.getLargura() - tileSize * 6,painel.getAltura() - tileSize);
        }
    }

    public boolean isAnalisandoClima() { return analisandoClima; }
}
