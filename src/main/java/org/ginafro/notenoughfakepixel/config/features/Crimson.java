package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorColour;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;
import org.ginafro.notenoughfakepixel.config.gui.core.config.Position;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Crimson {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    // Bosses Notifier (subcategoryId = 0)
    @Expose
    @ConfigOption(name = "Bladesoul Notifier", desc = "Notify when Bladesoul boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean crimsonBladesoulNotifier = true;

    @Expose
    @ConfigOption(name = "Mage Outlaw Notifier", desc = "Notify when Mage Outlaw boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean crimsonMageOutlawNotifier = true;

    @Expose
    @ConfigOption(name = "Ashfang Notifier", desc = "Notify when Ashfang boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean crimsonAshfangNotifier = true;

    @Expose
    @ConfigOption(name = "Barbarian Duke X Notifier", desc = "Notify when Barbarian Duke X boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean crimsonBarbarianDukeXNotifier = true;

    // Ashfang (subcategoryId = 1)
    @Expose
    @ConfigOption(name = "Ashfang Waypoint", desc = "Show waypoint on Ashfang.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean crimsonAshfangWaypoint = true;

    @Expose
    @ConfigOption(name = "Ashfang Waypoint Color", desc = "Color of Ashfang waypoint.", subcategoryId = 1)
    @ConfigEditorColour
    public static String crimsonAshfangWaypointColor = "0:100:255:0:255";

    @Expose
    @ConfigOption(name = "Gravity Orb Waypoint", desc = "Show waypoint on Gravity Orb.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean crimsonGravityOrbWaypoint = true;

    @Expose
    @ConfigOption(name = "Gravity Orb Waypoint Color", desc = "Color of Gravity Orb waypoint.", subcategoryId = 1)
    @ConfigEditorColour
    public static String crimsonBlazingSoulWaypointColor = "0:255:255:0:255";

    @Expose
    @ConfigOption(name = "Ashfang Hitboxes", desc = "Show hitboxes for Ashfang minions.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean crimsonAshfangHitboxes = true;

    @Expose
    @ConfigOption(name = "Ashfang Mute Chat", desc = "Mute chat messages from Ashfang minions.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean crimsonAshfangMuteChat = true;

    @Expose
    @ConfigOption(name = "Ashfang Mute Sound", desc = "Mute sounds from Ashfang minions.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean crimsonAshfangMuteSound = true;

    @Expose
    @ConfigOption(name = "Ashfang Hurt Sound", desc = "Play sound when Ashfang is hit by Blazing Soul.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean crimsonAshfangHurtSound = true;

    @Expose
    @ConfigOption(name = "Ashfang Overlay", desc = "Show overlay for Ashfang HP and Blazing Souls.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean crimsonAshfangOverlay = true;

    // Position for Ashfang Overlay
    @Expose
    public static Position ashfangOverlayPos = new Position(10, 10, false, true);

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : Crimson.class.getDeclaredFields()) {
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
            for (Field field : Crimson.class.getDeclaredFields()) {
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
