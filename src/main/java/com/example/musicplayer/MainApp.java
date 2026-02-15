package com.example.musicplayer;

import com.example.musicplayer.audio.AudioPlayerService;
import com.example.musicplayer.ui.MusicPlayerView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application bootstrap. Connects UI + audio service + visualizers.
 */
public class MainApp extends Application {
    private AudioPlayerService audioPlayerService;

    @Override
    public void start(Stage stage) {
        audioPlayerService = new AudioPlayerService();
        MusicPlayerView view = new MusicPlayerView(stage, audioPlayerService);

        Scene scene = new Scene(view.createContent(), 1100, 700);
        stage.setTitle("JavaFX Music Player with Real-Time 2D/3D Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (audioPlayerService != null) {
            audioPlayerService.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
