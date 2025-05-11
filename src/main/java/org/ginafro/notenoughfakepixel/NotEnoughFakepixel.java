package org.ginafro.notenoughfakepixel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.alerts.Alerts;
import org.ginafro.notenoughfakepixel.events.registers.ModEventRegistrar;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.inventory.equipment.EquipmentOverlay;
import org.ginafro.notenoughfakepixel.features.cosmetics.CosmeticsManager;
import org.ginafro.notenoughfakepixel.features.cosmetics.impl.Bandana;
import org.ginafro.notenoughfakepixel.features.cosmetics.loader.OBJLoader;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.CustomAliases.CustomAliases;
import org.ginafro.notenoughfakepixel.commands.CopyCommand;
import org.ginafro.notenoughfakepixel.config.gui.commands.Commands;
import org.ginafro.notenoughfakepixel.config.gui.config.ConfigEditor;
import org.ginafro.notenoughfakepixel.config.gui.core.GuiScreenElementWrapper;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.*;
import org.ginafro.notenoughfakepixel.features.skyblock.slotlocking.SlotLocking;
import org.ginafro.notenoughfakepixel.utils.*;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Mod(modid = "notenoughfakepixel", useMetadata = true)
public class NotEnoughFakepixel {

    public static NotEnoughFakepixel instance;

    public static final File nefFolder = new File(Minecraft.getMinecraft().mcDataDir, "NotEnoughFakepixel");

    public static final KeyBinding openGuiKey = new KeyBinding(
            "Open GUI", 
            Keyboard.KEY_P,
            "NotEnoughFakepixel"
    );

    private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    @Getter
    private Utils utils = new Utils();

    public static File configDirectory = new File("config/Notenoughfakepixel");
    public static File storageDirectory = new File("config/Notenoughfakepixel/storage");
    private File configFile;

    public static Configuration feature;

    @Getter
    private OBJLoader objLoader;

    public void registerCosmetics(){
        CosmeticsManager.registerCosmetics(new Bandana());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        SlotLocking.getInstance().loadConfig();
        objLoader = new OBJLoader();

        //registerCosmetics();
        //CapeManager.loadCapesFromGitHub();

        createDirectoryIfNotExists(nefFolder);
        createDirectoryIfNotExists(configDirectory);
        createDirectoryIfNotExists(storageDirectory);

        configFile = new File(configDirectory, "config.json");

        loadConfig();

        EquipmentOverlay.loadData();

        ClientCommandHandler.instance.registerCommand(new CopyCommand());
        new Aliases();


        ClientRegistry.registerKeyBinding(openGuiKey);
        Commands.init();
        Alerts.load();
        CustomAliases.load();
        ModEventRegistrar.registerModEvents();
        SlotLocking.getInstance().saveConfig();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfig));
    }

    private void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void loadConfig() {
        if (configFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(configFile.toPath()), StandardCharsets.UTF_8))) {
                feature = gson.fromJson(reader, Configuration.class);
            } catch (Exception ignored) {
            }
        }
        if (feature == null) {
            feature = new Configuration();
            saveConfig();
        }
    }

    public static void resetLockedSlots() {
        SlotLocking.getInstance().resetSlotLocking();
    }

    public static GuiScreen openGui;
    public static long lastOpenedGui;
    public static String th = "default";
    public static ResourceLocation bg = new ResourceLocation("notenoughfakepixel:backgrounds/" + th + "/background.png");

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            openGui = null;
            return;
        }

        if (openGui != null) {
            if (mc.thePlayer.openContainer != null) {
                mc.thePlayer.closeScreen();
            }
            mc.displayGuiScreen(openGui);
            openGui = null;
            lastOpenedGui = System.currentTimeMillis();
        }

        ScoreboardUtils.parseScoreboard();
    }

    public void saveConfig() {
        try {
            //noinspection ResultOfMethodCallIgnored
            configFile.createNewFile();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8))) {
                writer.write(gson.toJson(feature));
                SlotLocking.getInstance().saveConfig();
            }
        } catch (IOException ignored) {
        }
    }

    public static GuiScreen screenToOpen = null;
    private static int screenTicks = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;

        if (screenToOpen != null) {
            screenTicks++;
            if (screenTicks == 5) {
                Minecraft.getMinecraft().displayGuiScreen(screenToOpen);
                screenTicks = 0;
                screenToOpen = null;
            }
        }

        if (openGuiKey.isPressed() && Minecraft.getMinecraft().currentScreen == null) {
            screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(feature));
        }
    }

}