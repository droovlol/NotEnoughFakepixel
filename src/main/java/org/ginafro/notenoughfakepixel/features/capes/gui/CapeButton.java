package org.ginafro.notenoughfakepixel.features.capes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.features.capes.Cape;
import org.ginafro.notenoughfakepixel.features.capes.CapeManager;
import org.ginafro.notenoughfakepixel.features.capes.CapePreviewRenderer;

public class CapeButton extends GuiButton {

    public Cape ca;
    public ResourceLocation tex = new ResourceLocation("notenoughfakepixel","cape_gui.png");
    public CapeButton(int buttonId, int x, int y, int widthIn, int heightIn, Cape c) {
        super(buttonId, x, y, widthIn, heightIn, "");
        ca = c;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(this.enabled && this.visible){
            GlStateManager.color(1f,1f,1f);
            if(CapeGui.selected == ca){
                tex = new ResourceLocation("notenoughfakepixel","cape_gui_s.png");
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
            drawScaledCustomSizeModalRect(xPosition,yPosition,0,0,width,height,width,height,width,height);
            CapePreviewRenderer.drawCape(xPosition + width / 2,yPosition + 15,90,ca);
            drawName(mc);
             }
    }

    private void drawName(Minecraft mc) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(xPosition + (float)(width / 2),yPosition + height - 30,0);
        GlStateManager.scale(0.8f,0.8f,0.8f);
        drawCenteredString(mc.fontRendererObj,ca.capeName,0,0,-1);
        GlStateManager.popMatrix();
    }

    public void process(){
        CapeManager.setCape(ca.capeID);
    }

}
