package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.Debug;
import org.ginafro.notenoughfakepixel.variables.Constants;

public class SoundUtils {

    public static void playSound(int[] cords, String sound, float volume, float pitch) {
        playSound(cords, sound, volume, pitch, 1, 0);
    }

    public static void playSound(BlockPos pos, String sound, float volume, float pitch) {
        int[] position = new int[]{pos.getX(), pos.getY(), pos.getZ()};
        playSound(position, sound, volume, pitch, 1, 0);
    }

    public static void playSound(float x, float y, float z, String sound, float volume, float pitch) {
        ResourceLocation res = new ResourceLocation(sound);
        if (Minecraft.getMinecraft().getSoundHandler() == null) {
            return;
        }
        if (NotEnoughFakepixel.feature.debug.debug && NotEnoughFakepixel.feature.debug.showSounds){
            Logger.log( EnumChatFormatting.WHITE + "Playing sound: " + sound + " at " + x + ", " + y + ", " + z + " with volume " + volume + " and pitch " + pitch);
        }

        Minecraft.getMinecraft().getSoundHandler().playSound(
                new net.minecraft.client.audio.PositionedSoundRecord(
                        res,
                        volume,
                        pitch,
                        x,
                        y,
                        z
                )
        );
    }

    public static void playSound(int[] cords, String sound, float volume, float pitch, int times, int delay) {
        for (int i = 0; i < times; i++) {
            playSound(cords[0], cords[1], cords[2], sound, volume, pitch);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // ignored
            }
        }
    }
}
