package org.ginafro.notenoughfakepixel.features.skyblock.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;

import java.awt.*;

@RegisterEvents
public class HealthManaOverlay {

    private static int health,maxHealth,mana,maxMana,overflowMana,overflowHealth,defence,speed = 100;
    private final ResourceLocation BAR_EMPTY = new ResourceLocation("notenoughfakepixel","skyblock/stats/health_empty.png");
    private final ResourceLocation BAR_FILL = new ResourceLocation("notenoughfakepixel","skyblock/stats/health_fill.png");
    private final ResourceLocation EXP_EMPTY = new ResourceLocation("notenoughfakepixel","skyblock/stats/exp_empty.png");
    private final ResourceLocation EXP_FILL = new ResourceLocation("notenoughfakepixel","skyblock/stats/exp_fill.png");
    private final ResourceLocation HEART = new ResourceLocation("notenoughfakepixel","skyblock/stats/heart.png");
    private final ResourceLocation MANA = new ResourceLocation("notenoughfakepixel","skyblock/stats/mana.png");
    private final ResourceLocation EXP = new ResourceLocation("notenoughfakepixel","skyblock/stats/exp.png");
    private final ResourceLocation SPEED = new ResourceLocation("notenoughfakepixel","skyblock/stats/speed.png");
    private final ResourceLocation DEFENCE = new ResourceLocation("notenoughfakepixel","skyblock/stats/defense.png");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e){
        if(e.type == 2){
            updateStats(e.message);

        }
    }

    public static void setSpeed(int sp){
        speed = sp;
    }

    private void updateStats(IChatComponent message) {
        String text1 = message.getUnformattedText();
        if(text1.contains("❤") && text1.contains("❇") && text1.contains("✎")){
            String text = text1.replace("❤","").replace("❇","").replace("✎","");
                String[] stats = text.split(" {5}");
                String heal = StringUtils.stripControlCodes(stats[0].trim());
                String def = StringUtils.stripControlCodes(stats[1].trim());
                String man = StringUtils.stripControlCodes(stats[2].trim());
                defence = Integer.parseInt(def.replace("Defense","").trim());
                maxHealth = Integer.parseInt(heal.split("/")[1].trim());
                health = Integer.parseInt(heal.split("/")[0].trim());
                overflowHealth = Math.max(0,health - maxHealth);
                String[] manas = man.replace(" Mana","").split(" ");
                mana = Integer.parseInt(manas[0].split("/")[0].trim());
                maxMana = Integer.parseInt(manas[0].split("/")[1].trim());
                if(man.contains("ʬ")){
                    overflowMana = Integer.parseInt(manas[1].replace("ʬ","").trim());
                }


        }

    }

    @SubscribeEvent
    public void onDraw(RenderGameOverlayEvent e){
        if(maxHealth > 0 && maxMana > 0) {
            if (e.type == RenderGameOverlayEvent.ElementType.EXPERIENCE || e.type == RenderGameOverlayEvent.ElementType.ARMOR || e.type == RenderGameOverlayEvent.ElementType.HEALTH || e.type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT || e.type == RenderGameOverlayEvent.ElementType.FOOD) {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post e){
        if (e.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        if(maxMana != 0 || maxHealth != 0){
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int xHealth = sr.getScaledWidth() / 2 - 91;
            int xMana = sr.getScaledWidth() / 2 + 11;
            int xSpeed = sr.getScaledWidth() / 2 - 175;
            int ySpeed = sr.getScaledHeight() - 10;
            int y = sr.getScaledHeight() - 32;
            int yExp = sr.getScaledHeight() - 42;
            int barWidth = 80;
            int barHeight = 7;
            drawExpBar(xHealth,yExp);
            drawHealth(xHealth,y,barWidth,barHeight);
            drawMana(xMana,y,barWidth,barHeight);
            drawSpeedBar(xSpeed,ySpeed,barWidth,barHeight);
        }
    }

    private void drawSpeedBar(int x, int y, int barWidth, int barHeight) {
        GlStateManager.pushMatrix();
        int filled = Math.min((int)((speed/400f) * barWidth),barWidth);
        GlStateManager.resetColor();
        GlStateManager.color(1f,1f,1f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(SPEED);
        Gui.drawModalRectWithCustomSizedTexture(x - 11,y - 1,0f,0f,9,9,9,9);
        Minecraft.getMinecraft().getTextureManager().bindTexture(EXP_EMPTY);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0f,0f,barWidth,barHeight,barWidth,barHeight);
        Minecraft.getMinecraft().getTextureManager().bindTexture(EXP_FILL);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,filled,barHeight,barWidth,barHeight);
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        String text = String.valueOf(speed);
        font.drawStringWithShadow(text,x + (float) barWidth / 2 - (float) font.getStringWidth(text) / 2,y - 4,new Color(184,185,196).getRGB());

        GlStateManager.popMatrix();
    }

    private void drawExpBar(int x, int y) {
        int barWidth = 182;
        int barHeight = 8;
        float progress = Minecraft.getMinecraft().thePlayer.experience;
        int filled = (int)(progress * barWidth);
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(EXP);
        Gui.drawModalRectWithCustomSizedTexture(x - 11,y - 1,0f,0f,9,9,9,9);
        Minecraft.getMinecraft().getTextureManager().bindTexture(EXP_EMPTY);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0f,0f,barWidth,barHeight,barWidth,barHeight);
        GlStateManager.resetColor();
        GlStateManager.color(0.7843f,1f,0.5607f,1f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(EXP_FILL);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,filled,barHeight,barWidth,barHeight);
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        String text = String.valueOf(Minecraft.getMinecraft().thePlayer.experienceLevel);
        font.drawStringWithShadow(text,x + (float) barWidth / 2 - (float) font.getStringWidth(text) / 2,y - 4,new Color(200,255,143).getRGB());
        GlStateManager.popMatrix();
    }

    private void drawMana(int x, int y, int barWidth, int barHeight) {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.resetColor();
        GlStateManager.color(1f,1f,1f,1f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(MANA);
        Gui.drawModalRectWithCustomSizedTexture(x - 11,y - 1,0f,0f,9,9,9,9);
        Minecraft.getMinecraft().getTextureManager().bindTexture(BAR_EMPTY);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
        GlStateManager.resetColor();
        GlStateManager.color(0.0745f,0.905f,1f,1f);
        int filledWidth = (int) ((mana / (float) maxMana) * barWidth);
        Minecraft.getMinecraft().getTextureManager().bindTexture(BAR_FILL);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,filledWidth,barHeight,barWidth,barHeight);
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        font.drawStringWithShadow(String.valueOf(mana),x + (float) barWidth / 2 - (float) font.getStringWidth(String.valueOf(mana)) / 2,y - 4,new Color(19,231,255).getRGB());
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    public void drawHealth(int x,int y,int barWidth,int barHeight){
        GlStateManager.pushMatrix();
        GlStateManager.resetColor();
        GlStateManager.color(1f,1f,1f);
        GlStateManager.enableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(HEART);
        Gui.drawModalRectWithCustomSizedTexture(x - 11,y - 1,0f,0f,9,9,9,9);
        Minecraft.getMinecraft().getTextureManager().bindTexture(BAR_EMPTY);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
        GlStateManager.resetColor();
        GlStateManager.color(1f,0f,0f,1f);
        int filledWidth = Math.min((int) ((health / (float) maxHealth) * barWidth),barWidth);
        Minecraft.getMinecraft().getTextureManager().bindTexture(BAR_FILL);
        Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,filledWidth,barHeight,barWidth,barHeight);
        if(overflowHealth > 0){
            int fillWidth = (int)((overflowHealth / (float) maxHealth) * barWidth);
            GlStateManager.color(1f,0.84f,0.074f);
            Gui.drawModalRectWithCustomSizedTexture(x,y,0,0,fillWidth,barHeight,barWidth,barHeight);
        }
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        font.drawStringWithShadow(String.valueOf(health),x + (float) barWidth / 2 - (float) font.getStringWidth(String.valueOf(health)) / 2,y - 4 ,Color.red.getRGB());
        GlStateManager.resetColor();
        GlStateManager.popMatrix();

    }

}
