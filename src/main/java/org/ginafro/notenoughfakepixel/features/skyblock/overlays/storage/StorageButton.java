package org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.util.Map;

public class StorageButton extends GuiButton {
    public final int n;
    public StorageButton(int buttonId, int x, int y, int widthIn, int heightIn, int no) {
        super(buttonId, x, y, widthIn, heightIn, "");
        n = no;
    }

    public String s = "Backpack";
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(this.enabled && this.visible){
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
//            Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
//            GuiScreen.drawScaledCustomSizeModalRect(xPosition,yPosition,0f,0f,width,height,width,height,width,height);
            int c = ColorUtils.getColor(NotEnoughFakepixel.feature.overlays.chestColor).getRGB();
            drawRect(xPosition,yPosition,xPosition+width,yPosition+height,c);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            if(id == n){
                s = "Ender Chest";
            }
            String type = id == n ? "Ender Chest(Page" + (n+1) + ")" : "Backpack" + (n + 1);
            StorageData data = CustomConfigHandler.loadStorageData(type);
            if (data == null) {
                drawRect(xPosition,yPosition,xPosition+width,yPosition+height,new Color(31,31,31,250).getRGB());
                drawCenteredString(mc.fontRendererObj,"Open For Preview",xPosition + width/2,yPosition + height/2,-1);
                return;
            }
            float slotSize =( width -4f)/ 9.00f;
            float slotScale = (slotSize - 2f)/16.00f;
            drawBackpackName(mc,xPosition + 3, yPosition,slotScale);
            RenderHelper.enableStandardItemLighting();
            Map<Integer, ItemStack> stackMap = null;
            try {
                stackMap = data.getItemStacks();
            } catch (NBTException e) {
                throw new RuntimeException(e);
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            for(Map.Entry<Integer,ItemStack> entry : stackMap.entrySet()){
                int slotIndex = entry.getKey();
                ItemStack stack = entry.getValue();
                int columns = 9;
                int row = slotIndex / columns;
                int col = slotIndex % columns;
                float slotX = xPosition + col * (16 * slotScale + 2);
                float slotY = yPosition + 15 + row * (16 * slotScale + 2);
                renderItemScaled(mc,stack,slotX,slotY,slotScale);
//                itemToolTip(stack,slotX,slotY,mouseX,mouseY,slotScale);

            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    private void drawBackpackName(Minecraft mc,float x, float y, float scale){
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        mc.fontRendererObj.drawString(s + " " +(n+1),0, 0,Color.black.getRGB());
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
    }

    public void renderItemScaled(Minecraft mc, ItemStack stack, float x, float y, float scale) {
        String s = StorageOverlay.searchBar.getText();
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        drawHorizontalLine(0,16,0,-1);
        drawHorizontalLine(0,16,16,-1);
        drawVerticalLine(0,0,16,-1);
        drawVerticalLine(16,0,16,-1);
        if(!s.isEmpty()){
            if(stack.getDisplayName().toLowerCase().contains(s.toLowerCase())){
                int color = ColorUtils.getColor(NotEnoughFakepixel.feature.overlays.searchColor).getRGB();
                drawRect(0,0,16,16,color);
            }
        }
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
    }

    public void process(GuiContainer c){
        if(this.id == this.n) {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, 9 + n, 0, 1, Minecraft.getMinecraft().thePlayer);
        }else {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, 27 + n, 0, 1, Minecraft.getMinecraft().thePlayer);
        }
    }

}
