package com.echo.database;

import java.io.*;
import java.util.*;

public class MusicDatabase {

    private static final String FILE_PATH = "data/favorites.txt";

    public static void saveFavorites(List<String> paths) {

        try {
            new File("data").mkdirs();

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(FILE_PATH));

            for (String path : paths) {
                writer.write(path);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadFavorites() {

        List<String> list = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return list;

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}