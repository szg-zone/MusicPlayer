package com.example.musicplayer.visualization;

/**
 * Utility helpers for converting raw spectrum data into renderable values.
 */
public final class SpectrumMath {
    private SpectrumMath() {
    }

    /**
     * Converts a raw JavaFX spectrum magnitude (dB) to [0..1].
     *
     * Example with threshold = -60:
     *   positive = magnitude - threshold
     *   normalized = positive / abs(threshold)
     */
    public static double normalizedMagnitude(float magnitudeDb, int thresholdDb) {
        double positive = magnitudeDb - thresholdDb;
        double normalized = positive / Math.abs(thresholdDb);
        return clamp(normalized, 0.0, 1.0);
    }

    /**
     * Scales a normalized [0..1] value to a pixel/unit size for rendering.
     */
    public static double scale(double normalized, double min, double max) {
        return min + clamp(normalized, 0.0, 1.0) * (max - min);
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
