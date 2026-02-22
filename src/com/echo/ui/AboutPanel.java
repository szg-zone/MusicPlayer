package com.echo.ui;

import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JPanel {

    public AboutPanel() {

        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setBackground(new Color(25, 25, 70));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JLabel title = new JLabel("ECHO MUSIC PLAYER");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><div style='text-align:center;'>"
                + "A Java Swing Music Player<br><br>"
                + "Features:<br>"
                + "• Song List Loader<br>"
                + "• Play / Pause<br>"
                + "• Favorites (File I/O)<br>"
                + "• Animated Visualizer<br>"
                + "• CardLayout Navigation<br><br>"
                + "Built with pure Java."
                + "</div></html>");

        desc.setForeground(Color.LIGHT_GRAY);
        desc.setFont(new Font("SansSerif", Font.PLAIN, 16));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(20));
        content.add(desc);

        add(content, BorderLayout.CENTER);
    }
}