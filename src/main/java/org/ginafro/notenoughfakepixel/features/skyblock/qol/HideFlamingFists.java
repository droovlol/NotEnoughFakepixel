package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HideFlamingFists {

    private static final UUID TARGET_UUID = UUID.fromString("eaf71309-6ae5-3bf3-bf7f-51a579bbc6ee");
    private static final Set<EntityArmorStand> trackedStands = new HashSet<>();
    private static int checkTimer = 0;

    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event) {
        if (!NotEnoughFakepixel.feature.qol.qolHideFlamingFists || !ScoreboardUtils.currentGamemode.isSkyblock()) return;

        if (event.entity instanceof EntityArmorStand) {
            trackedStands.add((EntityArmorStand) event.entity);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!NotEnoughFakepixel.feature.qol.qolHideFlamingFists || event.phase != TickEvent.Phase.END) return;

        if (++checkTimer >= 5) {
            checkTimer = 0;
            checkArmorStands();
        }
    }

    private void checkArmorStands() {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            trackedStands.removeIf(stand -> stand.isDead || !stand.isEntityAlive());

            for (EntityArmorStand stand : trackedStands) {
                ItemStack head = stand.getEquipmentInSlot(4);
                if (isTargetSkull(head)) {
                    stand.setInvisible(true);
                }
            }
        });
    }

    // Unified skull detection method
    public static boolean isTargetSkull(ItemStack stack) {
        if (stack == null || stack.getItem() != Items.skull || stack.getItemDamage() != 3) return false;

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) return false;

        String uuidString = null;
        if (nbt.hasKey("SkullOwner", 10)) {
            NBTTagCompound skullOwner = nbt.getCompoundTag("SkullOwner");
            uuidString = skullOwner.getString("Id");
        } else if (nbt.hasKey("SkullOwner", 8)) {
            uuidString = nbt.getString("SkullOwner");
        }

        try {
            return uuidString != null && UUID.fromString(uuidString).equals(TARGET_UUID);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @SubscribeEvent
    public void onRenderArmorStand(RenderLivingEvent.Pre<?> event) {
        if (!NotEnoughFakepixel.feature.qol.qolHideFlamingFists || !(event.entity instanceof EntityArmorStand)) return;

        EntityArmorStand stand = (EntityArmorStand) event.entity;
        if (isTargetSkull(stand.getEquipmentInSlot(4))) {
            event.setCanceled(true);
        }
    }
}