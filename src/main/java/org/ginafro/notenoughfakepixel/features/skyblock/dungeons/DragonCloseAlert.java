package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.RenderEntityModelEvent;
import org.ginafro.notenoughfakepixel.utils.*;
import org.ginafro.notenoughfakepixel.variables.DungeonFloor;
import org.ginafro.notenoughfakepixel.variables.Location;
import org.ginafro.notenoughfakepixel.variables.Skins;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RegisterEvents
public class DragonCloseAlert {

    @Getter
    public static DragonCloseAlert INSTANCE;

    public DragonCloseAlert() {
        INSTANCE = this;
    }

    @AllArgsConstructor
    @Data
    private static class Orb {
        private final BlockPos pos;
        private final Skins skin;
        private final Color color;
    }

    private static final List<Orb> ORBS = Arrays.asList(
            new Orb(new BlockPos(43,6,64), Skins.RED_RELIC, Color.RED),
            new Orb(new BlockPos(43,6,102), Skins.GREEN_RELIC, Color.GREEN),
            new Orb(new BlockPos(85, 6, 102), Skins.BLUE_RELIC, Color.CYAN),
            new Orb(new BlockPos(85, 6, 64), Skins.ORANGE_RELIC, Color.ORANGE),
            new Orb(new BlockPos(64, 6, 125), Skins.PURPLE_RELIC, Color.PINK)
    );

    private static String displayText = "";
    private static long endTime = 0;

    private static final Map<EntityDragon, Color> DRAGON_COLOR_MAP = new HashMap<>();
    private static final Map<EntityDragon, String> DRAGON_HEALTH_MAP = new HashMap<>();

    public static final Map<String, Color> DRAGON_COLORS = MapUtils.mapOf(
            new MapUtils.Pair<>("Apex Dragon", Color.GREEN),
            new MapUtils.Pair<>("Flame Dragon", Color.ORANGE),
            new MapUtils.Pair<>("Power Dragon", Color.RED),
            new MapUtils.Pair<>("Soul Dragon", Color.PINK),
            new MapUtils.Pair<>("Ice Dragon", Color.CYAN)
    );

    public boolean isFinalPhase = false;
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chat(ClientChatReceivedEvent e) {
        if(e.message.getUnformattedText().contains("WITHER KING") &&
                (e.message.getUnformattedText().contains("I have nothing left to fight for, I finally had peace.") ||
                        e.message.getUnformattedText().contains("We will decide it all, here, now."))) {
            isFinalPhase = true;
        }
    }

    @SubscribeEvent
    public void onUnLoad(WorldEvent.Unload e) {
        isFinalPhase = false;
        DRAGON_COLOR_MAP.clear();
        DRAGON_HEALTH_MAP.clear();
    }

    public List<EntityDragon> getDragonsByColor(Color color) {
        return DRAGON_COLOR_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().equals(color))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void registerDragon(EntityDragon dragon, String health) {
        DRAGON_HEALTH_MAP.put(dragon, health);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if (ScoreboardUtils.currentLocation != Location.DUNGEON ||
                ScoreboardUtils.currentFloor != DungeonFloor.M7) return;

        DRAGON_COLOR_MAP.keySet().removeIf(this::isDying);
        DRAGON_HEALTH_MAP.keySet().removeIf(this::isDying);

        renderBoxes(e);
        renderDragonDistance(e);
    }

    private void renderDragonDistance(RenderWorldLastEvent e) {
        ORBS.forEach(orb -> {
            BlockPos pos = orb.getPos().add(0, 20, 0);

            for (EntityDragon dragon : DRAGON_COLOR_MAP.keySet()) {
                if (isDying(dragon)) continue;

                Color color = DRAGON_COLOR_MAP.get(dragon);
                if (color == null || !color.equals(orb.getColor())) continue;

                // Render health on dragon
                String health = DRAGON_HEALTH_MAP.get(dragon);
                if (health != null && !health.isEmpty()) {
                    RenderUtils.renderWaypointText(health, dragon.getPosition(), e.partialTicks, false);
                }

                // Proximity warning logic
                double distance = new Vec3(pos.getX(), pos.getY(), pos.getZ())
                        .distanceTo(new Vec3(dragon.posX, dragon.posY, dragon.posZ));

                if (distance < 20) {
                    String dragonName = DRAGON_COLORS.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(color))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("Dragon");

                    //showCustomOverlay(dragonName, 2000);
                    //SoundUtils.playSound(mc.thePlayer.getPosition(), "note.pling", 2.0F, 1.0F);
                }
            }
        });
    }

    private void showCustomOverlay(String text, int durationMillis) {
        displayText = text;
        endTime = System.currentTimeMillis() + durationMillis;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (System.currentTimeMillis() > endTime) return;

        FontRenderer fr = mc.fontRendererObj;

        int screenWidth = event.resolution.getScaledWidth();
        int screenHeight = event.resolution.getScaledHeight();

        GlStateManager.pushMatrix();
        GlStateManager.scale(4.0F, 4.0F, 4.0F);
        int textWidth = fr.getStringWidth(displayText);
        int x = (screenWidth / 8) - (textWidth / 2);
        int y = (screenHeight / 8) - 10;
        fr.drawStringWithShadow(displayText, x, y, 0xFF5555);
        GlStateManager.popMatrix();
    }

    private void renderBoxes(RenderWorldLastEvent e) {
        drawDragonBox(e);

        ORBS.forEach(orb -> {
            Color color = orb.getColor();
            BlockPos position = orb.getPos().add(0, 20, 0);
            RenderUtils.renderBoxAtCoords(
                    position.getX() - 21, position.getY() -11, position.getZ() -11,
                    position.getX() + 11, position.getY() + 11, position.getZ() + 13,
                    e.partialTicks, color, false
            );
        });
    }

    private void drawDragonBox(RenderWorldLastEvent e) {
        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityArmorStand) {
                EntityArmorStand stand = (EntityArmorStand) entity;
                ItemStack skull = stand.getCurrentArmor(3);
                if (skull == null || skull.getItem() == null || !skull.getItem().getUnlocalizedName().contains("skull"))
                    return;

                String texture = ItemUtils.getSkullTexture(skull);
                if (texture == null || texture.isEmpty()) return;

                List<Skins> skins = ORBS.stream().map(Orb::getSkin).collect(Collectors.toList());
                Skins skin = Skins.getSkinByValue(texture);
                if (skin == null || !skins.contains(skin)) return;

                Color color = ORBS.stream()
                        .filter(orb -> orb.getSkin().equals(skin))
                        .map(Orb::getColor)
                        .findFirst()
                        .orElse(Color.WHITE);

                EntityLivingBase entityLiving = stand.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
                                stand.getEntityBoundingBox().expand(1.5, 3.0, 1.5),
                                e1 -> e1 instanceof EntityDragon && !e1.isDead && e1 != mc.thePlayer)
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (entityLiving instanceof EntityDragon) {
                    EntityDragon dragon = (EntityDragon) entityLiving;
                    if (isDying(dragon)) return;
                    DRAGON_COLOR_MAP.put(dragon, color);
                }
            }
        });
    }

    @SubscribeEvent
    public void render(RenderEntityModelEvent e) {
        EntityLivingBase entity = e.getEntity();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!(entity instanceof EntityDragon) || entity.isInvisible() || isDying(entity))
            return;

        EntityDragon dragon = (EntityDragon) entity;
        if (dragon.isDead || dragon.getHealth() <= 0.1f) return;

        Color c = DRAGON_COLOR_MAP.get(dragon);
        if (c != null) {
            OutlineUtils.outlineEntity(e, 4f, c, true);
        }
    }

    private boolean isDying(EntityLivingBase entity) {
        return entity == null || entity.isDead || entity.getHealth() <= 0.1f;
    }
}