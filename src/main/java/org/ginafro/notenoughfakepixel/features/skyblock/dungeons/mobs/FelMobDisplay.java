package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.mobs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.Dungeons;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.ItemUtils;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;
import org.ginafro.notenoughfakepixel.variables.Skins;

import java.awt.*;

public class FelMobDisplay {

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsFelMob) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;

        WorldClient world = Minecraft.getMinecraft().theWorld;

        world.loadedEntityList.forEach(entity -> {
            if (entity == null) return;
            if (entity.getName() == null) return;
            if (entity instanceof EntityArmorStand){
                EntityArmorStand armorStand = (EntityArmorStand) entity;
                // checking if armor stand have a helmet
                if (armorStand.getEquipmentInSlot(4) == null) return;
                if (armorStand.getEquipmentInSlot(4).getTagCompound() == null) return;

                ItemStack head = armorStand.getEquipmentInSlot(4);
                if(ItemUtils.hasSkinValue(Skins.ENDERMAN_HEAD.getSkin(), head)){
                    Color color = new Color(
                            ColorUtils.getColor(NotEnoughFakepixel.feature.dungeons.dungeonsFelColor).getRGB()
                    );

                    RenderUtils.renderEntityHitbox(
                            entity,
                            event.partialTicks,
                            color,
                            MobDisplayTypes.FEL
                    );
                }

            }

//            Color color = new Color(
//                    Configuration.starredBoxColor.getRed(),
//                    Configuration.starredBoxColor.getGreen(),
//                    Configuration.starredBoxColor.getBlue(),
//                    Configuration.starredBoxColor.getAlpha()
//            );
//
//            RenderUtils.renderEntityHitbox(
//                    entity,
//                    event.partialTicks,
//                    color
//            );

        });
    }


}
