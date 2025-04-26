package org.ginafro.notenoughfakepixel.features.capes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.ginafro.notenoughfakepixel.features.capes.Cape;
import org.ginafro.notenoughfakepixel.features.capes.CapeManager;
import org.ginafro.notenoughfakepixel.utils.Utils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class CapeGui extends GuiScreen {

    public int wX,wY,wH,wW, bList;
    public int bW,bH;
    public float scale;
    public int scrollOffset;
    public int wheel = 0;
    public ScaledResolution sr;
    public static Cape selected;
    public Map<Integer,CapeButton> capeMap = new HashMap<>();
    public GuiTextField searchBar;
    public int sX,sY,sW,sH;
    @Override
    public void initGui() {
        super.initGui();
        mc = Minecraft.getMinecraft();
        sr = new ScaledResolution(mc);
        scale = Utils.getScale();
        wX = sr.getScaledWidth() / 2 - (((int)(749 * scale))/2);
        wY = sr.getScaledHeight() / 2 - (((int)(472 * scale))/2);
        wW = (int)(749 * scale);
        wH = (int)(472 * scale);
        bH = (int)(300 * scale);
        bList = (int) (wY + bH);
        bW = (wH - (int)(30 * scale)) / 3;
        for(int i = 0;i < CapeManager.getAllCapes().size();i++){
            Cape c = CapeManager.getAllCapes().get(i);
            CapeButton b = new CapeButton(i,-9999,-9999,bW,bH,c);
            if(c == CapeManager.getCape()){
                selected = c;
            }
            capeMap.putIfAbsent(c.capeID,b);
            buttonList.add(b);
        }
        sW = (int)(326 * scale);
        sH = (int)(37 * scale);
        sX = wX + wH - (int)(210 * scale);
        sY = wY + (int)(42 * scale);
        searchBar = new GuiTextField(1001,mc.fontRendererObj,sX,sY,sW,sH);
        searchBar.setEnableBackgroundDrawing(false);
        update();
    }

    public void update(){
        Map<Integer,CapeButton> caMap = new HashMap<>();
        if(!searchBar.getText().isEmpty()){
            capeMap.forEach((ca, cb) -> {
                if(cb.ca.capeName.startsWith(searchBar.getText())){
                    caMap.put(ca,cb);
                }
            });
        }else{
            caMap.putAll(capeMap);
        }
        for(int i = 0; i <= caMap.size();i++){
            CapeButton b = caMap.get(i);
            if(b == null){
                continue;
            }
            if(b.id < scrollOffset || b.id > scrollOffset + 4){
                b.visible = false;
                b.enabled = false;
                b.xPosition = -9999;
                b.yPosition = -9999;
            }
            if(b.id >= scrollOffset && b.id < scrollOffset + 4){
                b.xPosition = wX + (int)(20 * scale) + ((bW + (int)(10 * scale)) * Math.abs(scrollOffset - b.id));
                b.yPosition = wY + (int)(75 * scale);
                b.enabled = true;
                b.visible = true;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel","cape_manager.png"));
        drawScaledCustomSizeModalRect(wX,wY,0,0,wW,wH,wW,wH,wW,wH);
        update();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(wX * sr.getScaleFactor(),wY * sr.getScaleFactor(),wW * sr.getScaleFactor(),wH * sr.getScaleFactor());
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("notenoughfakepixel","cape_search.png"));
        drawScaledCustomSizeModalRect(sX,sY,0f,0f,sW,sH,sW,sH,sW,sH);
        searchBar.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if(keyCode == Keyboard.KEY_LEFT){
            scrollOffset -= 1;
        }
        if(keyCode == Keyboard.KEY_RIGHT){
            scrollOffset += 1;
        }
        if(scrollOffset > capeMap.size() - 4){
            scrollOffset = capeMap.size() - 4;
        }
        if(scrollOffset < 0){
            scrollOffset = 0;
        }
        if(keyCode == Keyboard.KEY_ESCAPE){
            if(searchBar.isFocused()){
                searchBar.setFocused(false);
            }else{
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }
        if(keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()){
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        searchBar.textboxKeyTyped(typedChar,keyCode);

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        wheel = Mouse.getDWheel();
        if(wheel == 0) return;
        if(wheel > 0){
            scrollOffset--;
        }else{
            scrollOffset++;
        }
        if(scrollOffset > capeMap.size() - 4){
            scrollOffset = capeMap.size() - 4;
        }
        if(scrollOffset < 0){
            scrollOffset = 0;
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        searchBar.mouseClicked(mouseX,mouseY,mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button instanceof CapeButton){
            ((CapeButton)button).process();
            selected = ((CapeButton)button).ca;
        }
    }
}
