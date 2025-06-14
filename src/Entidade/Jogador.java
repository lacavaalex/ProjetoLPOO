package Entidade;

import Main.Painel;

public class Jogador {

    private Painel painel;

    private String nome;
    private String habilidade;
    private String localizacao;

    private int vidaMax = 200;
    private int vida = getVidaMax();

    private boolean sede = false;
    private int contadorDaSede;
    private String nivelSede = "alto";

    private int fomeMax = 10;
    private int fome = 10;

    private int energiaMax = 10;
    private int energia = getEnergiaMax();

    private boolean envenenado;

    private String armaAtual = "Nenhuma arma equipada.";
    private final int ataqueInicial = 10;
    private int ataqueAtual;

    public Jogador(Painel painel) {
        this.painel = painel;
    }

    // Condições de jogo
    public void atualizarCondicaoJogador(int numLimite) {
        atualizarEnergia();
        atualizarAtaqueAtual();
        if (numLimite == painel.getQuantidadeSubStatesVisitadosTemporario()) {
            if (estaEnvenenado()) {
                setEnergia(getEnergia() - 1);
            }
            if (painel.getEventoClimatico().getClima().equals("tornado")
                    && !getHabilidade().equals("SOBREVIVENCIAL")) {
                int probabilidade = painel.definirUmaProbabilidade();
                boolean afetado = probabilidade <= 50;
                if (afetado) {
                    setVida(getVida() - 2);
                    afetado = false;
                }
            }
            atualizarFome();
            atualizarSede();
            atualizarVida();
            painel.resetarSubStatesVisitadosTemporario();
        }
    }

    public void atualizarVida() {
        if (!getHabilidade().equals("MEDICINAL")) {
            if (getEnergia() > getEnergiaMax() / 2
                    && !estaComSede()
                    && getFome() > getFomeMax() * 2 / 3
                    && getVida() < getVidaMax()
            ) {
                setVida(getVida() + 5);
            }
        } else {
            if (!estaComSede()
                    && getFome() > getFomeMax()/4
                    && getVida() < getVidaMax()
            ) {
                setVida(getVida() + 5);
            }
        }

        setVidaMax(getVidaMax() + painel.getAmbienteAtual().getBaseFortificacao());
    }

    public void atualizarFome() {
        if (!estaComSede()) {
            setFome(getFome() - 1);
        } else {
            if (!getHabilidade().equals("MEDICINAL")) {
                setFome(getFome() - 1);
            }
            else {
                setFome(getFome() - 2);
            }
        }
        if (getFome() <= 0) {
            setFome(0);
        }
    }

    public void atualizarSede() {
        if (estaComSede()) {
            setNivelSede("baixa");
            return;
        }

        boolean climaAceleraSede = painel.getEventoClimatico().getClima().equals("cavernoso")
                || painel.getEventoClimatico().getClima().equals("salgado");

        if (climaAceleraSede) {
            switch (contadorDaSede) {
                case 0:
                    setNivelSede("alto");
                    break;
                case 1:
                    setNivelSede("media");
                    break;
                default:
                    setNivelSede("baixa");
                    setSede(true);
            }
        } else {
            switch (contadorDaSede) {
                case 0:
                case 1:
                    setNivelSede("alto");
                    break;
                case 2:
                case 3:
                    setNivelSede("media");
                    break;
                default:
                    setNivelSede("baixa");
                    setSede(true);
            }
        }
        setContadorDaSede(getContadorDaSede() + 1);
    }

    public void atualizarEnergia() {
        if (estaComSede() || getFome() == 0) {
            setEnergia(0);
        } else {
            if (getFome() <= getFomeMax() / 4) {
                setEnergia(getEnergia() - 2);
            }
        }

        if (getEnergia() <= 0) {
            setEnergia(0);
        }
    }

    public void atualizarAtaqueAtual() {
        if (getEnergia() == 0) {
            int ataqueFraco = ataqueInicial / 2;
            setAtaqueAtual(ataqueFraco);
        } else {
            setAtaqueAtual(ataqueAtual);
        }
    }

    public void resetarTodosOsAtributos() {
        setVidaMax(vidaMax);
        resetVida();
        resetFome();
        resetEnergia();
        setAtaqueAtual(ataqueInicial);
        setArmaAtual("Nenhuma arma equipada.");
        setSede(false);
        contadorDaSede = 0;
        nivelSede = "alto";
        setEnvenenado(false);
        setNome(null);
    }

    // Getters e setters
    public String getArmaAtual() { return armaAtual; }
    public void setArmaAtual(String armaAtual) { this.armaAtual = armaAtual; }

    public int getAtaqueInicial() { return ataqueInicial; }
    public int getAtaqueAtual() { return ataqueAtual; }
    public void setAtaqueAtual(int ataqueAtual) { this.ataqueAtual = ataqueAtual; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome;}

    public String getHabilidade() { return habilidade; }
    public void setHabilidade(String habilidade) { this.habilidade = habilidade; }

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

    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { this.energia = energia; }
    public void resetEnergia() { setEnergia(energiaMax); }
    public int getEnergiaMax() { return energiaMax; }

    public boolean estaEnvenenado() { return envenenado; }
    public void setEnvenenado(boolean envenenado) {
        if (!getHabilidade().equals("MEDICINAL")) {
            this.envenenado = envenenado;
        }
    }

    public boolean estaComSede() { return sede; }
    public void setSede(boolean sede) {
        this.sede = sede;
        if (!sede) {
            setContadorDaSede(0);
            setNivelSede("alto");
        }
    }

    public int getContadorDaSede() { return contadorDaSede; }
    public void setContadorDaSede(int contadorDaSede) { this.contadorDaSede = contadorDaSede; }

    public String getNivelSede() { return nivelSede; }
    public void setNivelSede(String nivelSede) { this.nivelSede = nivelSede; }
}