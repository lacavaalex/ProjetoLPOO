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
                    System.out.println("Você está de barriga cheia. Fome: " + jogador.getFome());
                } else {
                    jogador.setFome(jogador.getFome() + getSustancia());
                    int fomeAtualizada = jogador.getFome();

                    if (fomeAtualizada > jogador.getFomeMax()) {
                        jogador.setFome(jogador.getFomeMax());
                    }
                    else if (fomeAtualizada == jogador.getFomeMax()) {
                        jogador.setVida(jogador.getVida() + 1);
                    }

                    if (getNome().equals("Fruta") || getNome().equals("Peixe")) {
                        int probabilidade = getPainel().definirUmaProbabilidade();

                        if (probabilidade <= 20 && getNome().equals("Fruta")
                        || probabilidade <= 5 && getNome().equals("Peixe")) {
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
        }
        System.out.println("Durabilidade " + getDurabilidade());
    }

    // Itens
    public void definirConsumo(String nome) {
        setTipo("consumo");
        switch (nome) {
            case "Cantil":
                setDurabilidadeMax(4);
                setEfeito("sede");
                break;

            case "Carne":
                setDurabilidadeMax(3);
                setEfeito("fome");
                setSustancia(4);
                break;

            case "Fruta":
                setDurabilidadeMax(1);
                setEfeito("fome");
                setSustancia(3);
                break;

            case "Peixe":
                setDurabilidade(2);
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
        if (!durabilidadeAtribuida) {
            setDurabilidade(getDurabilidadeMax());
            durabilidadeAtribuida = true;
        }

        if (getDurabilidade() == getDurabilidadeMax()) {
            setDurabilidade(getDurabilidadeMax() - 1);
        }
        else if (getDurabilidade() < getDurabilidadeMax()) {
            setDurabilidade(getDurabilidade() - 1);

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

