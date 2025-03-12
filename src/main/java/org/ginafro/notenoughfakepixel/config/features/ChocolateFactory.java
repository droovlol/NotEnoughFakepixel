package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorColour;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ChocolateFactory {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    @Expose
    @ConfigOption(name = "Show Waypoints on Chocolate Eggs", desc = "Show waypoints on chocolate eggs.")
    @ConfigEditorBoolean
    public static boolean chocolateChocolateEggWaypoints = true;

    @Expose
    @ConfigOption(name = "Show best upgrade on Chocolate factory", desc = "Show waypoints on chocolate eggs.")
    @ConfigEditorBoolean
    public static boolean chocolateChocolateShowBestUpgrade = true;

    @Expose
    @ConfigOption(name = "Chocolate Eggs Waypoints Color", desc = "Color of chocolate eggs waypoints.")
    @ConfigEditorColour
    public static String chocolateChocolateEggWaypointsColor = "0:210:105:30:255";

    @Expose
    @ConfigOption(name = "Show Best Upgrade", desc = "Show the best upgrade available.")
    @ConfigEditorBoolean
    public static boolean chocolateShowBestUpgrade = true;

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : ChocolateFactory.class.getDeclaredFields()) {
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
            for (Field field : ChocolateFactory.class.getDeclaredFields()) {
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