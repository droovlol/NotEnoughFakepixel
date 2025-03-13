package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.Dungeons;
import org.ginafro.notenoughfakepixel.utils.ItemUtils;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

import java.awt.*;

public class ShowNotOpenedChests {

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e){
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsShowOpenedChests) return;
        if (ScoreboardUtils.currentGamemode != Gamemode.SKYBLOCK) return;
        if (!(e.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) e.gui;
        Container container = chest.inventorySlots;

        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Croesus")) return;

        ContainerChest containerChest = (ContainerChest) container;
        for(Slot slot : containerChest.inventorySlots) {
            // Skip player inventory
            if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
            ItemStack item = slot.getStack();
            // Skip empty slots
            if (item == null) continue;
            // Check if the item is a skull
            if (item.getItem() instanceof ItemSkull) {

                if(ItemUtils.getLoreLine(item, "No Chests Opened!") != null){
                    highlightSlotGreen(slot, chest);
                }

                else if(ItemUtils.getLoreLine(item, "No more chests to open!") != null){
                    highlightSlotRed(slot, chest);
                }

            }

        }
    }

    public static void highlightSlotGreen(Slot slot, GuiChest chest) {
        RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(55, 255, 55).getRGB());
    }

    public static void highlightSlotRed(Slot slot, GuiChest chest) {
        RenderUtils.drawOnSlot(chest.inventorySlots.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(255, 55, 55).getRGB());
    }

}
