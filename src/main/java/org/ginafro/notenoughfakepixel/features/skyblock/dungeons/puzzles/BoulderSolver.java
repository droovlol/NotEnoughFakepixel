package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.puzzles;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.events.PacketWriteEvent;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class BoulderSolver {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static BlockPos boulderChest = null;
    public static String boulderRoomDirection = null; // Changed from EnumFacing to String
    public static BoulderState[][] grid = new BoulderState[7][6];
    public static int roomVariant = -1;
    public static ArrayList<ArrayList<BoulderPush>> variantSteps = new ArrayList<>();
    public static ArrayList<ArrayList<BoulderState>> expectedBoulders = new ArrayList<>();
    private static int ticks = 0;
    private static Thread workerThread = null;
    private static boolean prevInBoulderRoom = false;
    private static boolean inBoulderRoom = false;

    public BoulderSolver() {
        expectedBoulders.add(Lists.newArrayList(BoulderState.EMPTY, BoulderState.FILLED, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY));
        variantSteps.add(Lists.newArrayList(new BoulderPush(2, 4, Direction.RIGHT), new BoulderPush(2, 3, Direction.FORWARD), new BoulderPush(3, 3, Direction.RIGHT), new BoulderPush(4, 3, Direction.RIGHT), new BoulderPush(4, 1, Direction.FORWARD), new BoulderPush(5, 1, Direction.RIGHT)));

        expectedBoulders.add(Lists.newArrayList(BoulderState.FILLED, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED));
        variantSteps.add(Lists.newArrayList(new BoulderPush(3, 4, Direction.FORWARD), new BoulderPush(2, 4, Direction.LEFT), new BoulderPush(3, 3, Direction.RIGHT), new BoulderPush(3, 2, Direction.FORWARD), new BoulderPush(2, 2, Direction.LEFT), new BoulderPush(4, 2, Direction.RIGHT), new BoulderPush(2, 1, Direction.FORWARD), new BoulderPush(4, 1, Direction.FORWARD), new BoulderPush(3, 1, Direction.RIGHT)));

        expectedBoulders.add(Lists.newArrayList(BoulderState.FILLED, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED));
        variantSteps.add(Lists.newArrayList(new BoulderPush(1, 1, Direction.RIGHT)));

        expectedBoulders.add(Lists.newArrayList(BoulderState.FILLED, BoulderState.FILLED, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED, BoulderState.FILLED));
        variantSteps.add(Lists.newArrayList(new BoulderPush(4, 3, Direction.FORWARD), new BoulderPush(3, 3, Direction.LEFT), new BoulderPush(3, 1, Direction.FORWARD), new BoulderPush(2, 1, Direction.LEFT)));

        expectedBoulders.add(Lists.newArrayList(BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED));
        variantSteps.add(Lists.newArrayList(new BoulderPush(3, 4, Direction.FORWARD), new BoulderPush(3, 3, Direction.FORWARD), new BoulderPush(2, 1, Direction.FORWARD), new BoulderPush(1, 1, Direction.LEFT)));

        expectedBoulders.add(Lists.newArrayList(BoulderState.FILLED, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED, BoulderState.EMPTY, BoulderState.FILLED, BoulderState.EMPTY));
        variantSteps.add(Lists.newArrayList(new BoulderPush(1, 4, Direction.FORWARD), new BoulderPush(1, 1, Direction.RIGHT)));

        expectedBoulders.add(Lists.newArrayList(BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED, BoulderState.FILLED, BoulderState.FILLED, BoulderState.FILLED, BoulderState.FILLED, BoulderState.FILLED, BoulderState.EMPTY, BoulderState.FILLED));
        variantSteps.add(Lists.newArrayList(new BoulderPush(6, 4, Direction.FORWARD), new BoulderPush(6, 3, Direction.FORWARD), new BoulderPush(4, 1, Direction.FORWARD), new BoulderPush(5, 1, Direction.RIGHT)));

        expectedBoulders.add(Lists.newArrayList(BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.EMPTY, BoulderState.FILLED));
        variantSteps.add(Lists.newArrayList(new BoulderPush(0, 1, Direction.FORWARD)));
    }

    public static void update() {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsBoulderSolver) return;
        EntityPlayerSP player = mc.thePlayer;
        World world = mc.theWorld;
        if (ScoreboardUtils.currentLocation.isDungeon() && world != null && player != null && roomVariant != -2 && (workerThread == null || !workerThread.isAlive() || workerThread.isInterrupted())) {
            workerThread = new Thread(() -> {
                prevInBoulderRoom = inBoulderRoom;
                int quartzBlocksFound = 0;
                int barriersFound = 0;
                BlockPos plusPlusQuartz = null;
                BlockPos minusMinusQuartz = null;
                Iterable<BlockPos> blocks = BlockPos.getAllInBox(new BlockPos(player.posX - 25, 68, player.posZ - 25), new BlockPos(player.posX + 25, 68, player.posZ + 25));

                // Detect boulder room
                for (BlockPos blockPos : blocks) {
                    if (world.getBlockState(blockPos).getBlock() == Blocks.quartz_block) {
                        quartzBlocksFound++;
                        if (plusPlusQuartz == null || (blockPos.getX() >= plusPlusQuartz.getX() && blockPos.getZ() >= plusPlusQuartz.getZ())) {
                            plusPlusQuartz = blockPos;
                        }
                        if (minusMinusQuartz == null || (blockPos.getX() <= minusMinusQuartz.getX() && blockPos.getZ() <= minusMinusQuartz.getZ())) {
                            minusMinusQuartz = blockPos;
                        }
                        if (quartzBlocksFound == 8) break;
                    } else if (world.getBlockState(blockPos).getBlock() == Blocks.barrier) {
                        barriersFound++;
                    }
                }

                if (quartzBlocksFound == 8 && barriersFound >= 10) {
                    inBoulderRoom = true;
                    if (!prevInBoulderRoom || boulderChest == null || boulderRoomDirection == null) {

                        // Detect rotation of room
                        BlockPos northChest = minusMinusQuartz.add(11, +1, 0);
                        BlockPos eastChest = plusPlusQuartz.add(0, +1, -11);
                        BlockPos southChest = plusPlusQuartz.add(-11, +1, 0);
                        BlockPos westChest = minusMinusQuartz.add(0, +1, 11);

                        if (world.getBlockState(northChest).getBlock() == Blocks.log) {
                            boulderRoomDirection = "north";
                            boulderChest = northChest.add(0, -3, -2);;
                        } else if (world.getBlockState(eastChest).getBlock() == Blocks.log) {
                            boulderRoomDirection = "east";
                            boulderChest = eastChest.add(2, -3, 0);
                        } else if (world.getBlockState(southChest).getBlock() == Blocks.log) {
                            boulderRoomDirection = "south";
                            boulderChest = southChest.add(0, -3, 2);
                        } else if (world.getBlockState(westChest).getBlock() == Blocks.log) {
                            boulderRoomDirection = "west";
                            boulderChest = westChest.add(-2, -3, 0);
                        } else {
                            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not determine orientation of boulder room."));
                            return;
                        }

                        // Detect variant
                        if (roomVariant == -1) {
                            roomVariant = -2;
                            for (int i = 0; i < expectedBoulders.size(); i++) {
                                ArrayList<BoulderState> expected = expectedBoulders.get(i);
                                boolean isRight = true;
                                for (int j = 0; j < expected.size(); j++) {
                                    int column = j % 7;
                                    int row = (int) Math.floor(j / 7f);
                                    BoulderState state = expected.get(j);
                                    if (grid[column][row] != state && state != BoulderState.PLACEHOLDER) {
                                        isRight = false;
                                        break;
                                    }
                                }
                                if (isRight) {
                                    roomVariant = i;
                                    mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "NEF detected boulder variant " + (roomVariant + 1) + "."));
                                    break;
                                }
                            }
                            if (roomVariant == -2) {
                                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "NEF couldn't detect the boulder variant."));
                            }
                        }
                    }
                } else {
                    inBoulderRoom = false;
                }
            }, "NEF-Boulder-Puzzle");
            workerThread.start();
        }
    }

    public static void reset() {
        boulderChest = null;
        boulderRoomDirection = null;
        grid = new BoulderState[7][6];
        roomVariant = -1;
        workerThread = null;
        inBoulderRoom = false;
        prevInBoulderRoom = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        ticks++;
        if (ticks % 20 == 0) {
            ticks = 0;
            update();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsBoulderSolver || boulderChest == null || roomVariant < 0) return;
        Entity viewer = mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;

        ArrayList<BoulderPush> steps = variantSteps.get(roomVariant);
        for (BoulderPush step : steps) {
            if (grid[step.x][step.y] != BoulderState.EMPTY) {
                BlockPos farLeftPos = getFarLeftPos();
                BlockPos boulderPos = farLeftPos.offset(getRightColumn(), 3 * step.x).offset(getDownRow(), 3 * step.y);
                EnumFacing actualDirection = getActualDirection(step.direction);
                BlockPos buttonPos = boulderPos.offset(actualDirection.getOpposite(), 2).down();
                double x = buttonPos.getX() - viewerX;
                double y = buttonPos.getY() - viewerY;
                double z = buttonPos.getZ() - viewerZ;
                GlStateManager.disableCull();
                drawFilledBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), new Color(255, 0, 0, 255), 0.7f);
                GlStateManager.enableCull();
                break;
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketWriteEvent event) {
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if (event.packet instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.packet;
            if (packet.getPosition() != null && packet.getPosition().equals(boulderChest)) {
                roomVariant = -2;
            }
        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        reset();
    }

    // Helper methods for orientation
    private static BlockPos getFarLeftPos() {
        EnumFacing downRow = getDownRow();
        EnumFacing rightColumn = getRightColumn();
        return boulderChest.offset(downRow, 5).offset(rightColumn.getOpposite(), 9);
    }

    private static EnumFacing getDownRow() {
        switch (boulderRoomDirection) {
            case "north": return EnumFacing.SOUTH;
            case "east": return EnumFacing.WEST;
            case "south": return EnumFacing.NORTH;
            case "west": return EnumFacing.EAST;
            default: return EnumFacing.NORTH; // Fallback
        }
    }

    private static EnumFacing getRightColumn() {
        switch (boulderRoomDirection) {
            case "north": return EnumFacing.EAST;
            case "east": return EnumFacing.SOUTH;
            case "south": return EnumFacing.WEST;
            case "west": return EnumFacing.NORTH;
            default: return EnumFacing.EAST; // Fallback
        }
    }

    private static EnumFacing getActualDirection(Direction stepDirection) {
        switch (boulderRoomDirection) {
            case "north":
                switch (stepDirection) {
                    case FORWARD: return EnumFacing.NORTH;
                    case BACKWARD: return EnumFacing.SOUTH;
                    case LEFT: return EnumFacing.WEST;
                    case RIGHT: return EnumFacing.EAST;
                }
            case "east":
                switch (stepDirection) {
                    case FORWARD: return EnumFacing.EAST;
                    case BACKWARD: return EnumFacing.WEST;
                    case LEFT: return EnumFacing.NORTH;
                    case RIGHT: return EnumFacing.SOUTH;
                }
            case "south":
                switch (stepDirection) {
                    case FORWARD: return EnumFacing.SOUTH;
                    case BACKWARD: return EnumFacing.NORTH;
                    case LEFT: return EnumFacing.EAST;
                    case RIGHT: return EnumFacing.WEST;
                }
            case "west":
                switch (stepDirection) {
                    case FORWARD: return EnumFacing.WEST;
                    case BACKWARD: return EnumFacing.EAST;
                    case LEFT: return EnumFacing.SOUTH;
                    case RIGHT: return EnumFacing.NORTH;
                }
        }
        return EnumFacing.NORTH; // Fallback
    }

    // Utility methods from second snippet
    public static char[][] flipVertically(char[][] board) {
        char[][] newBoard = new char[7][7];
        for (int row = 0; row < 7; row++) {
            System.arraycopy(board[6 - row], 0, newBoard[row], 0, 7);
        }
        return newBoard;
    }

    public static char[][] flipHorizontally(char[][] board) {
        char[][] newBoard = new char[7][7];
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                newBoard[row][column] = board[row][6 - column];
            }
        }
        return newBoard;
    }

    public static char[][] rotateClockwise(char[][] board) {
        char[][] newBoard = new char[7][7];
        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                newBoard[column][6 - row] = board[row][column];
            }
        }
        return newBoard;
    }

    public enum Direction {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    public enum BoulderState {
        EMPTY,
        FILLED,
        PLACEHOLDER
    }

    public static class BoulderPush {
        int x, y;
        Direction direction;

        public BoulderPush(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }

    public static void drawFilledBoundingBox(AxisAlignedBB aabb, Color c, float alphaMultiplier) {
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.color(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f*alphaMultiplier);

        // Vertical
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();

        GlStateManager.color(c.getRed()/255f*0.8f, c.getGreen()/255f*0.8f, c.getBlue()/255f*0.8f, c.getAlpha()/255f*alphaMultiplier);

        // X-axis
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();

        GlStateManager.color(c.getRed()/255f*0.9f, c.getGreen()/255f*0.9f, c.getBlue()/255f*0.9f, c.getAlpha()/255f*alphaMultiplier);

        // Z-axis
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
    }
}