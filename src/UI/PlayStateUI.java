package UI;

import Main.Painel;
import Ambiente.*;
import Entidade.Jogador;
import java.awt.*;

public class PlayStateUI extends UI {

    private boolean recursosColetados = false;
    private boolean recursosGastos = false;

    public PlayStateUI(Painel painel, Jogador jogador) {
        super(painel, jogador);
    }

    public void playState() {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();
        this.numComando = painel.getUi().getNumComando();

        g2.setColor(Color.black);
        g2.fillRect(0, 0, larguraTela, alturaTela);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize;

        switch (subState) {
            // PONTO INICIAL
            case 0:
                escreverTexto("A luz misteriosa brilha à distância, floresta adentro.", y += tileSize);
                escreverTexto("Espalhadas pelo chão, há coisas que parecem ser úteis.", y += tileSize);
                escreverTexto("Uma brecha por entre as árvores revela uma montanha próxima.", y += tileSize);
                escreverTexto("Altitude pode ser uma vantagem.", y += tileSize);
                escreverTexto("Um chiado estranho parece se aproximar...", y += tileSize);
                escreverTexto("O que fazer?", y += tileSize);


                String[] opcoes0 = {"Seguir luz", "Ficar e coletar recursos", "Ir até montanha"};

                y += tileSize * 2;
                for (int i = 0; i < opcoes0.length; i++) {
                    String texto = opcoes0[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;

            // BRANCH DA LUZ
            case 1:
                escreverTexto("Você deixa a luz te guiar...", y += tileSize);
                escreverTexto(". . .", y += tileSize);
                escreverTexto("Encontrou no caminho: 1 pedra.", y += tileSize);
                painel.getInvent().adicionarItem("Pedra", 1);
                break;

            case 10:
                escreverTexto("Arbustos chacoalham ao seu redor enquanto anda.", y += tileSize);
                escreverTexto("...", y += tileSize);
                escreverTexto("Você está perto o suficiente da luz... é uma fogueira", y += tileSize);
                escreverTexto("Nínguem a vista, mas há uma tigela com água...", y += tileSize);
                escreverTexto("O que fazer?", y += tileSize);
                escreverTexto("", y += tileSize);


                String[] opcoes10 = {"Beber água", "Explorar arredores"};

                y += tileSize * 2;
                for (int i = 0; i < opcoes10.length; i++) {
                    String texto = opcoes10[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;

            case 11:
                escreverTexto("Você bebe água.", y += tileSize);
                jogador.setSede(false);
                escreverTexto("Hidratação no máximo.", y += tileSize);
                break;

            case 12:
                escreverTexto("Melhor não mexer com o que não é seu.",y += tileSize);
                escreverTexto("(e, afinal, quem sabe de quem pode ser...)",tileSize * 3);
                escreverTexto("",tileSize * 4);
                escreverTexto("Em torno dessa fogueira há vegetação baixa.",tileSize * 5);
                escreverTexto("Há um cervo à distância, mas você não tem equipamento para caça.",tileSize * 6);
                escreverTexto("",tileSize * 7);
                escreverTexto("Mais adiante, um barulho animador: água corrente!",tileSize * 8);
                escreverTexto("Apressando o passo, em minutos você chega ao lago.",tileSize * 9);
                break;
            case 1212:
                escreverTexto("Este é o lago.",tileSize * 2);
                escreverTexto("Você pode ficar e descansar, ou retornar à fogueira.",tileSize * 3);
                break;

            case 1213:
                escreverTexto("Você retorna a atenção à fogueira.", y += tileSize);

                String[] opcoes1213 = {"Beber a água", "Ir ao lago"};

                y += tileSize;
                for (int i = 0; i < opcoes1213.length; i++) {
                    String texto = opcoes1213[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;


            // BRANCH DA VÍBORA
            case 2:
                escreverTexto("Você busca por recursos.", y += tileSize);
                escreverTexto("Encontrou: 7 madeiras, 2 pedras, 1 galho pontiagudo.", y += tileSize);

                if (!recursosColetados) {
                    painel.getInvent().adicionarItem("Madeira", 7);
                    painel.getInvent().adicionarItem("Pedra", 2);
                    painel.getInvent().adicionarItem("Galho pontiagudo", 1);
                    recursosColetados = true;
                }
                break;

            case 20:
                escreverTexto("Aquele chiado... parece estar tão perto...", tileSize * 2);
                Evento eventoVibora = new EventoCriatura(painel, this, jogador, criatura, 1);
                eventoVibora.executar(g2);
                // NÃO DEFINI PROBABILIDADE POIS SEMPRE QUERO QUE ESTE EVENTO ACONTECA NESSE CASO
                break;

            case 21:
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F)); escreverTexto("COMBATE", y += tileSize);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F)); escreverTexto("Você a atacou com o GALHO PONTIAGUDO!", y += tileSize);
                g2.setColor(Color.red); escreverTexto("-1HP", y += tileSize);
                g2.setColor(Color.red); escreverTexto("Víbora-Rubro: 2HP", y += tileSize);
                g2.setColor(Color.white); escreverTexto("-Após notar sua investida, ela foge! HA!", y += tileSize);
                escreverTexto("", y += tileSize);
                escreverTexto("FIM DE COMBATE.", y += tileSize);
                escreverTexto("", y += tileSize);
                escreverTexto("DESFECHO: Você está levemente ENVENENADO.", y += tileSize);
                escreverTexto("e seu galho se quebrou na batalha.", y += tileSize);

                if (!recursosGastos) {
                    painel.getInvent().removerItem("Galho pontiagudo", 1);
                    recursosGastos = true;
                }

                break;

            case 22:
                escreverTexto("Você pensa em fugir, mas se sente meio tonto...",tileSize * 2);
                escreverTexto("Porcaria, a mordida da víbora o deixou", y += tileSize * 2);
                g2.setColor(Color.red); escreverTexto("envenenado.", y += tileSize);
                g2.setColor(Color.white); escreverTexto("", y += tileSize);
                escreverTexto("Ela é rápida, e você está abatido(a). É melhor não arriscar.", y += tileSize);

                botoes.mostrarBotaoVoltar();
                break;


            // BRANCH DA MONTANHA
            case 3:
                escreverTexto("Você se direciona até a montanha", y += tileSize);
                escreverTexto(". . .", y += tileSize);
                escreverTexto("Algo não parce certo.", y += tileSize);
                escreverTexto("Um movimento suspeito te acompanha.", y += tileSize);
                escreverTexto("Nada se revela, mas você não está sozinho.", y += tileSize);
                escreverTexto(". . .", y += tileSize);
                break;

            case 30:
                escreverTexto("Atingido o pé da montanha, você enxerga um trecho de subida.", y += tileSize);
                escreverTexto("O que quer que estava te seguindo aparenta ter parado...", y += tileSize);
                escreverTexto("Não há nada de destaque neste local, além de uma rocha firme.", y += tileSize);
                escreverTexto("Sua formação pode conceder um abrigo com boa visibilidade.", y += tileSize);
                escreverTexto("O que fazer?", y += tileSize);
                escreverTexto("", y += tileSize);


                String[] opcoes30 = {"Subir pelo trecho", "Descansar até o amanhecer"};

                y += tileSize * 2;
                for (int i = 0; i < opcoes30.length; i++) {
                    String texto = opcoes30[i];
                    int x = coordenadaXParaTextoCentralizado(texto);

                    if (numComando == i) {
                        g2.drawString(">", x - tileSize, y);
                    }
                    g2.drawString(texto, x, y);
                    y += tileSize;
                } break;

            case 31:
                escreverTexto("Você sobe o trecho semi-íngreme.", y += tileSize);
                escreverTexto("Olhando pra trás, há um olhar a espreita.", y += tileSize);
                escreverTexto("(Ainda bem que não fiquei).", y += tileSize);
                escreverTexto("", y += tileSize);
                escreverTexto("Você chega num paredão.", y += tileSize);
                break;
            case 3131:
                escreverTexto("O trecho acabou. O único caminho para além daqui...",tileSize * 2);
                escreverTexto("é para cima. A montanha sussura seu nome...",tileSize * 3);
                escreverTexto("",tileSize * 4);
                escreverTexto("Seria fatal escalar o paredão sem equipamentos e preparação.",tileSize * 5);
                escreverTexto("E ficar congelando aqui não é uma opção.",tileSize * 6);
                escreverTexto("Isso é um beco sem saída. Melhor retornar.",tileSize * 7);
                break;

            case 32:
                escreverTexto("Você se dirige àquela rocha para um abrigo temporário.", y += tileSize);
                escreverTexto("Vai ter que servir. Você já se afastou demais da clareira.", y += tileSize);
                escreverTexto("A energia aqui não é ideal, mas não parece ter mais nada à espreita", y += tileSize);
                escreverTexto("De olhos entreabertos, você deita para descansar...", y += tileSize);
                break;

            case 33:
                // Deixei de lado a questão de probabilidade por enquanto, mas será implementada aqui
                Evento eventoUrso = new EventoCriatura(painel, this, jogador, criatura, 2);
                eventoUrso.executar(g2);
                break;
            case 34:
                escreverTexto("Você tenta--", y += tileSize);
                g2.setColor(Color.red); escreverTexto("O urso irrompe um golpe fatal. -10HP", y += tileSize);
                break;



            default: System.out.println("default"); System.out.println(painel.getPlaySubState()); break;
        }
    }
}