package org.ginafro.notenoughfakepixel;

import com.google.gson.annotations.Expose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.ginafro.notenoughfakepixel.Alerts.AlertManagementGui;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.CustomAliases.AliasManagementGui;
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
            editOverlay(activeConfigCategory, 100, 20, NotEnoughFakepixel.feature.crimson.ashfangOverlayPos);
        }
        if ("editDungeonsMapPosition".equals(runnableId)) {
            editOverlay(activeConfigCategory, 128, 128, NotEnoughFakepixel.feature.dungeons.dungeonsMapPos);
        }
        if ("editMlfInfoPosition".equals(runnableId)) {
            Position tempPosition = new Position((int) NotEnoughFakepixel.feature.mlf.mlfInfoOffsetX, (int) NotEnoughFakepixel.feature.mlf.mlfInfoOffsetY);
            Minecraft.getMinecraft().displayGuiScreen(
                    new GuiPositionEditor(
                            tempPosition,
                            35, 60,
                            () -> new Map().renderDummy(),
                            () -> {
                                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                                NotEnoughFakepixel.feature.mlf.mlfInfoOffsetX = tempPosition.getAbsX(sr, 35);
                                NotEnoughFakepixel.feature.mlf.mlfInfoOffsetY = tempPosition.getAbsY(sr, 60);
                            },
                            () -> {}
                    )
            );
        }
        if ("editKdCounterPosition".equals(runnableId)) {
            Position tempPosition = new Position((int) NotEnoughFakepixel.feature.duels.kdCounterOffsetX, (int) NotEnoughFakepixel.feature.duels.kdCounterOffsetY);
            KDCounter kdCounter = new KDCounter();
            Minecraft.getMinecraft().displayGuiScreen(
                    new GuiPositionEditor(
                            tempPosition,
                            (int) kdCounter.getWidth(), (int) kdCounter.getHeight(),
                            kdCounter::renderDummy,
                            () -> {
                                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                                NotEnoughFakepixel.feature.duels.kdCounterOffsetX = tempPosition.getAbsX(sr, (int) kdCounter.getWidth());
                                NotEnoughFakepixel.feature.duels.kdCounterOffsetY = tempPosition.getAbsY(sr, (int) kdCounter.getHeight());
                            },
                            () -> {}
                    )
            );
        }
        if ("editScoreOverlayPosition".equals(runnableId)) {
            editOverlay(activeConfigCategory, 150, 115, NotEnoughFakepixel.feature.dungeons.scoreOverlayPos);
        }
        if ("resetItemValues".equals(runnableId)) {
            NotEnoughFakepixel.feature.qol.resetItemValues();
        }
        if ("resetSizeValues".equals(runnableId)) {
            NotEnoughFakepixel.feature.qol.resetSizeValues();
        }
        if ("editSlayerOverlayPosition".equals(runnableId)) {
            editOverlay(activeConfigCategory, 100, 20, NotEnoughFakepixel.feature.slayer.slayerBossHPPos);
        }
        if ("nefAlerts".equals(runnableId)) {
            Minecraft.getMinecraft().displayGuiScreen(new AlertManagementGui());
        }
        if("nefAlias".equals(runnableId)){
            Minecraft.getMinecraft().displayGuiScreen(new AliasManagementGui());
        }
        if("slotReset".equals(runnableId)){
            NotEnoughFakepixel.resetLockedSlots();
        }
    }

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

    @Expose
    @Category(name = "My Little Farm", desc = "Mlf settings.")
    public Info mlf = new Info();

    @Expose
    @Category(name = "Slot Locking", desc = "Slot Locking Settings")
    public SlotLocking sl = new SlotLocking();

    @Expose
    @Category(name = "Duels", desc = "Duels settings.")
    public Duels duels = new Duels();

    @Expose
    @Category(name = "Misc", desc = "Misc features.")
    public Misc misc = new Misc();

    @Expose
    @Category(name = "Overlays", desc = "GUI Overlays")
    public Overlays overlays = new Overlays();

    @Expose
    @Category(name = "Debug", desc = "Debug settings.")
    public Debug debug = new Debug();

    public static boolean isPojav() {
        return NotEnoughFakepixel.feature.debug.forcePojav || (System.getProperty("os.name").contains("Android") || System.getProperty("os.name").contains("Linux"));
    }
}