package com.echo.ui;

import com.echo.model.Song;
import com.echo.database.MusicDatabase;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicPlayerUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private AllSongsPanel allSongsPanel;
    private NowPlayingPanel nowPlayingPanel;
    private FavoritesPanel favoritesPanel;
    private AboutPanel aboutPanel;
    private PreferencesPanel preferencesPanel; // NEW

    private JPanel miniPlayer;
    private JLabel miniTitle;
    private JButton miniPlayBtn;
    private JSlider miniSlider;
    private JLabel miniTime;

    private Timer progressTimer;

    private List<Song> songs = new ArrayList<>();
    private List<String> favoritePaths = new ArrayList<>();

    private Clip clip;
    private int currentIndex = 0;
    private boolean isPlaying = false;
    private boolean isLooping = false;

    public MusicPlayerUI() {

        setTitle("ECHO Music Player");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createUI();
        createMiniPlayer();
        loadSongs();
        loadFavorites();

        setVisible(true);
    }

    // ================= UI =================

    private void createUI() {

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
        JButton prefBtn = createNavButton("Preferences"); // NEW

        navPanel.add(logo);
        navPanel.add(allBtn);
        navPanel.add(favBtn);
        navPanel.add(aboutBtn);
        navPanel.add(prefBtn); // NEW

        add(navPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        allSongsPanel = new AllSongsPanel();
        nowPlayingPanel = new NowPlayingPanel();
        favoritesPanel = new FavoritesPanel();
        aboutPanel = new AboutPanel();
        preferencesPanel = new PreferencesPanel(nowPlayingPanel); // NEW

        mainPanel.add(allSongsPanel, "ALL");
        mainPanel.add(nowPlayingPanel, "NOW");
        mainPanel.add(favoritesPanel, "FAV");
        mainPanel.add(aboutPanel, "ABOUT");
        mainPanel.add(preferencesPanel, "PREF"); // NEW

        add(mainPanel, BorderLayout.CENTER);

        allBtn.addActionListener(e -> cardLayout.show(mainPanel, "ALL"));

        favBtn.addActionListener(e -> {
            favoritesPanel.loadFavorites();
            cardLayout.show(mainPanel, "FAV");
        });

        aboutBtn.addActionListener(e -> cardLayout.show(mainPanel, "ABOUT"));

        prefBtn.addActionListener(e -> // NEW
                cardLayout.show(mainPanel, "PREF"));

        allSongsPanel.setSongClickListener(song -> {
            currentIndex = songs.indexOf(song);
            playSong(currentIndex);
            cardLayout.show(mainPanel, "NOW");
        });

        nowPlayingPanel.setPlayButtonListener(e -> togglePlayPause());
        nowPlayingPanel.setNextListener(e -> nextSong());
        nowPlayingPanel.setPreviousListener(e -> previousSong());
        nowPlayingPanel.setShuffleListener(e -> shuffleSongs());
        nowPlayingPanel.setLoopListener(e -> toggleLoop());
        nowPlayingPanel.setHeartListener(e -> toggleFavorite());
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

    // ================= MINI PLAYER =================

    private void createMiniPlayer() {

        miniPlayer = new JPanel(new BorderLayout());
        miniPlayer.setBackground(new Color(20, 20, 60));
        miniPlayer.setPreferredSize(new Dimension(getWidth(), 80));

        miniTitle = new JLabel("No song playing");
        miniTitle.setForeground(Color.WHITE);
        miniTitle.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));

        miniPlayBtn = new JButton("▶");
        miniPlayBtn.setFocusPainted(false);
        miniPlayBtn.setBorderPainted(false);
        miniPlayBtn.setContentAreaFilled(false);
        miniPlayBtn.setForeground(Color.WHITE);
        miniPlayBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        miniPlayBtn.addActionListener(e -> togglePlayPause());

        miniSlider = new JSlider();
        miniSlider.setOpaque(false);

        miniTime = new JLabel("0:00 / 0:00");
        miniTime.setForeground(Color.WHITE);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(miniTitle, BorderLayout.NORTH);
        center.add(miniSlider, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout());
        right.setOpaque(false);
        right.add(miniTime);
        right.add(miniPlayBtn);

        miniPlayer.add(center, BorderLayout.CENTER);
        miniPlayer.add(right, BorderLayout.EAST);

        add(miniPlayer, BorderLayout.SOUTH);

        setupSliderControl();
    }

    private void setupSliderControl() {

        miniSlider.addChangeListener(e -> {
            if (clip != null && miniSlider.getValueIsAdjusting()) {
                long newPosition =
                        (long) ((miniSlider.getValue() / 100.0) *
                                clip.getMicrosecondLength());
                clip.setMicrosecondPosition(newPosition);
            }
        });

        progressTimer = new Timer(500, e -> {
            if (clip != null && clip.isOpen()) {

                long current = clip.getMicrosecondPosition();
                long total = clip.getMicrosecondLength();

                if (total > 0) {
                    int percent = (int) ((current * 100) / total);
                    miniSlider.setValue(percent);
                }

                miniTime.setText(formatTime(current)
                        + " / " + formatTime(total));
            }
        });

        progressTimer.start();
    }

    private String formatTime(long microseconds) {
        long seconds = microseconds / 1_000_000;
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%d:%02d", mins, secs);
    }

    // ================= AUDIO =================

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

    private void playSong(int index) {

        try {

            if (clip != null) {
                clip.stop();
                clip.close();
            }

            Song song = songs.get(index);

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(song.getFile());

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            isPlaying = true;
            miniPlayBtn.setText("⏸");
            miniTitle.setText(song.getTitle() + " - " + song.getArtist());

            nowPlayingPanel.setSong(song);
            nowPlayingPanel.setPlayState(true);
            nowPlayingPanel.updateClip(clip); // IMPORTANT
            updateHeartIcon();

            clip.addLineListener(event -> {
    if (event.getType() == LineEvent.Type.STOP &&
            clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {

        if (isLooping) {
            playSong(currentIndex);
        } else {
            nextSong();
        }
    }
});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void togglePlayPause() {
        if (clip == null) return;

        if (isPlaying) {
            clip.stop();
            miniPlayBtn.setText("▶");
            nowPlayingPanel.setPlayState(false);
        } else {
            clip.start();
            miniPlayBtn.setText("⏸");
            nowPlayingPanel.setPlayState(true);
        }

        isPlaying = !isPlaying;
    }

    private void nextSong() {
        if (songs.isEmpty()) return;
        currentIndex = (currentIndex + 1) % songs.size();
        playSong(currentIndex);
    }

    private void previousSong() {
        if (songs.isEmpty()) return;
        currentIndex--;
        if (currentIndex < 0)
            currentIndex = songs.size() - 1;
        playSong(currentIndex);
    }

    private void shuffleSongs() {
        Collections.shuffle(songs);
        currentIndex = 0;
        playSong(currentIndex);
    }

    private void toggleLoop() {
        isLooping = !isLooping;
        nowPlayingPanel.setLoopState(isLooping);
    }

    private void loadFavorites() {
        favoritePaths = MusicDatabase.loadFavorites();
    }

    private void toggleFavorite() {
        if (songs.isEmpty()) return;

        String path =
                songs.get(currentIndex).getFile().getAbsolutePath();

        if (favoritePaths.contains(path))
            favoritePaths.remove(path);
        else
            favoritePaths.add(path);

        MusicDatabase.saveFavorites(favoritePaths);
        updateHeartIcon();
    }

    private void updateHeartIcon() {
        if (songs.isEmpty()) return;

        String path =
                songs.get(currentIndex).getFile().getAbsolutePath();

        boolean liked = favoritePaths.contains(path);
        nowPlayingPanel.toggleHeart(liked);
    }
}