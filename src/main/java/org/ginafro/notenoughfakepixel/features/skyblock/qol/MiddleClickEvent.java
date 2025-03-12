package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.config.features.Dungeons;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;
import org.lwjgl.input.Mouse;

import java.util.Arrays;
import java.util.List;

public class MiddleClickEvent {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static List<String> chestNames = Arrays.asList(
            "Experimentation Table",
            "Chronomatron",
            "Ultrasequencer",
            "Superpairs"
    );

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!QualityOfLife.qolMiddleClickChests) return;
        if (Mouse.getEventButton() != 0 || !Mouse.getEventButtonState()) return;

        if (!(mc.currentScreen instanceof GuiChest)) return;

        GuiChest chestGui = (GuiChest) mc.currentScreen;
        ContainerChest container = (ContainerChest) chestGui.inventorySlots;

        String currentChestName = container.getLowerChestInventory().getDisplayName().getUnformattedText();

        if (!Dungeons.dungeonsCustomGuiStartsWith) {
                if (currentChestName.startsWith("What starts with '")) {
                    event.setCanceled(true);

                    int slot = chestGui.getSlotUnderMouse() != null ? chestGui.getSlotUnderMouse().slotNumber : -1;

                    if (slot >= 0) {
                        mc.playerController.windowClick(
                                container.windowId,  // The window ID of the chest
                                slot,               // Slot clicked
                                2,                  // Middle-click (button 2)
                                3,                  // Click type (3 is PICKUP_ALL for middle-click)
                                mc.thePlayer        // Player entity
                        );
                    }
                }

        }
        if (!Dungeons.dungeonsCustomGuiClickIn) {
                if (currentChestName.startsWith("Click in order!")) {
                    event.setCanceled(true);

                    int slot = chestGui.getSlotUnderMouse() != null ? chestGui.getSlotUnderMouse().slotNumber : -1;

                    if (slot >= 0) {
                        mc.playerController.windowClick(
                                container.windowId,  // The window ID of the chest
                                slot,               // Slot clicked
                                2,                  // Middle-click (button 2)
                                3,                  // Click type (3 is PICKUP_ALL for middle-click)
                                mc.thePlayer        // Player entity
                        );
                    }
                }
        }
        if (!Dungeons.dungeonsCustomGuiColors) {
                if (currentChestName.startsWith("Select all the ")) {
                    event.setCanceled(true);

                    int slot = chestGui.getSlotUnderMouse() != null ? chestGui.getSlotUnderMouse().slotNumber : -1;

                    if (slot >= 0) {
                        mc.playerController.windowClick(
                                container.windowId,  // The window ID of the chest
                                slot,               // Slot clicked
                                2,                  // Middle-click (button 2)
                                3,                  // Click type (3 is PICKUP_ALL for middle-click)
                                mc.thePlayer        // Player entity
                        );
                    }
                }
        }
        if (!Dungeons.dungeonsCustomGuiPanes) {
                if (currentChestName.startsWith("Correct all the panes!")) {
                    event.setCanceled(true);

                    int slot = chestGui.getSlotUnderMouse() != null ? chestGui.getSlotUnderMouse().slotNumber : -1;

                    if (slot >= 0) {
                        mc.playerController.windowClick(
                                container.windowId,  // The window ID of the chest
                                slot,               // Slot clicked
                                2,                  // Middle-click (button 2)
                                3,                  // Click type (3 is PICKUP_ALL for middle-click)
                                mc.thePlayer        // Player entity
                        );
                    }
            }
        }

        for (String chestName : chestNames){
            if (currentChestName.startsWith(chestName)) {

                event.setCanceled(true);

                int slot = chestGui.getSlotUnderMouse() != null ? chestGui.getSlotUnderMouse().slotNumber : -1;

                if (slot >= 0) {
                    mc.playerController.windowClick(
                            container.windowId,  // The window ID of the chest
                            slot,               // Slot clicked
                            2,                  // Middle-click (button 2)
                            3,                  // Click type (3 is PICKUP_ALL for middle-click)
                            mc.thePlayer        // Player entity
                    );
                }
            }
        }
    }
}
