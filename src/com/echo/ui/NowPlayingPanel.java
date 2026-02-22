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

        // ================= VISUALIZER =================
        JLayeredPane layeredPane = new JLayeredPane();
layeredPane.setLayout(new OverlayLayout(layeredPane));

visualizer = new LiquidVisualizer();

JPanel heartContainer = new JPanel(new BorderLayout());
heartContainer.setOpaque(false);

heartBtn = createCircleButton("♡", 55);

JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
topRight.setOpaque(false);
topRight.add(heartBtn);

heartContainer.add(topRight, BorderLayout.NORTH);

layeredPane.add(heartContainer, Integer.valueOf(1));
layeredPane.add(visualizer, Integer.valueOf(0));

add(layeredPane, BorderLayout.CENTER);

        // ================= BOTTOM PANEL =================
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(150, 40, 90));
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        titleLabel = new JLabel("Title");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        artistLabel = new JLabel("Artist");
        artistLabel.setForeground(new Color(220, 220, 220));
        artistLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        artistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottom.add(titleLabel);
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(artistLabel);
        bottom.add(Box.createVerticalStrut(20));

        // ================= CONTROLS =================
        JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));

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

        bottom.add(controls);

        add(bottom, BorderLayout.SOUTH);
    }

    // ================= BUTTON STYLES =================

    private JButton createIconButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(150, 40, 90));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
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
        btn.setMaximumSize(new Dimension(size, size));
        btn.setMinimumSize(new Dimension(size, size));
        return btn;
    }

    // ================= STATE METHODS =================

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

    // ================= LISTENER HOOKS =================

    public void setPlayButtonListener(ActionListener l) { playBtn.addActionListener(l); }
    public void setNextListener(ActionListener l) { nextBtn.addActionListener(l); }
    public void setPreviousListener(ActionListener l) { prevBtn.addActionListener(l); }
    public void setShuffleListener(ActionListener l) { shuffleBtn.addActionListener(l); }
    public void setLoopListener(ActionListener l) { loopBtn.addActionListener(l); }
    public void setHeartListener(ActionListener l) { heartBtn.addActionListener(l); }
}