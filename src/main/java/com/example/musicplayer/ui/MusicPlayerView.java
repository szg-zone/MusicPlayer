package com.example.musicplayer.ui;

import com.example.musicplayer.audio.AudioPlayerService;
import com.example.musicplayer.visualization.Visualizer2D;
import com.example.musicplayer.visualization.Visualizer3D;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;

/**
 * JavaFX UI composition class.
 *
 * UI controls call AudioPlayerService methods (open/play/pause/stop/volume).
 * AnimationTimer pulls latest spectrum data each frame and routes it to the active visualizer.
 */
public class MusicPlayerView {
    private final Stage stage;
    private final AudioPlayerService audioService;

    private final Visualizer2D visualizer2D = new Visualizer2D();
    private final Visualizer3D visualizer3D = new Visualizer3D(AudioPlayerService.SPECTRUM_BANDS);

    public MusicPlayerView(Stage stage, AudioPlayerService audioService) {
        this.stage = stage;
        this.audioService = audioService;
    }

    public Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));

        StackPane visualizerPane = new StackPane(visualizer2D, visualizer3D);
        visualizer3D.setVisible(false);

        Button openButton = new Button("Open File");
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        CheckBox modeToggle = new CheckBox("3D Visualization Mode");

        Slider volumeSlider = new Slider(0, 1, 0.8);
        volumeSlider.setPrefWidth(220);
        Label volumeLabel = new Label("Volume");

        openButton.setOnAction(event -> {
            File audio = audioService.chooseAudioFile(stage);
            if (audio != null) {
                audioService.loadFile(audio);
                audioService.setVolume(volumeSlider.getValue());
                audioService.play();
            }
        });

        playButton.setOnAction(event -> audioService.play());
        pauseButton.setOnAction(event -> audioService.pause());
        stopButton.setOnAction(event -> audioService.stop());

        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) ->
                audioService.setVolume(newValue.doubleValue())
        );

        modeToggle.selectedProperty().addListener((obs, oldMode, use3d) -> {
            visualizer2D.setVisible(!use3d);
            visualizer3D.setVisible(use3d);
        });

        HBox controls = new HBox(10,
                openButton, playButton, pauseButton, stopButton,
                volumeLabel, volumeSlider,
                modeToggle
        );
        controls.setPadding(new Insets(10, 0, 0, 0));
        controls.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(volumeSlider, Priority.NEVER);

        root.setCenter(visualizerPane);
        root.setBottom(controls);

        // Main render loop. AudioSpectrumListener updates data roughly every 0.1s, and this
        // timer paints each JavaFX frame using the newest magnitude/phase snapshot.
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                var spectrum = audioService.getLatestSpectrum();
                if (modeToggle.isSelected()) {
                    visualizer3D.render(spectrum.magnitudes(), AudioPlayerService.SPECTRUM_THRESHOLD_DB);
                } else {
                    visualizer2D.render(spectrum.magnitudes(), AudioPlayerService.SPECTRUM_THRESHOLD_DB);
                }
            }
        };
        animationTimer.start();

        return root;
    }
}
