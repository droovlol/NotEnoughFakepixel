package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.RenderEntityModelEvent;
import org.ginafro.notenoughfakepixel.utils.OutlineUtils;

import java.awt.*;
import java.util.HashMap;


@RegisterEvents
public class DragonCloseAlert {

    public BlockPos power = new BlockPos(43,25,67);
    public BlockPos apex = new BlockPos(42, 25, 102);
    public BlockPos ice = new BlockPos(85, 25, 102);
    public BlockPos flame = new BlockPos(85,25,64);
    public BlockPos soul = new BlockPos(64,25,125);
    public HashMap<EntityDragon,BlockPos[]> statueOutline = new HashMap<>();
    public EntityDragon dApex,dPower,dIce,dFlame,dSoul;
    public int maxDistance = 20;
    public boolean isFinalPhase = false;
    public HashMap<EntityDragon,Boolean> nearby = new HashMap<>();
    private final HashMap<EntityDragon, Boolean> triggerTitle = new HashMap<>();

    @SubscribeEvent
    public void chat(ClientChatReceivedEvent e){
        if(e.message.getUnformattedText().contains("WITHER KING")){
            if(e.message.getUnformattedText().contains("I have nothing left to fight for, I finally had peace.") || e.message.getUnformattedText().contains("We will decide it all, here, now.") || e.message.getUnformattedText().contains("The Catacombs... are no more")){
                isFinalPhase = true;
            }
        }
    }

    @SubscribeEvent
    public void onUnLoad(WorldEvent.Unload e){
        isFinalPhase = false;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (!isFinalPhase || !Config.feature.dungeons.distBox) return;

        if (statueOutline.isEmpty()) {
            putOutlines(dApex,apex);
            putOutlines(dPower,power);
            putOutlines(dFlame,flame);
            putOutlines(dSoul,soul);
            putOutlines(dIce,ice);
        }

        statueOutline.forEach((en, p) -> {
            OutlineUtils.renderBlockOutline(p[0], p[1], e.partialTicks, getAssociatedColor(en), 10f);
        });
    }

    private void putOutlines(EntityDragon dragon,BlockPos p) {
            BlockPos pos = new BlockPos(p.getX() - 20,p.getY() - 10,p.getZ() - 20);
            BlockPos pos1 = new BlockPos(p.getX() + 20,p.getY() + 10,p.getZ() + 20);
            statueOutline.put(dragon,new BlockPos[]{pos,pos1});
    }

    @SubscribeEvent
    public void render(RenderEntityModelEvent e) {
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (Minecraft.getMinecraft().theWorld == null) return;
        final EntityLivingBase entity = e.getEntity();
        if (isDying(entity)) return;
        if (entity.isInvisible()) return;
        boolean canSee = Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity);

        if (!canSee) {
            return;
        }
        if (entity instanceof EntityDragon) {
            if(Config.feature.dungeons.dragOutline) {
                OutlineUtils.outlineEntity(e, 15.0f, Color.green, true);
            }
            boolean currentlyNearby = entity.getDistanceSq(power) <= (maxDistance * maxDistance);
            boolean wasNearby = nearby.getOrDefault(entity, false);

            if (!wasNearby && currentlyNearby) {
                triggerTitle.put((EntityDragon) entity, true);
            }
            nearby.put((EntityDragon) entity, currentlyNearby);
        }

    }

    private void checkDragonProximity(EntityDragon dragon, BlockPos pos, String name) {
        if (dragon == null || dragon.isDead) return;

        boolean currentlyNearby = dragon.getDistanceSq(pos) <= (maxDistance * maxDistance);
        boolean wasNearby = nearby.getOrDefault(dragon, false);

        if (!wasNearby && currentlyNearby) {
            triggerTitle.put(dragon, true);
        }

        if (triggerTitle.getOrDefault(dragon, false)) {
            if (Config.feature.dungeons.dragAlert) {
                Minecraft.getMinecraft().ingameGUI.displayTitle(
                        getAssociatedEnum(dragon) + "Dragon Near Statue",
                        getAssociatedEnum(dragon) + name,
                        2, 70, 2
                );
                triggerTitle.put(dragon, false);
            }
        }

        nearby.put(dragon, currentlyNearby);
    }



    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isFinalPhase) return;
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;

        WorldClient world = Minecraft.getMinecraft().theWorld;

        for (EntityArmorStand stand : world.getEntities(EntityArmorStand.class, s -> s.getName().contains("Dragon"))) {
            Entity entity = findAssociatedMob(stand);
            if (entity instanceof EntityDragon) {
                EntityDragon dragon = (EntityDragon) entity;
                if (dragon.getName().contains("Apex")) dApex = dragon;
                if (dragon.getName().contains("Power")) dPower = dragon;
                if (dragon.getName().contains("Ice")) dIce = dragon;
                if (dragon.getName().contains("Flame")) dFlame = dragon;
                if (dragon.getName().contains("Soul")) dSoul = dragon;
            }
        }

        checkDragonProximity(dPower, power, "Power Dragon");
        checkDragonProximity(dApex, apex, "Apex Dragon");
        checkDragonProximity(dIce, ice, "Ice Dragon");
        checkDragonProximity(dFlame, flame, "Flame Dragon");
        checkDragonProximity(dSoul, soul, "Soul Dragon");
    }

    private EntityLivingBase findAssociatedMob(EntityArmorStand armorStand) {
        return armorStand.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
                        armorStand.getEntityBoundingBox().expand(1.5, 3.0, 1.5),
                        e -> e != null &&
                                !(e instanceof EntityArmorStand) &&
                                e != Minecraft.getMinecraft().thePlayer
                ).stream()
                .findFirst()
                .orElse(null);
    }

    private boolean isDying(EntityLivingBase entity) {
        if (entity == null || entity.isDead) return true;
        return entity.getHealth() <= 0.1f;
    }

    private EnumChatFormatting getAssociatedEnum(EntityDragon dragon){
        if(dragon == dApex){
            return EnumChatFormatting.GREEN;
        }
        if(dragon == dSoul){
            return EnumChatFormatting.LIGHT_PURPLE;
        }
        if(dragon == dPower){
            return EnumChatFormatting.RED;
        }
        if(dragon == dIce){
            return EnumChatFormatting.AQUA;
        }
        if(dragon == dFlame){
            return EnumChatFormatting.GOLD;
        }
        return EnumChatFormatting.WHITE;
    }

    private Color getAssociatedColor(EntityDragon dragon){
        if(dragon == dApex){
            return Color.GREEN;
        }
        if(dragon == dSoul){
            return Color.PINK;
        }
        if(dragon == dPower){
            return Color.RED;
        }
        if(dragon == dIce){
            return Color.CYAN;
        }
        if(dragon == dFlame){
            return Color.ORANGE;
        }
        return Color.WHITE;
    }

}
