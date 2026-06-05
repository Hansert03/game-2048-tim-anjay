package pkg2048game;

import javax.sound.sampled.*;
import java.net.URL;
import javax.swing.Timer;

public class Sound {

    private static Clip bgMusic;

    // Musik background loop
    public static void playBGMusic() {
        try {
            if (bgMusic != null && bgMusic.isRunning()) return;
            URL url = Sound.class.getResource("/pkg2048game/BGMusic.wav");
            if (url == null) { System.out.println("BGMusic.wav tidak ditemukan"); return; }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            bgMusic = AudioSystem.getClip();
            bgMusic.open(ais);
            // Set volume awal normal
            setVolume(bgMusic, 0.5f);
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
            bgMusic.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Fade out musik pelan-pelan lalu stop
    public static void fadeBGMusic(int durationMs) {
        if (bgMusic == null || !bgMusic.isRunning()) return;
        
        int steps = 20;
        int delay = durationMs / steps;
        
        Timer fadeTimer = new Timer(delay, null);
        final float[] volume = {1.0f};
        
        fadeTimer.addActionListener(e -> {
            volume[0] -= 1.0f / steps;
            if (volume[0] <= 0f) {
                volume[0] = 0f;
                setVolume(bgMusic, 0f);
                bgMusic.stop();
                bgMusic.close();
                fadeTimer.stop();
            } else {
                setVolume(bgMusic, volume[0]);
            }
        });
        fadeTimer.start();
    }

    // Stop langsung
    public static void stopBGMusic() {
        if (bgMusic != null && bgMusic.isRunning()) {
            bgMusic.stop();
            bgMusic.close();
        }
    }
    public static void playSwap() {
        try {
            URL url = Sound.class.getResource("/pkg2048game/Swap.wav");
            if (url == null) { System.out.println("Swap.wav tidak ditemukan"); return; }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, 0.25f); // volume penuh
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }


    // Suara klik tombol (volume normal)
    public static void playClick() {
        try {
            URL url = Sound.class.getResource("/pkg2048game/Click.wav");
            if (url == null) { System.out.println("clickSound.wav tidak ditemukan"); return; }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, 1.0f); // volume penuh
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Suara game over (volume besar)
    public static void playGameOver() {
        try {
            URL url = Sound.class.getResource("/pkg2048game/Gameover.wav");
            if (url == null) { System.out.println("Gameover.wav tidak ditemukan"); return; }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, 2.0f); // ← lebih keras dari normal
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public static void playVictory() {
        try {
            URL url = Sound.class.getResource("/pkg2048game/Victory.wav");
            if (url == null) { System.out.println("Victory.wav tidak ditemukan"); return; }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, 2.0f); // ← lebih keras dari normal
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public static void playRevive() {
        try {
            URL url = Sound.class.getResource("/pkg2048game/Revive.wav");
            if (url == null) { System.out.println("Revive.wav tidak ditemukan"); return; }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, 2.0f); // ← lebih keras dari normal
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Helper set volume (0.0 = mute, 1.0 = normal, 2.0 = keras)
    private static void setVolume(Clip clip, float level) {
        try {
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert level ke decibel
            float dB = (float) (Math.log10(Math.max(level, 0.0001f)) * 20);
            // Clamp ke range yang diizinkan
            dB = Math.max(fc.getMinimum(), Math.min(fc.getMaximum(), dB));
            fc.setValue(dB);
        } catch (Exception e) { e.printStackTrace(); }
    }
}