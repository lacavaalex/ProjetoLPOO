package Ambiente;

public class AmbienteMontanha extends Ambiente {

    public AmbienteMontanha() {
        super();
        descreverAmbiente();
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("MONTANHA EPOPEICA");
        this.setDescricao("Imponente, desafiadora, majestosa.");
        this.setDificuldade("perigosas.");
        this.setRecursos("indefinido.");
        this.setFrequenciaEventos("desastres naturais, perigos escondidos.");
        this.setClima("altamente frio, piora com altitude.");
    }
}
