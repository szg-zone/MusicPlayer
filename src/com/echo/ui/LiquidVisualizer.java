package com.echo.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class LiquidVisualizer extends JPanel {

    private double phase = 0;

    public LiquidVisualizer() {

        setOpaque(false);

        Timer timer = new Timer(16, e -> {
            phase += 0.05;
            repaint();
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawWave(g2, getHeight() - 200, 40, 0.02,
                new Color(140, 60, 200, 180));

        drawWave(g2, getHeight() - 170, 30, 0.025,
                new Color(120, 40, 170, 180));

        drawWave(g2, getHeight() - 140, 20, 0.03,
                new Color(90, 20, 140, 200));
    }

    private void drawWave(Graphics2D g2, int baseY,
                          int amplitude,
                          double frequency,
                          Color color) {

        Path2D path = new Path2D.Double();
        path.moveTo(0, getHeight());

        for (int x = 0; x <= getWidth(); x++) {

            double y = baseY +
                    Math.sin((x * frequency) + phase) * amplitude;

            path.lineTo(x, y);
        }

        path.lineTo(getWidth(), getHeight());
        path.closePath();

        g2.setColor(color);
        g2.fill(path);
    }
}