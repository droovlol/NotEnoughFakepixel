package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;

public class GuiUtils {

    public int getCurrentWindowId() {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
            return -1;
        }

        GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;

        return chest.inventorySlots.windowId;
    }

}
