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
                    bars[i] = random.nextInt(120);
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

        if (clip == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int barWidth = width / bars.length;

        g2.setColor(new Color(200, 100, 255, 180));

        for (int i = 0; i < bars.length; i++) {
            int barHeight = bars[i];
            int x = i * barWidth;
            int y = height - barHeight;

            g2.fillRoundRect(x + 2, y, barWidth - 4,
                    barHeight, 10, 10);
        }
    }
}