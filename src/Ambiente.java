import javax.swing.*;
import java.awt.*;

public class Ambiente extends JPanel {

    Painel painel;
    Font pixelsans_10;

    private String nome, descricao, dificuldade, recursos, frequenciaEventos, clima;

    public void ambienteFloresta() {
        this.nome = "FLORESTA MACABRA";
        this.descricao = "Escura, densa, barulhenta.";
        this.dificuldade = "medianas.";
        this.recursos = "frutas, água, madeira, pedras.";
        this.frequenciaEventos = "muitas criaturas, esconderijos, riscos à saúde.";
        this.clima = "levemente frio.";
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDificuldade() {
        return dificuldade;
    }
    public String getRecursos() {
        return recursos;
    }
    public String getFrequenciaEventos() {
        return frequenciaEventos;
    }
    public String getClima() {
        return clima;
    }
}


