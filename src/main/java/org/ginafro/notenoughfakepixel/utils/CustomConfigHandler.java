package org.ginafro.notenoughfakepixel.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage.StorageData;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ginafro.notenoughfakepixel.utils.CustomConfigFiles.STORAGE_FOLDER;

public class CustomConfigHandler {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Map<String, List<String>> fairySoulsTemplate = new HashMap<>();

    public static <T> @Nullable T loadConfig(Class<T> config, File file) {
        try {
            if (!file.exists() || file.length() == 0) {
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create new config file: " + file);
                }
                return null;
            }

            return readJson(config, file);
        } catch (IOException e) {
            throw new RuntimeException("Could not read config file '" + file + "'", e);
        }
    }

    private static <T> @Nullable T readJson(Class<T> config, File file) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            return gson.fromJson(reader, config);

        } catch (JsonSyntaxException | IllegalStateException e) {
            Logger.logErrorConsole("Invalid JSON in config: " + file.getName());
            e.printStackTrace();
            makeBackup(file, ".corrupted");
            return null;
        }
    }


    public static StorageData loadStorageData(String chestName) {
        File storageFolder = new File(STORAGE_FOLDER.path);
        if (!storageFolder.exists()) return null;

        File file = new File(storageFolder, chestName.replace(" ", "") + ".json");
        if (!file.exists()) return null;

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, StorageData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveStorageData(StorageData data) {
        File folder = new File(STORAGE_FOLDER.path);
        if (!folder.exists() && !folder.mkdirs()) return;

        File file = new File(folder, data.chestName.replace(" ", "") + ".json");
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(Object config, File file) {
        File tempFile = new File(file.getParent(), file.getName() + ".temp");
        try {
            tempFile.createNewFile();
            try (
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(tempFile.toPath()), StandardCharsets.UTF_8))
            ) {
                writer.write(gson.toJson(config));
            }

            if (loadConfig(config.getClass(), tempFile) == null) {
                System.out.println("Config verification failed for " + tempFile + ", could not save config properly.");
                makeBackup(tempFile, ".backup");
                return;
            }

            try {
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                // If atomic move fails it could be because it isn't supported or because the implementation of it
                // doesn't overwrite the old file, in this case we will try a normal move.
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            makeBackup(tempFile, ".backup");
            e.printStackTrace();
        }
    }

    private static void makeBackup(File file, String suffix) {
        File backupFile = new File(file.getParent(), file.getName() + "-" + System.currentTimeMillis() + suffix);
        Logger.logConsole("trying to make backup: " + backupFile.getName());

        try {
            Files.move(file.toPath(), backupFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.move(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception __) {
                Logger.logErrorConsole("neu config gone");
            }
        } finally {
            file.delete();
        }
    }

}
