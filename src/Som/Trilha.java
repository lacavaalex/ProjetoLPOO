package Som;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Trilha {

    Clip clip;
    URL trilhaURL[] = new URL[30];

    public Trilha() {
        trilhaURL[0] = getClass().getResource("/Audio/O-Mundo-Funesto.wav");

        if (trilhaURL[0] == null) {
            System.err.println("Erro: Arquivo de áudio não encontrado.");
        }
    }


    public void setFile(int i) {
        try {
            if (trilhaURL[i] == null) {
                throw new IllegalArgumentException("Índice de áudio inválido: " + i);
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(trilhaURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.err.println("Erro ao carregar áudio [" + i + "]:");
            e.printStackTrace();
        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() { clip.loop(Clip.LOOP_CONTINUOUSLY); }

    public void stop() { clip.stop(); }
}
