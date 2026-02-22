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

        if (clip == null || !clip.isRunning()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int centerX = w / 2;
        int centerY = h / 2;

        double radius = 120;

        Path2D path = new Path2D.Double();

        for (int i = 0; i < 360; i += 3) {

            double rad = Math.toRadians(i);

            double wave =
                    Math.sin(angle + rad * 4) * 25
                            + random.nextDouble() * 10;

            double r = radius + wave;

            double x = centerX + r * Math.cos(rad);
            double y = centerY + r * Math.sin(rad);

            if (i == 0)
                path.moveTo(x, y);
            else
                path.lineTo(x, y);
        }

        path.closePath();

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(200, 255, 255, 180));
        g2.draw(path);
    }
}