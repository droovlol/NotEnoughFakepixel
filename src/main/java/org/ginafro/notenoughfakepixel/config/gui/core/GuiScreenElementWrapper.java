package org.ginafro.notenoughfakepixel.config.gui.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.gui.config.ConfigEditor;
import org.lwjgl.input.Mouse;

public class GuiScreenElementWrapper extends GuiScreen {

    public final GuiElement element;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private String lastConfigState; // Last serialized state of config
    private long lastChangeTime; // For debouncing
    private static final long SAVE_DELAY = 1000; // 1 second debounce
    private Configuration config; // Reference to the Configuration

    public GuiScreenElementWrapper(GuiElement element) {
        this.element = element;
        // Get Configuration from ConfigEditor
        if (element instanceof ConfigEditor) {
            this.config = ((ConfigEditor) element).getConfiguration();
            this.lastConfigState = gson.toJson(config);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        element.render();

        // Check for changes after debounce period
        if (config != null && System.currentTimeMillis() - lastChangeTime >= SAVE_DELAY) {
            checkAndSaveConfig();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        element.mouseInput(i, j);
        if (config != null) {
            lastChangeTime = System.currentTimeMillis(); // Potential change
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        element.keyboardInput();
        if (config != null) {
            lastChangeTime = System.currentTimeMillis(); // Potential change
        }
    }

    @Override
    public void onGuiClosed() {
        NotEnoughFakepixel.instance.saveConfig();
    }

    /**
     * Manually saves the current configuration to file.
     */
    public void saveConfig() {
        NotEnoughFakepixel.instance.saveConfig();
        if (config != null) {
            lastConfigState = gson.toJson(config); // Update last known state
        }
    }

    /**
     * Checks if the configuration has changed and saves if necessary.
     */
    private void checkAndSaveConfig() {
        if (config == null) return;

        String currentConfigState = gson.toJson(config);
        if (!currentConfigState.equals(lastConfigState)) {
            System.out.println("Configuration changed, saving...");
            saveConfig();
            lastConfigState = currentConfigState;
        }
    }

    /**
     * Gets the wrapped Configuration object, if available.
     */
    public Configuration getConfiguration() {
        return config;
    }
}