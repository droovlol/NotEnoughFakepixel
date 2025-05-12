package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Set;

@RegisterEvents
public class WardrobeShortcut {

    private final Set<Integer> activeKeySet = new HashSet<>();

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (!Config.feature.qol.qolShortcutWardrobe) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        // Check if the single key is pressed
        boolean keyPressed = Keyboard.isKeyDown(Config.feature.qol.qolWardrobeKey);

        // If the key is pressed and it's not already active
        if (keyPressed && !activeKeySet.contains(Config.feature.qol.qolWardrobeKey)) {
            // Execute the action
            player.sendChatMessage("/wardrobe");

            // Mark this key as active
            activeKeySet.add(Config.feature.qol.qolWardrobeKey);
        }

        // Clear key from activeKeySet when released
        if (!Keyboard.isKeyDown(Config.feature.qol.qolWardrobeKey)) {
            activeKeySet.remove(Config.feature.qol.qolWardrobeKey);
        }
    }

    @SubscribeEvent
    public void onKeyPressOnGui(GuiScreenEvent.KeyboardInputEvent event) {
        if (!Config.feature.qol.qolShortcutWardrobe) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!(event.gui instanceof GuiChest)) return;
        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;

        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Wardrobe")) return;

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        // Check if the single key is pressed
        boolean keyPressed = Keyboard.isKeyDown(Config.feature.qol.qolWardrobeKey);

        // If the key is pressed and it's not already active
        if (keyPressed && !activeKeySet.contains(Config.feature.qol.qolWardrobeKey)) {
            // Execute the action
            Minecraft.getMinecraft().thePlayer.closeScreen(); // Close the chest

            // Mark this key as active
            activeKeySet.add(Config.feature.qol.qolWardrobeKey);
        }

        // Clear key from activeKeySet when released
        if (!Keyboard.isKeyDown(Config.feature.qol.qolWardrobeKey)) {
            activeKeySet.remove(Config.feature.qol.qolWardrobeKey);
        }
    }
}