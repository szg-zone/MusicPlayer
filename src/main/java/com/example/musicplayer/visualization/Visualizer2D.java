package com.example.musicplayer.visualization;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * Draws a bar-based frequency visualizer on JavaFX Canvas.
 */
public class Visualizer2D extends Region {
    private final Canvas canvas = new Canvas(900, 520);

    public Visualizer2D() {
        getChildren().add(canvas);
    }

    /**
     * Called from AnimationTimer every frame.
     *
     * Magnitudes are converted from dB to normalized [0..1] values and then scaled to
     * the current canvas height for reactive bar rendering.
     */
    public void render(float[] magnitudes, int thresholdDb) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.setFill(Color.rgb(10, 12, 20));
        gc.fillRect(0, 0, width, height);

        if (magnitudes.length == 0) {
            return;
        }

        double barWidth = Math.max(2.0, width / magnitudes.length);

        for (int i = 0; i < magnitudes.length; i++) {
            double normalized = SpectrumMath.normalizedMagnitude(magnitudes[i], thresholdDb);
            double barHeight = SpectrumMath.scale(normalized, 2.0, height);
            double x = i * barWidth;
            double y = height - barHeight;

            gc.setFill(Color.hsb(200 + (normalized * 120), 0.85, 0.95));
            gc.fillRoundRect(x, y, Math.max(1.0, barWidth - 1.0), barHeight, 4, 4);
        }
    }

    @Override
    protected void layoutChildren() {
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
    }
}
