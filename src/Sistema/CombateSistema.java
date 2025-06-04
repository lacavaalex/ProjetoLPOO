package Sistema;

import Entidade.*;
import Evento.EventoCriatura;
import Main.Painel;
import UI.CombateUI;

public class CombateSistema {

    private Painel painel;
    private Jogador jogador;
    private Criatura criaturaEmCombate;
    private EventoCriatura eventoCriatura;
    private CombateUI combateUi;

    private boolean turnoJogador = true;
    private boolean fimDeCombate = false;

    private boolean esquivou = false;
    private boolean bloqueou = false;
    private boolean defesaFalha = false;
    private boolean escapou = false;
    private boolean fugaFalha = false;
    private boolean resistiu = false;
    private boolean critico = false;

    private int ataqueOriginalCriatura;
    private int ataqueFracoCriatura;
    private int ataqueBaseJogador;

    private int numComandoCombate;

    public CombateSistema(Painel painel, Jogador jogador, EventoCriatura eventoCriatura, CombateUI combateUi) {
        this.painel = painel;
        this.jogador = jogador;
        this.eventoCriatura = eventoCriatura;
        this.combateUi = combateUi;
    }

    // Configurações da tela de combate
    public void iniciarCombate(Criatura criatura) {
        this.criaturaEmCombate = criatura;
        ataqueOriginalCriatura = criaturaEmCombate.getAtaqueCriatura();

        if (ataqueBaseJogador == 0) {
            ataqueBaseJogador = jogador.getAtaqueAtual();
        }
        int ataqueFracoJogador = (int) (ataqueBaseJogador * 0.66f);

        if (painel.getEventoClimatico().getClima().equals("tempestade")
                && !jogador.getHabilidade().equals("SOBREVIVENCIAL")) {
            criaturaEmCombate.setAtaqueCriatura((int) (ataqueOriginalCriatura * 1.33f));
        } else {
            criaturaEmCombate.setAtaqueCriatura(ataqueOriginalCriatura);
        }
        if (painel.getEventoClimatico().getClima().equals("salgado")
                && !jogador.getHabilidade().equals("SOBREVIVENCIAL")) {
            jogador.setAtaqueAtual(Math.max(1, ataqueFracoJogador)); // Garante mínimo 1
        } else {
            jogador.setAtaqueAtual(ataqueBaseJogador); // Restaura o valor original
        }

        fimDeCombate = false;
        turnoJogador = true;
        numComandoCombate = 0;
    }

    // Finaliza o combate e reseta os atributos
    public void finalizarCombate() {
        painel.setFightState(false);
        eventoCriatura.setEventoCriaturaAtivo(false);

        if (!escapou) {
            eventoCriatura.incrementarContador();
            jogador.setEnergia(jogador.getEnergia() - 1);
        }

        jogador.setAtaqueAtual(ataqueBaseJogador);
        ataqueBaseJogador = 0;

        if (criaturaEmCombate != null) {
            criaturaEmCombate.setAtaqueCriatura(criaturaEmCombate.getAtaqueCriatura());
        }

        combateUi.retornarLayoutBotoes();
        resetarCriaturaEmCombate();
        escapou = false;

        if (jogador.getHabilidade().equals("MEDICINAL")) {
            jogador.setVida(jogador.getVida() + 2);
        }
    }

    // Sistema de turnos
    public void sistemaTurno() {
        if (!fimDeCombate) {
            if (this.criaturaEmCombate != null) {

                ataqueFracoCriatura = ataqueOriginalCriatura / 2;

                // Turno do jogador
                if (turnoJogador) {
                    processarTurnoJogador();
                }
                // Turno do inimigo
                else {
                    processarTurnoInimigo();
                    numComandoCombate = 0;
                }
            }
        }
    }

    public void processarTurnoJogador() {
        critico = false;
        fugaFalha = false;
        defesaFalha = false;
        esquivou = false;
        bloqueou = false;

        // ATACAR
        if (numComandoCombate == 0) {

            // Cálculo de vida inimigo
            criaturaEmCombate.setVidaCriatura(criaturaEmCombate.getVidaCriatura() - jogador.getAtaqueAtual());

            // Cálculo de morte do inimigo/troca de turno
            if (criaturaEmCombate.getVidaCriatura() <= 0) {
                fimDeCombate = true;
                combateUi.iniciarTransicao();
            } else {
                turnoJogador = false;
            }
        }

        // ESQUIVAR
        else if (numComandoCombate == 1) {

            double probabilidade = painel.definirUmaProbabilidade();
            if (jogador.getHabilidade().equals("COMBATIVA")) {
                probabilidade = probabilidade * 0.85;
            }

            if (probabilidade <= 30) {
                esquivou = true;

                criaturaEmCombate.setVidaCriatura(criaturaEmCombate.getVidaCriatura() - jogador.getAtaqueAtual());

                if (criaturaEmCombate.getVidaCriatura() <= 0) {
                    fimDeCombate = true;
                    combateUi.iniciarTransicao();
                } else {
                    turnoJogador = false;
                }
            } else {
                defesaFalha = true;
                turnoJogador = false;
            }
        }

        // BLOQUEAR
        else if (numComandoCombate == 2) {

            double probabilidade = painel.definirUmaProbabilidade();
            if (jogador.getHabilidade().equals("COMBATIVA")) {
                probabilidade = probabilidade * 0.9;
            }

            if (jogador.getArmaAtual().equals("Escudo")) {
                probabilidade = probabilidade * 0.9;
            }

            if (probabilidade <= 50) {
                bloqueou = true;

                criaturaEmCombate.setAtaqueCriatura(ataqueFracoCriatura);

            } else {
                defesaFalha = true;
            }
            turnoJogador = false;
        }

        // FUGIR
        else if (numComandoCombate == 4) {
            int percentual = painel.definirUmaProbabilidade();
            int probabilidade = painel.definirUmaProbabilidade();

            if (probabilidade <= percentual / 2) {
                escapou = true;
            } else {
                fugaFalha = true;
                turnoJogador = false;
            }
        }

        // Definir criticidade do proximo ataque
        int ataqueCriaturaCritico = painel.definirUmaProbabilidade();
        if (ataqueCriaturaCritico <= 20) {
            int novoAtaque = criaturaEmCombate.getAtaqueCriatura() + criaturaEmCombate.getAtaqueCriatura() / 2;
            criaturaEmCombate.setAtaqueCriatura(novoAtaque);
            critico = true;
        }
    }

    public void processarTurnoInimigo() {
        if (!bloqueou && !esquivou) {
            // Cálculo de vida jogador
            jogador.setVida(jogador.getVida() - criaturaEmCombate.getAtaqueCriatura());

            // Cálculo de morte do jogador/troca de turno
            if (jogador.getVida() <= 0) {

                double probabilidade = painel.definirUmaProbabilidade();
                if (probabilidade <= 20 && jogador.getHabilidade().equals("COMBATIVA")
                        || probabilidade <= 15 && jogador.getHabilidade().equals("SOBREVIVENCIAL")) {
                    jogador.setVida(jogador.getVidaMax() / 4);
                    turnoJogador = true;
                    resistiu = true;
                } else {
                    fimDeCombate = true;
                    finalizarCombate();
                    painel.setGameState(painel.getGameOverState());
                }
            } else {
                turnoJogador = true;
                criaturaEmCombate.setAtaqueCriatura(ataqueOriginalCriatura);
            }
        } else {
            turnoJogador = true;
        }
    }

    // Metodos de comando
    public void subtrairNumComandoCombate(int numOpcoes) {
        numComandoCombate--;
        if (numComandoCombate < 0) {
            numComandoCombate = numOpcoes - 1;
        }
    }

    public void adicionarNumComandoCombate(int numOpcoes) {
        numComandoCombate++;
        if (numComandoCombate > numOpcoes - 1) {
            numComandoCombate = 0;
        }
    }

    // Getters e setters
    public int getNumComando() { return numComandoCombate; }
    public void setNumComando(int numComandoCombate) { this.numComandoCombate = numComandoCombate; }

    public boolean isTurnoJogador() { return turnoJogador; }

    public boolean isCombateFinalizado() { return fimDeCombate; }

    public Criatura getCriaturaEmCombate() { return criaturaEmCombate; }
    public void resetarCriaturaEmCombate() { this.criaturaEmCombate = null; }

    public boolean isDefesaFalha() { return defesaFalha; }
    public boolean isFugaFalha() { return fugaFalha; }
    public boolean conseguiuEscapar() { return escapou; }
    public boolean isAtaqueEsquivado() { return esquivou; }
    public boolean isAtaqueBloqueado() { return bloqueou; }
    public boolean isAtaqueCritico() { return critico; }
    public boolean isAtaqueResistido() { return resistiu; }
    public void setResistiu(boolean resistiu) { this.resistiu = resistiu; }
}