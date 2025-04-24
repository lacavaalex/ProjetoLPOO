package Ambiente;

import Entidade.*;
import Controles.*;
import Main.Painel;
import UI.UI;
import Evento.*;

import java.awt.*;

public class AmbienteFloresta extends Ambiente {

    Painel painel;
    Jogador jogador;
    UI ui;
    Botões botoes;
    Criatura criatura;

    public AmbienteFloresta(Painel painel, Jogador jogador, UI ui) {
        super();
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();
        this.criatura = new Criatura();

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

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

        g2.setColor(Color.black);
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize;

        switch (subState) {
            // PONTO INICIAL
            case 0:
                ui.escreverTexto("A luz misteriosa brilha à distância, floresta adentro.", y += tileSize);
                ui.escreverTexto("Espalhadas pelo chão, há coisas que parecem ser úteis.", y += tileSize);
                ui.escreverTexto("Uma brecha por entre as árvores revela uma montanha próxima.", y += tileSize);
                ui.escreverTexto("Altitude pode ser uma vantagem.", y += tileSize);
                ui.escreverTexto("Um chiado estranho parece se aproximar...", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);

                ui.desenharOpcoes(new String[]{"Seguir luz", "Ficar e coletar recursos", "Ir até montanha"}, y += tileSize * 2);
                break;

            // BRANCH DA LUZ
            case 100:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoContinuar();

                ui.escreverTexto("Você deixa a luz te guiar...", y += tileSize);
                ui.escreverTexto(". . .", y += tileSize);
                ui.escreverTexto("Encontrou no caminho: 1 pedra.", y += tileSize);
                painel.getInvent().adicionarItem("Pedra", 1);
                break;

            case 101:
                ui.escreverTexto("Arbustos chacoalham ao seu redor enquanto anda.", y += tileSize);
                ui.escreverTexto("...", y += tileSize);
                ui.escreverTexto("Você está perto o suficiente da luz... é uma fogueira", y += tileSize);
                ui.escreverTexto("Nínguem a vista, mas há um cantil com água...", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);
                ui.escreverTexto("", y += tileSize);

                ui.desenharOpcoes(new String[]{"Pegar água", "Explorar arredores"}, y += tileSize * 2);
                break;

            case 102:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoVoltar();

                painel.getInvent().adicionarItem("Cantil", 1);
                ui.escreverTexto("Você pega o cantil e toma um gole d'água.", y += tileSize);
                jogador.setSede(false);
                ui.escreverTexto("Hidratação no máximo.", y += tileSize);
                break;

            case 103:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoContinuar();

                ui.escreverTexto("Melhor não mexer com o que não é seu.", y += tileSize);
                ui.escreverTexto("(e, afinal, quem sabe de quem pode ser...)", tileSize * 3);
                ui.escreverTexto("", tileSize * 4);
                ui.escreverTexto("Em torno dessa fogueira há vegetação baixa.", tileSize * 5);
                ui.escreverTexto("Há um cervo à distância, mas você não tem equipamento para caça.", tileSize * 6);
                ui.escreverTexto("", tileSize * 7);
                ui.escreverTexto("Mais adiante, um barulho animador: água corrente!", tileSize * 8);
                ui.escreverTexto("Apressando o passo, em minutos você chega ao lago.", tileSize * 9);
                break;

            case 104:
                ui.escreverTexto("Você retorna a atenção à fogueira.", y += tileSize);

                ui.desenharOpcoes(new String[]{"Ir ao lago"}, y += tileSize);
                break;

            // BRANCH DA VÍBORA
            case 200:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoContinuar();

                ui.escreverTexto("Você busca por recursos.", y += tileSize);
                ui.escreverTexto("Encontrou: 7 madeiras, 2 pedras, 1 galho pontiagudo.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Madeira", 7);
                    painel.getInvent().adicionarItem("Pedra", 2);
                    painel.getInvent().adicionarItem("Galho pontiagudo", 1);
                    setRecursosColetados(true);
                }
                break;

            case 201:
                botoes.mostrarBotaoContinuar();
                botoes.esconderBotaoMochila();

                ui.escreverTexto("Aquele chiado... parece estar tão perto...", tileSize * 4);
                break;

            case 202:
                Evento eventoVibora = new EventoCriatura(painel, ui, jogador, criatura, 1);
                eventoVibora.executar(g2);
                // NÃO DEFINI PROBABILIDADE POIS SEMPRE QUERO QUE ESTE EVENTO ACONTECA NESSE CASO
                break;

            case 203:
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
                ui.escreverTexto("COMBATE", y += tileSize);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
                ui.escreverTexto("Você a atacou com o GALHO PONTIAGUDO!", y += tileSize);
                g2.setColor(Color.red);
                ui.escreverTexto("-1HP", y += tileSize);
                g2.setColor(Color.red);
                ui.escreverTexto("Víbora-Rubro: 2HP", y += tileSize);
                g2.setColor(Color.white);
                ui.escreverTexto("-Após notar sua investida, ela foge! HA!", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("FIM DE COMBATE.", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("DESFECHO: Você está levemente ENVENENADO.", y += tileSize);
                ui.escreverTexto("e seu galho se quebrou na batalha.", y += tileSize);

                if (!isRecursosGastos()) {
                    painel.getInvent().removerItem("Galho pontiagudo", 1);
                    setRecursosGastos(true);
                }

                break;

            case 204:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoVoltar();

                ui.escreverTexto("Você pensa em fugir, mas se sente meio tonto...", tileSize * 2);
                ui.escreverTexto("Porcaria, a mordida da víbora o deixou", y += tileSize * 2);
                g2.setColor(Color.red);
                ui.escreverTexto("envenenado.", y += tileSize);
                g2.setColor(Color.white);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("Ela é rápida, e você está abatido(a). É melhor não arriscar.", y += tileSize);
                break;

            // BRANCH DA MONTANHA
            case 300:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoContinuar();

                ui.escreverTexto("Você se direciona até a montanha", y += tileSize);
                ui.escreverTexto(". . .", y += tileSize);
                ui.escreverTexto("Algo não parce certo.", y += tileSize);
                ui.escreverTexto("Um movimento suspeito te acompanha.", y += tileSize);
                ui.escreverTexto("Nada se revela, mas você não está sozinho.", y += tileSize);
                ui.escreverTexto(". . .", y += tileSize);
                break;

            case 301:
                ui.escreverTexto("Atingido o pé da montanha, você enxerga um trecho de subida.", y += tileSize);
                ui.escreverTexto("O que quer que estava te seguindo aparenta ter parado...", y += tileSize);
                ui.escreverTexto("Não há nada de destaque neste local, além de uma rocha firme.", y += tileSize);
                ui.escreverTexto("Sua formação pode conceder um abrigo com boa visibilidade.", y += tileSize);
                ui.escreverTexto("O que fazer?", y += tileSize);
                ui.escreverTexto("", y += tileSize);

                ui.desenharOpcoes(new String[]{"Subir pelo trecho", "Descansar até o amanhecer"}, y += tileSize * 2);
                break;

            case 302:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoContinuar();

                ui.escreverTexto("Você sobe o trecho semi-íngreme.", y += tileSize);
                ui.escreverTexto("Olhando pra trás, há um olhar a espreita.", y += tileSize);
                ui.escreverTexto("(Ainda bem que não fiquei).", y += tileSize);
                ui.escreverTexto("", y += tileSize);
                ui.escreverTexto("Você chega num paredão.", y += tileSize);
                break;

            case 303:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoContinuar();

                ui.escreverTexto("Você se dirige àquela rocha para um abrigo temporário.", y += tileSize);
                ui.escreverTexto("Vai ter que servir. Você já se afastou demais da clareira.", y += tileSize);
                ui.escreverTexto("A energia aqui não é ideal, mas não parece ter mais nada à espreita", y += tileSize);
                ui.escreverTexto("De olhos entreabertos, você deita para descansar...", y += tileSize);
                break;

            case 304:
                // Deixei de lado a questão de probabilidade por enquanto, mas será implementada aqui
                Evento eventoUrso = new EventoCriatura(painel, ui, jogador, criatura, 2);
                eventoUrso.executar(g2);
                break;
            case 305:
                botoes.esconderBotaoMochila();
                botoes.mostrarBotaoContinuar();

                ui.escreverTexto("Você tenta--", y += tileSize);
                g2.setColor(Color.red);
                ui.escreverTexto("O urso irrompe um golpe fatal. -10HP", y += tileSize);
                break;

            /*case 999:
                Evento eventoChuva = new EventoClimatico(painel, this, jogador, botoes, 1);
                eventoChuva.executar(g2);
                break;*/

            default:
                System.out.println("default");
                System.out.println(painel.getPlaySubState());
                break;
        }
    }
}
