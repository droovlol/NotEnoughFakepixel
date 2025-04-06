package org.ginafro.notenoughfakepixel.features.skyblock.qol.CustomAliases;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class AddAliasGui extends GuiScreen{

    private GuiScreen parentScreen;
    private GuiTextField messageField;
    private GuiTextField alertField;
    private int modeIndex = 0;
    private int locationIndex = 0;
    private final String[] modes = {"Starts With", "Contains", "Ends With"};
    private final String[] locations = {"Anywhere", "Skyblock", "Dungeons"};
    private GuiButton modeButton;
    private GuiButton locationButton;

    public AddAliasGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        int centerX = width / 2;
        int centerY = height / 2;
        int fieldWidth = 200;
        int buttonWidth = 100;
        int spacing = 25;

        messageField = new GuiTextField(1, fontRendererObj, centerX - fieldWidth / 2, centerY - spacing - 10, fieldWidth, 20);
        alertField = new GuiTextField(2, fontRendererObj, centerX - fieldWidth / 2, centerY + 10, fieldWidth, 20);
        locationButton = new GuiButton(4, centerX - buttonWidth/2, centerY + spacing + 20, buttonWidth, 20, "Location: " + locations[locationIndex]);
        GuiButton saveButton = new GuiButton(5, centerX - buttonWidth / 2, centerY + 2 * spacing + 30, buttonWidth, 20, "Save");
        buttonList.add(locationButton);
        buttonList.add(saveButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, 0x80000000);

        int centerX = width / 2;
        int centerY = height / 2;
        int spacing = 25;

        String command = "Command:";
        fontRendererObj.drawString(command, centerX - fontRendererObj.getStringWidth(command) / 2, centerY - spacing - 20, 0xFFFFFF);
        messageField.drawTextBox();

        String alias = "Alias:";
        fontRendererObj.drawString(alias, centerX - fontRendererObj.getStringWidth(alias) / 2, centerY, 0xFFFFFF);
        alertField.drawTextBox();

//        String instruction = "To use Color codes use '&' it will replace the default color code symbol since we cant type it";
//        int saveButtonY = centerY + 2 * spacing + 30;
//        int instructionY = saveButtonY + 20 + 10;
//        fontRendererObj.drawString(instruction, centerX - fontRendererObj.getStringWidth(instruction) / 2, instructionY, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        messageField.textboxKeyTyped(typedChar, keyCode);
        alertField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        messageField.mouseClicked(mouseX, mouseY, mouseButton);
        alertField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 4) {
            locationIndex = (locationIndex + 1) % locations.length;
            locationButton.displayString = "Location: " + locations[locationIndex];
        } else if (button.id == 5) {
            String location = locations[locationIndex];
            String command = messageField.getText();
            if(!command.startsWith("/")){
                command = "/" + command;
            }
            String allias = alertField.getText();
            CustomAliases.Alias alias = new CustomAliases.Alias(location, command, allias, true);
            CustomAliases.aliases.add(alias);
            CustomAliases.save();
            CustomAliases.registerAliases();
            if (parentScreen instanceof AliasManagementGui) {
                parentScreen.initGui();
            }
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }


}
