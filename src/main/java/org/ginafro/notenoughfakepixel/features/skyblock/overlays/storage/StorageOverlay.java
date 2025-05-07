package org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.CustomConfigHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageOverlay extends GuiScreen {
    public static int buttonCount = 0;
    public static int enderChests = 0;
    public final GuiScreen g;
    public final GuiContainer gc;
    public int boxWidth;
    public int boxHeight;
    public int xPos;
    public int yPos;
    public int buttonWidth;
    public int buttonHeight;
    public int buttonListHeight;
    public static GuiTextField searchBar;
    private float scrollOffset = 0.0f;
    private final int ROWS_VISIBLE = 2;
    private final int BUTTONS_PER_ROW = 3;
    private final int ROW_SPACING = 10;
    private final int COL_SPACING = 10;
    private final int PADDING = 10;
    private HashMap<Integer, Boolean> chestMap = new HashMap<>();
    private boolean isDraggingScrollBar = false;
    private int scrollBarY = 0;

    public StorageOverlay(GuiScreen gs) {
        g = gs;
        gc = (GuiContainer) gs;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        buttonCount = 0;
        enderChests = 0;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int totalButtonsWidth = (int)(this.width * 0.8);
        buttonWidth = (totalButtonsWidth - 2 * PADDING - (BUTTONS_PER_ROW - 1) * COL_SPACING) / BUTTONS_PER_ROW;

        int totalButtonsHeight = (int)(this.height * 0.6);
        buttonHeight = (totalButtonsHeight - 2 * PADDING - (ROWS_VISIBLE - 1) * ROW_SPACING) / ROWS_VISIBLE;

        boxWidth = totalButtonsWidth;
        buttonListHeight = totalButtonsHeight;

        xPos = (this.width - boxWidth) / 2;
        yPos = (this.height - buttonListHeight) / 2 - 20;

        int searchBarWidth = (int)(boxWidth * 0.6);
        searchBar = new GuiTextField(1001, mc.fontRendererObj, xPos + (boxWidth - searchBarWidth) / 2, yPos - 45, searchBarWidth, 35);

        for (int i = 0; i < 9; i++) {
            Slot slot = gc.inventorySlots.getSlot(9 + i);
            if (slot != null && slot.getStack() != null && slot.getStack().getDisplayName().toLowerCase().contains("ender")) {
                buttonCount++;
                enderChests++;
            }
        }
        if (enderChests > 9) {
            enderChests = 9;
        }
        for (int i = 0; i < 18; i++) {
            Slot slot = gc.inventorySlots.getSlot(27 + i);
            if (slot != null && slot.getStack() != null && !slot.getStack().getDisplayName().toLowerCase().contains("empty")) {
                buttonCount++;
            }
        }
        int minus = 0;
        for (int i = 0; i < buttonCount; i++) {
            if (i + 1 > enderChests) {
                minus = enderChests;
            }
            this.buttonList.add(new StorageButton(i, -99, -99, buttonWidth, buttonHeight, (i) - minus));
        }
        updateVisibleButtons();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        int c = ColorUtils.getColor(NotEnoughFakepixel.feature.overlays.storageColor).getRGB();
        drawRect(xPos, yPos, xPos + boxWidth, yPos + buttonListHeight, c);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(xPos * sr.getScaleFactor(), (mc.displayHeight - (yPos + buttonListHeight) * sr.getScaleFactor()), boxWidth * sr.getScaleFactor(), buttonListHeight * sr.getScaleFactor());

        GlStateManager.pushMatrix();
        float slotHeight = buttonHeight + ROW_SPACING;
        float pixelOffset = scrollOffset * slotHeight;
        GlStateManager.translate(0, -pixelOffset, 0);

        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.popMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        drawScrollBar();
        updateVisibleButtons();
        for (GuiButton gb : buttonList) {
            if (gb instanceof StorageButton && gb.visible) {
                StorageButton b = (StorageButton) gb;
                String type = b.id == b.n ? "Ender Chest(Page" + (b.n + 1) + ")" : "Backpack" + (b.n + 1);
                StorageData data = CustomConfigHandler.loadStorageData(type);
                Map<Integer, ItemStack> stackMap = null;
                try {
                    if (data != null) {
                        stackMap = data.getItemStacks();
                    }
                } catch (NBTException e) {
                    throw new RuntimeException(e);
                }
                if (stackMap != null) {
                    float slotSize = (b.width - 4f) / 9.00f;
                    float slotScale = (slotSize - 2f) / 16.00f;
                    for (Map.Entry<Integer, ItemStack> entry : stackMap.entrySet()) {
                        int slotIndex = entry.getKey();
                        ItemStack stack = entry.getValue();
                        int columns = 9;
                        int row = slotIndex / columns;
                        int col = slotIndex % columns;
                        float slotX = b.xPosition + 2 + col * (16 * slotScale + 2);
                        float slotY = b.yPosition + 15 + row * (16 * slotScale + 2);
                        int size = (int) (16 * slotScale);
                        // Calculate the rendered Y-position of the slot
                        float renderedSlotY = slotY - pixelOffset;
                        // Only show tooltip if the slot is within the visible area and under the mouse
                        if (mouseX >= slotX && mouseX <= slotX + size &&
                                mouseY >= renderedSlotY && mouseY <= renderedSlotY + size &&
                                renderedSlotY >= yPos && renderedSlotY + size <= yPos + buttonListHeight) {
                            drawToolTip(stack, mouseX, mouseY);
                        }
                    }
                }
            }
        }
        if (!NotEnoughFakepixel.feature.overlays.storageSearch) {
            searchBar.setVisible(false);
            searchBar.setFocused(false);
            searchBar.setEnabled(false);
        } else {
            searchBar.drawTextBox();
        }
    }

    private void drawToolTip(ItemStack stack, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 500);
        renderCustomToolTip(stack, mouseX, mouseY);
        GlStateManager.popMatrix();
    }

    private void renderCustomToolTip(ItemStack stack, int x, int y) {
        List<String> tooltip = stack.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
        for (int i = 0; i < tooltip.size(); ++i) {
            if (i == 0) {
                tooltip.set(i, stack.getRarity().rarityColor + tooltip.get(i));
            } else {
                tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
            }
        }
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = Minecraft.getMinecraft().fontRendererObj;
        drawHoveringText(tooltip, x, y, font);
    }

    protected void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = font.getStringWidth(textLine);
                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            int tooltipX = x + 12;
            int tooltipY = y - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
            }

            if (tooltipX + tooltipTextWidth > this.width) {
                tooltipX = this.width - tooltipTextWidth - 8;
            }

            if (tooltipY + tooltipHeight + 6 > this.height) {
                tooltipY = this.height - tooltipHeight - 6;
            }

            this.zLevel = 300.0F;
            int backgroundColor = 0xF0100010;
            drawGradientRect(tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            int borderColorStart = 0x505000FF;
            int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            drawGradientRect(tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            for (int i = 0; i < textLines.size(); ++i) {
                String line = textLines.get(i);
                font.drawStringWithShadow(line, (float)tooltipX, (float)tooltipY, -1);
                if (i == 0) {
                    tooltipY += 2;
                }
                tooltipY += 10;
            }

            this.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    private void drawScrollBar() {
        int totalRows = (buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW;
        if (totalRows <= ROWS_VISIBLE) return;
        int scrollBarWidth = 10;
        float scrollBarHeight = (float) ROWS_VISIBLE / totalRows * buttonListHeight;
        float maxScrollOffset = totalRows - ROWS_VISIBLE;
        float scrollFraction = scrollOffset / maxScrollOffset;
        scrollBarY = yPos + (int) (scrollFraction * (buttonListHeight - scrollBarHeight));
        drawRect(xPos + boxWidth - scrollBarWidth, yPos, xPos + boxWidth, yPos + buttonListHeight, Color.GRAY.getRGB());
        drawRect(xPos + boxWidth - scrollBarWidth, scrollBarY, xPos + boxWidth, scrollBarY + (int) scrollBarHeight, Color.LIGHT_GRAY.getRGB());
    }

    private void updateVisibleButtons() {
        int totalRows = (buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW;
        float slotHeight = buttonHeight + ROW_SPACING;
        int firstRow = (int) Math.floor(scrollOffset);
        int lastRow = firstRow + ROWS_VISIBLE + 1;

        for (int i = 0; i < buttonList.size(); i++) {
            GuiButton button = buttonList.get(i);
            int row = i / BUTTONS_PER_ROW;
            int col = i % BUTTONS_PER_ROW;

            if (row >= firstRow && row <= lastRow) {
                button.visible = true;
                button.enabled = true;
                button.xPosition = xPos + PADDING + col * (buttonWidth + COL_SPACING);
                button.yPosition = yPos + PADDING + row * (int) slotHeight;
            } else {
                button.visible = false;
                button.enabled = false;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof StorageButton) {
            StorageButton b = (StorageButton) button;
            b.process(this.gc);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            float scrollAmount = wheel > 0 ? -0.2f : 0.2f;
            int totalRows = (buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW;
            float maxScrollOffset = Math.max(0, totalRows - ROWS_VISIBLE);
            scrollOffset = Math.max(0, Math.min(scrollOffset + scrollAmount, maxScrollOffset));
            updateVisibleButtons();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        float slotHeight = buttonHeight + ROW_SPACING;
        float pixelOffset = scrollOffset * slotHeight;
        int adjustedMouseY = mouseY + (int) pixelOffset;
        super.mouseClicked(mouseX, adjustedMouseY, mouseButton);
        searchBar.mouseClicked(mouseX, mouseY, mouseButton);
        int scrollBarWidth = 10;
        float scrollBarHeight = (float) ROWS_VISIBLE / ((buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW) * buttonListHeight;
        if (mouseX >= xPos + boxWidth - scrollBarWidth && mouseX <= xPos + boxWidth && mouseY >= scrollBarY && mouseY <= scrollBarY + (int) scrollBarHeight) {
            isDraggingScrollBar = true;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isDraggingScrollBar = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (isDraggingScrollBar) {
            int totalRows = (buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW;
            float maxScrollOffset = totalRows - ROWS_VISIBLE;
            float scrollFraction = (float) (mouseY - yPos) / buttonListHeight;
            scrollOffset = Math.max(0, Math.min(scrollFraction * totalRows, maxScrollOffset));
            updateVisibleButtons();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (Keyboard.KEY_ESCAPE == keyCode) {
            if (!searchBar.isFocused()) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else {
                searchBar.setFocused(false);
            }
            return;
        }
        if (Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode) {
            if (!searchBar.isFocused()) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }
        searchBar.textboxKeyTyped(typedChar, keyCode);
    }

    public static class StorageEvent {
        @SubscribeEvent
        public void onRender(RenderGameOverlayEvent.Pre e) {
            if (e.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
                if (Minecraft.getMinecraft().currentScreen instanceof StorageOverlay) {
                    e.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e) {
            if (e.gui instanceof GuiContainer) {
                GuiContainer gc = (GuiContainer) e.gui;
                if (gc instanceof GuiInventory) return;
                if (gc.inventorySlots.getSlot(4).getStack() != null) {
                    if (gc.inventorySlots.getSlot(4).getStack().getDisplayName().contains("Ender")) {
                        if (NotEnoughFakepixel.feature.overlays.storageOverlay) {
                            Minecraft.getMinecraft().displayGuiScreen(new StorageOverlay(e.gui));
                        }
                    }
                }
            }
        }
    }
}