package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorColour;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorDropdown;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Slayer {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    // Slayer Mobs (subcategoryId = 0)
    @Expose
    @ConfigOption(name = "Slayer Minibosses Display", desc = "Draw a box around slayer minibosses.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean slayerMinibosses = true;

    @Expose
    @ConfigOption(name = "Miniboss Spawn Title", desc = "Show a title when a miniboss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean slayerMinibossTitle = true;

    @Expose
    @ConfigOption(name = "Miniboss Sound Notification", desc = "Play a sound when a miniboss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean slayerMinibossSound = true;

    @Expose
    @ConfigOption(name = "Slayer Minibosses Color", desc = "Color of slayer minibosses.", subcategoryId = 0)
    @ConfigEditorColour
    public static String slayerColor = "0:92:154:255:255";

    @Expose
    @ConfigOption(name = "Slayer Bosses Display", desc = "Draw a box around slayer bosses.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean slayerBosses = true;

    @Expose
    @ConfigOption(name = "Slayer Bosses Color", desc = "Color of slayer bosses.", subcategoryId = 0)
    @ConfigEditorColour
    public static String slayerBossColor = "0:92:154:255:255";

    // Quality of Life (subcategoryId = 1)
    @Expose
    @ConfigOption(name = "Faster Maddox Calling", desc = "Method for faster Maddox calling.", subcategoryId = 1)
    @ConfigEditorDropdown(values = {"Auto Open", "Semi Auto", "Disabled"})
    public static int slayerMaddoxCalling = 1;

    @Expose
    @ConfigOption(name = "Slayer Boss Time", desc = "Show slayer boss time.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean slayerBossTimer = true;

    // Voidgloom Seraph (subcategoryId = 2)
    @Expose
    @ConfigOption(name = "Show Beacon Waypoint", desc = "Show waypoint for beacon.", subcategoryId = 2)
    @ConfigEditorBoolean
    public static boolean slayerShowBeaconPath = true;

    @Expose
    @ConfigOption(name = "Beacon Color", desc = "Color of beacon waypoint.", subcategoryId = 2)
    @ConfigEditorColour
    public static String slayerBeaconColor = "0:128:0:128:255";

    // Inferno Demonlord (subcategoryId = 3)
    @Expose
    @ConfigOption(name = "Display Blaze Pillar Title", desc = "Display title when blaze pillar is nearby.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean slayerFirePillarDisplay = true;

    @Expose
    @ConfigOption(name = "Blaze Attunements Display", desc = "Display blaze attunements.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean slayerBlazeAttunements = true;

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : Slayer.class.getDeclaredFields()) {
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
            for (Field field : Slayer.class.getDeclaredFields()) {
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
