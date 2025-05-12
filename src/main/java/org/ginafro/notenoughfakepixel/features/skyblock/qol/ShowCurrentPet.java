package org.ginafro.notenoughfakepixel.features.skyblock.qol;

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
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.*;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

@RegisterEvents
public class ShowCurrentPet {

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e){
        if (!NotEnoughFakepixel.feature.qol.qolShowPetEquipped) return;
        if (ScoreboardUtils.currentGamemode != Gamemode.SKYBLOCK) return;
        if (!(e.gui instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) e.gui;
        Container container = chest.inventorySlots;

        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Pets")) return;

        ContainerChest containerChest = (ContainerChest) container;
        for(Slot slot : containerChest.inventorySlots) {
            // Skip player inventory
            if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
            ItemStack item = slot.getStack();
            // Skip empty slots
            if (item == null) continue;
            // Check if the item is a skull
            if (item.getItem() instanceof ItemSkull) {

                String isEquipped = ItemUtils.getLoreLine(item, "Click to despawn!");
                if(isEquipped != null){
                    InventoryUtils.highlightSlot(slot, chest, ColorUtils.getColor(NotEnoughFakepixel.feature.qol.qolPetEquippedColor));
                }
            }
        }
    }
}
