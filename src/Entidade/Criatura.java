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
                setVidaMaxCriatura(11);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(2);

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
                setLarguraImagemEscala(9);
                setAlturaImagemEscala(9);
                setDistanciaBordaEscala(11);

                setBoss(true);
                break;

            case 13:
                setNomeCriatura("Lobo Famélico");
                setVidaMaxCriatura(30);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(8);

                setNomeImagem("lobo_famelico");
                setLarguraImagemEscala(6);
                setAlturaImagemEscala(6);
                setDistanciaBordaEscala(7);

                setBoss(false);
                break;

            case 14:
                setNomeCriatura("Corvo Espectral");
                setVidaMaxCriatura(20);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(2);

                setNomeImagem("corvo_espectral");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(9);

                setBoss(true);
                break;

            case 21:
                setNomeCriatura("Crustáceo Tríclope");
                setVidaMaxCriatura(12);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(3);

                setNomeImagem("crustaceo_triclope");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(7);

                setBoss(false);
                break;


            case 22:
                setNomeCriatura("O Crustoso Cruel");
                setVidaMaxCriatura(70);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(5);

                setNomeImagem("crustoso_cruel");
                setLarguraImagemEscala(12);
                setAlturaImagemEscala(12);
                setDistanciaBordaEscala(13);

                setBoss(true);
                break;

            default:
                throw new IllegalArgumentException("Criatura desconhecida: " + tipo);
        }
    }

    // Getters e setters
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
