package Ambiente;

public class AmbienteFloresta extends Ambiente {

    public AmbienteFloresta() {
        super();
        descreverAmbiente();
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("FLORESTA MACABRA");
        this.setDescricao("Escura, densa, barulhenta.");
        this.setDificuldade("medianas.");
        this.setRecursos("frutas, água, madeira, pedras.");
        this.setFrequenciaEventos("muitas criaturas, esconderijos, riscos à saúde.");
        this.setClima("levemente frio.");
    }

}
