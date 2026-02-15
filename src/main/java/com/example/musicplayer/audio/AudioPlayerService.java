package com.example.musicplayer.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Arrays;

/**
 * Owns MediaPlayer creation and exposes the latest audio spectrum data for visualizers.
 */
public class AudioPlayerService {
    public static final int SPECTRUM_BANDS = 128;
    public static final double SPECTRUM_INTERVAL_SECONDS = 0.1;
    public static final int SPECTRUM_THRESHOLD_DB = -60;

    private MediaPlayer mediaPlayer;

    /**
     * Latest values received from AudioSpectrumListener.
     *
     * JavaFX invokes the spectrum callback at the configured interval (0.1s here).
     * We clone the arrays when writing and reading so rendering code can safely consume
     * the values without seeing in-place mutations.
     */
    private volatile float[] latestMagnitudes = new float[SPECTRUM_BANDS];
    private volatile float[] latestPhases = new float[SPECTRUM_BANDS];

    /**
     * Opens a file chooser that accepts common audio formats.
     */
    public File chooseAudioFile(Window owner) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Audio File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.aac", "*.m4a")
        );
        return chooser.showOpenDialog(owner);
    }

    /**
     * Creates a new MediaPlayer for the selected file and wires AudioSpectrumListener.
     */
    public void loadFile(File audioFile) {
        if (audioFile == null) {
            return;
        }

        disposePlayer();
        resetSpectrum();

        try {
            Media media = new Media(audioFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (MediaException exception) {
            throw new IllegalStateException("Unable to load audio file: " + audioFile.getAbsolutePath(), exception);
        }

        mediaPlayer.setAudioSpectrumNumBands(SPECTRUM_BANDS);
        mediaPlayer.setAudioSpectrumInterval(SPECTRUM_INTERVAL_SECONDS);
        mediaPlayer.setAudioSpectrumThreshold(SPECTRUM_THRESHOLD_DB);

        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            latestMagnitudes = magnitudes.clone();
            latestPhases = phases.clone();
        });
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(Math.max(0.0, Math.min(1.0, volume)));
        }
    }

    /**
     * Returns a safe snapshot used by both 2D and 3D renderers each animation frame.
     */
    public SpectrumFrame getLatestSpectrum() {
        return new SpectrumFrame(latestMagnitudes.clone(), latestPhases.clone());
    }

    public boolean hasTrackLoaded() {
        return mediaPlayer != null;
    }

    public void shutdown() {
        disposePlayer();
    }

    private void resetSpectrum() {
        latestMagnitudes = new float[SPECTRUM_BANDS];
        latestPhases = new float[SPECTRUM_BANDS];
        Arrays.fill(latestMagnitudes, SPECTRUM_THRESHOLD_DB);
    }

    private void disposePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    public record SpectrumFrame(float[] magnitudes, float[] phases) {
    }
}
