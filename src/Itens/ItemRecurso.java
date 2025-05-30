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

                if (getTipoNovoItem().equals("combate") && painel.getInvent().acharItem(getOpcaoCrafting())) {
                    System.out.println("Arma já existe no inventário.");
                }
                else {
                    painel.getInvent().removerItem(getNome(), 1);

                    painel.getInvent().removerItem(getItemParaRemoverNoCrafting(), 1);
                    if (getItemParaRemoverNoCrafting().equals(painel.getJogador().getArmaAtual())) {
                        painel.getJogador().setArmaAtual("Nenhuma arma definida.");
                    }

                    painel.getInvent().adicionarItem(getOpcaoCrafting(), getTipoNovoItem(), 1);

                    if (getTipoNovoItem().equals("combate") && !painel.getInvent().acharItem(getOpcaoCrafting())) {
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
                            painel.getAmbienteAtual().setBaseFortificacao(painel.getAmbienteAtual().getBaseFortificacao() + 1);
                            break;
                    }
                }
                painel.getInvent().removerItem(getNome(), 1);
            }
        }
    }

    // Itens
    public void definirRecurso(String nome) {
        setTipo("recurso");
        setNome(nome);

        switch (nome) {
            case "Madeira":
                if (painel.getInvent().acharItem("Pedra")) {
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
                if (painel.getInvent().acharItem("Madeira")) {
                    setItemParaRemoverNoCrafting("Madeira");
                    setOpcaoCrafting("Estilingue");
                    setTipoNovoItem("combate");
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

            case "Lâmina metálica":
                if (painel.getInvent().acharItem("Madeira")) {
                    setItemParaRemoverNoCrafting("Madeira");

                    if (getQuantidade() == 1) {
                        setOpcaoCrafting("Lança");
                        setTipoNovoItem("combate");
                    }
                    else if (getQuantidade() >= 2) {
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
                if (painel.getInvent().acharItem("Espada Basilar")) {
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
                if (painel.getInvent().acharItem("Galho pontiagudo")) {
                    setItemParaRemoverNoCrafting("Galho pontiagudo");
                    setOpcaoCrafting("Vara de pesca");
                    setTipoNovoItem("combate");
                }
                else if (painel.getInvent().acharItem("Espeto crustáceo")) {
                    setItemParaRemoverNoCrafting("Espeto crustáceo");
                    setOpcaoCrafting("Corda de escalada");
                    setTipoNovoItem("recurso");
                }
                else {
                    setOpcaoCrafting("...");
                    setTipoNovoItem(null);
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