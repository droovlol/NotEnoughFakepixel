package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Set;

@RegisterEvents
public class WarpsShortcut {

    private final Set<Integer> activeKeySet = new HashSet<>();

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (!Config.feature.qol.qolShortcutWarps) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        checkWarp(player, Config.feature.qol.qolShortcutWarpIs, "/warp is");
        checkWarp(player, Config.feature.qol.qolShortcutWarpHub, "/warp hub");
        checkWarp(player, Config.feature.qol.qolShortcutWarpDh, "/warp dh");
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