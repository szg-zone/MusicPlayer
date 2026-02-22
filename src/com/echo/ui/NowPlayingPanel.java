package com.echo.ui;

import com.echo.model.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NowPlayingPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel artistLabel;

    private JButton playBtn;
    private JButton nextBtn;
    private JButton prevBtn;
    private JButton shuffleBtn;
    private JButton loopBtn;
    private JButton heartBtn;

    private LiquidVisualizer visualizer;

    public NowPlayingPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // ================= VISUALIZER LAYER =================
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(900, 400));
        layeredPane.setLayout(null);

        visualizer = new LiquidVisualizer();
        visualizer.setBounds(0, 0, 900, 400);

        heartBtn = new JButton("\u2661"); // ♡
        heartBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        heartBtn.setFocusPainted(false);
        heartBtn.setBackground(Color.WHITE);
        heartBtn.setBounds(820, 25, 50, 50);

        layeredPane.add(visualizer, Integer.valueOf(0));
        layeredPane.add(heartBtn, Integer.valueOf(1));

        add(layeredPane, BorderLayout.CENTER);

        // ================= BOTTOM PANEL =================
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(150, 40, 90));
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Title");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        artistLabel = new JLabel("Artist");
        artistLabel.setForeground(Color.LIGHT_GRAY);
        artistLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        artistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================= CONTROL ROW =================
        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        shuffleBtn = new JButton("Shuffle");
        prevBtn = new JButton("Prev");
        playBtn = new JButton("Pause");
        nextBtn = new JButton("Next");
        loopBtn = new JButton("Loop");

        styleControlButton(shuffleBtn);
        styleControlButton(prevBtn);
        styleControlButton(playBtn);
        styleControlButton(nextBtn);
        styleControlButton(loopBtn);

        controls.add(shuffleBtn);
        controls.add(prevBtn);
        controls.add(playBtn);
        controls.add(nextBtn);
        controls.add(loopBtn);

        bottom.add(titleLabel);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(artistLabel);
        bottom.add(Box.createVerticalStrut(15));
        bottom.add(controls);

        add(bottom, BorderLayout.SOUTH);
    }

    private void styleControlButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    // ================= SET SONG =================
    public void setSong(Song song) {
        titleLabel.setText(song.getTitle());
        artistLabel.setText(song.getArtist());
    }

    // ================= PLAY STATE =================
    public void setPlayState(boolean playing) {
        playBtn.setText(playing ? "Pause" : "Play");
    }

    // ================= LOOP STATE =================
    public void setLoopState(boolean looping) {
        loopBtn.setBackground(looping ? Color.GREEN : Color.WHITE);
    }

    // ================= HEART TOGGLE =================
    public void toggleHeart(boolean liked) {
        heartBtn.setText(liked ? "\u2764" : "\u2661");
    }

    // ================= LISTENER HOOKS =================
    public void setPlayButtonListener(ActionListener l) {
        playBtn.addActionListener(l);
    }

    public void setNextListener(ActionListener l) {
        nextBtn.addActionListener(l);
    }

    public void setPreviousListener(ActionListener l) {
        prevBtn.addActionListener(l);
    }

    public void setShuffleListener(ActionListener l) {
        shuffleBtn.addActionListener(l);
    }

    public void setLoopListener(ActionListener l) {
        loopBtn.addActionListener(l);
    }

    public void setHeartListener(ActionListener l) {
        heartBtn.addActionListener(l);
    }
}