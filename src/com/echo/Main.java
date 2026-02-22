package com.echo;

import javax.swing.SwingUtilities;
import com.echo.ui.MusicPlayerUI;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MusicPlayerUI();
        });
    }
}