package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Experimentation {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    @Expose
    @ConfigOption(name = "Chronomatron Solver", desc = "Enable Chronomatron solver.")
    @ConfigEditorBoolean
    public static boolean experimentationChronomatronSolver = true;

    @Expose
    @ConfigOption(name = "Ultrasequencer Solver", desc = "Enable Ultrasequencer solver.")
    @ConfigEditorBoolean
    public static boolean experimentationUltraSequencerSolver = true;

    @Expose
    @ConfigOption(name = "Hide Tooltips", desc = "Hide tooltips during experiments.")
    @ConfigEditorBoolean
    public static boolean experimentationHideTooltips = true;

    @Expose
    @ConfigOption(name = "Prevent Missclicks", desc = "Prevent missclicks during experiments.")
    @ConfigEditorBoolean
    public static boolean experimentationPreventMissclicks = true;

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : Experimentation.class.getDeclaredFields()) {
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
            for (Field field : Experimentation.class.getDeclaredFields()) {
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
