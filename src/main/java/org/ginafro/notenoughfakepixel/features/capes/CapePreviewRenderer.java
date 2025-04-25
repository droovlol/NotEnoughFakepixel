package org.ginafro.notenoughfakepixel.features.capes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class CapePreviewRenderer {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static AbstractClientPlayer previewPlayer;

    public static AbstractClientPlayer getPreviewPlayer(Cape cape) {
        // Generate a fresh preview player every call, or cache if you want
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(("CapePreview" + cape.capeID).getBytes()), "CapePreview");

        return new AbstractClientPlayer(mc.theWorld, profile) {
            private final NetworkPlayerInfo info = new NetworkPlayerInfo(profile);

            @Override
            public ResourceLocation getLocationCape() {
                return CapeManager.getCapeTexture(cape);
            }

            @Override
            public boolean isWearing(EnumPlayerModelParts part) {
                return part == EnumPlayerModelParts.CAPE || super.isWearing(part);
            }

            @Override
            public boolean isInvisible() {
                return false;
            }

            @Override
            public NetworkPlayerInfo getPlayerInfo() {
                return info;
            }
        };
    }

    public static void drawSpinningCapePlayer(int x, int y, int scale, Cape cape, float rotationYaw) {
        AbstractClientPlayer player = getPreviewPlayer(cape);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0F, 0F, 1F);
        GlStateManager.rotate(rotationYaw, 0F, 1F, 0F); // Spinning rotation

        // Head follows body for now
        player.prevRotationYaw = rotationYaw;
        player.rotationYaw = rotationYaw;
        player.renderYawOffset = rotationYaw;
        player.rotationYawHead = rotationYaw;

        // Simulate basic motion for cape physics
        long t = Minecraft.getSystemTime();
        float motionFactor = t % 1000L / 500F;
        player.prevPosX = player.posX = motionFactor;
        player.prevPosZ = player.posZ = motionFactor;
        player.limbSwing = t / 100F;
        player.limbSwingAmount = 1.0f;

        RenderManager rm = mc.getRenderManager();
        rm.playerViewY = 180.0F;
        rm.doRenderEntity(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        GlStateManager.popMatrix();
    }
}
