package Entidade;

import Main.Painel;

public class Jogador {

    private Painel painel;

    private String nome;
    private String localizacao;

    private int vidaMax = 10;
    private int vida = getVidaMax();

    private boolean sede = false;
    private int contadorDaSede;
    private String nivelSede = "alto";
    private int fomeMax = 10;
    private int fome = 10;
    private int energiaMax = 10;
    private int energia = getEnergiaMax();
    private boolean sanidade;

    private String armaAtual = "Nenhuma arma definida.";
    private int ataqueAtual = 1;

    public Jogador(Painel painel) {
        this.painel = painel;
    }

    // Condições de jogo
    public void atualizarCondicaoJogador(int numLimite) {
        atualizarEnergia();
        atualizarAtaqueAtual();
        if (numLimite == painel.getQuantidadeSubStatesVisitadosTemporario()) {
            atualizarFome();
            atualizarSede();
            painel.resetarSubStatesVisitadosTemporario();
        }
    }

    public void atualizarFome() {
        if (!EstaComSede()) {
            setFome(getFome() - 1);
        } else {
            setFome(getFome() - 2);
        }
    }

    public void atualizarSede() {
        if (!EstaComSede()) {
            switch (contadorDaSede) {
                case 0, 1, 2:
                    nivelSede = "alto";
                    setSede(false);
                    contadorDaSede += 1;
                    break;
                case 3, 4, 5:
                    nivelSede = "media";
                    setSede(false);
                    contadorDaSede += 1;
                    break;
                default:
                    nivelSede = "baixa";
                    setSede(true);
            }
        } else {
            nivelSede = "baixa";
        }
    }

    public void atualizarEnergia() {
        if (EstaComSede() || getFome() == 0 ) {
            setEnergia(0);
        }
        else {
            if (getFome() == getFomeMax()) {
                setEnergia(getEnergiaMax());
            }
            else if (getFome() > getFomeMax()  * 3 / 4) {
                if (getEnergia() < getEnergiaMax()) {
                    setEnergia(getEnergia() + 1);
                }
            }
            else if (getFome() <= getFomeMax() / 4) {
                setEnergia(getEnergia() - 2);
            }
        }
    }

    public void atualizarAtaqueAtual() {
        if (getEnergia() == 0) {
            if (ataqueAtual > 1) {
                setAtaqueAtual(getAtaqueAtual() / 2);
            }
        }
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

    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { this.energia = energia; }
    public void resetEnergia() { setEnergia(energiaMax); }
    public int getEnergiaMax() { return energiaMax; }
    public void setEnergiaMax(int energiaMax) { this.energiaMax = energiaMax; }

    public String getNivelSede() { return nivelSede; }

    public boolean EstaComSede() { return sede; }
    public void setSede(boolean sede) {
        this.sede = sede;
        if (!sede) {
            contadorDaSede = 0;
        }
    }
}