# 🎧 ECHO Music Player

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Desktop-App-orange?style=for-the-badge" />
</p>

<p align="center">
  <a href="https://github.com/szg-zone/MusicPlayer/stargazers">
    <img src="https://img.shields.io/github/stars/szg-zone/MusicPlayer?style=social" />
  </a>
  <a href="https://github.com/szg-zone/MusicPlayer/network/members">
    <img src="https://img.shields.io/github/forks/szg-zone/MusicPlayer?style=social" />
  </a>
  <a href="https://github.com/szg-zone/MusicPlayer/issues">
    <img src="https://img.shields.io/github/issues/szg-zone/MusicPlayer" />
  </a>
</p>

---

ECHO is a modern desktop music player built with Java Swing. It features a clean user interface, multiple dynamic audio visualizers, and a file-based favorites system.

---

## ✨ Features

- **Core Playback Controls**: Play, pause, next, previous, shuffle, and loop functionality.
- **Library Management**: Automatically loads `.wav` files from a local `music` directory.
- **Favorites System**: Save your favorite tracks, which persist between sessions using a local text file.
- **Dynamic Audio Visualizers**: Switch between three distinct, animated visual styles while your music plays:
    - 🌊 **Liquid**: A smooth, multi-layered wave animation.
    - 📊 **Bars**: Classic vertical bars that move rhythmically.
    - 🔵 **Circular Lines**: A chaotic, generative circular line art display.
- **Modular UI**: The user interface is built with a `CardLayout` for easy navigation between All Songs, Favorites, Now Playing, and Preferences.
- **Mini Player**: A persistent mini-player at the bottom of the window displays the current track, progress, and core playback controls.

---

## 📂 Project Structure

```
szg-zone-musicplayer/
├── data/
│   └── favorites.txt       # Stores paths to favorite songs
├── music/                  # Add your .wav music files here
└── src/
    └── com/
        └── echo/
            ├── Main.java               # Application entry point
            ├── database/
            │   └── MusicDatabase.java  # Handles loading/saving favorites
            ├── model/
            │   └── Song.java           # Data model for a song
            └── ui/
                ├── MusicPlayerUI.java      # Main application frame
                ├── NowPlayingPanel.java    # Displays current song and visualizer
                ├── AllSongsPanel.java      # Lists all available songs
                ├── FavoritesPanel.java     # Lists favorite songs
                ├── PreferencesPanel.java   # Allows selection of visualizer
                └── Visualizer.java         # Interface for all visualizers
```

---

## ▶️ How To Run

### 1. Requirements
- Java Development Kit (JDK) 8 or higher.

To check your Java version, run:
```bash
java -version
```

### 2. Add Music
1.  Create a folder named `music` in the project's root directory.
2.  Add your `.wav` files to this folder.

The player parses filenames in the format `Artist - Title.wav`. If this format is not used, the artist will be listed as "Unknown".

### 3. Compile
From the project root directory, compile the source files into an `out` directory:
```bash
javac -d out src/com/echo/Main.java src/com/echo/ui/*.java src/com/echo/model/*.java src/com/echo/database/*.java
```

### 4. Run
Execute the main class from the output directory:
```bash
java -cp out com.echo.Main
```

---

## ⚙️ Customization

### Changing the Visualizer
1.  Navigate to the **Preferences** tab in the sidebar.
2.  Click one of the available visualizer options:
    - Liquid
    - Bars
    - Circular Lines
3.  The visualizer in the **Now Playing** view will update instantly.

---

## 🛠 Built With

- **Java & Java Swing**: For the core application logic and graphical user interface.
- **Java AWT (Abstract Window Toolkit)**: Used for `Graphics2D` rendering in the audio visualizers.
- **Java Sound API**: For audio playback of `.wav` files.
- **File I/O**: For persisting user favorites locally.

