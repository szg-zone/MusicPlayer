package com.echo.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class AudioVisualizer extends JPanel {

    private int[] bars = new int[40];
    private Random random = new Random();

    public AudioVisualizer() {

        setBackground(new Color(25, 25, 70));

        Timer timer = new Timer(60, e -> {
            for (int i = 0; i < bars.length; i++) {
                bars[i] = random.nextInt(150);
            }
            repaint();
        });

        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(180, 80, 200));

        int width = getWidth() / bars.length;

        for (int i = 0; i < bars.length; i++) {
            int height = bars[i];
            g2.fillRect(i * width,
                    getHeight() - height,
                    width - 4,
                    height);
        }
    }
}