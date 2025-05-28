package org.ginafro.notenoughfakepixel.events;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.util.BlockPos;

public class SecretChestOpenedEvent extends CustomEvent{

    public GuiChest chest;

    public SecretChestOpenedEvent(GuiChest chest){
        this.chest = chest;
    }

}
