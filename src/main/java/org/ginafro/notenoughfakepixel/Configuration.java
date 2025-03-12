package org.ginafro.notenoughfakepixel;

import com.google.gson.annotations.Expose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.ginafro.notenoughfakepixel.config.features.*;
import org.ginafro.notenoughfakepixel.config.gui.config.ConfigEditor;
import org.ginafro.notenoughfakepixel.config.gui.core.GuiElement;
import org.ginafro.notenoughfakepixel.config.gui.core.GuiScreenElementWrapper;
import org.ginafro.notenoughfakepixel.config.gui.core.config.Position;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.Category;
import org.ginafro.notenoughfakepixel.config.gui.core.config.gui.GuiPositionEditor;
import org.ginafro.notenoughfakepixel.features.duels.Duels;
import org.ginafro.notenoughfakepixel.features.duels.KDCounter;
import org.ginafro.notenoughfakepixel.features.mlf.Info;
import org.ginafro.notenoughfakepixel.features.mlf.Map;
import org.ginafro.notenoughfakepixel.features.skyblock.crimson.AshfangOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonsMap;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.score.ScoreOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.MiningOverlay;

public class Configuration {

    private void editOverlay(String activeConfig, int width, int height, Position position) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiPositionEditor(position, width, height, () -> {}, () -> {}, () -> NotEnoughFakepixel.screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(NotEnoughFakepixel.feature, activeConfig))));
    }

    public void executeRunnable(String runnableId) {
        String activeConfigCategory = null;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiScreenElementWrapper) {
            GuiScreenElementWrapper wrapper = (GuiScreenElementWrapper) Minecraft.getMinecraft().currentScreen;
            GuiElement element = wrapper.element;
            if (element instanceof ConfigEditor) {
                activeConfigCategory = ((ConfigEditor) element).getSelectedCategoryName();
            }
        }
        if ("editAshfangPosition".equals(runnableId)) {
                Minecraft.getMinecraft().displayGuiScreen(
                        new GuiPositionEditor(
                                org.ginafro.notenoughfakepixel.config.features.Crimson.ashfangOverlayPos,    // Position to edit
                                100,                        // Estimated width of the overlay
                                20,                         // Estimated height (2 lines, ~10px each)
                                () -> new AshfangOverlay().renderDummy(), // Render preview
                                () -> {},                   // Callback when position changes (optional)
                                () -> {}                    // Callback when editor closes (optional)
                        )
                );
        }
        if ("editDungeonsMapPosition".equals(runnableId)) {
            // Create a temporary Position object to interface with GuiPositionEditor
            Position tempPosition = new Position((int) Dungeons.dungeonsMapOffsetX, (int) Dungeons.dungeonsMapOffsetY);

            Minecraft.getMinecraft().displayGuiScreen(
                    new GuiPositionEditor(
                            tempPosition,
                            128, 128, // Map size
                            () -> new DungeonsMap().drawMap(null), // Render dummy map
                            () -> {
                                // Sync changes back to Dungeons offsets
                                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                                Dungeons.dungeonsMapOffsetX = tempPosition.getAbsX(sr, 128);
                                Dungeons.dungeonsMapOffsetY = tempPosition.getAbsY(sr, 128);
                            },
                            () -> {} // Closed callback
                    )
            );
        }
        if ("editMlfInfoPosition".equals(runnableId)) {
            Position tempPosition = new Position((int) Info.mlfInfoOffsetX, (int) Info.mlfInfoOffsetY);
            Minecraft.getMinecraft().displayGuiScreen(
                    new GuiPositionEditor(
                            tempPosition,
                            35, 60, // Info HUD size
                            () -> new Map().renderDummy(),
                            () -> {
                                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                                Info.mlfInfoOffsetX = tempPosition.getAbsX(sr, 35);
                                Info.mlfInfoOffsetY = tempPosition.getAbsY(sr, 60);
                            },
                            () -> {}
                    )
            );
        }
        if ("editKdCounterPosition".equals(runnableId)) {
            Position tempPosition = new Position((int) Duels.kdCounterOffsetX, (int) Duels.kdCounterOffsetY);
            KDCounter kdCounter = new KDCounter();
            Minecraft.getMinecraft().displayGuiScreen(
                    new GuiPositionEditor(
                            tempPosition,
                            (int) kdCounter.getWidth(), (int) kdCounter.getHeight(),
                            kdCounter::renderDummy,
                            () -> {
                                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                                Duels.kdCounterOffsetX = tempPosition.getAbsX(sr, (int) kdCounter.getWidth());
                                Duels.kdCounterOffsetY = tempPosition.getAbsY(sr, (int) kdCounter.getHeight());
                            },
                            () -> {}
                    )
            );
        }
        if ("editScoreOverlayPosition".equals(runnableId)) {
            Position tempPosition = new Position((int) Dungeons.scoreOverlayOffsetX, (int) Dungeons.scoreOverlayOffsetY);
            ScoreOverlay scoreOverlay = new ScoreOverlay();
            Minecraft.getMinecraft().displayGuiScreen(
                    new GuiPositionEditor(
                            tempPosition,
                            scoreOverlay.getWidth(Dungeons.scoreOverlayScale),
                            scoreOverlay.getHeight(Dungeons.scoreOverlayScale),
                            scoreOverlay::renderDummy,
                            () -> {
                                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                                Dungeons.scoreOverlayOffsetX = tempPosition.getAbsX(sr, scoreOverlay.getWidth(Dungeons.scoreOverlayScale));
                                Dungeons.scoreOverlayOffsetY = tempPosition.getAbsY(sr, scoreOverlay.getHeight(Dungeons.scoreOverlayScale));
                            },
                            () -> {}
                    )
            );
        }
        if ("editMiningOverlayPosition".equals(runnableId)) {
            Position tempPosition = new Position((int) Mining.miningOverlayOffsetX, (int) Mining.miningOverlayOffsetY);
            MiningOverlay miningOverlay = new MiningOverlay();
            Minecraft.getMinecraft().displayGuiScreen(
                    new GuiPositionEditor(
                            tempPosition,
                            (int) miningOverlay.getWidth(Mining.miningOverlayScale, true),
                            (int) miningOverlay.getHeight(Mining.miningOverlayScale, true),
                            miningOverlay::renderDummy,
                            () -> {
                                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                                Mining.miningOverlayOffsetX = tempPosition.getAbsX(sr, (int) miningOverlay.getWidth(Mining.miningOverlayScale, true));
                                Mining.miningOverlayOffsetY = tempPosition.getAbsY(sr, (int) miningOverlay.getHeight(Mining.miningOverlayScale, true));
                            },
                            () -> {}
                    )
            );
        }

    }

    @Expose
    @Category(name = "General", desc = "General settings.")
    public General general = new General();

    @Expose
    @Category(name = "Quality of Life", desc = "Quality of Life settings.")
    public QualityOfLife qol = new QualityOfLife();

    @Expose
    @Category(name = "Dungeons", desc = "Dungeons settings.")
    public Dungeons dungeons = new Dungeons();

    @Expose
    @Category(name = "Diana", desc = "Diana settings.")
    public DianaF diana = new DianaF();

    @Expose
    @Category(name = "Slayer", desc = "Slayer settings.")
    public Slayer slayer = new Slayer();

    @Expose
    @Category(name = "Experimentation Table", desc = "Experimentation Table settings.")
    public Experimentation experimentation = new Experimentation();

    @Expose
    @Category(name = "Chocolate Factory", desc = "Chocolate Factory settings.")
    public ChocolateFactory chocolateFactory = new ChocolateFactory();

    @Expose
    @Category(name = "Crimson", desc = "Crimson settings.")
    public Crimson crimson = new Crimson();

    @Expose
    @Category(name = "Mining", desc = "Mining settings.")
    public Mining mining = new Mining();

    @Expose
    @Category(name = "Fishing", desc = "Fishing settings.")
    public Fishing fishing = new Fishing();

    public static boolean isPojav() {
        return System.getProperty("os.name").contains("Android") || System.getProperty("os.name").contains("Linux");
    }

}
// add keybinds, search bar, config doesnt save