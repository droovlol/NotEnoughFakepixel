package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.Logger;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RegisterEvents
public class WardrobeShortcut {

    private final Set<Integer> activeKeySet = new HashSet<>();

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (!Config.feature.qol.qolShortcutWardrobe) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        boolean keyPressed = Keyboard.isKeyDown(Config.feature.qol.qolWardrobeKey);

        if (keyPressed && !activeKeySet.contains(Config.feature.qol.qolWardrobeKey)) {
            player.sendChatMessage("/wardrobe");

            activeKeySet.add(Config.feature.qol.qolWardrobeKey);
        }

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

        boolean keyPressed = Keyboard.isKeyDown(Config.feature.qol.qolWardrobeKey);

        if (keyPressed && !activeKeySet.contains(Config.feature.qol.qolWardrobeKey)) {
            Minecraft.getMinecraft().thePlayer.closeScreen();

            activeKeySet.add(Config.feature.qol.qolWardrobeKey);
        }

        if (!Keyboard.isKeyDown(Config.feature.qol.qolWardrobeKey)) {
            activeKeySet.remove(Config.feature.qol.qolWardrobeKey);
        }
    }

    private static final int[] SLOTS = {
            36, 37, 38, 39, 40, 41, 42, 43, 44
    };

    private boolean isWardrobeOpen = false;
    private int currentWindowId = -1;
    private final boolean[] keyPressedState = new boolean[9];

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!(event.gui instanceof GuiChest)) {
            reset();
            return;
        }

        ContainerChest container = (ContainerChest) ((GuiChest) event.gui).inventorySlots;
        IInventory chest = container.getLowerChestInventory();
        String chestName = chest.getDisplayName().getUnformattedText();


        if (chestName.startsWith("Wardrobe")) {
            isWardrobeOpen = true;
            currentWindowId = container.windowId;
        } else {
            reset();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!isWardrobeOpen) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (!ScoreboardUtils.currentGamemode.isSkyblock()) {
            return;
        }

        if (!Config.feature.qol.qolShortcutWardrobe) {
            return;
        }

        if (mc.currentScreen == null || !(mc.currentScreen instanceof GuiChest)) {
            reset();
            return;
        }

        for (int slot = 1; slot <= 9; slot++) {
            int keyCode = getKeyCodeForSlot(slot);
            boolean isKeyPressed = Keyboard.isKeyDown(keyCode);
            int slotIndex = slot - 1;

            if (isKeyPressed && !keyPressedState[slotIndex]) {
                clickWardrobeSlot(slotIndex);
                keyPressedState[slotIndex] = true;
            } else if (!isKeyPressed) {
                keyPressedState[slotIndex] = false;
            }
        }
    }

    private int getKeyCodeForSlot(int slot) {
        switch (slot) {
            case 1: return Config.feature.qol.qolWardrobeKey1;
            case 2: return Config.feature.qol.qolWardrobeKey2;
            case 3: return Config.feature.qol.qolWardrobeKey3;
            case 4: return Config.feature.qol.qolWardrobeKey4;
            case 5: return Config.feature.qol.qolWardrobeKey5;
            case 6: return Config.feature.qol.qolWardrobeKey6;
            case 7: return Config.feature.qol.qolWardrobeKey7;
            case 8: return Config.feature.qol.qolWardrobeKey8;
            case 9: return Config.feature.qol.qolWardrobeKey9;
            default:
                return Keyboard.KEY_NONE;
        }
    }

    private void clickWardrobeSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= SLOTS.length) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.playerController == null || mc.thePlayer == null) {
            return;
        }

        int targetSlot = SLOTS[slotIndex];

        mc.playerController.windowClick(
                currentWindowId,
                targetSlot,
                0,
                0,
                mc.thePlayer
        );
        mc.playerController.windowClick(
                currentWindowId,
                targetSlot,
                0,
                0,
                mc.thePlayer
        );
    }

    private void reset() {
        isWardrobeOpen = false;
        currentWindowId = -1;
        for (int i = 0; i < keyPressedState.length; i++) {
            keyPressedState[i] = false;
        }
    }
}