package org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageOverlay extends GuiScreen {
    public static int buttonCount = 0;
    public static int enderChests = 0;
    public final GuiScreen g;
    public final GuiContainer gc;
    public int boxWidth,boxHeight,xPos,yPos;
    public int buttonWidth,buttonHeight;
    public int buttonListHeight;
    public static GuiTextField searchBar;
    public float scale;
    private int scrollOffset = 0; // Tracks scrolling
    private final int ROWS_VISIBLE = 2;
    private int BUTTONS_PER_ROW = 4;
    private HashMap<Integer,Boolean> chestMap = new HashMap<>();
    public StorageOverlay(GuiScreen gs){
        g = gs;
        gc = (GuiContainer) gs;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        buttonCount = 0;
        enderChests = 0;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        scale = (float) Minecraft.getMinecraft().displayWidth/width;
        xPos = sr.getScaledWidth() / 16 - 10;
        yPos = sr.getScaledHeight() / 16;
//        buttonWidth = (int)(sr.getScaledWidth() * 0.09375);
//        buttonHeight = (int)(sr.getScaledHeight() * 0.10856);
        buttonWidth = 300 / sr.getScaleFactor();
        buttonHeight = 200 / sr.getScaleFactor();
        boxWidth = (sr.getScaledWidth() / 8) * 7 + 20;
        boxHeight = (sr.getScaledHeight() / 8) * 6;
        buttonListHeight = boxHeight - sr.getScaledHeight() / 16;
        searchBar = new GuiTextField(1001,mc.fontRendererObj,(width / 2 - 125)/sr.getScaleFactor(),10/sr.getScaleFactor(),250/sr.getScaleFactor(),35/sr.getScaleFactor());
    for(int i = 0;i < 9;i++){
        if(gc.inventorySlots.getSlot(9 + i).getStack().getDisplayName().toLowerCase().contains("ender")){
            buttonCount++;
            enderChests++;
        }
    }
    if(enderChests > 9){
        enderChests = 9;
    }
    for(int i = 0;i < 18;i++){
        if(!gc.inventorySlots.getSlot(27 + i).getStack().getDisplayName().toLowerCase().contains("empty")){
            buttonCount++;
        }
    }
    int minus = 0;
    for(int i = 0; i < buttonCount;i++){
        if(i+1 > enderChests){
            minus = enderChests;
        }
        this.buttonList.add(new StorageButton(i, -99, -99, buttonWidth, buttonHeight,(i)-minus));

    }
        updateVisibleButtons();
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ResourceLocation bg = new ResourceLocation("notenoughfakepixel","backgrounds/dark/background.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(bg);
//        GuiScreen.drawModalRectWithCustomSizedTexture(xPos,yPos,0f,0f,boxWidth,buttonListHeight,boxWidth,buttonListHeight);
        int c = ColorUtils.getColor(NotEnoughFakepixel.feature.overlays.storageColor).getRGB();
        drawRect(xPos,yPos,boxWidth,buttonListHeight,c);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int scaleFactor = new ScaledResolution(mc).getScaleFactor();
        GL11.glScissor(xPos * scaleFactor, (mc.displayHeight - (yPos + buttonListHeight) * scaleFactor), boxWidth * scaleFactor, buttonListHeight * scaleFactor);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        updateVisibleButtons();
        drawInventory(mouseX,mouseY);
        for(GuiButton gb : buttonList){
            if(gb instanceof StorageButton) {
                StorageButton b = (StorageButton) gb;
                String type = b.id == b.n ? "Ender Chest(Page" + (b.n + 1) + ")" : "Backpack" + (b.n + 1);
                StorageData data = CustomConfigHandler.loadStorageData(type);
                Map<Integer, ItemStack> stackMap = null;
                try {
                    if(data != null){
                        stackMap = data.getItemStacks();
                    }
                } catch (NBTException e) {
                    throw new RuntimeException(e);
                }
                if (stackMap != null) {
                    float slotSize =(b.width -4f)/ 9.00f;
                    float slotScale = (slotSize - 2f)/16.00f;
                    for (Map.Entry<Integer, ItemStack> entry : stackMap.entrySet()) {
                        int slotIndex = entry.getKey();
                        ItemStack stack = entry.getValue();
                        int columns = 9;
                        int row = slotIndex / columns;
                        int col = slotIndex % columns;
                        float slotX = b.xPosition + 2 + col * (16 * slotScale + 2);
                        float slotY = b.yPosition + 15 + row * (16 * slotScale + 2);
                        int size = (int) (16 * slotScale);
                        if (mouseX >= slotX && mouseX <= slotX + size &&
                                mouseY >= slotY && mouseY <= slotY + size) {
                            drawToolTip(stack, mouseX, mouseY,slotScale);
                        }
                    }
                }
            }
        }
        if(!NotEnoughFakepixel.feature.overlays.storageSearch){
         searchBar.setVisible(false);
         searchBar.setFocused(false);
         searchBar.setEnabled(false);
        }else {
            searchBar.drawTextBox();
        }
    }

    private void drawToolTip(ItemStack stack, int mouseX, int mouseY,float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(mouseX,mouseY,0);
        GlStateManager.scale(scale,scale,scale);
        renderToolTip(stack,0,0);
        GlStateManager.popMatrix();
    }

    private void drawInventory(int mouseX,int mouseY){
        GlStateManager.scale(1f,1f,1f);
        int xIndex = 0;
        int yIndex = 0;
        int iy = (yPos + buttonListHeight + 5);
        int ix = (this.width / 2 - 80 + 3  );
        drawRect((ix - 3),iy,ix + (157),iy+(82),new Color(31,31,31,125).getRGB());
        drawString(fontRendererObj,"INVENTORY", ix, iy + 3,-1);
        for(int i = 0; i < Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots.size();i++){
            Slot slot = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();
            if(stack != null && stack.stackSize > 0) {
                int x = ix + (16 * xIndex) ;
                int y = iy + (16 * yIndex) ;
                GlStateManager.color(1.0f,1.0f,1.0f);
                RenderHelper.enableGUIStandardItemLighting();
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack,x,y);
                if(stack.stackSize > 1) {
                    fontRendererObj.drawString(String.valueOf(stack.stackSize), x + 14, y + 14, -1);
                }
                if(mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16){
                    this.zLevel = 500;
                    this.renderToolTip(stack,mouseX,mouseY);
                    this.zLevel = 1;
                }
            }
            xIndex++;
            if (xIndex > 8) {
                xIndex = 0;
                yIndex++;
            }
        }
    }


    private void updateVisibleButtons() {
        int startIndex = scrollOffset * BUTTONS_PER_ROW;
        int endIndex = Math.min(startIndex + ROWS_VISIBLE * BUTTONS_PER_ROW, buttonList.size());

        ScaledResolution sr = new ScaledResolution(mc);
        int xIndex = 0;
        int yIndex = 0;
        for (int i = 0; i < buttonList.size(); i++) {
            GuiButton button = buttonList.get(i);
            if (i >= startIndex && i < endIndex) {
                button.visible = true;
                button.enabled = true;
                button.xPosition = xPos + (70 / sr.getScaleFactor()) + xIndex * (buttonWidth + 5);
                button.yPosition = yPos + (70 / sr.getScaleFactor()) + yIndex * (buttonHeight + 5);
                xIndex++;
                if (xIndex >= BUTTONS_PER_ROW) {
                    xIndex = 0;
                    yIndex++;
                }
            } else {
                button.visible = false;
                button.enabled = false;
            }
        }
        for(GuiButton button : buttonList){
            if(button instanceof StorageButton){
                if(button.yPosition + button.height > yPos + buttonListHeight){
                    button.visible = false;
                }else{
                    button.visible = true;
                }
            }
        }
    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button instanceof StorageButton){
            StorageButton b = (StorageButton) button;
            b.process(this.gc);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            int maxScrollOffset = Math.max(0, (buttonCount + BUTTONS_PER_ROW) / BUTTONS_PER_ROW - ROWS_VISIBLE);
            scrollOffset = Math.max(0, Math.min(scrollOffset - wheel / 120, maxScrollOffset));
            updateVisibleButtons();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        searchBar.mouseClicked(mouseX,mouseY,mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(Keyboard.KEY_ESCAPE == keyCode ){
            if(!searchBar.isFocused()){
                Minecraft.getMinecraft().displayGuiScreen(null);
            }else{
                searchBar.setFocused(false);
            }
            return;
        }
        if(Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode){
            if(!searchBar.isFocused()){
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }

        searchBar.textboxKeyTyped(typedChar,keyCode);
    }


    public static class StorageEvent {

        @SubscribeEvent
        public void onRender(RenderGameOverlayEvent.Pre e){
            if(e.type == RenderGameOverlayEvent.ElementType.HOTBAR){
                if(Minecraft.getMinecraft().currentScreen instanceof StorageOverlay) {
                    e.setCanceled(true);
                }
            }
        }

    @SubscribeEvent
    public void onOpen(GuiScreenEvent.BackgroundDrawnEvent e) {
        if (e.gui instanceof GuiContainer) {
            GuiContainer gc = (GuiContainer) e.gui;
            if(gc instanceof GuiInventory) return;
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
