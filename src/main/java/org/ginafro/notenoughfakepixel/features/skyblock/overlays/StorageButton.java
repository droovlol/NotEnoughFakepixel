package org.ginafro.notenoughfakepixel.features.skyblock.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class StorageButton extends GuiButton {
    private final ResourceLocation rs;
    private final int n;
    public StorageButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation r, int no) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.rs = r;
        n = no;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(this.enabled && this.visible){
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
            GlStateManager.scale(1.0f,1.0f,1.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
            GuiScreen.drawScaledCustomSizeModalRect(xPosition,yPosition,0f,0f,width,height,width,height,width,height);
            int x = xPosition + (width/2) + 5;
            if(id > n){
                x = xPosition + (width/2);
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GL11.glTranslated(x, yPosition + 2, 0);
            GL11.glScaled(0.7f, 0.7f, 0.7f);
            mc.fontRendererObj.drawString(String.valueOf(n),0, 0,Color.black.getRGB());
            GlStateManager.popMatrix();
        }
    }

    public void process(GuiContainer c){
        if(id > n) {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, 9 + n, 0, 1, Minecraft.getMinecraft().thePlayer);
        }else {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, 27 + n, 0, 1, Minecraft.getMinecraft().thePlayer);
        }
    }

}
