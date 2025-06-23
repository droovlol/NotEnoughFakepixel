package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.RenderEntityModelEvent;
import org.ginafro.notenoughfakepixel.utils.*;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RegisterEvents
public class BlazeAttunements {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRenderEntityModel(RenderEntityModelEvent event) {
        if (!Config.feature.slayer.slayerBlazeAttunements) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock() || !ScoreboardUtils.currentLocation.isCrimson()) return;

        Entity entity = event.getEntity();
        // Find the armor stand above the entity
        List<Entity> armorStands = mc.theWorld.getEntitiesWithinAABB(
                EntityArmorStand.class,
                entity.getEntityBoundingBox().offset(0, 2.0, 0).expand(1.0, 1.0, 1.0)
        );

        for (Entity armorStand : armorStands) {
            if (armorStand instanceof EntityArmorStand) {
                String displayName = ((EntityArmorStand) armorStand).getDisplayName().getUnformattedText();
                Matcher matcher = COLOR_PATTERN.matcher(displayName);
                if (matcher.find()) {
                    String attunement = matcher.group().toUpperCase();

                    boolean isValidEntity = entity instanceof EntityBlaze ||
                            entity instanceof EntityPigZombie ||
                            (entity instanceof EntitySkeleton && ((EntitySkeleton) entity).getSkeletonType() == 1);

                    if (isValidEntity) {
                        boolean allowed = false;
                        if (entity instanceof EntitySkeleton && ((EntitySkeleton) entity).getSkeletonType() == 1) { // Wither Skeleton
                            allowed = attunement.equals("SPIRIT") || attunement.equals("CRYSTAL");
                        } else if (entity instanceof EntityPigZombie) { // Pigman
                            allowed = attunement.equals("ASHEN") || attunement.equals("AURIC");
                        } else if (entity instanceof EntityBlaze) {
                            allowed = true;
                        }

                        if (allowed && entity.equals(event.getEntity())) {
                            Color color = new Color(getColorForAttunement(attunement));
                            if (Configuration.isPojav()) {
                                EntityHighlightUtils.renderEntityOutline(event, color);
                            } else {
                                OutlineUtils.outlineEntity(event, 6.0f, color, true);
                            }
                        }
                    }
                }
            }
        }
    }

    private static final Pattern COLOR_PATTERN = Pattern.compile("ASHEN|SPIRIT|CRYSTAL|AURIC");

    private static final Map<String, Integer> ATTUNEMENT_COLORS = MapUtils.mapOf(
            MapUtils.Pair.of("ASHEN", Color.DARK_GRAY.getRGB()),
            MapUtils.Pair.of("SPIRIT", Color.WHITE.getRGB()),
            MapUtils.Pair.of("CRYSTAL", Color.CYAN.getRGB()),
            MapUtils.Pair.of("AURIC", Color.YELLOW.getRGB())

    );

    private static int getColorForAttunement(String attunement) {
        return ATTUNEMENT_COLORS.getOrDefault(attunement, -1);
    }
}