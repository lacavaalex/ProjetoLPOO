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
        switch (nome) {
            case "Madeira":
                setNome(nome);
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
                setNome(nome);
                if (painel.getInvent().acharItem("Madeira")) {
                    setItemParaRemoverNoCrafting("Madeira");
                    setOpcaoCrafting("Estilingue");
                    setTipoNovoItem("combate");
                } else {
                    setOpcaoCrafting("...");
                    setTipoNovoItem(null);
                }
                break;

            case "Punhado de sementes":
                setNome(nome);

                if (painel.getPlaySubState() == 1) {
                    setOpcaoCrafting("Plantio");
                }
                else {
                    setOpcaoCrafting("...");
                }
                setTipoNovoItem(null);
                break;

            case "Corda":
                setNome(nome);
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