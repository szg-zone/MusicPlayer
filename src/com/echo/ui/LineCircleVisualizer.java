package com.echo.ui;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Random;

public class LineCircleVisualizer extends JPanel implements Visualizer {

    private double angle = 0;
    private Clip clip;
    private Timer timer;
    private Random random = new Random();

    public LineCircleVisualizer() {

        setOpaque(false);

        timer = new Timer(16, e -> {
            angle += 0.04;
            repaint();
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

        int w = getWidth();
        int h = getHeight();

        // Same gradient background
        GradientPaint bg = new GradientPaint(
                0, 0, new Color(10, 10, 40),
                0, h, new Color(150, 40, 90)
        );
        g2.setPaint(bg);
        g2.fillRect(0, 0, w, h);

        if (clip == null || !clip.isRunning()) return;

        int centerX = w / 2;
        int centerY = h / 2;
        double baseRadius = 120;

        // 🔥 DRAW MULTIPLE CHAOTIC RINGS
        for (int layer = 0; layer < 8; layer++) {

            Path2D path = new Path2D.Double();

            double layerOffset = layer * 4;
            double dynamicRadius = baseRadius + layerOffset;

            for (int i = 0; i < 360; i += 2) {

                double rad = Math.toRadians(i);

                double noise =
                        Math.sin(angle + rad * 3) * 25
                                + Math.sin(angle * 2 + rad * 5) * 10
                                + random.nextDouble() * 5;

                double r = dynamicRadius + noise;

                double x = centerX + r * Math.cos(rad);
                double y = centerY + r * Math.sin(rad);

                if (i == 0)
                    path.moveTo(x, y);
                else
                    path.lineTo(x, y);
            }

            path.closePath();

            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(220, 255, 255, 40 + layer * 20));
            g2.draw(path);
        }
    }
}