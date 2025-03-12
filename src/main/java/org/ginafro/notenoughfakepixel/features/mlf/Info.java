package org.ginafro.notenoughfakepixel.features.mlf;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Info {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();
    // MLF Info HUD (subcategoryId = 1 for organization)
    @Expose
    @ConfigOption(name = "MLF Info HUD", desc = "Enable the MLF info HUD.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean mlfInfoHud = true;

    @Expose
    @ConfigOption(name = "MLF Info Offset X", desc = "Horizontal offset of the MLF info HUD.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1800.0f, minStep = 1.0f)
    public static float mlfInfoOffsetX = 5.0f;

    @Expose
    @ConfigOption(name = "MLF Info Offset Y", desc = "Vertical offset of the MLF info HUD.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1250.0f, minStep = 1.0f)
    public static float mlfInfoOffsetY = 0.0f;

    @Expose
    @ConfigOption(name = "MLF Info Background Color", desc = "Background color of the MLF info HUD.", subcategoryId = 1)
    @ConfigEditorColour
    public static String mlfInfoBackgroundColor = "0:102:0:0:0"; // Default: semi-transparent black

    @Expose
    @ConfigOption(name = "Edit MLF Info Position", desc = "Adjust the MLF info HUD position visually", subcategoryId = 1)
    @ConfigEditorButton(runnableId = "editMlfInfoPosition", buttonText = "Edit Position")
    public static String editMlfInfoPositionButton = "";

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : Info.class.getDeclaredFields()) {
                if (field.isAnnotationPresent(Expose.class) && field.isAnnotationPresent(ConfigOption.class)) {
                    field.setAccessible(true);
                    staticFieldValues.put(field.getName(), field.get(null)); // null because static
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load static fields from the map after deserialization
    public void loadStaticFields() {
        try {
            for (Field field : Info.class.getDeclaredFields()) {
                if (field.isAnnotationPresent(Expose.class) && field.isAnnotationPresent(ConfigOption.class)) {
                    field.setAccessible(true);
                    Object value = staticFieldValues.get(field.getName());
                    if (value != null) {
                        if (field.getType() == int.class && value instanceof Number) {
                            field.setInt(null, ((Number) value).intValue());
                        } else if (field.getType() == boolean.class && value instanceof Boolean) {
                            field.setBoolean(null, (Boolean) value);
                        } else {
                            field.set(null, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}