package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.block.BlockLever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.variables.MobDisplayTypes;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class RenderUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int colour) {
        drawOnSlot(size, xSlotPos, ySlotPos, colour, -1);
    }

    public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int colour, int number) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int guiLeft = (scaledResolution.getScaledWidth() - 176) / 2;
        int guiTop = (scaledResolution.getScaledHeight() - 222) / 2;
        int x = guiLeft + xSlotPos;
        int y = guiTop + ySlotPos;

        // Move down when chest isn't 6 rows
        if (size != 90) y += (6 - (size - 36) / 9) * 9;

        GL11.glTranslated(0, 0, 1);
        Gui.drawRect(x, y, x + 16, y + 16, colour);
        GL11.glTranslated(0, 0, -1);

        if (number != -1) {
            String text = String.valueOf(number);
            int textWidth = mc.fontRendererObj.getStringWidth(text);

            // Push OpenGL states
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 300); // Bring the text to the foreground
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();

            // Render the string
            mc.fontRendererObj.drawStringWithShadow(text, x + 8 - textWidth / 2, y + 8 - 4, 0xFFFFFF);

            // Restore OpenGL states
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");

    public static void renderBeaconBeam(BlockPos block, int rgb, float alphaMult, float partialTicks) {
        double viewerX;
        double viewerY;
        double viewerZ;

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;


        double x = block.getX() - viewerX;
        double y = block.getY() - viewerY;
        double z = block.getZ() - viewerZ;

        double distSq = x * x + y * y + z * z;

        RenderUtils.renderBeaconBeam(x, y, z, rgb, 1.0f, partialTicks, distSq > 10 * 10);
    }

    private static void renderBeaconBeam(
            double x, double y, double z, int rgb, float alphaMult,
            float partialTicks, Boolean disableDepth
    ) {
        int height = 300;
        int bottomOffset = 0;
        int topOffset = bottomOffset + height;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (disableDepth) {
            GlStateManager.disableDepth();
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(beaconBeam);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        double time = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + (double) partialTicks;
        double d1 = MathHelper.func_181162_h(-time * 0.2D - (double) MathHelper.floor_double(-time * 0.1D));

        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;
        double d2 = time * 0.025D * -1.5D;
        double d4 = 0.5D + Math.cos(d2 + 2.356194490192345D) * 0.2D;
        double d5 = 0.5D + Math.sin(d2 + 2.356194490192345D) * 0.2D;
        double d6 = 0.5D + Math.cos(d2 + (Math.PI / 4D)) * 0.2D;
        double d7 = 0.5D + Math.sin(d2 + (Math.PI / 4D)) * 0.2D;
        double d8 = 0.5D + Math.cos(d2 + 3.9269908169872414D) * 0.2D;
        double d9 = 0.5D + Math.sin(d2 + 3.9269908169872414D) * 0.2D;
        double d10 = 0.5D + Math.cos(d2 + 5.497787143782138D) * 0.2D;
        double d11 = 0.5D + Math.sin(d2 + 5.497787143782138D) * 0.2D;
        double d14 = -1.0D + d1;
        double d15 = (double) (height) * 2.5D + d14;
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d6, y + topOffset, z + d7).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d6, y + bottomOffset, z + d7).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + bottomOffset, z + d11).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d10, y + topOffset, z + d11).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d8, y + topOffset, z + d9).tex(1.0D, d15).color(r, g, b, alphaMult).endVertex();
        worldrenderer.pos(x + d8, y + bottomOffset, z + d9).tex(1.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + bottomOffset, z + d5).tex(0.0D, d14).color(r, g, b, 1.0F).endVertex();
        worldrenderer.pos(x + d4, y + topOffset, z + d5).tex(0.0D, d15).color(r, g, b, alphaMult).endVertex();
        tessellator.draw();

        GlStateManager.disableCull();
        double d12 = -1.0D + d1;
        double d13 = height + d12;
        float alphaConst = 0.25F;

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.2D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.2D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + bottomOffset, z + 0.8D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.8D, y + topOffset, z + 0.8D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.8D).tex(1.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.8D).tex(1.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + bottomOffset, z + 0.2D).tex(0.0D, d12).color(r, g, b, alphaConst).endVertex();
        worldrenderer.pos(x + 0.2D, y + topOffset, z + 0.2D).tex(0.0D, d13).color(r, g, b, alphaConst * alphaMult).endVertex();
        tessellator.draw();

        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        if (disableDepth) {
            GlStateManager.enableDepth();
        }
    }

    public static void renderEntityHitbox(Entity entity, float partialTicks, Color color, MobDisplayTypes type) {
        if (type == MobDisplayTypes.ITEMBIG) {
            renderItemBigHitbox(entity, partialTicks, color);
            return;
        }

        Vector3f loc = new Vector3f(
                (float) entity.posX - 0.5f,
                (float) entity.posY - 0.5f,
                (float) entity.posZ - 0.5f);

        if (type == MobDisplayTypes.BAT ||
                type == MobDisplayTypes.ENDERMAN_BOSS ||
                type == MobDisplayTypes.WOLF_BOSS ||
                type == MobDisplayTypes.SPIDER_BOSS) {
            GlStateManager.disableDepth();
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        Entity player = mc.getRenderViewEntity();
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double x = loc.x - playerX + 0.5;
        double y = loc.y - playerY - 0.5;
        if (type == MobDisplayTypes.BAT) {
            y = (loc.y - playerY) + 1;
        } else if (type == MobDisplayTypes.FEL) {
            y = loc.y - playerY + 2.3;
        }
        double z = loc.z - playerZ + 0.5;

        double y1 = y + type.getY1();
        double y2 = y + type.getY2();
        double x1 = x + type.getX1();
        double x2 = x + type.getX2();
        double z1 = z + type.getZ1();
        double z2 = z + type.getZ2();

        drawHitbox(x1, x2, y1, y2, z1, z2, color, type);

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void renderItemBigHitbox(Entity entity, float partialTicks, Color color) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        if (bb == null) return;

        double scale = Config.feature.dungeons.dungeonsScaleItemDrop;

        Entity player = mc.getRenderViewEntity();
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        // Compute original box coordinates relative to player
        double x1 = bb.minX - playerX;
        double x2 = bb.maxX - playerX;
        double y1 = bb.minY - playerY;
        double y2 = bb.maxY - playerY;
        double z1 = bb.minZ - playerZ;
        double z2 = bb.maxZ - playerZ;

        // Compute the center of the bounding box
        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        double centerZ = (z1 + z2) / 2;

        // Scale bounding box relative to center
        x1 = centerX + (x1 - centerX) * scale;
        x2 = centerX + (x2 - centerX) * scale;
        y1 = centerY + (y1 - centerY) * scale;
        y2 = centerY + (y2 - centerY) * scale;
        z1 = centerZ + (z1 - centerZ) * scale;
        z2 = centerZ + (z2 - centerZ) * scale;

        double yOffset = (Config.feature.dungeons.dungeonsScaleItemDrop - 1f) * (entity.height / 2f);
        y1 += yOffset;
        y2 += yOffset;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        drawHitbox(x1, x2, y1, y2, z1, z2, color, MobDisplayTypes.ITEMBIG);

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }


    private static void drawHitbox(double x1, double x2, double y1, double y2, double z1, double z2, Color color, MobDisplayTypes type) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        if (type == MobDisplayTypes.GAIA) {
            GL11.glLineWidth(5.0f);
        } else {
            GL11.glLineWidth(3.0f);
        }

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        double[][] vertices = {
                {x1, y1, z1}, {x2, y1, z1}, {x2, y2, z1}, {x1, y2, z1},
                {x1, y1, z2}, {x2, y1, z2}, {x2, y2, z2}, {x1, y2, z2}
        };

        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            worldRenderer.pos(vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2])
                    .color(red, green, blue, alpha).endVertex();
            worldRenderer.pos(vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2])
                    .color(red, green, blue, alpha).endVertex();
        }

        tessellator.draw();
    }

    /**
     * Renders a floating name tag in 3D space at the specified position, facing the player.
     * Includes a semi-transparent background and dynamically scales with distance.
     *
     * @param str          The string to render as the tag.
     * @param pos          The world position as {x, y, z}.
     * @param color        The color of the tag text.
     * @param partialTicks The render partial ticks used for smooth interpolation.
     */
    public static void drawTag(String str, double[] pos, Color color, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer font = mc.fontRendererObj;
        EntityPlayerSP player = mc.thePlayer;
        RenderManager renderManager = mc.getRenderManager();

        Vec3 viewerPos = getInterpolatedPos(player, partialTicks);
        Vec3 tagPos = new Vec3(pos[0] - viewerPos.xCoord + 0.5, pos[1] - viewerPos.yCoord + 0.5, pos[2] - viewerPos.zCoord + 0.5);

        double distance = player.getDistance(pos[0], pos[1], pos[2]);
        float scale = Math.max(2.0F, (float) distance / 5.0F) * 0.016666668F;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(tagPos.xCoord, tagPos.yCoord + 2.5, tagPos.zCoord);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);

        setupRenderStateForText();

        drawTagBackground(font, str);
        font.drawString(str, -font.getStringWidth(str) / 2, 0, colorToInt(color));

        restoreRenderState();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    /**
     * Calculates an interpolated position for the given entity based on partial ticks.
     * This creates smooth transitions between render frames.
     *
     * @param entity       The entity whose position to interpolate.
     * @param partialTicks The partial tick value for interpolation.
     * @return A {@link Vec3} representing the smoothly interpolated world position.
     */
    private static Vec3 getInterpolatedPos(Entity entity, float partialTicks) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        return new Vec3(x, y, z);
    }

    /**
     * Configures OpenGL state for rendering floating text in the world.
     * Disables lighting, depth testing, and enables blending for transparency.
     */
    private static void setupRenderStateForText() {
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
    }

    /**
     * Restores OpenGL state after rendering a 3D floating label.
     * Re-enables depth testing, lighting, and disables blending to prevent side effects.
     */
    private static void restoreRenderState() {
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    /**
     * Draws a semi-transparent black background behind the name tag text.
     *
     * @param font The {@link FontRenderer} used to measure the string width.
     * @param str  The text string being rendered (used to size the box).
     */
    private static void drawTagBackground(FontRenderer font, String str) {
        int width = font.getStringWidth(str) / 2;

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();

        GlStateManager.disableTexture2D();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(-width - 1, -1, 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos(-width - 1, 8, 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos(width + 1, 8, 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos(width + 1, -1, 0.0D).color(0, 0, 0, 64).endVertex();
        tess.draw();
        GlStateManager.enableTexture2D();
    }

    public static void draw3DLine(Vec3 pos1, Vec3 pos2, Color color, int lineWidth, boolean depth, float partialTicks) {
        draw3DLine(pos1, pos2, color, lineWidth, depth, partialTicks, false, false, null);
    }

    /**
     * Draws a 3D line between two world-space positions using OpenGL line rendering.
     * <p>
     * This version supports advanced customization, including drawing from the player's head direction
     * and offsetting the starting point when targeting levers with specific orientations.
     *
     * @param pos1         The starting {@link Vec3} world position.
     * @param pos2         The ending {@link Vec3} world position.
     * @param color        The color of the line, including alpha.
     * @param lineWidth    The width of the line in pixels (OpenGL units).
     * @param depth        If {@code true}, respects depth testing; if {@code false}, renders on top of everything.
     * @param partialTicks The partial tick value used for interpolating the render position.
     * @param fromHead     If {@code true}, overrides {@code pos2} with a vector in the player's look direction.
     * @param isLever      If {@code true}, adjusts {@code pos1} to align with the center of a lever's hitbox.
     * @param orientation  The lever's orientation (used only if {@code isLever} is {@code true}).
     */
    public static void draw3DLine(Vec3 pos1, Vec3 pos2, Color color, int lineWidth, boolean depth,
                                  float partialTicks, boolean fromHead, boolean isLever, BlockLever.EnumOrientation orientation) {

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        Vec3 interp = getInterpolatedPosition(viewer, partialTicks);

        Vec3 start = isLever ? getLeverCenter(pos1, orientation) : pos1;
        Vec3 end = fromHead ? getPlayerLookVec() : pos2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-interp.xCoord, -interp.yCoord, -interp.zCoord);
        setupRenderState(depth, lineWidth);

        renderLine(start, end, color);

        cleanupRenderState(depth);
        GlStateManager.translate(interp.xCoord, interp.yCoord, interp.zCoord);
        GlStateManager.popMatrix();
    }

    /**
     * Calculates the interpolated position of an entity based on the current partial ticks.
     * This allows for smooth rendering between ticks.
     *
     * @param entity       The entity to interpolate (e.g., the render view entity).
     * @param partialTicks The current render partial ticks.
     * @return The interpolated {@link Vec3} position.
     */
    private static Vec3 getInterpolatedPosition(Entity entity, float partialTicks) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        return new Vec3(x, y, z);
    }

    /**
     * Calculates the central position of a lever based on its orientation.
     * This adjusts the start point of the line so it appears to originate from the correct part of the lever.
     *
     * @param pos         The base {@link Vec3} block position of the lever.
     * @param orientation The orientation of the lever.
     * @return A {@link Vec3} representing the center of the lever face.
     */
    private static Vec3 getLeverCenter(Vec3 pos, BlockLever.EnumOrientation orientation) {
        double x = pos.xCoord, y = pos.yCoord, z = pos.zCoord;

        switch (orientation) {
            case UP_X:
            case UP_Z:
                return new Vec3(x + 0.5, y + 0.1, z + 0.5);
            case NORTH:
                return new Vec3(x + 0.5, y + 0.5, z + 0.875);
            case SOUTH:
                return new Vec3(x + 0.5, y + 0.5, z + 0.125);
            case WEST:
                return new Vec3(x + 0.875, y + 0.5, z + 0.5);
            case EAST:
                return new Vec3(x + 0.125, y + 0.5, z + 0.5);
            default:
                return new Vec3(x + 0.5, y + 0.5, z - 1.125);
        }
    }

    /**
     * Gets a direction vector based on the player's current yaw and pitch, simulating a "look" direction.
     * Used when drawing lines from the player's head or eye position.
     *
     * @return A normalized {@link Vec3} representing the player's look direction.
     */
    private static Vec3 getPlayerLookVec() {
        Minecraft mc = Minecraft.getMinecraft();
        float yaw = -mc.thePlayer.rotationYaw;
        float pitch = -mc.thePlayer.rotationPitch;
        return new Vec3(0, 0, 1)
                .rotatePitch((float) Math.toRadians(pitch))
                .rotateYaw((float) Math.toRadians(yaw));
    }

    /**
     * Configures the OpenGL state for line rendering.
     * Disables textures, lighting, and depth as needed, and sets blend modes.
     *
     * @param depth      If false, disables depth testing and depth writes.
     * @param lineWidth  The width of the OpenGL line to draw.
     */
    private static void setupRenderState(boolean depth, int lineWidth) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(lineWidth);

        if (!depth) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
        }
    }

    /**
     * Restores OpenGL state after rendering to avoid interfering with other game rendering operations.
     * Re-enables depth testing, texture, lighting, and alpha.
     *
     * @param depth If false, re-enables depth testing and depth mask.
     */
    private static void cleanupRenderState(boolean depth) {
        if (!depth) {
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a single 3D line between two points in the world.
     *
     * @param start The starting {@link Vec3} position.
     * @param end   The ending {@link Vec3} position.
     * @param color The line color, including alpha transparency.
     */
    private static void renderLine(Vec3 start, Vec3 end, Color color) {
        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f,
                color.getBlue() / 255f, color.getAlpha() / 255f);
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(start.xCoord, start.yCoord, start.zCoord).endVertex();
        wr.pos(end.xCoord, end.yCoord, end.zCoord).endVertex();
        Tessellator.getInstance().draw();
    }

    public static void highlightBlock(BlockPos pos, Color color, boolean disableDepth, float partialTicks) {
        highlightBlock(pos, color, disableDepth, false, partialTicks);
    }

    public static void highlightBlock(BlockPos pos, Color color, boolean disableDepth, boolean isButton, float partialTicks) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        double x = pos.getX() - viewerX;
        double y = pos.getY() - viewerY;
        double z = pos.getZ() - viewerZ;

        if (disableDepth) GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        double initialToAddX = 0;
        if (!disableDepth) {
            initialToAddX = .05;
        }
        if (!isButton) {
            if (disableDepth) {
                RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1), 1f, color);
            } else {
                RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x - initialToAddX, y, z, x + 1 + initialToAddX, y + 1, z + 1), 1f, color);
            }
        } else {
            RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x, y + 0.5 - 0.13, z + 0.5 - 0.191, x - .13, y + 0.5 + 0.13, z + 0.5 + 0.191), 1f, color);
        }


        GlStateManager.enableLighting();
        if (disableDepth) GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }

    public static void drawLeverBoundingBox(BlockPos pos, EnumFacing facing, Color color, float partialTicks) {
        // Get the player's camera position
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        // Convert world position to render position
        double x = pos.getX() - viewerX;
        double y = pos.getY() - viewerY;
        double z = pos.getZ() - viewerZ;

        // Define bounding box relative to lever position
        AxisAlignedBB boundingBox;
        switch (facing) {
            case NORTH:
                boundingBox = new AxisAlignedBB(x + 0.25, y + 0.1875, z + 0.75, x + 0.75, y + 0.8125, z + 1);
                break;
            case SOUTH:
                boundingBox = new AxisAlignedBB(x + 0.25, y + 0.1875, z, x + 0.75, y + 0.8125, z + 0.25);
                break;
            case WEST:
                boundingBox = new AxisAlignedBB(x + 0.75, y + 0.1875, z + 0.25, x + 1, y + 0.8125, z + 0.75);
                break;
            case EAST:
                boundingBox = new AxisAlignedBB(x, y + 0.1875, z + 0.25, x + 0.25, y + 0.8125, z + 0.75);
                break;
            default:
                boundingBox = new AxisAlignedBB(x + 0.25, y + 0.1875, z - 1.25, x + 0.75, y + 0.8125, z - 1);
                break;
        }

        // Disable culling and lighting for proper rendering
        GlStateManager.disableCull();
        GlStateManager.disableLighting();

        // Render bounding box
        RenderUtils.drawFilledBoundingBox(boundingBox, 1f, color);

        // Restore rendering settings
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
    }

    public static void drawFilledBoundingBox(AxisAlignedBB box, float alpha, Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        setupGlStateForBox();

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = MathHelper.clamp_float(color.getAlpha() / 255f * alpha, 0.0f, 1.0f);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        // Top & Bottom
        drawFace(wr, tessellator, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, r, g, b, a); // Bottom
        drawFace(wr, tessellator, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.minZ, r, g, b, a); // Top

        // Sides (slightly darker)
        drawFace(wr, tessellator, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.minZ, r * 0.8f, g * 0.8f, b * 0.8f, a); // West
        drawFace(wr, tessellator, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r * 0.8f, g * 0.8f, b * 0.8f, a); // East

        // Front & Back (slightly different shade)
        drawFace(wr, tessellator, box.minX, box.maxY, box.minZ, box.maxX, box.minY, box.minZ, r * 0.9f, g * 0.9f, b * 0.9f, a); // North
        drawFace(wr, tessellator, box.minX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, r * 0.9f, g * 0.9f, b * 0.9f, a); // South

        restoreGlState();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    /**
     * Prepares the OpenGL state for rendering a filled bounding box.
     * Disables lighting, textures, and depth writing, and enables blending.
     * Call this before drawing faces.
     */
    private static void setupGlStateForBox() {
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
    }

    /**
     * Restores the OpenGL state after rendering a filled bounding box.
     * Re-enables depth writing, textures, lighting, and disables blending.
     * Call this after finishing all drawing operations.
     */
    private static void restoreGlState() {
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    /**
     * Draws a single face (quad) of a bounding box using the specified vertex coordinates and color.
     *
     * @param wr   The WorldRenderer instance used for buffering vertex data.
     * @param tess The Tessellator instance used to draw the buffered data.
     * @param x1   The x-coordinate of the first corner.
     * @param y1   The y-coordinate of the first corner.
     * @param z1   The z-coordinate of the first corner.
     * @param x2   The x-coordinate of the opposite corner.
     * @param y2   The y-coordinate of the opposite corner.
     * @param z2   The z-coordinate of the opposite corner.
     * @param r    Red color component (0.0–1.0).
     * @param g    Green color component (0.0–1.0).
     * @param b    Blue color component (0.0–1.0).
     * @param a    Alpha (transparency) component (0.0–1.0).
     */
    private static void drawFace(WorldRenderer wr, Tessellator tess, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a) {
        GlStateManager.color(r, g, b, a);
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(x1, y1, z1).endVertex();
        wr.pos(x2, y1, z1).endVertex();
        wr.pos(x2, y2, z2).endVertex();
        wr.pos(x1, y2, z2).endVertex();
        tess.draw();
    }

    /**
     * Converts a {@link Color} object to a single 24-bit RGB integer.
     * <p>
     * The resulting integer uses the format <code>0xRRGGBB</code> where:
     * <ul>
     *   <li><b>RR</b> is the red component (8 bits)</li>
     *   <li><b>GG</b> is the green component (8 bits)</li>
     *   <li><b>BB</b> is the blue component (8 bits)</li>
     * </ul>
     *
     * @param color The {@link Color} to convert. Alpha is ignored.
     * @return An integer representation of the color in 0xRRGGBB format.
     */
    public static int colorToInt(Color color) {
        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    public static void drawFilledBoundingBoxEntity(AxisAlignedBB aabb, float alpha, Color color, float partialTicks) {
        // Used for BlazeAttunements
        Entity render = Minecraft.getMinecraft().getRenderViewEntity();

        double coordX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        double coordY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        double coordZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-coordX, -coordY, -coordZ);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, (color.getAlpha() / 255f) * alpha);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        // Draw the six faces of the box
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();

        // Restore OpenGL state
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
