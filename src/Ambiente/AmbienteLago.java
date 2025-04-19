package Ambiente;

public class AmbienteLago extends Ambiente {

    public AmbienteLago() {
        super();
        descreverAmbiente();
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("LAGO SERENO");
        this.setDescricao("Limpo, vasto, estranhamente silencioso.");
        this.setDificuldade("tranquilas.");
        this.setRecursos("água.");
        this.setFrequenciaEventos("quieto, um certo ar de misticismo.");
        this.setClima("levemente frio");
    }
}