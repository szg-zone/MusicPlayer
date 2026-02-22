package com.echo.ui;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class AudioVisualizer extends JPanel implements Visualizer {

    private Clip clip;
    private Random random = new Random();
    private Timer timer;
    private int[] bars = new int[40];

    public AudioVisualizer() {

        setOpaque(false);

        timer = new Timer(50, e -> {
            if (clip != null && clip.isRunning()) {
                for (int i = 0; i < bars.length; i++) {
                    bars[i] = 40 + random.nextInt(140);
                }
                repaint();
            }
        });

        timer.start();
    }

    @Override
    public void setClip(Clip clip) {
        this.clip = clip;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // ===== GRADIENT BACKGROUND =====
        GradientPaint bg = new GradientPaint(
                0, 0, new Color(10, 10, 40),
                0, height, new Color(150, 40, 90)
        );
        g2.setPaint(bg);
        g2.fillRect(0, 0, width, height);

        if (clip == null) return;

        int barWidth = width / bars.length;

        // SAFE REGION
        int bottomLimit = height - 140;
        int topLimit = height / 3;

        int maxBarHeight = bottomLimit - topLimit;

        g2.setColor(new Color(200, 120, 255, 220));

        for (int i = 0; i < bars.length; i++) {

            int scaledHeight =
                    (int)((bars[i] / 180.0) * maxBarHeight);

            int x = i * barWidth;
            int y = bottomLimit - scaledHeight;

            g2.fillRoundRect(
                    x + 2,
                    y,
                    barWidth - 4,
                    scaledHeight,
                    10,
                    10
            );
        }
    }
}