package Entidade;

public class Jogador {

    private String nome;
    private String localizacao;

    private int vidaMax = 10;
    private int vida = getVidaMax();

    private boolean sede = false;
    private int fomeMax = 10;
    private int fome = 10;
    private int energia;
    private boolean sanidade;

    private String armaAtual = "Nenhuma arma definida.";
    private int ataqueAtual = 1;

    public Jogador() {

    }

// Getters e setters
    public String getArmaAtual() { return armaAtual; }
    public void setArmaAtual(String armaAtual) { this.armaAtual = armaAtual; }

    public int getAtaqueAtual() { return ataqueAtual; }
    public void setAtaqueAtual(int ataqueAtual) { this.ataqueAtual = ataqueAtual; }

    public String getNome() { return nome; }
    public String setNome(String nome) {
        this.nome = nome;
        return nome;
    }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
    public void resetVida() { setVida(vidaMax); }
    public int getVidaMax() { return vidaMax; }
    public void setVidaMax(int vidaMax) { this.vidaMax = vidaMax; }

    public int getFome() { return fome; }
    public void setFome(int fome) { this.fome = fome; }
    public void resetFome() { setFome(fomeMax); }
    public int getFomeMax() { return fomeMax; }
    public void setFomeMax(int fomeMax) { this.fomeMax = fomeMax; }

    public boolean EstaComSede() { return sede; }
    public void setSede(boolean sede) { this.sede = sede; }
}

