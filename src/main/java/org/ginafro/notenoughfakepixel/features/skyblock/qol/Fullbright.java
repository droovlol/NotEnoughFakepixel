package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;

public class Fullbright {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final float BRIGHTEST = 1.0f;
    private static final float FULLBRIGHT = 10000.0f;

    @SubscribeEvent
    public void onRender(RenderHandEvent event) {
        if(NotEnoughFakepixel.feature.qol.qolFullbright) changeBrightness(FULLBRIGHT);
        else changeBrightness(BRIGHTEST);
    }

    private void changeBrightness(float toLevel) {
        float moveBy = toLevel - mc.gameSettings.gammaSetting;
        if (moveBy == 0) return;
        mc.gameSettings.gammaSetting += moveBy;
        mc.gameSettings.saveOptions();
    }
}
