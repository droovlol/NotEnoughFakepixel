package org.ginafro.notenoughfakepixel.features.skyblock.overlays.bazaar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;

import java.awt.*;

public class BazaarOverlay extends GuiScreen {
    GuiScreen parent;
    GuiButton selected;
    public BazaarOverlay(GuiScreen s){
        parent = s;
    }

    @Override
    public void initGui() {
        GuiContainer container = (GuiContainer)parent;
        ContainerChest chest = (ContainerChest) container.inventorySlots;
        mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        GuiButton farming = new GuiButton(1,sr.getScaledWidth() / 5 - 15,15,"");
        GuiButton mining = new GuiButton(2,sr.getScaledWidth() / 5 - 15,15,"");
        GuiButton combat = new GuiButton(3,sr.getScaledWidth() / 5 - 15,15,"");
        GuiButton woodfish = new GuiButton(4,sr.getScaledWidth() / 5 - 15,15,"");
        GuiButton oddities = new GuiButton(5,sr.getScaledWidth() / 5 - 15,15,"");
        farming.visible = false;
        mining.visible = false;
        combat.visible = false;
        woodfish.visible = false;
        oddities.visible = false;
        this.buttonList.add(oddities);
        this.buttonList.add(farming);
        this.buttonList.add(mining);
        this.buttonList.add(combat);
        this.buttonList.add(woodfish);
        String name = chest.getLowerChestInventory().getDisplayName().getUnformattedText();
        switch(name){
            case "Farming":
                selected = farming;
                break;
            case "Mining":
                selected = mining;
                break;
            case "Combat":
                selected = combat;
                break;
            case "Woods":
                selected = woodfish;
                break;
            case "Oddities":
                selected = oddities;
                break;
            default:
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        drawRect(5,5,sr.getScaledWidth() - 5,sr.getScaledHeight() - 5, new Color(31,31,31,42).getRGB());
        if(selected != null) {
            drawRect((sr.getScaledWidth() / 5 * selected.id) - 15, 15, (sr.getScaledWidth() / 5 * selected.id) + 15, 45, new Color(31, 31, 31, 178).getRGB());
            for (GuiButton b : this.buttonList) {
                if (b.id != selected.id && b.id < 6) {
                    drawRect(b.xPosition, b.yPosition, b.xPosition + 30, b.yPosition + 30, new Color(31, 31, 31, 102).getRGB());
                }
            }
        }
    }

}
