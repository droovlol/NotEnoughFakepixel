package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Set;

public class WarpsShortcut {

    private final Set<Integer> activeKeySet = new HashSet<>();

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (!NotEnoughFakepixel.feature.qol.qolShortcutWarps) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        checkWarp(player, NotEnoughFakepixel.feature.qol.qolShortcutWarpIs, "/warp is");
        checkWarp(player, NotEnoughFakepixel.feature.qol.qolShortcutWarpHub, "/warp hub");
        checkWarp(player, NotEnoughFakepixel.feature.qol.qolShortcutWarpDh, "/warp dh");
    }

    private void checkWarp(EntityPlayerSP player, int keyBind, String command) {
        // Validate keybind
        if (keyBind < 0 || keyBind >= Keyboard.KEYBOARD_SIZE) {
            return;
        }

        // Check if the key is currently pressed
        boolean keyPressed = Keyboard.isKeyDown(keyBind);

        // If the key is pressed and not already active
        if (keyPressed && !activeKeySet.contains(keyBind)) {
            // Execute the action
            player.sendChatMessage(command);

            // Mark this key as active
            activeKeySet.add(keyBind);
        }

        // Clear key from activeKeySet when released
        if (!Keyboard.isKeyDown(keyBind)) {
            activeKeySet.remove(keyBind);
        }
    }
}