package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Mining {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    @Expose
    @ConfigOption(name = "Enable Mining Ability Notifier", desc = "Notify when mining ability is ready.")
    @ConfigEditorBoolean
    public static boolean miningAbilityNotifier = true;

    @Expose
    @ConfigOption(name = "Disable Don Espresso Messages", desc = "Disable Don Espresso event messages.")
    @ConfigEditorBoolean
    public static boolean miningDisableDonEspresso = true;

    @Expose
    @ConfigOption(name = "Fix Drill Animation Reset", desc = "Fix drill animation resetting on fuel update.")
    @ConfigEditorBoolean
    public static boolean miningDrillFix = true;

    @Expose
    @ConfigOption(name = "Puzzler Solver", desc = "Enable Puzzler block solver.")
    @ConfigEditorBoolean
    public static boolean miningPuzzlerSolver = true;

    @Expose
    @ConfigOption(name = "Remove Ghosts Invisibility", desc = "Remove invisibility from ghosts.")
    @ConfigEditorBoolean
    public static boolean miningShowGhosts = true;

    // Mining Overlay (subcategoryId = 1)
    @Expose
    @ConfigOption(name = "Mining Overlay", desc = "Enable the mining overlay in Dwarven Mines.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean miningOverlay = true;

    @Expose
    @ConfigOption(name = "Mining Overlay Offset X", desc = "Horizontal offset of the mining overlay.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1800.0f, minStep = 1.0f)
    public static float miningOverlayOffsetX = 10.0f;

    @Expose
    @ConfigOption(name = "Mining Overlay Offset Y", desc = "Vertical offset of the mining overlay.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1250.0f, minStep = 1.0f)
    public static float miningOverlayOffsetY = 10.0f;

    @Expose
    @ConfigOption(name = "Mining Overlay Scale", desc = "Scale of the mining overlay text.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.5f, maxValue = 5.0f, minStep = 0.1f)
    public static float miningOverlayScale = 1.0f;

    @Expose
    @ConfigOption(name = "Mining Overlay Background Color", desc = "Background color of the mining overlay.", subcategoryId = 1)
    @ConfigEditorColour
    public static String miningOverlayBackgroundColor = "0:150:0:0:0"; // Semi-transparent black

    @Expose
    @ConfigOption(name = "Show Ability Cooldown", desc = "Show the mining ability cooldown in the overlay.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean miningAbilityCooldown = true;

    @Expose
    @ConfigOption(name = "Show Mithril Powder", desc = "Show mithril powder in the overlay.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean miningMithrilPowder = true;

    @Expose
    @ConfigOption(name = "Show Drill Fuel", desc = "Show drill fuel in the overlay.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean miningDrillFuel = true;

    @Expose
    @ConfigOption(name = "Edit Mining Overlay Position", desc = "Adjust the mining overlay position visually", subcategoryId = 1)
    @ConfigEditorButton(runnableId = "editMiningOverlayPosition", buttonText = "Edit Position")
    public static String editMiningOverlayPositionButton = "";

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : Mining.class.getDeclaredFields()) {
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
            for (Field field : Mining.class.getDeclaredFields()) {
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