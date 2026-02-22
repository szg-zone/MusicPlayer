package com.echo.ui;

import javax.swing.*;
import java.awt.*;

public class PreferencesPanel extends JPanel {

    public PreferencesPanel(NowPlayingPanel panel) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(25, 25, 60));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Select Visualizer");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton liquid = new JButton("Liquid");
        JButton bars = new JButton("Bars");
        JButton circle = new JButton("Circular Lines");

        liquid.addActionListener(e ->
                panel.setVisualizer("LIQUID"));

        bars.addActionListener(e ->
                panel.setVisualizer("BAR"));

        circle.addActionListener(e ->
                panel.setVisualizer("LINE"));

        add(title);
        add(Box.createVerticalStrut(20));
        add(liquid);
        add(Box.createVerticalStrut(10));
        add(bars);
        add(Box.createVerticalStrut(10));
        add(circle);
    }
}