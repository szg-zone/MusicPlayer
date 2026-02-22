package com.echo.ui;

import com.echo.model.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

public class AllSongsPanel extends JPanel {

    private JList<Song> songList;
    private DefaultListModel<Song> model;
    private Consumer<Song> listener;

    public AllSongsPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        model = new DefaultListModel<>();
        songList = new JList<>(model);

        songList.setFont(new Font("SansSerif", Font.PLAIN, 16));
        songList.setFixedCellHeight(50);

        songList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Song selected = songList.getSelectedValue();
                    if (listener != null) listener.accept(selected);
                }
            }
        });

        add(new JScrollPane(songList), BorderLayout.CENTER);
    }

    public void setSongs(List<Song> songs) {
        model.clear();
        for (Song s : songs) model.addElement(s);
    }

    public void setSongClickListener(Consumer<Song> listener) {
        this.listener = listener;
    }
}