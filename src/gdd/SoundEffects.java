package gdd;

import static gdd.Global.SFX_ENEMY_HIT;
import static gdd.Global.SFX_PLAYER_EXPLOSION;
import static gdd.Global.SFX_SHOT;
import static gdd.Global.SFX_WALL_BREAK;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/** Loads and replays the short, non-looping gameplay effects. */
public class SoundEffects {

    private final Map<String, Clip> clips = new HashMap<>();

    public SoundEffects() {
        load(SFX_SHOT);
        load(SFX_ENEMY_HIT);
        load(SFX_WALL_BREAK);
        load(SFX_PLAYER_EXPLOSION);
    }

    private void load(String path) {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(
                new File(path).getAbsoluteFile())) {
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clips.put(path, clip);
        } catch (Exception e) {
            System.err.println("Unable to load sound effect " + path + ": " + e.getMessage());
        }
    }

    private void play(String path) {
        Clip clip = clips.get(path);
        if (clip == null) {
            return;
        }

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void playShot() {
        play(SFX_SHOT);
    }

    public void playEnemyHit() {
        play(SFX_ENEMY_HIT);
    }

    public void playWallBreak() {
        play(SFX_WALL_BREAK);
    }

    public void playPlayerExplosion() {
        play(SFX_PLAYER_EXPLOSION);
    }

    public void close() {
        for (Clip clip : clips.values()) {
            clip.stop();
            clip.close();
        }
        clips.clear();
    }
}
