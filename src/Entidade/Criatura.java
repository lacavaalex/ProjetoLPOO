package Entidade;

public class Criatura {

    private String nome;
    private String nomeImagem;
    private int larguraImagemEscala, alturaImagemEscala, distanciaBordaEscala;
    private int vidaMax;
    private int vida;
    private int ataque = 0;
    private boolean boss;

    public Criatura() {}

    public void definirCriatura(int tipo) {
        switch (tipo) {
            case 11:
                setNomeCriatura("Víbora-Rubro");
                setVidaMaxCriatura(3);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(1);

                setNomeImagem("vibora_rubro");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(7);

                setBoss(false);
                break;

            case 12:
                setNomeCriatura("Urso Pai");
                setVidaMaxCriatura(100);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(100);

                setNomeImagem("urso_pai");
                setLarguraImagemEscala(8);
                setAlturaImagemEscala(8);
                setDistanciaBordaEscala(9);

                setBoss(true);
                break;

            case 21:
                setNomeCriatura("Crustáceo Tríclope");
                setVidaMaxCriatura(3);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(2);

                setNomeImagem("crustaceo_triclope");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(7);

                setBoss(false);
                break;


            case 22:
                setNomeCriatura("O Crustoso Cruel");
                setVidaMaxCriatura(50);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(10);

                setNomeImagem("crustoso_cruel");
                setLarguraImagemEscala(12);
                setAlturaImagemEscala(12);
                setDistanciaBordaEscala(14);

                setBoss(true);
                break;

            default:
                throw new IllegalArgumentException("Criatura desconhecida: " + tipo);
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

    public boolean isBoss() { return boss; }
    public void setBoss(boolean boss) { this.boss = boss; }
}
