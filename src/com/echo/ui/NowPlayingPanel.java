package com.echo.ui;

import com.echo.model.Song;

import javax.sound.sampled.Clip;
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

    private Visualizer visualizer;
    private JPanel visualContainer;

    public NowPlayingPanel() {

        setLayout(new OverlayLayout(this));
        setOpaque(false);

        visualContainer = new JPanel(new BorderLayout());
        visualContainer.setOpaque(false);

        setVisualizer("LIQUID");

        JPanel heartLayer = new JPanel(new BorderLayout());
        heartLayer.setOpaque(false);

        heartBtn = createCircleButton("♡", 55);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        topRight.setOpaque(false);
        topRight.add(heartBtn);

        heartLayer.add(topRight, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Title");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        artistLabel = new JLabel("Artist");
        artistLabel.setForeground(new Color(220, 220, 220));
        artistLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        artistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(artistLabel);
        content.add(Box.createVerticalStrut(20));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        controls.setOpaque(false);

        shuffleBtn = createIconButton("⤮");
        prevBtn = createIconButton("⏮");
        playBtn = createCircleButton("⏯", 65);
        nextBtn = createIconButton("⏭");
        loopBtn = createIconButton("⟲");

        controls.add(shuffleBtn);
        controls.add(prevBtn);
        controls.add(playBtn);
        controls.add(nextBtn);
        controls.add(loopBtn);

        content.add(controls);
        bottom.add(content, BorderLayout.SOUTH);

        add(bottom);
        add(heartLayer);
        add(visualContainer);
    }

    public void setVisualizer(String type) {

        visualContainer.removeAll();

        if (type.equals("LIQUID"))
            visualizer = new LiquidVisualizer();
        else if (type.equals("BAR"))
            visualizer = new AudioVisualizer();
        else
            visualizer = new LineCircleVisualizer();

        visualContainer.add((Component) visualizer);
        visualContainer.revalidate();
        visualContainer.repaint();
    }

    public void updateClip(Clip clip) {
        if (visualizer != null)
            visualizer.setClip(clip);
    }

    private JButton createIconButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 22));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton createCircleButton(String text, int size) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setForeground(new Color(150, 40, 90));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(size, size));
        return btn;
    }

    public void setSong(Song song) {
        titleLabel.setText(song.getTitle());
        artistLabel.setText(song.getArtist());
    }

    public void setPlayState(boolean playing) {
        playBtn.setText(playing ? "⏸" : "▶");
    }

    public void setLoopState(boolean looping) {
        loopBtn.setForeground(looping ? Color.GREEN : Color.WHITE);
    }

    public void toggleHeart(boolean liked) {
        heartBtn.setText(liked ? "❤" : "♡");
    }

    public void setPlayButtonListener(ActionListener l) { playBtn.addActionListener(l); }
    public void setNextListener(ActionListener l) { nextBtn.addActionListener(l); }
    public void setPreviousListener(ActionListener l) { prevBtn.addActionListener(l); }
    public void setShuffleListener(ActionListener l) { shuffleBtn.addActionListener(l); }
    public void setLoopListener(ActionListener l) { loopBtn.addActionListener(l); }
    public void setHeartListener(ActionListener l) { heartBtn.addActionListener(l); }
}