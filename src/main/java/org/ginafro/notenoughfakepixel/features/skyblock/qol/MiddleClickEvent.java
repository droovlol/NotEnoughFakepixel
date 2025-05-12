package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Mouse;

import java.util.Arrays;
import java.util.List;

@RegisterEvents
public class MiddleClickEvent {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final List<String> excludedNames = Arrays.asList(
            "Chest", "Large Chest", "Anvil", "Storage", "Drill Anvil", "Enchant Item",
            "Runic Pedestal", "Rune Removal", "Reforge Anvil", "Reforge Item",
            "Offer Pets", "Exp Sharing", "Convert to Dungeon Item", "Upgrade Item", "Accessory Bag"
    );

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (!Config.feature.qol.qolMiddleClickChests) return;
        if (Mouse.getEventButton() != 0 || !Mouse.getEventButtonState()) return;
        if (!(mc.currentScreen instanceof GuiChest)) return;

        GuiChest chestGui = (GuiChest) mc.currentScreen;
        ContainerChest container = (ContainerChest) chestGui.inventorySlots;
        String chestName = container.getLowerChestInventory().getDisplayName().getUnformattedText();

        // Exclusion checks
        if (excludedNames.contains(chestName)) return;
        if (chestName.startsWith("Wardrobe") || chestName.startsWith("Ender Chest") || chestName.contains("Backpack"))
            return;

        event.setCanceled(true);

        int slot = chestGui.getSlotUnderMouse() != null ? chestGui.getSlotUnderMouse().slotNumber : -1;

        if (slot >= 0) {
            mc.playerController.windowClick(
                    container.windowId,
                    slot,
                    2,
                    0,
                    mc.thePlayer
            );
        }
    }
}
