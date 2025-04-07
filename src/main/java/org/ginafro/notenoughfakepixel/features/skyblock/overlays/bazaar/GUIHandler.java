package org.ginafro.notenoughfakepixel.features.skyblock.overlays.bazaar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

public class GUIHandler {

    @SubscribeEvent
    public void onOpen(GuiOpenEvent e){
        if(e.gui instanceof GuiContainer){
            GuiContainer container = (GuiContainer) e.gui;
            if(container.inventorySlots instanceof ContainerChest) {
                ContainerChest chest = (ContainerChest) container.inventorySlots;
                System.out.println(chest.getLowerChestInventory().getDisplayName().getUnformattedText());
                if (chest.getLowerChestInventory().getDisplayName().getUnformattedText().startsWith("Bazaar")) {
                    NotEnoughFakepixel.openGui = new BazaarOverlay(e.gui);
                }
            }
        }
    }

}
