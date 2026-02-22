package com.echo.ui;

import com.echo.model.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NowPlayingPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel artistLabel;
    private JButton playBtn;
    private AudioVisualizer visualizer;

    public NowPlayingPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // ================= VISUALIZER (TOP) =================
        visualizer = new AudioVisualizer();
        add(visualizer, BorderLayout.CENTER);

        // ================= BOTTOM PANEL =================
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(110, 40, 130)); // Echo purple
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Song Title
        titleLabel = new JLabel("Title");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Artist
        artistLabel = new JLabel("Artist");
        artistLabel.setForeground(Color.LIGHT_GRAY);
        artistLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        artistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Controls Panel
        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        playBtn = new JButton("Pause");
        playBtn.setFocusPainted(false);
        playBtn.setBackground(Color.WHITE);
        playBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        controls.add(playBtn);

        bottom.add(titleLabel);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(artistLabel);
        bottom.add(Box.createVerticalStrut(15));
        bottom.add(controls);

        add(bottom, BorderLayout.SOUTH);
    }

    // ================= SET CURRENT SONG =================
    public void setSong(Song song) {
        titleLabel.setText(song.getTitle());
        artistLabel.setText(song.getArtist());
    }

    // ================= UPDATE PLAY STATE =================
    public void setPlayState(boolean playing) {
        playBtn.setText(playing ? "Pause" : "Play");
    }

    // ================= CONNECT BUTTON TO UI CONTROLLER =================
    public void setPlayButtonListener(ActionListener listener) {
        playBtn.addActionListener(listener);
    }
}