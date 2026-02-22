package com.echo.ui;

import com.echo.model.Song;
import com.echo.database.MusicDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FavoritesPanel extends JPanel {

    private DefaultListModel<Song> model;
    private JList<Song> list;
    private Consumer<Song> listener;

    public FavoritesPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Your Favorites");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setFont(new Font("SansSerif", Font.PLAIN, 16));
        list.setFixedCellHeight(50);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Song selected = list.getSelectedValue();
                    if (listener != null && selected != null) {
                        listener.accept(selected);
                    }
                }
            }
        });

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        loadFavorites();
    }

    public void loadFavorites() {

        model.clear();
        List<String> paths = MusicDatabase.loadFavorites();

        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {

                String name = file.getName().replace(".wav", "");
                String[] parts = name.split("-");

                String artist = parts.length > 1 ? parts[0].trim() : "Unknown";
                String songTitle = parts.length > 1 ? parts[1].trim() : name;

                model.addElement(new Song(songTitle, artist, file));
            }
        }
    }

    public void setSongClickListener(Consumer<Song> listener) {
        this.listener = listener;
    }
}