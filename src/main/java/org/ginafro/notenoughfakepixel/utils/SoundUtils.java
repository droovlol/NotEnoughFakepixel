package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

public class SoundUtils {

    public static void playSound(int[] cords, String sound, float volume, float pitch) {
        playGlobalSound(sound, volume, pitch);
    }

    public static void playSound(BlockPos pos, String sound, float volume, float pitch) {
        playGlobalSound(sound, volume, pitch);
    }

    public static void playSound(int x, int y, int z, String sound, float volume, float pitch) {
        playGlobalSound(sound, volume, pitch);
    }

    public static void playSound(int[] cords, String sound, float volume, float pitch, int times, int delay) {
        for (int i = 0; i < times; i++) {
            playGlobalSound(sound, volume, pitch);
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    // ignored
                }
            }
        }
    }

    public static void playGlobalSound(String sound, float volume, float pitch) {
        if (!NotEnoughFakepixel.feature.misc.enableSounds) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.getSoundHandler() == null) return;

        try {
            if (NotEnoughFakepixel.feature.debug.debug && NotEnoughFakepixel.feature.debug.showSounds) {
                Logger.log(EnumChatFormatting.YELLOW + "Playing global sound: " + sound + " with volume " + volume + " and pitch " + pitch);
            }

            mc.getSoundHandler().playSound(
                    PositionedSoundRecord.create(new ResourceLocation(sound), pitch)
            );
        } catch (Exception e) {
            if (NotEnoughFakepixel.feature.debug.debug) {
                Logger.log(EnumChatFormatting.RED + "Failed to play global sound: " + sound + ". Error " + e.getMessage());
            }
        }
    }
}
