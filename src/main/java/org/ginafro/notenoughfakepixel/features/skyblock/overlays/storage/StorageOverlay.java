package org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTException;
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
    public float scale;
    private int scrollOffset = 0;
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
        scale = (float) Minecraft.getMinecraft().displayWidth / width;
        buttonWidth = 400 / sr.getScaleFactor();
        buttonHeight = 250 / sr.getScaleFactor();
        int totalButtonsWidth = BUTTONS_PER_ROW * buttonWidth + (BUTTONS_PER_ROW - 1) * COL_SPACING;
        int totalButtonsHeight = ROWS_VISIBLE * buttonHeight + (ROWS_VISIBLE - 1) * ROW_SPACING;
        boxWidth = totalButtonsWidth + 2 * PADDING;
        buttonListHeight = totalButtonsHeight + 2 * PADDING;
        xPos = (sr.getScaledWidth() - boxWidth) / 2;
        yPos = (sr.getScaledHeight() - buttonListHeight) / 2 - 20; // Move previews up by 20 pixels
        searchBar = new GuiTextField(1001, mc.fontRendererObj, xPos + (boxWidth - 250) / 2, yPos - 45, 250, 35); // Center search bar above previews
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
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        drawScrollBar();
        updateVisibleButtons();
        drawInventory(mouseX, mouseY);
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
                        if (mouseX >= slotX && mouseX <= slotX + size && mouseY >= slotY && mouseY <= slotY + size) {
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
        renderToolTip(stack, mouseX, mouseY);
        GlStateManager.popMatrix();
    }

    private void drawScrollBar() {
        int totalRows = (buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW;
        if (totalRows <= ROWS_VISIBLE) return;
        int scrollBarWidth = 10;
        int scrollBarHeight = (int) ((float) ROWS_VISIBLE / totalRows * buttonListHeight);
        int maxScrollOffset = totalRows - ROWS_VISIBLE;
        scrollBarY = yPos + (int) ((float) scrollOffset / maxScrollOffset * (buttonListHeight - scrollBarHeight));
        drawRect(xPos + boxWidth - scrollBarWidth, yPos, xPos + boxWidth, yPos + buttonListHeight, Color.GRAY.getRGB());
        drawRect(xPos + boxWidth - scrollBarWidth, scrollBarY, xPos + boxWidth, scrollBarY + scrollBarHeight, Color.LIGHT_GRAY.getRGB());
    }

    private void drawInventory(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        int invWidth = 176;
        int invHeight = 120; // Increased inventory height to make it look bigger
        int ix = (width - invWidth) / 2;
        int iy = yPos + buttonListHeight + 10;
        int invColor = ColorUtils.getColor(NotEnoughFakepixel.feature.overlays.storageColor).getRGB();
        drawRect(ix, iy, ix + invWidth, iy + invHeight, invColor);
        fontRendererObj.drawStringWithShadow("INVENTORY", ix + 8, iy + 6, 0xFFFFFF);
        int armorStartX = ix + 80;
        for (int i = 0; i < 4; i++) {
            Slot slot = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i);
            if (slot != null && slot.getStack() != null) {
                int armorX = armorStartX + i * 18;
                int armorY = iy + 6;
                renderItem(slot.getStack(), armorX, armorY, mouseX, mouseY);
            }
        }
        int invGridX = ix + 8;
        int invGridY = iy + 30; // Adjusted Y position for main inventory grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = 9 + row * 9 + col;
                Slot slot = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(slotIndex);
                if (slot != null && slot.getStack() != null) {
                    int slotX = invGridX + col * 18;
                    int slotY = invGridY + row * 18;
                    renderItem(slot.getStack(), slotX, slotY, mouseX, mouseY);
                }
            }
        }
        int hotbarY = iy + 100; // Adjusted Y position for hotbar
        for (int col = 0; col < 9; col++) {
            int slotIndex = 36 + col;
            Slot slot = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(slotIndex);
            if (slot != null && slot.getStack() != null) {
                int slotX = invGridX + col * 18;
                renderItem(slot.getStack(), slotX, hotbarY, mouseX, mouseY);
            }
        }
    }

    private void renderItem(ItemStack stack, int x, int y, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        if (stack.stackSize > 1) {
            fontRendererObj.drawStringWithShadow(String.valueOf(stack.stackSize), x + 14, y + 14, -1);
        }
        if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
            this.zLevel = 500;
            renderToolTip(stack, mouseX, mouseY);
            this.zLevel = 0;
        }
    }

    private void updateVisibleButtons() {
        int startIndex = scrollOffset * BUTTONS_PER_ROW;
        int endIndex = Math.min(startIndex + ROWS_VISIBLE * BUTTONS_PER_ROW, buttonList.size());
        for (int i = 0; i < buttonList.size(); i++) {
            GuiButton button = buttonList.get(i);
            if (i >= startIndex && i < endIndex) {
                button.visible = true;
                button.enabled = true;
                int row = (i - startIndex) / BUTTONS_PER_ROW;
                int col = (i - startIndex) % BUTTONS_PER_ROW;
                button.xPosition = xPos + PADDING + col * (buttonWidth + COL_SPACING);
                button.yPosition = yPos + PADDING + row * (buttonHeight + ROW_SPACING);
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
            int maxScrollOffset = Math.max(0, (buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW - ROWS_VISIBLE);
            scrollOffset = Math.max(0, Math.min(scrollOffset + (wheel > 0 ? -1 : 1), maxScrollOffset));
            updateVisibleButtons();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        searchBar.mouseClicked(mouseX, mouseY, mouseButton);
        int scrollBarWidth = 10;
        if (mouseX >= xPos + boxWidth - scrollBarWidth && mouseX <= xPos + boxWidth && mouseY >= scrollBarY && mouseY <= scrollBarY + (int) ((float) ROWS_VISIBLE / ((buttonCount + BUTTONS_PER_ROW - 1) / BUTTONS_PER_ROW) * buttonListHeight)) {
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
            int maxScrollOffset = totalRows - ROWS_VISIBLE;
            float scrollFraction = (float) (mouseY - yPos) / buttonListHeight;
            scrollOffset = Math.max(0, Math.min((int) (scrollFraction * totalRows), maxScrollOffset));
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