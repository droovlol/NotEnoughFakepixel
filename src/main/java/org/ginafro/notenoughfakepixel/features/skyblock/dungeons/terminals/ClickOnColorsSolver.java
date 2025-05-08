package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockCarpet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.SoundUtils;
import org.ginafro.notenoughfakepixel.variables.F7ColorsDict;
import org.lwjgl.input.Mouse;

import java.util.*;

public class ClickOnColorsSolver {

    private static final int SLOT_SIZE = 16;
    private static final int COLUMNS = 9;
    private static final int INNER_COLUMNS = 7;
    private static final int INNER_ROWS = 4;

    private final Set<Integer> clickedSlots = new HashSet<>();
    private long lastRecheckTime = 0;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsTerminalSelectColorsSolver) return;
        if (!(event.gui instanceof GuiChest)) return;
        if (!DungeonManager.checkEssentialsF7()) return;

        GuiChest chestGui = (GuiChest) event.gui;
        Container container = chestGui.inventorySlots;
        if (!(container instanceof ContainerChest)) return;

        String displayName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText().trim();
        if (NotEnoughFakepixel.feature.dungeons.dungeonsCustomGuiColors && displayName.startsWith("Select all the")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsTerminalSelectColorsSolver) return;
        if (!(event.gui instanceof GuiChest)) return;
        if (!DungeonManager.checkEssentialsF7()) return;

        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;

        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Select all the")) return;

        String targetColor = title.split("the ")[1].split(" items")[0].toLowerCase();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRecheckTime >= 1000) {
            clickedSlots.clear();
            lastRecheckTime = currentTime;
        }

        if (NotEnoughFakepixel.feature.dungeons.dungeonsCustomGuiColors) {
            List<Slot> correctSlots = new ArrayList<>();
            for (Slot slot : ((ContainerChest) container).inventorySlots) {
                int slotId = ((ContainerChest) container).inventorySlots.indexOf(slot);
                if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory || slotId == 49) continue;

                int row = slotId / COLUMNS;
                int col = slotId % COLUMNS;

                if (row < 1 || row > 4 || col < 1 || col > 7) continue;

                ItemStack item = slot.getStack();
                if (item == null || item.isItemEnchanted()) continue;

                if (isCorrectColor(item, targetColor)) {
                    correctSlots.add(slot);
                }
            }

            renderCustomGui(chest, correctSlots, targetColor);
        } else {
            handleDefaultGui(chest, targetColor);
        }
    }

    private void renderCustomGui(GuiChest chest, List<Slot> correctSlots, String targetColor) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float scale = NotEnoughFakepixel.feature.dungeons.dungeonsTerminalsScale;
        int guiWidth = (int) (INNER_COLUMNS * SLOT_SIZE * scale);
        int guiHeight = (int) (INNER_ROWS * SLOT_SIZE * scale);
        int guiLeft = (sr.getScaledWidth() - guiWidth) / 2;
        int guiTop = (sr.getScaledHeight() - guiHeight) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft, guiTop, 0);
        GlStateManager.scale(scale, scale, 1.0f);

        Gui.drawRect(-2, -12,
                (INNER_COLUMNS * SLOT_SIZE) + 2,
                (INNER_ROWS * SLOT_SIZE) + 2,
                0x80000000);

        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                "§8[§bNEF§8] §a" + capitalizeFirstLetter(targetColor),
                0, -10, 0xFFFFFF);

        for (Slot slot : correctSlots) {
            if (!clickedSlots.contains(slot.slotNumber)) {
                int slotId = ((ContainerChest) chest.inventorySlots).inventorySlots.indexOf(slot);
                int row = slotId / COLUMNS;
                int col = slotId % COLUMNS;

                int innerX = (col - 1) * SLOT_SIZE;
                int innerY = (row - 1) * SLOT_SIZE;

                drawRect(innerX + 1, innerY + 1,
                        innerX + SLOT_SIZE - 1,
                        innerY + SLOT_SIZE - 1,
                        ColorUtils.getColor(NotEnoughFakepixel.feature.dungeons.dungeonsCorrectColor).getRGB());
            }
        }
        GlStateManager.popMatrix();
    }

    private void handleDefaultGui(GuiChest chest, String targetColor) {
        ContainerChest container = (ContainerChest) chest.inventorySlots;
        for (Slot slot : container.inventorySlots) {
            if (slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;

            ItemStack item = slot.getStack();
            if (item == null) continue;

            int slotId = container.inventorySlots.indexOf(slot);
            if (slotId == 49) {
                if (NotEnoughFakepixel.feature.dungeons.dungeonsTerminalHideIncorrect) {
                    item.setItem(container.inventorySlots.get(0).getStack().getItem());
                    item.getItem().setDamage(item, 15);
                }
                continue;
            }

            if (item.isItemEnchanted()) {
                if (NotEnoughFakepixel.feature.dungeons.dungeonsTerminalHideIncorrect) {
                    item.setItem(container.inventorySlots.get(0).getStack().getItem());
                    item.getItem().setDamage(item, 15);
                }
                continue;
            }

            if (isCorrectColor(item, targetColor)) {
                RenderUtils.drawOnSlot(container.inventorySlots.size(),
                        slot.xDisplayPosition, slot.yDisplayPosition,
                        ColorUtils.getColor(NotEnoughFakepixel.feature.dungeons.dungeonsCorrectColor).getRGB());
            } else if (NotEnoughFakepixel.feature.dungeons.dungeonsTerminalHideIncorrect) {
                item.setItem(container.inventorySlots.get(0).getStack().getItem());
                item.getItem().setDamage(item, 15);
            }
        }
    }

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsPreventMissclicks) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (!Mouse.getEventButtonState()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiChest)) return;

        GuiChest guiChest = (GuiChest) mc.currentScreen;
        Container container = guiChest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;

        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!title.startsWith("Select all the")) return;

        if (NotEnoughFakepixel.feature.dungeons.dungeonsCustomGuiColors) {
            handleCustomGuiClick(event, guiChest, title);
        } else {
            handleDefaultGuiClick(event, guiChest);
        }
    }

    private void handleCustomGuiClick(GuiScreenEvent.MouseInputEvent.Pre event, GuiChest guiChest, String title) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        float scale = NotEnoughFakepixel.feature.dungeons.dungeonsTerminalsScale;

        int mouseX = (Mouse.getEventX() * sr.getScaledWidth()) / mc.displayWidth;
        int mouseY = sr.getScaledHeight() - (Mouse.getEventY() * sr.getScaledHeight()) / mc.displayHeight - 1;

        int guiWidth = (int) (INNER_COLUMNS * SLOT_SIZE * scale);
        int guiHeight = (int) (INNER_ROWS * SLOT_SIZE * scale);
        int guiLeft = (sr.getScaledWidth() - guiWidth) / 2;
        int guiTop = (sr.getScaledHeight() - guiHeight) / 2;

        if (mouseX < guiLeft || mouseX >= guiLeft + guiWidth ||
                mouseY < guiTop || mouseY >= guiTop + guiHeight) {
            event.setCanceled(true);
            return;
        }

        float relX = (mouseX - guiLeft) / scale;
        float relY = (mouseY - guiTop) / scale;

        String targetColor = title.split("the ")[1].split(" items")[0].toLowerCase();
        ContainerChest container = (ContainerChest) guiChest.inventorySlots;

        for (Slot slot : container.inventorySlots) {
            int slotId = container.inventorySlots.indexOf(slot);
            if (slot.inventory == mc.thePlayer.inventory || slotId == 49) continue;

            int row = slotId / COLUMNS;
            int col = slotId % COLUMNS;
            if (row < 1 || row > 4 || col < 1 || col > 7) continue;

            ItemStack item = slot.getStack();
            if (item == null || item.isItemEnchanted()) continue;

            if (isCorrectColor(item, targetColor)) {
                int innerX = (col - 1) * SLOT_SIZE;
                int innerY = (row - 1) * SLOT_SIZE;

                if (relX >= innerX && relX < innerX + SLOT_SIZE &&
                        relY >= innerY && relY < innerY + SLOT_SIZE) {
                    mc.playerController.windowClick(
                            container.windowId,
                            slot.slotNumber,
                            2,
                            0,
                            mc.thePlayer
                    );
                    playCompletionSound();
                    clickedSlots.add(slot.slotNumber);
                    event.setCanceled(true);
                    return;
                }
            }
        }
        event.setCanceled(true);
    }

    private void handleDefaultGuiClick(GuiScreenEvent.MouseInputEvent.Pre event, GuiChest guiChest) {
        Slot hoveredSlot = guiChest.getSlotUnderMouse();
        if (hoveredSlot != null && hoveredSlot.getStack() != null) {
            ItemStack item = hoveredSlot.getStack();
            if (Block.getBlockFromItem(item.getItem()) instanceof BlockStainedGlassPane &&
                    item.getMetadata() == 15) {
                event.setCanceled(true);
            } else {
                playCompletionSound();
            }
        }
    }

    private boolean isCorrectColor(ItemStack item, String targetColor) {
        if (item.getItem() == Items.dye) {
            return targetColor.equals(F7ColorsDict.getColorFromDye(item.getMetadata()).toString());
        } else if (Block.getBlockFromItem(item.getItem()) instanceof BlockStainedGlassPane ||
                Block.getBlockFromItem(item.getItem()) instanceof BlockStainedGlass ||
                Block.getBlockFromItem(item.getItem()) instanceof BlockColored ||
                Block.getBlockFromItem(item.getItem()) instanceof BlockCarpet) {
            return targetColor.equals(F7ColorsDict.getColorFromMain(item.getMetadata()).toString());
        }
        return false;
    }

    private String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void playCompletionSound() {
        Minecraft mc = Minecraft.getMinecraft();
        SoundUtils.playSound(
                mc.thePlayer.getPosition(),
                "gui.button.press",
                1.0f,
                1.0f
        );
    }

    private static void drawRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }
}