package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.config.gui.core.ChromaColour;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Constants;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;
import org.ginafro.notenoughfakepixel.config.features.Slayer;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Arrays;

public class SlayerMobsDisplay {

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (Slayer.slayerBosses) onRender(event, true);
        if (Slayer.slayerMinibosses) onRender(event, false);
    }

    private void onRender(RenderWorldLastEvent event, boolean isBoss) {
        switch (ScoreboardUtils.currentLocation) {
            case HUB:
            case PRIVATE_HUB:
                showHitboxHub(event.partialTicks);
                break;
            case PARK:
                showHitbox(MobDisplayTypes.WOLF, event.partialTicks, Constants.SVEN_SLAYER_MINIBOSSES, isBoss);
                break;
            case SPIDERS_DEN:
                showHitbox(MobDisplayTypes.SPIDER, event.partialTicks, Constants.TARANTULA_SLAYER_MINIBOSSES, isBoss);
                break;
            case THE_END:
                showHitbox(MobDisplayTypes.ENDERMAN, event.partialTicks, Constants.VOIDGLOOM_SLAYER_MINIBOSSES, isBoss);
                break;
            case CRIMSON_ISLE:
                showHitbox(MobDisplayTypes.NONE, event.partialTicks, Constants.BLAZE_SLAYER_MINIBOSSES, isBoss);
                break;
        }
    }

    private void showHitboxHub(float partialTicks) {
        Color bossColor = ColorUtils.getColor(Slayer.slayerBossColor);
        Color minibossColor = ColorUtils.getColor(Slayer.slayerColor);

        WorldClient world = Minecraft.getMinecraft().theWorld;
        world.loadedEntityList.forEach(entity -> {
            if (entity == null || entity.getName() == null) return;
            if (!(entity instanceof EntityArmorStand)) return;

            // Check for bosses
            for (String name : Constants.SLAYER_BOSSES) {
                if (entity.getName().contains(name)) {
                    MobDisplayTypes type = entity.getName().contains("Sven Packmaster") ? MobDisplayTypes.WOLF : MobDisplayTypes.NONE;
                    RenderUtils.renderEntityHitbox(entity, partialTicks, bossColor, type);
                    return;
                }
            }

            // Check for Sven minibosses
            for (String name : Constants.SVEN_SLAYER_MINIBOSSES) {
                if (entity.getName().contains(name)) {
                    RenderUtils.renderEntityHitbox(entity, partialTicks, minibossColor, MobDisplayTypes.WOLF);
                    return;
                }
            }

            // Check for Revenant minibosses
            for (String name : Constants.REVENANT_SLAYER_MINIBOSSES) {
                if (entity.getName().contains(name)) {
                    RenderUtils.renderEntityHitbox(entity, partialTicks, minibossColor, MobDisplayTypes.NONE);
                    return;
                }
            }
        });
    }

    private void showHitbox(MobDisplayTypes type, float partialTicks, String[] namesList, boolean isBoss) {
        Color color = ColorUtils.getColor(Slayer.slayerBossColor);
        WorldClient world = Minecraft.getMinecraft().theWorld;
        world.loadedEntityList.forEach(entity -> {
            if (entity == null || entity.getName() == null) return;
            if (!(entity instanceof EntityArmorStand)) return;

            String[] names = isBoss ? Constants.SLAYER_BOSSES : namesList;
            for (String name : names) {
                if (entity.getName().contains(name)) {
                    RenderUtils.renderEntityHitbox(entity, partialTicks, color, type);
                }
            }
        });
    }
}