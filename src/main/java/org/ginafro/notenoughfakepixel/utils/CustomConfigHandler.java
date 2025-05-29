package org.ginafro.notenoughfakepixel.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage.StorageData;
import org.ginafro.notenoughfakepixel.features.skyblock.slotlocking.SlotLocking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import static org.ginafro.notenoughfakepixel.utils.CustomConfigFiles.STORAGE_FOLDER;

public class CustomConfigHandler {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static File slotLock = new File(Config.configDirectory,"slotlocking.json");
    public static SlotLocking.SlotLockingConfig loadConfig() {
        SlotLocking.SlotLockingConfig locking = new SlotLocking.SlotLockingConfig();
        try {
            if(!Config.configDirectory.exists()){
                Config.configDirectory.mkdirs();
            }
            if(!slotLock.exists()){
                slotLock.createNewFile();
            }
            FileReader reader = new FileReader(slotLock);
            SlotLocking.SlotLockingConfig temp = gson.fromJson(reader, SlotLocking.SlotLockingConfig.class);
            if(temp != null){
                locking = temp;
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not read config file ", e);
        }
        return locking;
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

    public static void saveConfig(SlotLocking.SlotLockingConfig config) {
        try {
            if(!slotLock.exists()){
                slotLock.createNewFile();
            }
           FileWriter writer = new FileWriter(slotLock);
            writer.write(gson.toJson(config));
            writer.flush();
            writer.close();
        } catch (Exception e) {
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
