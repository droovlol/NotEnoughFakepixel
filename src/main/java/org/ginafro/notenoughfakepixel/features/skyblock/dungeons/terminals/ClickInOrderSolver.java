package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.ginafro.notenoughfakepixel.utils.SoundUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.LinkedList;

@RegisterEvents
public class ClickInOrderSolver {

    private int currentRound = 0;
    private int clientNextToClick = 1;
    private static final int SLOT_SIZE = 16;
    private static final int REGION_COLS = 7;
    private static final int REGION_ROWS = 2;
    private final LinkedList<Integer> clickQueue = new LinkedList<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (!Config.feature.dungeons.dungeonsTerminalClickInOrderSolver) return;
        if (!(event.gui instanceof GuiChest)) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        Container container = ((GuiChest) event.gui).inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (Config.feature.dungeons.dungeonsCustomGuiClickIn && title.startsWith("Click in")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onOpen(GuiOpenEvent e) {
        if (!Config.feature.dungeons.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (e.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) e.gui;
            Container container = chest.inventorySlots;
            if (container instanceof ContainerChest) {
                String title = ((ContainerChest) container).getLowerChestInventory()
                        .getDisplayName().getUnformattedText();
                if (title.startsWith("Click in")) {
                    currentRound = 0;
                    clientNextToClick = 1;
                    clickQueue.clear();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!Config.feature.dungeons.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (!(event.gui instanceof GuiChest)) return;
        GuiChest chest = (GuiChest) event.gui;
        Container container = chest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory()
                .getDisplayName().getUnformattedText();
        if (!title.startsWith("Click in")) return;
        ContainerChest containerChest = (ContainerChest) container;

        updateCurrentRound(containerChest);
        if (Config.feature.dungeons.dungeonsCustomGuiClickInQueue) {
            processQueue(containerChest);
        }

        int nextToClick = Config.feature.dungeons.dungeonsCustomGuiClickInQueue ? clientNextToClick : currentRound + 1;

        if (Config.feature.dungeons.dungeonsCustomGuiClickIn) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            float scale = Config.feature.dungeons.dungeonsTerminalsScale;
            int guiWidth = (int) (REGION_COLS * SLOT_SIZE * scale);
            int guiHeight = (int) (REGION_ROWS * SLOT_SIZE * scale);
            int screenWidth = sr.getScaledWidth();
            int screenHeight = sr.getScaledHeight();
            int guiLeft = (screenWidth - guiWidth) / 2;
            int guiTop = (screenHeight - guiHeight) / 2;

            GlStateManager.pushMatrix();
            GlStateManager.translate(guiLeft, guiTop, 0);
            GlStateManager.scale(scale, scale, 1.0f);
            Gui.drawRect(-2, -12, (REGION_COLS * SLOT_SIZE) + 2, (REGION_ROWS * SLOT_SIZE) + 2, 0x80000000);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                    "§8[§bNEF§8] §aClick in Order!",
                    0, -10, 0xFFFFFF);

            for (int row = 1; row <= REGION_ROWS; row++) {
                for (int col = 1; col <= REGION_COLS; col++) {
                    int slotIndex = row * 9 + col;
                    Slot slot = containerChest.getSlot(slotIndex);
                    if (slot == null || slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                    if (slot.getStack() == null || !(Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane))
                        continue;

                    int overlayColor = 0;
                    if (slot.getStack().getItemDamage() != 5) {
                        if (slot.getStack().stackSize == nextToClick) {
                            overlayColor = ColorUtils.getColor(Config.feature.dungeons.dungeonsCorrectColor).getRGB();
                        } else if (slot.getStack().stackSize == nextToClick + 1) {
                            overlayColor = ColorUtils.getColor(Config.feature.dungeons.dungeonsAlternativeColor).getRGB();
                        } else if (slot.getStack().stackSize == nextToClick + 2) {
                            Color baseColor = ColorUtils.getColor(Config.feature.dungeons.dungeonsAlternativeColor);
                            overlayColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 150).getRGB();
                        }
                    }

                    int x = (col - 1) * SLOT_SIZE;
                    int y = (row - 1) * SLOT_SIZE;
                    drawRect(x, y, x + SLOT_SIZE, y + SLOT_SIZE, overlayColor);

                    if (slot.getStack().getItemDamage() != 5) {
                        String stackSizeText = String.valueOf(slot.getStack().stackSize);
                        int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(stackSizeText);
                        int textX = x + (SLOT_SIZE / 2) - (textWidth / 2);
                        int textY = y + (SLOT_SIZE / 2) - 4;
                        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                                stackSizeText, textX, textY, 0xFFFFFF);
                    }
                }
            }
            GlStateManager.popMatrix();
        } else {
            for (int row = 1; row <= REGION_ROWS; row++) {
                for (int col = 1; col <= REGION_COLS; col++) {
                    int slotIndex = row * 9 + col;
                    Slot slot = containerChest.getSlot(slotIndex);
                    if (slot == null || slot.inventory == Minecraft.getMinecraft().thePlayer.inventory) continue;
                    if (slot.getStack() == null || !(Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane))
                        continue;

                    int overlayColor = 0;
                    if (slot.getStack().getItemDamage() != 5) {
                        if (slot.getStack().stackSize == nextToClick) {
                            overlayColor = ColorUtils.getColor(Config.feature.dungeons.dungeonsCorrectColor).getRGB();
                        } else if (slot.getStack().stackSize == nextToClick + 1) {
                            overlayColor = ColorUtils.getColor(Config.feature.dungeons.dungeonsAlternativeColor).getRGB();
                        }
                    }

                    RenderUtils.drawOnSlot(container.inventorySlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, overlayColor);
                }
            }
        }
    }

    private void updateCurrentRound(ContainerChest containerChest) {
        for (int row = 1; row <= REGION_ROWS; row++) {
            for (int col = 1; col <= REGION_COLS; col++) {
                int slotIndex = row * 9 + col;
                Slot slot = containerChest.getSlot(slotIndex);
                if (slot == null || slot.getStack() == null) continue;
                if (Block.getBlockFromItem(slot.getStack().getItem()) instanceof BlockStainedGlassPane) {
                    if (slot.getStack().getItemDamage() == 5) {
                        if (slot.getStack().stackSize == currentRound + 1) {
                            currentRound++;
                        }
                    }
                }
            }
        }
    }

    private void processQueue(ContainerChest containerChest) {
        if (clickQueue.isEmpty()) return;
        int slotNumber = clickQueue.getFirst();
        Slot slot = containerChest.getSlot(slotNumber);
        if (slot == null || slot.getStack() == null) return;
        if (slot.getStack().getItemDamage() == 5) {
            clickQueue.removeFirst();
            SoundUtils.playSound(Minecraft.getMinecraft().thePlayer.getPosition(), "gui.button.press", 1.0f, 1.0f);
        } else {
            Minecraft mc = Minecraft.getMinecraft();
            mc.playerController.windowClick(containerChest.windowId, slotNumber, 2, 0, mc.thePlayer);
        }
    }

    @SubscribeEvent
    public void onMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!Config.feature.dungeons.dungeonsTerminalClickInOrderSolver) return;
        if (!DungeonManager.checkEssentialsF7()) return;
        if (!Mouse.getEventButtonState() || Mouse.getEventButton() != 0) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiChest)) return;
        GuiChest guiChest = (GuiChest) mc.currentScreen;
        Container container = guiChest.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String title = ((ContainerChest) container).getLowerChestInventory()
                .getDisplayName().getUnformattedText();
        if (!title.startsWith("Click in")) return;
        ContainerChest containerChest = (ContainerChest) container;

        if (Config.feature.dungeons.dungeonsCustomGuiClickIn) {
            ScaledResolution sr = new ScaledResolution(mc);
            float scale = Config.feature.dungeons.dungeonsTerminalsScale;
            int guiWidth = (int) (REGION_COLS * SLOT_SIZE * scale);
            int guiHeight = (int) (REGION_ROWS * SLOT_SIZE * scale);
            int screenWidth = sr.getScaledWidth();
            int screenHeight = sr.getScaledHeight();
            int guiLeft = (screenWidth - guiWidth) / 2;
            int guiTop = (screenHeight - guiHeight) / 2;

            int mouseX = (Mouse.getEventX() * sr.getScaledWidth()) / mc.displayWidth;
            int mouseY = sr.getScaledHeight() - (Mouse.getEventY() * sr.getScaledHeight()) / mc.displayHeight - 1;

            if (mouseX < guiLeft || mouseX >= guiLeft + guiWidth ||
                    mouseY < guiTop || mouseY >= guiTop + guiHeight) {
                event.setCanceled(true);
                return;
            }

            float relX = (mouseX - guiLeft) / scale;
            float relY = (mouseY - guiTop) / scale;
            int col = (int) (relX / SLOT_SIZE);
            int row = (int) (relY / SLOT_SIZE);
            if (col < 0 || col >= REGION_COLS || row < 0 || row >= REGION_ROWS) {
                event.setCanceled(true);
                return;
            }
            int slotIndex = (row + 1) * 9 + (col + 1);
            Slot slot = containerChest.getSlot(slotIndex);
            if (slot == null || slot.getStack() == null) {
                event.setCanceled(true);
                return;
            }

            if (Config.feature.dungeons.dungeonsCustomGuiClickInQueue) {
                if (slot.getStack().stackSize == clientNextToClick && slot.getStack().getItemDamage() != 5) {
                    clickQueue.add(slot.slotNumber);
                    clientNextToClick++;
                    event.setCanceled(true);
                }
            } else {
                if (slot.getStack().stackSize == currentRound + 1 && slot.getStack().getItemDamage() != 5) {
                    mc.playerController.windowClick(container.windowId, slot.slotNumber, 2, 0, mc.thePlayer);
                    SoundUtils.playSound(mc.thePlayer.getPosition(), "gui.button.press", 1.0f, 1.0f);
                    event.setCanceled(true);
                }
            }
        } else {
            Slot hoveredSlot = guiChest.getSlotUnderMouse();
            if (hoveredSlot != null && hoveredSlot.getStack() != null) {
                if (Config.feature.dungeons.dungeonsCustomGuiClickInQueue) {
                    if (hoveredSlot.getStack().stackSize == clientNextToClick && hoveredSlot.getStack().getItemDamage() != 5) {
                        clickQueue.add(hoveredSlot.slotNumber);
                        clientNextToClick++;
                        event.setCanceled(true);
                    }
                } else {
                    if (hoveredSlot.getStack().stackSize == currentRound + 1 && hoveredSlot.getStack().getItemDamage() != 5) {
                        mc.playerController.windowClick(container.windowId, hoveredSlot.slotNumber, 2, 0, mc.thePlayer);
                        SoundUtils.playSound(mc.thePlayer.getPosition(), "gui.button.press", 1.0f, 1.0f);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private static void drawRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }
}