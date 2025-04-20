package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class GuiUtils {

    public int getCurrentWindowId() {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
            return -1;
        }

        GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;

        return chest.inventorySlots.windowId;
    }

    public static void drawHorizontalLine(int x,int x1,int y,int width,int color){
        Gui.drawRect(x,y,x1,y+width,color);
    }


    public static void drawVerticalLine(int y,int y1,int x,int width,int color){
        Gui.drawRect(x,y,x+width,y1,color);
    }

    public static void drawOutlineRect(int x,int y,int width,int height, int lineWidth,int color){
        drawHorizontalLine(x,x+width,y,lineWidth,color);
        drawHorizontalLine(x,x+width,y+height,lineWidth,color);
        drawVerticalLine(y,y+height,x,lineWidth,color);
        drawVerticalLine(y,y+height,x+width,lineWidth,color);
    }


    public static void renderToolTip(ItemStack stack, int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for(int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
            } else {
                list.set(i, EnumChatFormatting.GRAY + (String)list.get(i));
            }
        }

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        drawHoveringText(list, x, y, font == null ? mc.fontRendererObj : font);
    }

    public static void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
        net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(textLines, x, y, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, -1, font);
    }

}
