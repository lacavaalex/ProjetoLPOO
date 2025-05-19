package Entidade;

public class Criatura {

    private String nome;
    private String nomeImagem;
    private int larguraImagemEscala, alturaImagemEscala, distanciaBordaEscala;
    private int vidaMax;
    private int vida;
    private int ataque = 0;

    public Criatura() {}

    public void definirCriatura(int tipo) {
        switch (tipo) {
            case 1:
                setNomeCriatura("Víbora-Rubro");
                setVidaMaxCriatura(3);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(1);

                setNomeImagem("vibora_rubro");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(6);
                break;

            case 2:
                setNomeCriatura("Urso Pai");
                setVidaMaxCriatura(100);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(100);

                setNomeImagem("urso_pai");
                setLarguraImagemEscala(8);
                setAlturaImagemEscala(8);
                setDistanciaBordaEscala(9);
                break;

            default:
                System.out.println("Criatura desconhecida: ");
                break;
        }
    }

    public String getDescricao() {
        return "" + getNomeCriatura() + ": " + String.valueOf(getVidaCriatura()) + "HP / " + String.valueOf(getAtaqueCriatura()) + "ATK";
    }

    public String getNomeImagem() { return nomeImagem; }
    public void setNomeImagem(String nomeImagem) { this.nomeImagem = nomeImagem; }

    public int getLarguraImagemEscala() { return larguraImagemEscala; }
    public void setLarguraImagemEscala(int larguraImagemEscala) { this.larguraImagemEscala = larguraImagemEscala; }

    public int getAlturaImagemEscala() { return alturaImagemEscala; }
    public void setAlturaImagemEscala(int alturaImagemEscala) { this.alturaImagemEscala = alturaImagemEscala; }

    public int getDistanciaBordaEscala() { return distanciaBordaEscala; }
    public void setDistanciaBordaEscala(int distanciaBordaEscala) { this.distanciaBordaEscala = distanciaBordaEscala; }

    public String getNomeCriatura() {
        return nome;
    }
    public String setNomeCriatura(String nome) { this.nome = nome; return nome; }

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
