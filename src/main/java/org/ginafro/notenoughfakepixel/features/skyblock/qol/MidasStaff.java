package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;
import org.ginafro.notenoughfakepixel.events.PacketReadEvent;
import org.ginafro.notenoughfakepixel.utils.InventoryUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import java.util.Collection;

public class MidasStaff {

    private int grassSoundCounter = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!QualityOfLife.qolDisableMidaStaffAnimation) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (InventoryUtils.getSlot("Midas Staff") == -1) return;
        AxisAlignedBB bb = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().expand(20, 20, 20);
        Collection<EntityFallingBlock> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityFallingBlock.class, bb);
        for (EntityFallingBlock entity : entities) {
            if (entity.getBlock().toString().equals("minecraft:gold_block")) {
                if (!entity.isDead) entity.setDead();
            }
        }
    }

    @SubscribeEvent
    public void onParticlePacketReceive(PacketReadEvent event) {
        if (!QualityOfLife.qolDisableMidaStaffAnimation) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (InventoryUtils.getSlot("Midas Staff") == -1) return;
        Packet packet = event.packet;
        if (packet instanceof S2APacketParticles) {
            S2APacketParticles particle = (S2APacketParticles) packet;
            //System.out.println(particle.getParticleType().getParticleName());
            if (particle.getParticleType().getParticleName().equals("lava")) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onSoundPacketReceive(PacketReadEvent event) {
        if (!QualityOfLife.qolDisableMidaStaffAnimation) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (InventoryUtils.getSlot("Midas Staff") == -1) return;
        Packet packet = event.packet;
        if (packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;
            String soundName = soundEffect.getSoundName();
            if (soundName.equals("dig.grass")) {
                grassSoundCounter++;
                if (grassSoundCounter > 1) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void handleClick(PlayerInteractEvent event) {
        if (!QualityOfLife.qolDisableMidaStaffAnimation) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (InventoryUtils.getSlot("Midas Staff") != -1) {
            if (!(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) return; // Check if right click on air
            grassSoundCounter = 0;
            //int[] playerCoords = new int[] {Minecraft.getMinecraft().thePlayer.getPosition().getX(), Minecraft.getMinecraft().thePlayer.getPosition().getY(), Minecraft.getMinecraft().thePlayer.getPosition().getZ()};
            //SoundUtils.playSound(playerCoords, "dig.grass", 1.0f, 1.0f);
        }

    }


}
