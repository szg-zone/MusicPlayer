package com.echo.ui;

import com.echo.model.Song;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private AllSongsPanel allSongsPanel;
    private NowPlayingPanel nowPlayingPanel;
    private FavoritesPanel favoritesPanel;
    private AboutPanel aboutPanel;

    private List<Song> songs = new ArrayList<>();
    private Clip clip;
    private Song currentSong;
    private boolean isPlaying = false;

    public MusicPlayerUI() {

        setTitle("ECHO Music Player");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createUI();
        loadSongs();

        setVisible(true);
    }

    private void createUI() {

        // ================= LEFT NAV =================
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(18, 18, 45));
        navPanel.setPreferredSize(new Dimension(200, getHeight()));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("ECHO");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 26));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        JButton allBtn = createNavButton("All Songs");
        JButton favBtn = createNavButton("Favorites");
        JButton aboutBtn = createNavButton("About");

        navPanel.add(logo);
        navPanel.add(allBtn);
        navPanel.add(favBtn);
        navPanel.add(aboutBtn);

        add(navPanel, BorderLayout.WEST);

        // ================= MAIN PANEL =================
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        allSongsPanel = new AllSongsPanel();
        nowPlayingPanel = new NowPlayingPanel();
        favoritesPanel = new FavoritesPanel();
        aboutPanel = new AboutPanel();

        mainPanel.add(allSongsPanel, "ALL");
        mainPanel.add(nowPlayingPanel, "NOW");
        mainPanel.add(favoritesPanel, "FAV");
        mainPanel.add(aboutPanel, "ABOUT");

        add(mainPanel, BorderLayout.CENTER);

        // ================= NAV ACTIONS =================
        allBtn.addActionListener(e -> cardLayout.show(mainPanel, "ALL"));

        favBtn.addActionListener(e -> {
            favoritesPanel.loadFavorites(); // refresh list
            cardLayout.show(mainPanel, "FAV");
        });

        aboutBtn.addActionListener(e ->
                cardLayout.show(mainPanel, "ABOUT"));

        // ================= SONG CLICK =================
        allSongsPanel.setSongClickListener(song -> {
            playSong(song);
            nowPlayingPanel.setSong(song);
            cardLayout.show(mainPanel, "NOW");
        });

        // ================= PLAY/PAUSE BUTTON =================
        nowPlayingPanel.setPlayButtonListener(e -> togglePlayPause());
    }

    private JButton createNavButton(String text) {

        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 70));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        return btn;
    }

    private void loadSongs() {

        File folder = new File("music");
        if (!folder.exists()) return;

        for (File file : folder.listFiles()) {

            if (file.getName().toLowerCase().endsWith(".wav")) {

                String name = file.getName().replace(".wav", "");
                String[] parts = name.split("-");

                String artist = parts.length > 1 ? parts[0].trim() : "Unknown";
                String title = parts.length > 1 ? parts[1].trim() : name;

                songs.add(new Song(title, artist, file));
            }
        }

        allSongsPanel.setSongs(songs);
    }

    private void playSong(Song song) {

        try {

            if (clip != null) {
                clip.stop();
                clip.close();
            }

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(song.getFile());

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            currentSong = song;
            isPlaying = true;
            nowPlayingPanel.setPlayState(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void togglePlayPause() {

        if (clip == null) return;

        if (isPlaying) {
            clip.stop();
            nowPlayingPanel.setPlayState(false);
        } else {
            clip.start();
            nowPlayingPanel.setPlayState(true);
        }

        isPlaying = !isPlaying;
    }
}