package Itens;

import Main.Painel;

public class ItemRecurso extends Item {

    private Painel painel;

    private String opcaoCrafting;
    private String itemParaRemoverNoCrafting;
    private String tipoNovoItem;

    public ItemRecurso(Painel painel) {
        super(painel);
        this.painel = painel;
    }

    @Override
    public void usar(String nome) {
        definirRecurso(nome);
        if (!getOpcaoCrafting().equals("...")) {

            if (getTipoNovoItem() != null && getItemParaRemoverNoCrafting() != null) {

                if (getTipoNovoItem().equals("combate") && painel.getInventSystem().acharItem(getOpcaoCrafting())) {
                    System.out.println("Arma já existe no inventário.");
                }
                else {
                    painel.getInventSystem().removerItem(getNome(), 1);

                    painel.getInventSystem().removerItem(getItemParaRemoverNoCrafting(), 1);
                    if (getItemParaRemoverNoCrafting().equals(painel.getJogador().getArmaAtual())) {
                        painel.getJogador().setArmaAtual("Nenhuma arma definida.");
                    }

                    painel.getInventSystem().adicionarItem(getOpcaoCrafting(), getTipoNovoItem(), 1);

                    if (getTipoNovoItem().equals("combate") && !painel.getInventSystem().acharItem(getOpcaoCrafting())) {
                        setOpcaoCrafting("...");
                    }
                }
            }

            else {
                if (painel.getPlaySubState() == 1) {
                    switch (getOpcaoCrafting()) {
                        case "Plantio":
                            painel.getAmbienteAtual().setBaseFonteDeAlimento(true);
                            break;
                        case "Fogueira":
                            painel.getAmbienteAtual().setBaseFogoAceso(true);
                            break;
                        case "Cercado":
                        case "Muro":
                            painel.getAmbienteAtual().setBaseFortificacao(painel.getAmbienteAtual().getBaseFortificacao() + 1);
                            break;
                    }
                    painel.getInventSystem().removerItem(getNome(), 1);
                }
                if (painel.getPlaySubState() == 1 || painel.getPlaySubState() != 1) {

                    if (getOpcaoCrafting().equals("Curar veneno")) {
                        if (painel.getJogador().estaEnvenenado()) {
                            painel.getJogador().setEnvenenado(false);
                            painel.getInventSystem().removerItem(getNome(), 1);
                        }
                    }
                    else if (getOpcaoCrafting().equals("Regenerar")) {
                        if (painel.getJogador().getVida() < painel.getJogador().getVidaMax()) {
                            int novaVida = (painel.getJogador().getVida() + 5);
                            if (novaVida <= painel.getJogador().getVidaMax()) {
                                painel.getJogador().setVida(novaVida);
                            } else {
                                painel.getJogador().setVida(painel.getJogador().getVidaMax());
                            }
                            painel.getInventSystem().removerItem(getNome(), 1);
                        }
                    }
                    else if (getOpcaoCrafting().equals("Se energizar")) {
                        if (painel.getJogador().getEnergia() < painel.getJogador().getEnergiaMax()) {
                            int novaEnergia = (painel.getJogador().getEnergia() + 5);
                            if (novaEnergia <= painel.getJogador().getEnergiaMax()) {
                                painel.getJogador().setEnergia(novaEnergia);
                            } else {
                                painel.getJogador().setEnergia(painel.getJogador().getEnergiaMax());
                            }
                            painel.getInventSystem().removerItem(getNome(), 1);
                        }
                    }
                }
            }
        }
    }

    // Itens
    public void definirRecurso(String nome) {
        setTipo("recurso");
        setNome(nome);

        switch (nome) {
            case "Madeira":
                if (painel.getInventSystem().acharItem("Pedra")) {
                    setItemParaRemoverNoCrafting("Pedra");
                    setOpcaoCrafting("Estilingue");
                    setTipoNovoItem("combate");
                } else if (painel.getPlaySubState() == 1) {
                    setOpcaoCrafting("Cercado");
                    setTipoNovoItem(null);
                } else {
                    setOpcaoCrafting("...");
                    setTipoNovoItem(null);
                }
                break;

            case "Pedra":
                if (painel.getInventSystem().acharItem("Madeira")) {
                    setItemParaRemoverNoCrafting("Madeira");
                    setOpcaoCrafting("Estilingue");
                    setTipoNovoItem("combate");
                } else if (painel.getPlaySubState() == 1) {
                    setOpcaoCrafting("Muro");
                    setTipoNovoItem(null);
                } else {
                    setOpcaoCrafting("...");
                    setTipoNovoItem(null);
                }
                break;

            case "Planta medicinal":
                if (painel.getJogador().estaEnvenenado()) {
                    setOpcaoCrafting("Curar veneno");
                }
                else {
                    setOpcaoCrafting("...");
                }
                setTipoNovoItem(null);
                break;

            case "Rocha regenerativa":
                if (painel.getJogador().getVida() < painel.getJogador().getVidaMax()) {
                    setOpcaoCrafting("Regenerar");
                }
                else {
                    setOpcaoCrafting("...");
                }
                setTipoNovoItem(null);
                break;

            case "Rocha intensa":
                if (painel.getJogador().getEnergia() < painel.getJogador().getEnergiaMax()) {
                    setOpcaoCrafting("Se energizar");
                }
                else {
                    setOpcaoCrafting("...");
                }
                setTipoNovoItem(null);
                break;

            case "Lâmina metálica":
                if (painel.getInventSystem().acharItem("Madeira")) {
                    setItemParaRemoverNoCrafting("Madeira");

                    if (getQuantidade() == 1) {
                        setOpcaoCrafting("Lança");
                        setTipoNovoItem("combate");
                    }
                    if (getQuantidade() >= 2) {
                        setOpcaoCrafting("Foice");
                        setTipoNovoItem("combate");
                    }
                }
                else {
                    setOpcaoCrafting("...");
                    setTipoNovoItem(null);
                }
                break;

            case "Punhado de sementes":
                if (painel.getPlaySubState() == 1) {
                    setOpcaoCrafting("Plantio");
                }
                else {
                    setOpcaoCrafting("...");
                }
                setTipoNovoItem(null);
                break;

            case "Carvão":
                if (painel.getPlaySubState() == 1) {
                    setOpcaoCrafting("Fogueira");
                }
                else {
                    setOpcaoCrafting("...");
                }
                setTipoNovoItem(null);
                break;

            case "Carvão estranho":
                if (painel.getInventSystem().acharItem("Espada Basilar")) {
                    setItemParaRemoverNoCrafting("Espada Basilar");
                    setOpcaoCrafting("Espada Flamejante");
                    setTipoNovoItem("combate");
                }
                else {
                    setOpcaoCrafting("...");
                    setTipoNovoItem(null);
                }
                break;

            case "Corda":
                if (painel.getInventSystem().acharItem("Espeto crustáceo")) {
                    setItemParaRemoverNoCrafting("Espeto crustáceo");
                    setOpcaoCrafting("Corda de escalada");
                    setTipoNovoItem("recurso");
                } else {
                    if (painel.getInventSystem().acharItem("Galho pontiagudo")) {
                        setItemParaRemoverNoCrafting("Galho pontiagudo");
                        setOpcaoCrafting("Vara de pesca");
                        setTipoNovoItem("combate");
                    } else {
                        setOpcaoCrafting("...");
                        setTipoNovoItem(null);
                    }
                }
                break;

            case "Corda de escalada":
                setOpcaoCrafting("...");
                setTipoNovoItem(null);
                break;

            case "Jóia azul":
                setOpcaoCrafting("...");
                setTipoNovoItem(null);
                break;

            case "Jóia vermelha":
                setOpcaoCrafting("...");
                setTipoNovoItem(null);
                break;

            default:
                throw new IllegalArgumentException("Recurso desconhecido: " + nome);
        }
        setDurabilidadeMax(null);
    }

    public String getOpcaoCrafting() { return opcaoCrafting; }
    public void setOpcaoCrafting(String opcaoCrafting) { this.opcaoCrafting = opcaoCrafting; }

    public String getItemParaRemoverNoCrafting() { return itemParaRemoverNoCrafting; }
    public void setItemParaRemoverNoCrafting(String itemParaRemoverNoCrafting) { this.itemParaRemoverNoCrafting = itemParaRemoverNoCrafting; }

    public String getTipoNovoItem() { return tipoNovoItem; }
    public void setTipoNovoItem(String tipoNovoItem) { this.tipoNovoItem = tipoNovoItem; }
}