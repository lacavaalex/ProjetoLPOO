package Itens;

import Entidade.Jogador;
import Main.Painel;

public class ItemConsumo extends Item {

    private Jogador jogador;

    private int sustancia;
    private String efeito;
    private boolean efeitoAtribuido;
    private boolean durabilidadeAtribuida = false;

    public ItemConsumo(Painel painel, Jogador jogador) {
        super(painel);
        this.jogador = jogador;
    }


    @Override
    public void usar(String nome) {
        definirConsumo(nome);

        // Efeito
        if (!efeitoAtribuido) {
            efeito = getEfeito();
            efeitoAtribuido = true;
        }
        switch (efeito) {
            case "fome":
                if (jogador.getFome() >= jogador.getFomeMax()) {
                    jogador.setFome(jogador.getFomeMax());
                } else {
                    jogador.setFome(jogador.getFome() + getSustancia());
                    int fomeAtualizada = jogador.getFome();

                    if (fomeAtualizada > jogador.getFomeMax()) {
                        jogador.setFome(jogador.getFomeMax());
                    }
                    else if (fomeAtualizada == jogador.getFomeMax()) {
                        if (jogador.getVida() < jogador.getVidaMax()) {
                            jogador.setVida(jogador.getVida() + 1);
                        }
                    }

                    if (getNome().equals("Fruta") || getNome().equals("Peixe") ||
                    getNome().equals("Carne magra") || getNome().equals("Carne suculenta")) {

                        int probabilidade = getPainel().definirUmaProbabilidade();

                        if (probabilidade <= 4 && getNome().equals("Peixe")
                                || probabilidade <= 8 && getNome().equals("Carne magra")
                                || probabilidade <= 8 && getNome().equals("Carne suculenta")
                                || probabilidade <= 20 && getNome().equals("Fruta")) {
                            jogador.setEnvenenado(true);
                        }
                    }
                    gastarDurabilidade();
                }
                break;

            case "sede":
                if (!jogador.estaComSede()) {
                    System.out.println("Você está sem sede. Sede: " + jogador.estaComSede());
                } else {
                    jogador.setSede(false);
                    gastarDurabilidade();
                }
                break;

            case "energia":
                if (jogador.getEnergia() >= jogador.getEnergiaMax()) {
                    jogador.setEnergia(jogador.getEnergiaMax());
                    System.out.println("Sua energia está no máximo.");
                } else {
                    jogador.setEnergia(jogador.getEnergia() + getSustancia());
                    int energiaAtualizada = jogador.getEnergia();

                    if (energiaAtualizada > jogador.getEnergiaMax()) {
                        jogador.setEnergia(jogador.getEnergiaMax());
                    }
                    else if (energiaAtualizada == jogador.getEnergiaMax()) {
                        if (jogador.getVida() < jogador.getVidaMax()) {
                            jogador.setVida(jogador.getVida() + 1);
                        }
                    }
                    gastarDurabilidade();
                }
                break;
        }
        System.out.println("Durabilidade " + getDurabilidade());
    }

    // Itens
    public void definirConsumo(String nome) {
        setTipo("consumo");
        switch (nome) {
            case "Cantil":
                setDurabilidadeMax(3);
                setEfeito("sede");
                break;

            case "Mel":
                setDurabilidadeMax(1);
                setEfeito("energia");
                setSustancia(2);
                break;

            case "Carne suculenta":
                setDurabilidadeMax(2);
                setEfeito("fome");
                setSustancia(4);
                break;

            case "Carne magra":
                setDurabilidadeMax(1);
                setEfeito("fome");
                setSustancia(2);
                break;

            case "Fruta":
                setDurabilidadeMax(1);
                setEfeito("fome");
                setSustancia(3);
                break;

            case "Peixe":
                setDurabilidadeMax(2);
                setEfeito("fome");
                setSustancia(3);
                break;

            default:
                throw new IllegalArgumentException("Alimento desconhecido: " + nome);
        }
        if (!durabilidadeAtribuida) {
            setDurabilidade(getDurabilidadeMax());
            durabilidadeAtribuida = true;
        } else {
            setDurabilidade(getDurabilidade());
        }
    }

    // Calculo de durabilidade
    public void gastarDurabilidade() {
        if (getDurabilidade() <= getDurabilidadeMax()) {
            setDurabilidade(getDurabilidadeMax() - 1);

            if (getDurabilidade() == 0) {
                getPainel().getInvent().removerItem(getNome(), 1);

                Item resto = getPainel().getInvent().getInvent().get(getNome());
                if (resto instanceof ItemConsumo proximaUnidade) {
                    proximaUnidade.setDurabilidade(proximaUnidade.getDurabilidadeMax());
                }
            }
        }
    }

    // Getters e setters
    public int getSustancia() { return sustancia; }
    public void setSustancia(int sustancia) {this.sustancia = sustancia; }

    public String getEfeito() { return efeito; }
    public void setEfeito(String efeito) { this.efeito = efeito; }
}