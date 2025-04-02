package org.ginafro.notenoughfakepixel.features.skyblock.qol.CustomAliases;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.Aliases;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class CustomAliases {

    public static List<CustomAliases.Alias> aliases = new ArrayList<>();
    public static HashMap<CustomAliases.Alias, Pattern> patterns = new HashMap<>();
    public static String configFile = NotEnoughFakepixel.configDirectory + "/nefalias.json";
    private static final Minecraft mc = Minecraft.getMinecraft();

    public CustomAliases(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void unregisterAlias(Alias alias) {
        if(alias != null){
            if(ClientCommandHandler.instance.getCommands().containsKey(alias.alias)) {
                ClientCommandHandler.instance.getCommands().remove(alias.alias);
            }
        }
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent e){
        registerAliases();
    }

    public static void registerAliases(){
        for(Alias a : aliases){
            if(a.toggled){
                ClientCommandHandler.instance.registerCommand(new Aliases.AliasCommand(a.alias , a.command));
            }
        }
    }

    public static void save() {
        for (CustomAliases.Alias alias : aliases) {
                Pattern pattern = Pattern.compile(alias.command);
                patterns.put(alias, pattern);
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(aliases, writer);
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void load() {
        File file = new File(configFile);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                List<CustomAliases.Alias> loadedAlerts = new GsonBuilder().create().fromJson(
                        reader,
                        new TypeToken<List<CustomAliases.Alias>>(){}.getType()
                );
                if (loadedAlerts != null) {
                    aliases.clear(); // Clear existing alerts
                    aliases.addAll(loadedAlerts); // Load new ones
                    // Rebuild patterns for Regex alerts
                    for (CustomAliases.Alias alias : aliases) {
                        Pattern pattern = Pattern.compile(alias.command);
                        patterns.put(alias, pattern);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Failed to load aliases from " + configFile);
            }
        } else {
            System.out.println("No aliases file found at " + configFile + ", starting with empty list");
        }
    }
    public static class Alias {
        public String location;
        public String command;
        public String alias;
        public boolean toggled;

        public Alias( String location, String command, String a, boolean toggled) {
            this.location = location;
            this.command = command;
            this.alias = a;
            this.toggled = toggled;
        }

        public void toggle() {
            toggled = !toggled;
        }
    }
}
