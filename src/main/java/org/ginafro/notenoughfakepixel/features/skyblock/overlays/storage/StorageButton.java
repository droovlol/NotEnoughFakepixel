package org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTException;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.CustomConfigHandler;

import java.awt.Color;
import java.util.Map;

public class StorageButton extends GuiButton {
    public final int n;
    private Map<Integer, ItemStack> stackMapCache = null;
    private String typeCache = null;

    public StorageButton(int buttonId, int x, int y, int widthIn, int heightIn, int no) {
        super(buttonId, x, y, widthIn, heightIn, "");
        n = no;
    }

    public String s = "Backpack";

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.enabled || !this.visible) return;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int c = ColorUtils.getColor(NotEnoughFakepixel.feature.overlays.chestColor).getRGB();
        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, c);
        if (id == n) {
            s = "Ender Chest";
        }
        String type = id == n ? "Ender Chest(Page" + (n + 1) + ")" : "Backpack" + (n + 1);
        if (!type.equals(typeCache)) {
            StorageData data = CustomConfigHandler.loadStorageData(type);
            stackMapCache = null;
            if (data != null) {
                try {
                    stackMapCache = data.getItemStacks();
                } catch (NBTException e) {
                    throw new RuntimeException(e);
                }
            }
            typeCache = type;
        }
        if (stackMapCache == null) {
            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(31, 31, 31, 250).getRGB());
            drawCenteredString(mc.fontRendererObj, "Open For Preview", xPosition + width / 2, yPosition + height / 2, -1);
            GlStateManager.popMatrix();
            return;
        }
        String title = s + " " + (n + 1);
        // Scale title text based on button width
        float textScale = Math.min(1.0f, (float)width / 150.0f); // Base scale for width ~150 pixels
        int titleWidth = mc.fontRendererObj.getStringWidth(title);
        int titleX = xPosition + (width - (int)(titleWidth * textScale)) / 2;
        int titleY = yPosition + 5;
        GlStateManager.pushMatrix();
        GlStateManager.translate(titleX, titleY, 0);
        GlStateManager.scale(textScale, textScale, 1.0f);
        mc.fontRendererObj.drawStringWithShadow(title, 0, 0, 0xFFFFFF);
        GlStateManager.popMatrix();
        float slotSize = (width - 4f) / 9.00f;
        float slotScale = Math.max((slotSize - 2f) / 16.00f, 0.5f); // Minimum scale of 0.5
        RenderHelper.enableStandardItemLighting();
        for (Map.Entry<Integer, ItemStack> entry : stackMapCache.entrySet()) {
            int slotIndex = entry.getKey();
            ItemStack stack = entry.getValue();
            int columns = 9;
            int row = slotIndex / columns;
            int col = slotIndex % columns;
            float slotX = xPosition + col * (16 * slotScale + 2);
            float slotY = yPosition + 15 + row * (16 * slotScale + 2);
            renderItemScaled(mc, stack, slotX, slotY, slotScale);
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void renderItemScaled(Minecraft mc, ItemStack stack, float x, float y, float scale) {
        String searchText = StorageOverlay.searchBar != null ? StorageOverlay.searchBar.getText() : "";
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        drawHorizontalLine(0, 16, 0, -1);
        drawHorizontalLine(0, 16, 16, -1);
        drawVerticalLine(0, 0, 16, -1);
        drawVerticalLine(16, 0, 16, -1);
        if (!searchText.isEmpty() && stack.getDisplayName().toLowerCase().contains(searchText.toLowerCase())) {
            int color = ColorUtils.getColor(NotEnoughFakepixel.feature.overlays.searchColor).getRGB();
            drawRect(0, 0, 16, 16, color);
        }
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
    }

    public void process(GuiContainer c) {
        if (this.id == this.n) {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, 9 + n, 0, 1, Minecraft.getMinecraft().thePlayer);
        } else {
            Minecraft.getMinecraft().playerController.windowClick(c.inventorySlots.windowId, 27 + n, 0, 1, Minecraft.getMinecraft().thePlayer);
        }
    }
}