package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorColour;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;
import org.ginafro.notenoughfakepixel.config.gui.core.config.Position;

import java.util.HashMap;
import java.util.Map;

public class Crimson {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    @Expose
    @ConfigOption(name = "Bladesoul Notifier", desc = "Notify when Bladesoul boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean crimsonBladesoulNotifier = true;

    @Expose
    @ConfigOption(name = "Mage Outlaw Notifier", desc = "Notify when Mage Outlaw boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean crimsonMageOutlawNotifier = true;

    @Expose
    @ConfigOption(name = "Ashfang Notifier", desc = "Notify when Ashfang boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean crimsonAshfangNotifier = true;

    @Expose
    @ConfigOption(name = "Barbarian Duke X Notifier", desc = "Notify when Barbarian Duke X boss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean crimsonBarbarianDukeXNotifier = true;

    @Expose
    @ConfigOption(name = "Ashfang Waypoint", desc = "Show waypoint on Ashfang.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean crimsonAshfangWaypoint = true;

    @Expose
    @ConfigOption(name = "Ashfang Waypoint Color", desc = "Color of Ashfang waypoint.", subcategoryId = 1)
    @ConfigEditorColour
    public String crimsonAshfangWaypointColor = "0:100:255:0:255";

    @Expose
    @ConfigOption(name = "Gravity Orb Waypoint", desc = "Show waypoint on Gravity Orb.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean crimsonGravityOrbWaypoint = true;

    @Expose
    @ConfigOption(name = "Gravity Orb Waypoint Color", desc = "Color of Gravity Orb waypoint.", subcategoryId = 1)
    @ConfigEditorColour
    public String crimsonBlazingSoulWaypointColor = "0:255:255:0:255";

    @Expose
    @ConfigOption(name = "Ashfang Hitboxes", desc = "Show hitboxes for Ashfang minions.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean crimsonAshfangHitboxes = true;

    @Expose
    @ConfigOption(name = "Ashfang Mute Chat", desc = "Mute chat messages from Ashfang minions.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean crimsonAshfangMuteChat = true;

    @Expose
    @ConfigOption(name = "Ashfang Mute Sound", desc = "Mute sounds from Ashfang minions.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean crimsonAshfangMuteSound = true;

    @Expose
    @ConfigOption(name = "Ashfang Hurt Sound", desc = "Play sound when Ashfang is hit by Blazing Soul.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean crimsonAshfangHurtSound = true;

    @Expose
    @ConfigOption(name = "Ashfang Overlay", desc = "Show overlay for Ashfang HP and Blazing Souls.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean crimsonAshfangOverlay = true;

    @Expose
    public Position ashfangOverlayPos = new Position(10, 10, false, true);
}