package Ambiente;

import Controles.Botoes;
import Entidade.Criatura;
import Entidade.Jogador;
import Evento.EventoClimatico;
import Evento.EventoCriatura;
import Main.Painel;
import UI.UI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AmbienteMontanha extends Ambiente {

    private Painel painel;
    private Jogador jogador;
    private UI ui;
    private Botoes botoes;
    private Criatura criatura;

    private EventoCriatura eventoLeopardo;
    private EventoCriatura eventoFunesto;

    private EventoClimatico eventoNevasca;

    private BufferedImage fundoMontanha2;
    private BufferedImage figura;
    private BufferedImage figura2;
    private BufferedImage figura3;

    private final int leopardo = 4001;
    private final int funesto = 4002;

    public AmbienteMontanha(Painel painel, Jogador jogador, UI ui) {
        super(painel, jogador);
        this.painel = painel;
        this.jogador = jogador;
        this.ui = ui;
        this.botoes = painel.getBotoes();
        this.criatura = new Criatura();

        this.eventoLeopardo = new EventoCriatura(painel, ui, jogador, criatura);
        this.eventoFunesto = new EventoCriatura(painel, ui, jogador, criatura);

        this.eventoNevasca = painel.getEventoClimatico();

        descreverAmbiente();
        fundoMontanha2 = ui.setupImagens("montanha_epopeica-2", "background");
        figura = ui.setupImagens("figura_misteriosa", "zoom");
        figura2 = ui.setupImagens("figura_misteriosa2", "zoom");
        figura3 = ui.setupImagens("figura_misteriosa3", "zoom");
    }

    @Override
    public void descreverAmbiente() {
        this.setNome("MONTANHA EPOPEICA");
        this.setDescricao("Imponente, desafiadora, majestosa.");
        this.setDificuldade("perigosas.");
        this.setRecursos("indefinido.");
        this.setFrequenciaEventos("desastres naturais, perigos escondidos.");
        this.setClimaAmbiente("altamente frio, piora com altitude.");
        this.setNomeFundoCard("montanha_epopeica");
        this.setNomeFundoCombate("montanha_epopeica_combate");
    }

    @Override
    public void playState(Graphics2D g2) {
        int tileSize = painel.getTileSize();
        int larguraTela = painel.getLargura();
        int alturaTela = painel.getAltura();

        int numComando = ui.getNumComando();

        g2.fillRect(0, 0, larguraTela, alturaTela);
        ui.desenharPlanoDeFundo(fundoMontanha2);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
        g2.setColor(Color.white);

        int subState = painel.getPlaySubState();
        int y = tileSize * 2;

        switch (subState) {

            case leopardo:
                definirOcorrenciaDeEventoCriatura(g2, eventoLeopardo, 31);
                break;

            case funesto:
                definirOcorrenciaDeEventoCriatura(g2, eventoFunesto, 32);
                break;

            case 1:
                botoes.esconderBotao("Voltar à base");
                ui.mostrarAcampamento();
                break;

            case 300:
                definirTelaDeBotao("voltar");
                definirOcorrenciaDeEventoClimatico(g2, eventoNevasca, 4);

                ui.escreverTexto("O trecho da floresta até a montanha acabou.", y);
                ui.escreverTexto("O único caminho para além daqui é para cima.", y += tileSize);
                ui.escreverTexto("(A montanha sussurra seu nome...)", y += tileSize);
                ui.escreverTexto("Seria fatal escalar o paredão sem equipamentos e preparação.", y += tileSize);
                ui.escreverTexto("E ficar congelando aqui não é uma opção.", y += tileSize);
                ui.escreverTexto("Isso é um beco sem saída. Melhor retornar.", y += tileSize);
                break;

            case 301:
                definirTelaDeBotao("continuar");
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                g2.setColor(Color.gray);
                ui.escreverTexto(jogador.getNome() + "...", painel.getAltura()/2 - tileSize * 2);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
                g2.setColor(Color.WHITE);
                ui.escreverTexto("Há um movimento estranho na sua mochila.", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("As jóias... brilham forte aqui.", painel.getAltura()/2);
                ui.escreverTexto("O segredo desse lugar só pode estar lá em cima.",painel.getAltura()/2 + tileSize);
                ui.escreverTexto("Se ao menos houvesse uma forma de escalar...",painel.getAltura()/2 + tileSize * 2);
                break;

            case 302:
                if (painel.getInvent().acharItem("Corda de escalada")) {
                    ui.escreverTexto("Pelo jeito você tem uma corda de escalada...", y);
                    ui.escreverTexto("Seria agora o momento de enfrentar este desafio?", y += tileSize);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 15F));
                    ui.escreverTexto("(Escalar é uma decisão definitiva.)", y += tileSize);
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));

                    ui.desenharOpcoes(new String[] {"Escalar", "Agora não"}, y += tileSize * 2, numComando);
                }
                else {
                    if (!painel.getInvent().acharItem("Corda")) {
                        if (!isRecursosColetados()) {
                            painel.getInvent().adicionarItem("Corda", "recurso", 1);
                            setRecursosColetados(true);
                        }
                    } else if (painel.getInvent().acharItem("Corda")) {
                        ui.escreverTexto("Algo na sua mochila pode permitir que você crie um", y += tileSize);
                        ui.escreverTexto("equipamento de escalada. Uma corda, e algo firme, pontudo.", y += tileSize);
                    }
                }
                break;

            case 303:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("Você atira a corda improvisada para o alto.", y);
                ui.escreverTexto("A escalada se inicia.", y += tileSize);
                ui.escreverTexto("Toda sua jornada é refletida neste momento.", y += tileSize * 2);
                ui.escreverTexto("Será que a hostilidade das criaturas fazia sentido?", y += tileSize);
                ui.escreverTexto("Por quê o urso agia como se te conhecesse?", y += tileSize);
                ui.escreverTexto("O que eram todos aqueles avisos? E essas jóias?", y += tileSize);
                ui.escreverTexto("E quem era aquela figura misteriosa que te empurrou?", y += tileSize);
                ui.escreverTexto("O topo desta montanha deve guardar alguma dessas respostas.", y += tileSize * 2);
                ui.escreverTexto("'Por favor...'", y += tileSize);
                break;

            case 304:
                ui.escreverTexto("Surgindo num ponto firme, você vê um largo declive.", y);
                ui.escreverTexto("A neve brilha até nesta escuridão.", y += tileSize);
                ui.escreverTexto("Uma pequena fenda, no topo, chama atenção.", y += tileSize);
                ui.escreverTexto("Uma luz de fogueira emana de dentro dela...", y += tileSize);
                ui.escreverTexto("Leopardos parecem rondar esse território.", y += tileSize);
                ui.escreverTexto("Há duas rotas: uma mais curta, com menos criaturas", y += tileSize);
                ui.escreverTexto("e uma arriscada, mas com muitos recursos. Por onde seguir?", y += tileSize);

                ui.desenharOpcoes(new String[] {"Prezar pela segurança", "Se arriscar com os leopardos"}, y += tileSize * 2, numComando);
                break;

            case 305:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Você opta por tentar passar despercebido...", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("Evidentemente não deu certo.", painel.getAltura()/2);
                break;

            case 306:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Avançando por um fio, você avista algo:", y);
                ui.escreverTexto("Um arbusto de frutos.", y += tileSize);
                ui.escreverTexto("Gelados, mas devem servir.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Fruta", "consumo", 2);
                    setRecursosColetados(true);
                }
                break;

            case 307:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Seus pés congelam nesta neve.", y);
                ui.escreverTexto("A montanha vai te consumir de um jeito ou de outro.", y += tileSize);
                ui.escreverTexto("Seu ritmo é lento, mas há uma trilha a frente. ", y += tileSize);
                ui.escreverTexto("Mais um pouco... ", y += tileSize);
                break;

            case 308:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Algumas rochas rosadas estão espalhadas aqui... como?", y);
                ui.escreverTexto("Não importa, a alguns metros da trilha, você agarra uma.", y += tileSize);
                ui.escreverTexto("Porcaria, lá vem mais um...", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Rocha regenerativa", "recurso", 1);
                    setRecursosColetados(true);
                }
                break;

            case 309:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("É bom que esses recursos valham a pena.", y);
                ui.escreverTexto("Não importa, a alguns metros da trilha, você agarra umas frutas.", y += tileSize);

                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Fruta", "consumo", 4);
                    painel.getInvent().adicionarItem("Planta medicinal", "recurso", 1);
                    setRecursosColetados(true);
                }
                break;

            case 310:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Minérios da velha gruta estão por toda a parte.", y);
                ui.escreverTexto("A cor é inesquecível.", y += tileSize);
                ui.escreverTexto("Você agarra, e... cuidado!", y += tileSize * 2);
                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Rocha regenerativa", "recurso", 2);
                    painel.getInvent().adicionarItem("Rocha intensa", "recurso", 1);
                    setRecursosColetados(true);
                }
                break;

            case 311:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Não há porque desistir agora.", y);
                ui.escreverTexto("O caminho é duro, mas você está quase lá", y += tileSize);
                ui.escreverTexto("Uma pequena fonte jorra água. Você enche um cantil.", y += tileSize);
                ui.escreverTexto("... talvez não devesse ter parado.", y += tileSize);
                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Cantil", "consumo", 1);
                    setRecursosColetados(true);
                }
                break;

            case 312:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Quase lá... há uma trilha de subida se formando...", y);
                ui.escreverTexto("Alguns itens a frente se mostram promissores, você pega...", y += tileSize);
                ui.escreverTexto("E surge mais um leopardo.", y += tileSize);
                ui.escreverTexto("Que seja. O que é mais um embate?", y += tileSize);
                if (!isRecursosColetados()) {
                    painel.getInvent().adicionarItem("Carvão estranho", "recurso", 1);
                    setRecursosColetados(true);
                }
                break;

            case 313:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Enfim, passou do declive.", y);
                ui.escreverTexto("Seu corpo não está ileso, mas ainda segue firme.", y += tileSize);
                ui.escreverTexto("De alguma forma, você sente que é isso que deve fazer.", y += tileSize);
                ui.escreverTexto("Essa montanha clama por você desde o momento que a viu.", y += tileSize);
                ui.escreverTexto("As jóias que o guiam resplandecem cada vez mais.", y += tileSize);
                ui.escreverTexto("O que quer que te espera no fim desta trilha...", y += tileSize);
                ui.escreverTexto("só pode ser a razão disso tudo.", y += tileSize);
                break;

            case 314:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("A tribulação culmina numa pequena fenda rochosa.", y);
                ui.escreverTexto("O iluminar de fogueira não está mais aqui, nem", y += tileSize);
                ui.escreverTexto("mesmo sinal de que houve acampamento qualquer aqui.", y += tileSize);
                ui.escreverTexto("(Um calafrio sobre por sua espinha.)", y += tileSize * 2);
                ui.escreverTexto("Receosamente, você adentra o local...", y += tileSize * 2);
                ui.escreverTexto("De repente, as jóias voam de sua mochila para o fundo da caverna.", y += tileSize);
                break;

            case 315:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Você corre para buscá-las.", y);
                ui.escreverTexto("Para uma malformação geológica, esse lugar é fundo.", y += tileSize);
                ui.escreverTexto("O breu se expande, e se expande.", y += tileSize);
                ui.escreverTexto("E a luz das jóias se distancia...", y += tileSize);
                ui.escreverTexto("Até que elas param,e caem no chão.", y += tileSize * 2);
                ui.escreverTexto("É como se tivessem alcançado seu destino.", y += tileSize);
                break;

            case 316:
                definirTelaDeBotao("continuar");
                ui.escreverTexto("Da escuridão, você ouve passos...", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("E vê alguém.", painel.getAltura()/2);
                break;

            case 317:
                definirTelaDeBotao("continuar");

                int dS = painel.getDialogueState();

                desenharImagemZoom(g2, figura);

                String dialogo = switch (dS) {
                    case 0 -> "``Você demorou, " + jogador.getNome() + "``";
                    case 1 -> "``Obrigado por trazer as pérolas.``";
                    case 2 -> "``Me perdoe pelo empurrão.``";
                    case 3 -> "``Mas não achei que você entraria na gruta...``";
                    case 4 -> "``...sem minha ajudinha.``";
                    case 5 -> "``Eu mesmo demorei para encontrá-la.``";
                    case 6 -> "``E precisava que você chegasse logo.``";
                    case 7 -> "``Não aguento mais esperar.``";
                    case 8 -> "``Cada segundo aqui é um novo tormento.``";
                    case 9 -> "``E, apenas com as três pérolas dos guardiões...``";
                    case 10 -> "``...o portal pode ser aberto.``";
                    case 11 -> "``E essa é a primeira vez que conseguimos.``";
                    case 12 -> "``Que orgulho, " + jogador.getNome() + "``";
                    case 13 -> "``Você foi o mais capaz de nós.``";
                    case 14 -> "``Agora, passe pra cá a terceira pérola.``";
                    case 15 -> "``...``";
                    case 16 -> "``Huh..? Você ainda não entendeu?``";
                    case 17 -> "``Sua estupidez é emblemática.``";
                    case 18 -> "``A espada do Urso Pai.``";
                    case 19 -> "``A pérola púrpura é parte dela.``";
                    case 20 -> "``Belo trabalho com ele, aliás.``";
                    case 21 -> "``Sempre quis terminar o trabalho.``";
                    case 22 -> "``Desprezo todas as almas asquerosas deste reino.``";
                    case 23 -> "``Cada sangue que derramei, fiz com prazer.``";
                    case 24 -> "``...``";
                    case 25 -> "``Estou impaciente.``";
                    case 26 -> "``Me entregue logo a espada.``";
                    case 27 -> "``É agora que saio desse inferno.``";
                    default -> throw new IllegalStateException("Diálogo desconhecido: " + dS);
                };

                g2.setColor(Color.black);
                ui.escreverTexto(dialogo, painel.getAltura() - tileSize);
                g2.setColor(Color.white);
                break;

            case 318:
                ui.desenharOpcoes(new String[]{"Entregar a espada...", "``Inferno? Esse lugar é um paraíso perto do que te espera.``"}, painel.getAltura()/2, numComando);
                break;

            case 319:
                definirTelaDeBotao("continuar");

                int dS2 = painel.getDialogueState();

                if (dS2 == 0) {
                    desenharImagemZoom(g2, figura);
                }
                else if (dS2 <= 8) {
                    desenharImagemZoom(g2, figura3);
                }

                String dialogo2 = switch (dS2) {
                    case 0 -> "``...``";
                    case 1 -> "``...``";
                    case 2 -> "``Agradecido, " + jogador.getNome() + "``";
                    case 3 -> "``Quanta complacência.``";
                    case 4 -> "``Não era exatamente o que eu esperava de nós.``";
                    case 5 -> "``Foi bom trabalhar com você.``";
                    case 6 -> "``Uma pena que apenas um de nós pode voltar.``";
                    case 7 -> "``...``";
                    case 8 -> "``Tenha um bom descanso.``";
                    case 9 -> "A LÂMINA DA ESPADA É CRAVADA EM SEU PEITO.";
                    default -> throw new IllegalStateException("Diálogo desconhecido: " + dS2);
                };

                g2.setColor(Color.red);
                ui.escreverTexto(dialogo2, painel.getAltura() - tileSize);
                g2.setColor(Color.white);
                break;

            case 320:
                definirTelaDeBotao("continuar");

                int dS3 = painel.getDialogueState();

                if (dS3 <= 3) {
                    desenharImagemZoom(g2, figura);
                }
                else {
                    desenharImagemZoom(g2, figura2);
                }

                String dialogo3 = switch (dS3) {
                    case 0 -> "``...``";
                    case 1 -> "``Quanta coragem...``";
                    case 2 -> "``...``";
                    case 3 -> "``Mas saiba que...``";
                    case 4 -> "``...QUANDO EU DESPEDAÇAR SUA CARNE PODRE...``";
                    case 5 -> "``...NEM OS VERMES OUSARÃO ROÊ-LA.";
                    default -> throw new IllegalStateException("Diálogo desconhecido: " + dS3);
                };

                g2.setColor(Color.black);
                ui.escreverTexto(dialogo3, painel.getAltura() - tileSize);
                g2.setColor(Color.white);
                break;

            case 321:
                definirTelaDeBotao("continuar");

                ui.escreverTexto("...", painel.getAltura()/2 - tileSize);
                ui.escreverTexto("Depois de tudo, só restava o suspiro.", painel.getAltura()/2);
                break;

            case 322:
                botoes.esconderBotao("Abrir mochila");
                Composite composite2 = g2.getComposite();
                if (!isTransicaoFinalizada()) {
                    transicaoDeTela(g2);

                    ui.escreverTexto("Você poderia buscar o tal portal.", y);
                    ui.escreverTexto("Poderia vasculhar essa terra por mais pistas.", y += tileSize);
                    ui.escreverTexto("Tentar colocar sentido em algo que não faz.", y += tileSize);
                    ui.escreverTexto("Mas... do que adianta?.", y += tileSize);
                    ui.escreverTexto("Você já fez suficiente aqui.", y += tileSize);
                    ui.escreverTexto("Mais do que sua existência implica.", y += tileSize);
                    ui.escreverTexto("Não há próximo passo. Não há luz no fim do túnel.", y += tileSize);
                    ui.escreverTexto("Não há saída.", y += tileSize);
                    ui.escreverTexto("É impossível fugir de sua própria escuridão.", y += tileSize);
                    ui.escreverTexto("O que havia de se feito, já foi.", y += tileSize);
                    ui.escreverTexto("Que venha o veredito do destino...", y += tileSize);
                    ui.desenharOpcoes(new String[] {"..."}, y += tileSize, numComando);
                    g2.setComposite(composite2);
                }

                if (isTransicaoFinalizada()) {
                    painel.setPlaySubState(323);
                }
                break;

            case 323:
                painel.setGameState(painel.getVictoryState());
                break;

            default:
                throw new IllegalArgumentException("Substate desconhecido: " + subState);
        }
    }
}