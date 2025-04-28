package Entidade;

public class Criatura {

    private String nome;
    private int vidaMax;
    private int vida;
    private int ataque = 0;

    public Criatura() {

    }

    public void definirCriatura(int tipo) {
        switch (tipo) {
            case 1:
                setNomeCriatura("Víbora-Rubro");
                setVidaMaxCriatura(3);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(1);
                break;

            case 2:
                setNomeCriatura("Urso Pai");
                setVidaMaxCriatura(100);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(100);
                break;

            default:
                System.out.println("Criatura desconhecida: ");
                break;
        }
    }

    public String getDescricao() {
        return "" + getNomeCriatura() + ": " + String.valueOf(getVidaCriatura()) + "HP / " + String.valueOf(getAtaqueCriatura()) + "ATK";
    }
    public String getNomeCriatura() {
        return nome;
    }
    public String setNomeCriatura(String nome) {
        this.nome = nome;
        return nome;
    }
    public int getVidaMaxCriatura() { return vidaMax; }
    public void setVidaMaxCriatura(int vidaMax) { this.vidaMax = vidaMax; }
    public int getVidaCriatura() {
        return vida;
    }
    public void setVidaCriatura(int vida) {
        this.vida = vida;
    }
    public int getAtaqueCriatura() {
        return ataque;
    }
    public void setAtaqueCriatura(int ataque) {
        this.ataque = ataque;
    }
}
