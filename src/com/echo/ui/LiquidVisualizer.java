package com.echo.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class LiquidVisualizer extends JPanel {

    private double phase = 0;

    public LiquidVisualizer() {
        setOpaque(false);

        Timer timer = new Timer(16, e -> {
            phase += 0.02;
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

        int w = getWidth();
        int h = getHeight();

        // ===== FULL BACKGROUND GRADIENT =====
        GradientPaint bg = new GradientPaint(
                0, 0, new Color(10, 10, 40),
                0, h, new Color(150, 40, 90)
        );
        g2.setPaint(bg);
        g2.fillRect(0, 0, w, h);

        // ===== WAVES =====
        drawWave(g2, h * 0.55, 80, 0.012,
                new Color(150, 80, 220, 180), 1.0);

        drawWave(g2, h * 0.60, 60, 0.018,
                new Color(120, 50, 190, 200), 1.5);

        drawWave(g2, h * 0.65, 45, 0.022,
                new Color(90, 30, 150, 230), 2.0);
    }

    private void drawWave(Graphics2D g2,
                          double baseY,
                          double amplitude,
                          double frequency,
                          Color color,
                          double speed) {

        int w = getWidth();
        int h = getHeight();

        Path2D path = new Path2D.Double();
        path.moveTo(0, h);

        for (int x = 0; x <= w; x++) {

            double dynamicAmp = amplitude +
                    Math.sin(phase * 2 + x * 0.002) * 10;

            double y = baseY +
                    Math.sin((x * frequency) + (phase * speed)) * dynamicAmp;

            path.lineTo(x, y);
        }

        path.lineTo(w, h);
        path.closePath();

        g2.setColor(color);
        g2.fill(path);
    }
}