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

    private List<Song> songs = new ArrayList<>();
    private List<String> favoritePaths = new ArrayList<>();

    private Clip clip;
    private int currentIndex = 0;
    private boolean isPlaying = false;
    private boolean isLooping = false;

    public MusicPlayerUI() {

        setTitle("ECHO Music Player");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createUI();
        loadSongs();
        loadFavorites();

        setVisible(true);
    }

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

        navPanel.add(logo);
        navPanel.add(allBtn);
        navPanel.add(favBtn);
        navPanel.add(aboutBtn);

        add(navPanel, BorderLayout.WEST);

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

        allBtn.addActionListener(e -> cardLayout.show(mainPanel, "ALL"));
        favBtn.addActionListener(e -> {
            favoritesPanel.loadFavorites();
            cardLayout.show(mainPanel, "FAV");
        });
        aboutBtn.addActionListener(e -> cardLayout.show(mainPanel, "ABOUT"));

        allSongsPanel.setSongClickListener(song -> {
            currentIndex = songs.indexOf(song);
            playSong(currentIndex);
            nowPlayingPanel.setSong(song);
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

            AudioInputStream originalStream =
                    AudioSystem.getAudioInputStream(song.getFile());

            AudioFormat baseFormat = originalStream.getFormat();

            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);

            AudioInputStream decodedStream =
                    AudioSystem.getAudioInputStream(decodedFormat, originalStream);

            clip = AudioSystem.getClip();
            clip.open(decodedStream);
            clip.start();

            isPlaying = true;
            nowPlayingPanel.setPlayState(true);
            nowPlayingPanel.setSong(song);
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
            nowPlayingPanel.setPlayState(false);
        } else {
            clip.start();
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
        if (currentIndex < 0) currentIndex = songs.size() - 1;
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

        String path = songs.get(currentIndex).getFile().getAbsolutePath();

        if (favoritePaths.contains(path)) {
            favoritePaths.remove(path);
        } else {
            favoritePaths.add(path);
        }

        MusicDatabase.saveFavorites(favoritePaths);
        updateHeartIcon();
    }

    private void updateHeartIcon() {
        if (songs.isEmpty()) return;

        String path = songs.get(currentIndex).getFile().getAbsolutePath();
        boolean liked = favoritePaths.contains(path);
        nowPlayingPanel.toggleHeart(liked);
    }
}