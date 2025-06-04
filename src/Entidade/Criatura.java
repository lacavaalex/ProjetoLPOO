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
            case 1:
                setNomeCriatura("Víbora-Rubro");
                setVidaMaxCriatura(50);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(10);

                setNomeImagem("vibora_rubro");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(7);

                setBoss(false);
                break;

            case 2:
                setNomeCriatura("Urso Pai");
                setVidaMaxCriatura(250);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(25);

                setNomeImagem("urso_pai");
                setLarguraImagemEscala(11);
                setAlturaImagemEscala(11);
                setDistanciaBordaEscala(12);

                setBoss(true);
                break;

            case 3:
                setNomeCriatura("Lobo Famélico");
                setVidaMaxCriatura(80);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(14);

                setNomeImagem("lobo_famelico");
                setLarguraImagemEscala(8);
                setAlturaImagemEscala(8);
                setDistanciaBordaEscala(10);

                setBoss(false);
                break;

            case 4:
                setNomeCriatura("Corvo Espectral");
                setVidaMaxCriatura(100);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(8);

                setNomeImagem("corvo_espectral");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(9);

                setBoss(true);
                break;

            case 11:
                setNomeCriatura("Crustáceo Tríclope");
                setVidaMaxCriatura(60);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(12);

                setNomeImagem("crustaceo_triclope");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(7);

                setBoss(false);
                break;

            case 12:
                setNomeCriatura("O Crustoso Cruel");
                setVidaMaxCriatura(250);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(18);

                setNomeImagem("crustoso_cruel");
                setLarguraImagemEscala(12);
                setAlturaImagemEscala(12);
                setDistanciaBordaEscala(12);

                setBoss(true);
                break;

            case 21:
                setNomeCriatura("Víbora-Mineral");
                setVidaMaxCriatura(100);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(10);

                setNomeImagem("vibora_mineral");
                setLarguraImagemEscala(4);
                setAlturaImagemEscala(4);
                setDistanciaBordaEscala(7);

                setBoss(false);
                break;

            case 22:
                setNomeCriatura("Goblin Salgado");
                setVidaMaxCriatura(70);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(14);

                setNomeImagem("goblin_sal");
                setLarguraImagemEscala(6);
                setAlturaImagemEscala(6);
                setDistanciaBordaEscala(8);

                setBoss(false);
                break;

            case 23:
                setNomeCriatura("Golem de Sódio");
                setVidaMaxCriatura(400);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(18);

                setNomeImagem("golem_sal");
                setLarguraImagemEscala(12);
                setAlturaImagemEscala(12);
                setDistanciaBordaEscala(12);

                setBoss(true);
                break;

            case 31:
                setNomeCriatura("Leopardo Glacial");
                setVidaMaxCriatura(100);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(17);

                setNomeImagem("leopardo_glacial");
                setLarguraImagemEscala(8);
                setAlturaImagemEscala(8);
                setDistanciaBordaEscala(10);

                setBoss(false);
                break;

            case 32:
                setNomeCriatura("O Funesto");
                setVidaMaxCriatura(400);
                setVidaCriatura(getVidaMaxCriatura());
                setAtaqueCriatura(35);

                setNomeImagem("funesto");
                setLarguraImagemEscala(10);
                setAlturaImagemEscala(10);
                setDistanciaBordaEscala(11);

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
