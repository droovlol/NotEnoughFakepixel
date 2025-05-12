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
public class PetsShortcut {
    private final Set<Integer> activeKeySet = new HashSet<>();

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (!Config.feature.qol.qolShortcutPets) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        // Get the single key bind
        int keyBind = Config.feature.qol.qolPetsKey;

        // Check if the key is currently pressed
        boolean keyPressed = Keyboard.isKeyDown(keyBind);

        // If the key is pressed and not already active
        if (keyPressed && !activeKeySet.contains(keyBind)) {
            // Execute the action
            player.sendChatMessage("/pets");

            // Mark this key as active
            activeKeySet.add(keyBind);
        }

        // Clear key from activeKeySet when released
        if (!Keyboard.isKeyDown(keyBind)) {
            activeKeySet.remove(keyBind);
        }
    }

    @SubscribeEvent
    public void onKeyPressOnGui(GuiScreenEvent.KeyboardInputEvent event) {
        if (!Config.feature.qol.qolShortcutPets) return; // Assuming this should check pets, not wardrobe
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!(event.gui instanceof GuiChest)) return;
        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;

        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Pets")) return;

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        // Get the single key bind
        int keyBind = Config.feature.qol.qolPetsKey;

        // Check if the key is currently pressed
        boolean keyPressed = Keyboard.isKeyDown(keyBind);

        // If the key is pressed and not already active
        if (keyPressed && !activeKeySet.contains(keyBind)) {
            // Execute the action
            Minecraft.getMinecraft().thePlayer.closeScreen(); // Close the chest

            // Mark this key as active
            activeKeySet.add(keyBind);
        }

        // Clear key from activeKeySet when released
        if (!Keyboard.isKeyDown(keyBind)) {
            activeKeySet.remove(keyBind);
        }
    }
}