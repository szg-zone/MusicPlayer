package com.echo.model;

import java.io.File;

public class Song {

    private String title;
    private String artist;
    private File file;

    public Song(String title, String artist, File file) {
        this.title = title;
        this.artist = artist;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}