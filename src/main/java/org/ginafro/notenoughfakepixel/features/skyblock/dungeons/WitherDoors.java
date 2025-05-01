package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WitherDoors {
    private Minecraft mc = Minecraft.getMinecraft();
    private List<BlockPos> allDoors = new ArrayList<>();
    private Set<BlockPos> activeDoors = new HashSet<>();
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 1000;

    private List<BlockPos> findDoors() {
        if (!ScoreboardUtils.currentLocation.isDungeon()) {
            return new ArrayList<>();
        }
        List<BlockPos> doors = new ArrayList<>();
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(
                new BlockPos(mc.thePlayer.posX - 192, 69, mc.thePlayer.posZ - 192),
                new BlockPos(mc.thePlayer.posX + 192, 69, mc.thePlayer.posZ + 192)
        );

        for (BlockPos blockPos : blocks) {
            if (isDoorStructure(blockPos)) {
                doors.add(blockPos);
            }
        }
        return doors;
    }

    private boolean isDoorStructure(BlockPos base) {
        boolean isCoal = true;
        boolean isRedClay = true;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 3; z++) {
                    BlockPos pos = base.add(x, y, z);
                    IBlockState state = mc.theWorld.getBlockState(pos);
                    Block block = state.getBlock();
                    if (block != Blocks.coal_block) {
                        isCoal = false;
                    }
                    if (block != Blocks.stained_hardened_clay || state.getValue(BlockColored.COLOR) != EnumDyeColor.RED) {
                        isRedClay = false;
                    }
                    if (!isCoal && !isRedClay) {
                        return false;
                    }
                }
            }
        }
        return isCoal || isRedClay;
    }

    private void updateDoors() {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsWitherDoors) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) {
            allDoors.clear();
            activeDoors.clear();
            return;
        }
        allDoors = findDoors();
        Iterator<BlockPos> iterator = allDoors.iterator();
        while (iterator.hasNext()) {
            BlockPos door = iterator.next();
            if (!isDoorStructure(door)) {
                iterator.remove();
                activeDoors.remove(door);
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsWitherDoors) return;
        if (!ScoreboardUtils.currentLocation.isDungeon()) {
            return;
        }
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("RIGHT CLICK on the WITHER DOOR to open it") || msg.startsWith("RIGHT CLICK on the BLOOD DOOR to open it") || msg.startsWith("A Wither Key was picked up!")) {
            activateNextDoor();
        }
    }

    private void activateNextDoor() {
        for (BlockPos door : allDoors) {
            if (!activeDoors.contains(door)) {
                activeDoors.clear();
                activeDoors.add(door);
                break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsWitherDoors) return;
        if (!ScoreboardUtils.currentLocation.isDungeon() || allDoors.isEmpty()) {
            return;
        }

        double playerX = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * event.partialTicks;
        double playerY = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * event.partialTicks;
        double playerZ = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * event.partialTicks;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(2.0F);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        for (BlockPos door : allDoors) {
            boolean isActive = activeDoors.contains(door);
            addDoorVertices(worldRenderer, door, isActive, playerX, playerY, playerZ);
        }

        tessellator.draw();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private void addDoorVertices(WorldRenderer worldRenderer, BlockPos door, boolean isActive, double playerX, double playerY, double playerZ) {
        double minX = door.getX() - playerX;
        double minY = door.getY() - playerY;
        double minZ = door.getZ() - playerZ;
        double maxX = minX + 3;
        double maxY = minY + 4;
        double maxZ = minZ + 3;

        Color color = isActive ?
                ColorUtils.getColor(NotEnoughFakepixel.feature.dungeons.dungeonsWitherDoorsActive) :
                ColorUtils.getColor(NotEnoughFakepixel.feature.dungeons.dungeonsWitherDoorsInactive);
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;

        // Bottom face
        worldRenderer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();

        // Top face
        worldRenderer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();

        // Vertical edges
        worldRenderer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsWitherDoors) return;
        if (event.phase == TickEvent.Phase.END) {
            if (System.currentTimeMillis() - lastUpdateTime > UPDATE_INTERVAL) {
                updateDoors();
                lastUpdateTime = System.currentTimeMillis();
            }
        }
    }
}