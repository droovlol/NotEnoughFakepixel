package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.util.BlockPos;
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

    public static void playGlobalSound(String sound, float volume, float pitch) {
        if (!NotEnoughFakepixel.feature.misc.enableSounds) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getSoundHandler() == null) return;

        mc.addScheduledTask(() -> {
            float oldLevel = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER);
            Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MASTER, 1);
            mc.getSoundHandler().playSound(
                    PositionedSoundRecord.create(new ResourceLocation(sound), pitch)
            );
            Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MASTER, oldLevel);
        });
    }
}
