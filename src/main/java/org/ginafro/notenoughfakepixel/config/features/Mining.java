package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class Mining {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    @Expose
    @ConfigOption(name = "Enable Mining Ability Notifier", desc = "Notify when mining ability is ready.")
    @ConfigEditorBoolean
    public boolean miningAbilityNotifier = true;

    @Expose
    @ConfigOption(name = "Disable Don Espresso Messages", desc = "Disable Don Espresso event messages.")
    @ConfigEditorBoolean
    public boolean miningDisableDonEspresso = true;

    @Expose
    @ConfigOption(name = "Fix Drill Animation Reset", desc = "Fix drill animation resetting on fuel update.")
    @ConfigEditorBoolean
    public boolean miningDrillFix = true;

    @Expose
    @ConfigOption(name = "Puzzler Solver", desc = "Enable Puzzler block solver.")
    @ConfigEditorBoolean
    public boolean miningPuzzlerSolver = true;

    @Expose
    @ConfigOption(name = "Remove Ghosts Invisibility", desc = "Remove invisibility from ghosts.")
    @ConfigEditorBoolean
    public boolean miningShowGhosts = true;

    @Expose
    @ConfigOption(name = "Mining Overlay", desc = "Enable the mining overlay in Dwarven Mines.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean miningOverlay = true;

    @Expose
    @ConfigOption(name = "Mining Overlay Offset X", desc = "Horizontal offset of the mining overlay.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1800.0f, minStep = 1.0f)
    public float miningOverlayOffsetX = 10.0f;

    @Expose
    @ConfigOption(name = "Mining Overlay Offset Y", desc = "Vertical offset of the mining overlay.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1250.0f, minStep = 1.0f)
    public float miningOverlayOffsetY = 10.0f;

    @Expose
    @ConfigOption(name = "Mining Overlay Scale", desc = "Scale of the mining overlay text.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.5f, maxValue = 5.0f, minStep = 0.1f)
    public float miningOverlayScale = 1.0f;

    @Expose
    @ConfigOption(name = "Mining Overlay Background Color", desc = "Background color of the mining overlay.", subcategoryId = 1)
    @ConfigEditorColour
    public String miningOverlayBackgroundColor = "0:150:0:0:0";

    @Expose
    @ConfigOption(name = "Show Ability Cooldown", desc = "Show the mining ability cooldown in the overlay.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean miningAbilityCooldown = true;

    @Expose
    @ConfigOption(name = "Show Mithril Powder", desc = "Show mithril powder in the overlay.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean miningMithrilPowder = true;

    @Expose
    @ConfigOption(name = "Show Drill Fuel", desc = "Show drill fuel in the overlay.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean miningDrillFuel = true;

    @Expose
    @ConfigOption(name = "Edit Mining Overlay Position", desc = "Adjust the mining overlay position visually", subcategoryId = 1)
    @ConfigEditorButton(runnableId = "editMiningOverlayPosition", buttonText = "Edit Position")
    public String editMiningOverlayPositionButton = "";
}