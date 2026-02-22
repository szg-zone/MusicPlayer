# 🎧 ECHO Music Player

ECHO is a modern desktop music player built using **Java Swing**.  
It features multiple animated visualizers, a favorites system with file persistence, and a modular UI architecture.

---

## ✨ Features

### 🎵 Core Playback
- Play / Pause  
- Next / Previous  
- Shuffle  
- Loop mode  
- Auto-play next track  
- Mini progress bar with live time tracking  

### 🎨 Switchable Visualizers
Select your preferred visual style from the Preferences panel:

- 🌊 Liquid Wave Visualizer  
- 📊 Bar Visualizer  
- 🔵 Circular Line Visualizer  

Visualizers update instantly while music is playing.

### ❤️ Favorites System
- Click the heart icon to toggle favorite
- Favorites are stored in `data/favorites.txt`
- File-based persistence (no external database required)

---

## 📂 Project Structure

└── szg-zone-musicplayer/
    ├── data/
    │   └── favorites.txt
    └── src/
        └── com/
            └── echo/
                ├── Main.java
                ├── database/
                │   └── MusicDatabase.java
                ├── model/
                │   └── Song.java
                └── ui/
                    ├── AboutPanel.java
                    ├── AllSongsPanel.java
                    ├── AudioVisualizer.java
                    ├── FavoritesPanel.java
                    ├── LineCircleVisualizer.java
                    ├── LiquidVisualizer.java
                    ├── MusicPlayerUI.java
                    ├── NowPlayingPanel.java
                    ├── PreferencesPanel.java
                    └── Visualizer.java


---

## ▶️ How To Run

### 1️⃣ Requirements

- Java JDK 8 or higher  
  (Recommended: JDK 17+)

Check installed version:

```bash
java -version

```

### 2️⃣ Compile

From the project root directory:
```bash
javac -d out src/com/echo/Main.java src/com/echo/ui/*.java src/com/echo/model/*.java src/com/echo/database/*.java

```

### 3️⃣ Run
```bash
java -cp out com.echo.Main

```

## 🎼 Adding Music

### Create a folder named:
```bash
music
```
Add your .wav files inside it.

### Recommended Naming Format
```bash
ArtistName - SongTitle.wav
```

Example:
```bash
TheWeeknd - BlindingLights.wav
```
If the format is not followed:
- Artist defaults to Unknown

## ⚙️ Preferences

### Navigate to:

```bash
Sidebar → Preferences
```

Select your preferred visualizer:
- Liquid
- Bars
- Circular Lines

Changes apply instantly.

## 🛠 Built With

- Java
- Java Swing
- AWT (Graphics2D)
- Java Sound API
- File I/O

### 🚀 Future Improvements

- Real FFT-based audio visualization
- MP3 support
- Playlist management
- Persistent user preferences
- Theme customization
- Runnable JAR export

## 👤 Author

Sharvin Tejasvi

## 📄 License

This project is intended for educational and personal use.
