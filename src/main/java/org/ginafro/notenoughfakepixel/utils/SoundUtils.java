package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

public class SoundUtils {

    public static void playSound(int[] cords, String sound, float volume, float pitch) {
        playSound(cords[0], cords[1], cords[2], sound, volume, pitch);
    }

    public static void playSound(BlockPos pos, String sound, float volume, float pitch) {
        playSound(pos.getX(), pos.getY(), pos.getZ(), sound, volume, pitch);
    }

    public static void playSound(float x, float y, float z, String sound, float volume, float pitch) {
        if (!NotEnoughFakepixel.feature.misc.enableSounds) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        try {
            if (NotEnoughFakepixel.feature.debug.debug && NotEnoughFakepixel.feature.debug.showSounds) {
                Logger.log(EnumChatFormatting.WHITE + "Playing sound: " + sound + " at " + x + ", " + y + ", " + z + " with volume " + volume + " and pitch " + pitch);
            }

            mc.thePlayer.playSound(sound, volume, pitch);

        } catch (Exception e) {
            if (NotEnoughFakepixel.feature.debug.debug) {
                Logger.log(EnumChatFormatting.RED + "Failed to play sound: " + sound + ". Error " + e.getMessage());
            }
        }
    }

    public static void playSound(int[] cords, String sound, float volume, float pitch, int times, int delay) {
        for (int i = 0; i < times; i++) {
            playSound(cords[0], cords[1], cords[2], sound, volume, pitch);
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    // ignored
                }
            }
        }
    }
}