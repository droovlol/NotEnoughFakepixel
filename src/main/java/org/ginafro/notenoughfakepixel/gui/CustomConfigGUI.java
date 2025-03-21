package org.ginafro.notenoughfakepixel.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import org.ginafro.notenoughfakepixel.Configuration;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class CustomConfigGUI extends GuiScreen {
    /*
    private final Configuration config;
    private final Map<String, Map<String, List<Field>>> categorizedFields = new LinkedHashMap<>();
    private String currentCategory = "General";
    private String searchQuery = "";
    private int scrollOffset = 0;
    private boolean draggingSlider = false;
    private Field currentSliderField;
    private boolean isSearchBarFocused = false;
    private int totalContentHeight = 0;
    private static final int TAB_SPACING = 5;
    private static final int SCROLL_BAR_WIDTH = 10;
    private Field openDropdownField = null;
    private Field openColorField = null;
    private List<Field> drawnFields = new ArrayList<>();
    private List<Integer> fieldYPositions = new ArrayList<>();
    private GuiTextField searchField;
    private Field hoveredField = null;
    private ColorComponent draggingColorComponent = null;
    private boolean draggingScrollBar = false;
    private int dragOffsetY = 0;
    private int scrollBarY = 0;
    private int storedPanelX;
    private int storedPanelY;
    private int storedPanelWidth;
    private int storedPanelHeight;
    private int storedScrollOffset;
    private List<Field> storedDrawnFields = new ArrayList<>();
    private List<Integer> storedFieldYPositions = new ArrayList<>();

    private enum ColorComponent {
        ALPHA, RED, GREEN, BLUE
    }

    public CustomConfigGUI(Configuration config) {
        this.config = config;
        organizeFieldsByCategory();
    }

    private void organizeFieldsByCategory() {
        Field[] fields = Configuration.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(KeyBind.class)) continue;
            String category = "General";
            String subcategory = "General";

            if (field.isAnnotationPresent(Switch.class)) {
                Switch annotation = field.getAnnotation(Switch.class);
                category = annotation.category();
                subcategory = annotation.subcategory();
            } else if (field.isAnnotationPresent(Slider.class)) {
                Slider annotation = field.getAnnotation(Slider.class);
                category = annotation.category();
                subcategory = annotation.subcategory();
            } else if (field.isAnnotationPresent(Dropdown.class)) {
                Dropdown annotation = field.getAnnotation(Dropdown.class);
                category = annotation.category();
                subcategory = annotation.subcategory();
            } else if (field.isAnnotationPresent(Color.class)) {
                Color annotation = field.getAnnotation(Color.class);
                category = annotation.category();
                subcategory = annotation.subcategory();
            } else if (field.isAnnotationPresent(Text.class)) {
                Text annotation = field.getAnnotation(Text.class);
                category = annotation.category();
                subcategory = annotation.subcategory();
            }

            categorizedFields.computeIfAbsent(category, k -> new LinkedHashMap<>())
                    .computeIfAbsent(subcategory, k -> new ArrayList<>())
                    .add(field);
        }
    }

    @Override
    public void initGui() {
        if (searchField == null) {
            searchField = new GuiTextField(0, fontRendererObj, width / 2 - 75, 40, 150, 20);
            searchField.setMaxStringLength(256);
        } else {
            searchField.xPosition = width / 2 - 75;
            searchField.yPosition = 40;
        }
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Draw title
        String title = "NotEnoughFakepixel";
        int titleWidth = fontRendererObj.getStringWidth(title);
        float titleScale = 2.0F;
        GL11.glPushMatrix();
        GL11.glScalef(titleScale, titleScale, 1.0F);
        int scaledTitleX = (int) ((width / titleScale - titleWidth) / 2);
        int scaledTitleY = 5;
        fontRendererObj.drawStringWithShadow(title, scaledTitleX, scaledTitleY, 0xFFFFFF);
        GL11.glPopMatrix();

        // Draw search bar
        fontRendererObj.drawStringWithShadow("Search:", searchField.xPosition - 50, searchField.yPosition + 4, 0xFFFFFF);
        searchField.drawTextBox();

        // Draw category tabs
        int tabY = 70;
        for (String category : categorizedFields.keySet()) {
            boolean isHovered = mouseX >= 10 && mouseX <= 160 && mouseY >= tabY && mouseY <= tabY + 20;
            drawTab(10, tabY, 150, 20, category, category.equals(currentCategory), isHovered);
            tabY += 25;
        }

        // Draw configuration panel
        int panelX = 170;
        int panelWidth = width - 180;
        int panelY = 70;
        int panelHeight = height - 80;
        drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x80444444);

        // Populate drawn fields and their positions
        drawnFields.clear();
        fieldYPositions.clear();
        int unscrolledY = panelY + 10;
        if (searchQuery.isEmpty()) {
            if (!currentCategory.equals("General")) {
                Map<String, List<Field>> currentCategoryFields = categorizedFields.get(currentCategory);
                if (currentCategoryFields != null) {
                    boolean first = true;
                    for (Map.Entry<String, List<Field>> entry : currentCategoryFields.entrySet()) {
                        if (!first) {
                            unscrolledY += 15; // Add spacing before subsequent subcategories
                        }
                        first = false;
                        drawnFields.add(null); // Subcategory header
                        fieldYPositions.add(unscrolledY);
                        unscrolledY += 25;
                        for (Field field : entry.getValue()) {
                            drawnFields.add(field);
                            fieldYPositions.add(unscrolledY);
                            unscrolledY += getControlHeight(field);
                        }
                    }
                }
            }
        } else {
            String[] searchWords = searchQuery.toLowerCase().split("\\s+");
            for (String category : categorizedFields.keySet()) {
                Map<String, List<Field>> subcategoryMap = categorizedFields.get(category);
                for (List<Field> fields : subcategoryMap.values()) {
                    for (Field field : fields) {
                        String fieldNameLower = getFieldName(field).toLowerCase();
                        boolean matches = Arrays.stream(searchWords).allMatch(fieldNameLower::contains);
                        if (matches) {
                            drawnFields.add(field);
                            fieldYPositions.add(unscrolledY);
                            unscrolledY += getControlHeight(field);
                        }
                    }
                }
            }
        }

        storedPanelX = panelX;
        storedPanelY = panelY;
        storedPanelWidth = panelWidth;
        storedPanelHeight = panelHeight;
        storedScrollOffset = scrollOffset;
        storedDrawnFields = new ArrayList<>(drawnFields);
        storedFieldYPositions = new ArrayList<>(fieldYPositions);

        // Calculate scroll bar
        totalContentHeight = calculateTotalContentHeight();
        int maxScroll = Math.max(0, totalContentHeight - panelHeight);
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        if (totalContentHeight > panelHeight) {
            float scrollPercent = (float) scrollOffset / maxScroll;
            int scrollBarHeight = Math.max(20, (int) (panelHeight * (panelHeight / (float) totalContentHeight)));
            scrollBarY = panelY + (int) ((panelHeight - scrollBarHeight) * scrollPercent);
            drawRect(panelX + panelWidth - SCROLL_BAR_WIDTH - 2, panelY,
                    panelX + panelWidth - 2, panelY + panelHeight, 0xFF333333);
            drawRect(panelX + panelWidth - SCROLL_BAR_WIDTH - 2, scrollBarY,
                    panelX + panelWidth - 2, scrollBarY + scrollBarHeight, 0xFF666666);
        }

        // Draw panel content with scissor
        enableScissor(panelX, panelY, panelX + panelWidth - SCROLL_BAR_WIDTH - 4, panelY + panelHeight);
        drawCategoryContent(panelX + 10, panelY + 10 - scrollOffset, mouseX, mouseY);
        disableScissor();

        // Draw tooltip if applicable
        if (hoveredField != null) {
            String description = getFieldDescription(hoveredField);
            if (description != null && !description.isEmpty()) {
                drawTooltip(description, mouseX, mouseY);
            }
        }
    }

    private void drawTab(int x, int y, int w, int h, String text, boolean active, boolean hovered) {
        int color = active ? 0xFF666666 : (hovered ? 0xFF555555 : 0xFF333333);
        drawRect(x, y, x + w, y + h, color);
        int textWidth = fontRendererObj.getStringWidth(text);
        fontRendererObj.drawStringWithShadow(text, x + (w - textWidth) / 2, y + (h - 8) / 2, 0xFFFFFF);
    }

    private void drawCategoryContent(int x, int y, int mouseX, int mouseY) {
        hoveredField = null;
        if (currentCategory.equals("General") && searchQuery.isEmpty()) {
            drawClickableLink(x, y, "GitHub: https://github.com/davidbelesp/NotEnoughFakepixel", "https://github.com/davidbelesp/NotEnoughFakepixel", mouseX, mouseY);
            drawClickableLink(x, y + 15, "Discord: https://discord.gg/xDr4YTTafe", "https://discord.gg/xDr4YTTafe", mouseX, mouseY);
            y += 40;
        }
        String lastSubcategory = null;
        boolean first = true;
        for (int i = 0; i < drawnFields.size(); i++) {
            Field field = drawnFields.get(i);
            if (field == null) {
                if (!first) {
                    y += 15; // Add spacing before subsequent subcategories
                }
                first = false;
                Map<String, List<Field>> currentCategoryFields = categorizedFields.get(currentCategory);
                if (currentCategoryFields != null && !searchQuery.isEmpty()) continue;
                final int nextIndex = i + 1;
                String subcategory = currentCategoryFields.entrySet().stream()
                        .filter(e -> e.getValue().contains(drawnFields.get(nextIndex)))
                        .findFirst()
                        .map(Map.Entry::getKey)
                        .orElse("General");
                if (!subcategory.equals(lastSubcategory)) {
                    float subcategoryScale = 1.5F;
                    String displayText = fontRendererObj.trimStringToWidth(subcategory, (int) ((width - 190) / subcategoryScale));
                    GL11.glPushMatrix();
                    GL11.glScalef(subcategoryScale, subcategoryScale, 1.0F);
                    int scaledX = (int) ((x + ((width - 190) / subcategoryScale - fontRendererObj.getStringWidth(displayText)) / 2) / subcategoryScale);
                    int scaledY = (int) (y / subcategoryScale);
                    fontRendererObj.drawStringWithShadow(displayText, scaledX, scaledY, 0xFFFFFF);
                    GL11.glPopMatrix();
                    lastSubcategory = subcategory;
                }
                y += 25;
                continue;
            }
            if (mouseX >= x && mouseX <= x + 350 && mouseY >= y && mouseY <= y + getControlHeight(field)) {
                hoveredField = field;
            }
            drawControl(x, y, field);
            y += getControlHeight(field);
        }
    }

    private void drawClickableLink(int x, int y, String displayText, String url, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + fontRendererObj.getStringWidth(displayText) && mouseY >= y && mouseY <= y + 8;
        fontRendererObj.drawStringWithShadow(displayText, x, y, hovered ? 0x55FFFF : 0xFFFFFF);
        if (hovered) {
            drawRect(x, y + 9, x + fontRendererObj.getStringWidth(displayText), y + 10, 0x55FFFF);
        }
    }

    private String getFieldName(Field field) {
        if (field.isAnnotationPresent(Switch.class)) return field.getAnnotation(Switch.class).name();
        if (field.isAnnotationPresent(Slider.class)) return field.getAnnotation(Slider.class).name();
        if (field.isAnnotationPresent(Dropdown.class)) return field.getAnnotation(Dropdown.class).name();
        if (field.isAnnotationPresent(Color.class)) return field.getAnnotation(Color.class).name();
        if (field.isAnnotationPresent(Text.class)) return field.getAnnotation(Text.class).name();
        return field.getName();
    }

    private int getControlHeight(Field field) {
        if (field == null) return 25;
        if (field.isAnnotationPresent(Dropdown.class) && field == openDropdownField) {
            return 20 + field.getAnnotation(Dropdown.class).options().length * 20;
        } else if (field.isAnnotationPresent(Color.class) && field == openColorField) {
            return 100;
        }
        return 25;
    }

    private int calculateTotalContentHeight() {
        if (currentCategory.equals("General") && searchQuery.isEmpty()) {
            return 40;
        }
        int height = 0;
        int subcategoryCount = 0;
        for (Field field : drawnFields) {
            if (field == null) {
                subcategoryCount++;
            }
            height += getControlHeight(field);
        }
        if (subcategoryCount > 1) {
            height += (subcategoryCount - 1) * 15; // Add 15 pixels for each transition between subcategories
        }
        return height;
    }

    private void drawControl(int x, int y, Field field) {
        if (field == null) return;
        try {
            String name = getFieldName(field);
            fontRendererObj.drawStringWithShadow(name, x, y + 4, 0xFFFFFF);

            if (field.isAnnotationPresent(Switch.class)) {
                boolean value = (boolean) field.get(null);
                drawSwitch(x + 250, y, value);
            } else if (field.isAnnotationPresent(Slider.class)) {
                Slider annotation = field.getAnnotation(Slider.class);
                float value = field.getFloat(null);
                drawSlider(x + 250, y, value, annotation.min(), annotation.max());
            } else if (field.isAnnotationPresent(Dropdown.class)) {
                Dropdown annotation = field.getAnnotation(Dropdown.class);
                int value = field.getInt(null);
                drawDropdown(x + 250, y, field, value, annotation.options());
            } else if (field.isAnnotationPresent(Color.class)) {
                OneColor value = (OneColor) field.get(null);
                drawColorPicker(x + 250, y, field, value);
            } else if (field.isAnnotationPresent(Text.class)) {
                String text = (String) field.get(null);
                drawRect(x + 250, y, x + 400, y + 20, 0xFF333333);
                fontRendererObj.drawStringWithShadow(text, x + 255, y + 6, 0xFFFFFF);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawSwitch(int x, int y, boolean value) {
        drawRect(x, y + 2, x + 40, y + 18, value ? 0xFF00FF00 : 0xFFFF0000);
        drawRect(x + (value ? 20 : 0), y, x + 20 + (value ? 20 : 0), y + 20, 0xFFFFFFFF);
    }

    private void drawSlider(int x, int y, float value, float min, float max) {
        drawRect(x, y + 4, x + 150, y + 6, 0xFF666666);
        float percentage = (value - min) / (max - min);
        int knobX = (int) (x + (150 * percentage));
        drawRect(knobX - 2, y, knobX + 2, y + 10, 0xFFFFFFFF);
    }

    private void drawDropdown(int x, int y, Field field, int value, String[] options) {
        drawRect(x, y, x + 150, y + 20, 0xFF333333);
        fontRendererObj.drawStringWithShadow(options[value], x + 5, y + 6, 0xFFFFFF);
        if (openDropdownField == field) {
            for (int i = 0; i < options.length; i++) {
                int optionY = y + 20 + (i * 20);
                drawRect(x, optionY, x + 150, optionY + 20, i == value ? 0xFF555555 : 0xFF333333);
                fontRendererObj.drawStringWithShadow(options[i], x + 5, optionY + 6, 0xFFFFFF);
            }
        } else {
            drawRect(x + 130, y, x + 150, y + 20, 0xFF666666);
            drawString(fontRendererObj, "▼", x + 135, y + 6, 0xFFFFFF);
        }
    }

    private void drawColorPicker(int x, int y, Field field, OneColor color) {
        drawRect(x, y, x + 20, y + 20, color.getRGB()); // Preview box remains at x + 250 to x + 270
        if (field == openColorField) {
            int sliderY = y + 25;
            drawColorSlider(x + 20, sliderY, "A", color.getAlpha(), 0, 255); // Shift sliders right to align with preview
            sliderY += 20;
            drawColorSlider(x + 20, sliderY, "R", color.getRed(), 0, 255);
            sliderY += 20;
            drawColorSlider(x + 20, sliderY, "G", color.getGreen(), 0, 255);
            sliderY += 20;
            drawColorSlider(x + 20, sliderY, "B", color.getBlue(), 0, 255);
            drawRect(x + 360, y, x + 380, y + 20, color.getRGB()); // Result box remains unchanged
        }
    }

    private void drawColorSlider(int x, int y, String label, int value, int min, int max) {
        fontRendererObj.drawStringWithShadow(label, x - 15, y + 4, 0xFFFFFF); // Move label left of the slider
        drawRect(x, y + 4, x + 150, y + 6, 0xFF666666); // Slider from x to x + 150 (e.g., 270 to 420)
        float percentage = (value - min) / (float) (max - min);
        int knobX = (int) (x + (150 * percentage));
        drawRect(knobX - 2, y, knobX + 2, y + 10, 0xFFFFFFFF);
        fontRendererObj.drawStringWithShadow(String.valueOf(value), x + 155, y + 4, 0xFFFFFF); // Value text after slider
    }

    private String getFieldDescription(Field field) {
        if (field.isAnnotationPresent(Switch.class)) return field.getAnnotation(Switch.class).description();
        if (field.isAnnotationPresent(Slider.class)) return field.getAnnotation(Slider.class).description();
        if (field.isAnnotationPresent(Dropdown.class)) return field.getAnnotation(Dropdown.class).description();
        if (field.isAnnotationPresent(Color.class)) return field.getAnnotation(Color.class).description();
        if (field.isAnnotationPresent(Text.class)) return field.getAnnotation(Text.class).description();
        return "";
    }

    private void drawTooltip(String text, int mouseX, int mouseY) {
        int textWidth = fontRendererObj.getStringWidth(text);
        int tooltipX = mouseX + 10;
        int tooltipY = mouseY + 10;
        drawRect(tooltipX - 2, tooltipY - 2, tooltipX + textWidth + 2, tooltipY + 10, 0xFF000000);
        fontRendererObj.drawStringWithShadow(text, tooltipX, tooltipY, 0xFFFFFF);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Handle search bar focus
        if (mouseX >= searchField.xPosition && mouseX <= searchField.xPosition + searchField.width &&
                mouseY >= searchField.yPosition && mouseY <= searchField.yPosition + searchField.height) {
            isSearchBarFocused = true;
            searchField.setFocused(true);
        } else {
            isSearchBarFocused = false;
            searchField.setFocused(false);
        }

        // Handle tab clicks
        int tabY = 70;
        for (String category : categorizedFields.keySet()) {
            if (mouseX >= 10 && mouseX <= 160 && mouseY >= tabY && mouseY <= tabY + 20) {
                currentCategory = category;
                scrollOffset = 0;
                searchQuery = "";
                break;
            }
            tabY += 25;
        }

        // Define panel boundaries
        int panelX = 170;
        int panelY = 70;
        int panelWidth = width - 180;
        int panelHeight = height - 80;

        // Handle General category links
        if (currentCategory.equals("General") && searchQuery.isEmpty()) {
            int link1ScreenY = panelY + 10 - scrollOffset;
            int link1VisibleTop = Math.max(link1ScreenY, panelY);
            int link1VisibleBottom = Math.min(link1ScreenY + 8, panelY + panelHeight);
            if (mouseX >= panelX && mouseX <= panelX + fontRendererObj.getStringWidth("GitHub: https://github.com/davidbelesp/NotEnoughFakepixel") &&
                    mouseY >= link1VisibleTop && mouseY <= link1VisibleBottom) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText("§eClick to open: ").appendSibling(
                                new ChatComponentText("§nhttps://github.com/davidbelesp/NotEnoughFakepixel")
                                        .setChatStyle(new net.minecraft.util.ChatStyle()
                                                .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/davidbelesp/NotEnoughFakepixel")))));
            }

            int link2ScreenY = panelY + 25 - scrollOffset;
            int link2VisibleTop = Math.max(link2ScreenY, panelY);
            int link2VisibleBottom = Math.min(link2ScreenY + 8, panelY + panelHeight);
            if (mouseX >= panelX && mouseX <= panelX + fontRendererObj.getStringWidth("Discord: https://discord.gg/xDr4YTTafe") &&
                    mouseY >= link2VisibleTop && mouseY <= link2VisibleBottom) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText("§eClick to open: ").appendSibling(
                                new ChatComponentText("§nhttps://discord.gg/xDr4YTTafe")
                                        .setChatStyle(new net.minecraft.util.ChatStyle()
                                                .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/xDr4YTTafe")))));
            }
        }

        // Handle control clicks
        for (int i = 0; i < storedDrawnFields.size(); i++) {
            Field field = storedDrawnFields.get(i);
            if (field == null) continue;
            int y = storedFieldYPositions.get(i);
            int screenY = y - storedScrollOffset;
            int controlHeight = getControlHeight(field);

            int visibleTop = Math.max(screenY, storedPanelY);
            int visibleBottom = Math.min(screenY + controlHeight, storedPanelY + storedPanelHeight);
            if (mouseX >= storedPanelX + 10 && mouseX <= storedPanelX + storedPanelWidth - SCROLL_BAR_WIDTH - 4 &&
                    mouseY >= visibleTop && mouseY <= visibleBottom) {
                int relativeX = mouseX - (storedPanelX + 10);
                int relativeY = mouseY - screenY;
                handleControlClick(field, relativeX, relativeY);
            }
        }

        // Handle scroll bar click
        int maxScroll = Math.max(0, totalContentHeight - panelHeight);
        if (totalContentHeight > panelHeight) {
            int scrollBarX = panelX + panelWidth - SCROLL_BAR_WIDTH - 2;
            int scrollBarWidth = SCROLL_BAR_WIDTH;
            int scrollBarHeight = Math.max(20, (int) (panelHeight * (panelHeight / (float) totalContentHeight)));
            if (mouseX >= scrollBarX && mouseX <= scrollBarX + scrollBarWidth &&
                    mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight) {
                draggingScrollBar = true;
                dragOffsetY = mouseY - scrollBarY;
            }
        }
    }

    private void handleControlClick(Field field, int relativeX, int relativeY) {
        try {
            if (field.isAnnotationPresent(Switch.class)) {
                if (relativeX >= 250 && relativeX <= 290 && relativeY >= 0 && relativeY <= 20) {
                    boolean current = (boolean) field.get(null);
                    field.set(null, !current);
                    config.save();
                }
            } else if (field.isAnnotationPresent(Slider.class)) {
                if (relativeX >= 250 && relativeX <= 400 && relativeY >= 0 && relativeY <= 10) {
                    draggingSlider = true;
                    currentSliderField = field;
                    updateSliderValue(field, relativeX);
                }
            } else if (field.isAnnotationPresent(Dropdown.class)) {
                Dropdown annotation = field.getAnnotation(Dropdown.class);
                if (relativeX >= 250 && relativeX <= 400 && relativeY >= 0 && relativeY <= 20) {
                    openDropdownField = (openDropdownField == field) ? null : field;
                } else if (openDropdownField == field && relativeX >= 250 && relativeX <= 400 && relativeY > 20) {
                    int option = (relativeY - 20) / 20;
                    if (option >= 0 && option < annotation.options().length) {
                        field.setInt(null, option);
                        config.save();
                        openDropdownField = null;
                    }
                }
            } else if (field.isAnnotationPresent(Color.class)) {
                if (relativeX >= 250 && relativeX <= 270 && relativeY >= 0 && relativeY <= 20) {
                    openColorField = (openColorField == field) ? null : field;
                } else if (field == openColorField && relativeX >= 250 && relativeX <= 400) {
                    if (relativeY >= 25 && relativeY < 45) {
                        draggingColorComponent = ColorComponent.ALPHA;
                        updateColorSlider(field, ColorComponent.ALPHA, relativeX);
                    } else if (relativeY >= 45 && relativeY < 65) {
                        draggingColorComponent = ColorComponent.RED;
                        updateColorSlider(field, ColorComponent.RED, relativeX);
                    } else if (relativeY >= 65 && relativeY < 85) {
                        draggingColorComponent = ColorComponent.GREEN;
                        updateColorSlider(field, ColorComponent.GREEN, relativeX);
                    } else if (relativeY >= 85 && relativeY < 105) {
                        draggingColorComponent = ColorComponent.BLUE;
                        updateColorSlider(field, ColorComponent.BLUE, relativeX);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        int panelX = 170;
        int panelY = 70;
        int panelHeight = height - 80;

        if (draggingSlider && currentSliderField != null) {
            int relativeX = mouseX - (panelX + 10);
            updateSliderValue(currentSliderField, relativeX);
        } else if (openColorField != null && draggingColorComponent != null) {
            int relativeX = mouseX - (panelX + 10);
            updateColorSlider(openColorField, draggingColorComponent, relativeX);
        } else if (draggingScrollBar) {
            int scrollBarHeight = Math.max(20, (int) (panelHeight * (panelHeight / (float) totalContentHeight)));
            int newScrollBarY = mouseY - dragOffsetY;
            int minY = panelY;
            int maxY = panelY + panelHeight - scrollBarHeight;
            newScrollBarY = Math.max(minY, Math.min(maxY, newScrollBarY));
            float scrollPercent = (float) (newScrollBarY - minY) / (maxY - minY);
            int maxScroll = totalContentHeight - panelHeight;
            scrollOffset = (int) (scrollPercent * maxScroll);
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        draggingSlider = false;
        currentSliderField = null;
        draggingColorComponent = null;
        draggingScrollBar = false;
    }

    private void updateSliderValue(Field field, int relativeX) {
        try {
            Slider annotation = field.getAnnotation(Slider.class);
            float min = annotation.min();
            float max = annotation.max();
            float percentage = Math.max(0, Math.min(1, (relativeX - 250) / 150f));
            float value = min + (max - min) * percentage;
            field.setFloat(null, value);
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateColorSlider(Field field, ColorComponent component, int relativeX) {
        try {
            OneColor color = (OneColor) field.get(null);
            int min = 0;
            int max = 255;
            float percentage = Math.max(0, Math.min(1, (relativeX - 250) / 150f));
            int value = (int) (min + (max - min) * percentage);
            int argb = color.getRGB();
            int a = (argb >> 24) & 0xFF;
            int r = (argb >> 16) & 0xFF;
            int g = (argb >> 8) & 0xFF;
            int b = argb & 0xFF;
            switch (component) {
                case ALPHA: a = value; break;
                case RED: r = value; break;
                case GREEN: g = value; break;
                case BLUE: b = value; break;
            }
            field.set(null, new OneColor((a << 24) | (r << 16) | (g << 8) | b));
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (isSearchBarFocused) {
            searchField.textboxKeyTyped(typedChar, keyCode);
            searchQuery = searchField.getText();
            scrollOffset = 0;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            scrollOffset -= wheel > 0 ? 20 : -20;
            int maxScroll = Math.max(0, totalContentHeight - (height - 80));
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        }
    }

    private void enableScissor(int x, int y, int x2, int y2) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int scale = sr.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x * scale, (height - y2) * scale, (x2 - x) * scale, (y2 - y) * scale);
    }

    private void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    } */
}