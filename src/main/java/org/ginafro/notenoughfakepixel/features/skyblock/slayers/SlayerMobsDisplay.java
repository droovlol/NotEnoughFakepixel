package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.RenderEntityModelEvent;
import org.ginafro.notenoughfakepixel.utils.*;
import org.ginafro.notenoughfakepixel.variables.Constants;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;

import java.awt.*;
import java.util.List;

@RegisterEvents
public class SlayerMobsDisplay {

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (Config.feature.slayer.slayerBosses) onRender(event, true);
        if (Config.feature.slayer.slayerMinibosses) onRender(event, false);
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
        Color bossColor = ColorUtils.getColor(Config.feature.slayer.slayerBossColor);
        Color minibossColor = ColorUtils.getColor(Config.feature.slayer.slayerColor);

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
        Color color = ColorUtils.getColor(Config.feature.slayer.slayerBossColor);
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

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRenderEntityModel(RenderEntityModelEvent event) {
        if (!Config.feature.slayer.slayerBossesOutline) return;

        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = event.getEntity();
        if (mc.theWorld == null || entity == null) return;

        if (entity.isInvisible()) return;

        List<Entity> armorStands = mc.theWorld.getEntitiesWithinAABB(
                EntityArmorStand.class,
                entity.getEntityBoundingBox().offset(0, 2.0, 0).expand(1.0, 1.0, 1.0)
        );

        Color bossColor = ColorUtils.getColor(Config.feature.slayer.slayerBossColor);
        Color minibossColor = ColorUtils.getColor(Config.feature.slayer.slayerColor);

        for (Entity armorStand : armorStands) {
            if (!(armorStand instanceof EntityArmorStand) || armorStand.getName() == null) continue;

            // Check for bosses
            for (String name : Constants.SLAYER_BOSSES) {
                if (armorStand.getName().contains(name) && Config.feature.slayer.slayerBosses) {
                    if (Configuration.isPojav()) {
                        EntityHighlightUtils.renderEntityOutline(event, bossColor);
                    } else {
                        OutlineUtils.outlineEntity(event, 6.0f, bossColor, true);
                    }
                    return;
                }
            }

            // Check for all minibosses
            for (String[] minibosses : new String[][]{
                    Constants.SVEN_SLAYER_MINIBOSSES,
                    Constants.REVENANT_SLAYER_MINIBOSSES,
                    Constants.TARANTULA_SLAYER_MINIBOSSES,
                    Constants.VOIDGLOOM_SLAYER_MINIBOSSES,
                    Constants.BLAZE_SLAYER_MINIBOSSES
            }) {
                for (String name : minibosses) {
                    if (armorStand.getName().contains(name) && Config.feature.slayer.slayerMinibosses) {
                        if (Configuration.isPojav()) {
                            EntityHighlightUtils.renderEntityOutline(event, minibossColor);
                        } else {
                            OutlineUtils.outlineEntity(event, 6.0f, minibossColor, true);
                        }
                        return;
                    }
                }
            }
        }
    }
}