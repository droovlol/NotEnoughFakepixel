package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorColour;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

import java.util.HashMap;
import java.util.Map;

public class DianaF {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    @Expose
    @ConfigOption(name = "Show Waypoints on Burrows", desc = "Show waypoints on burrows.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean dianaShowWaypointsBurrows = true;

    @Expose
    @ConfigOption(name = "Empty Burrow Color", desc = "Color of empty burrows.", subcategoryId = 0)
    @ConfigEditorColour
    public String dianaEmptyBurrowColor = "0:100:0:255:255";

    @Expose
    @ConfigOption(name = "Show Labels on Waypoints", desc = "Show labels on burrow waypoints.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean dianaShowLabelsWaypoints = true;

    @Expose
    @ConfigOption(name = "Mob Burrow Color", desc = "Color of mob burrows.", subcategoryId = 0)
    @ConfigEditorColour
    public String dianaMobBurrowColor = "0:255:255:255:255";

    @Expose
    @ConfigOption(name = "Show Tracers on Waypoints", desc = "Show tracers on burrow waypoints.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean dianaShowTracersWaypoints = true;

    @Expose
    @ConfigOption(name = "Treasure Burrow Color", desc = "Color of treasure burrows.", subcategoryId = 0)
    @ConfigEditorColour
    public String dianaTreasureBurrowColor = "0:255:0:0:255";

    @Expose
    @ConfigOption(name = "Track Gaia Hits", desc = "Track when Gaia Construct can be damaged.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean dianaGaiaConstruct = true;

    @Expose
    @ConfigOption(name = "Gaia Hittable Color", desc = "Color when Gaia is hittable.", subcategoryId = 1)
    @ConfigEditorColour
    public String dianaGaiaHittableColor = "0:250:255:0:255";

    @Expose
    @ConfigOption(name = "Show Hittable Siamese", desc = "Show when Siamese can be damaged.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean dianaSiamese = true;

    @Expose
    @ConfigOption(name = "Gaia Un-hittable Color", desc = "Color when Gaia is not hittable.", subcategoryId = 1)
    @ConfigEditorColour
    public String dianaGaiaUnhittableColor = "0:255:0:0:255";

    @Expose
    @ConfigOption(name = "Minos Inquisitor Alert", desc = "Alert when Minos Inquisitor is dug.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean dianaMinosInquisitorAlert = true;

    @Expose
    @ConfigOption(name = "Minos Inquisitor Outline", desc = "Outline Minos Inquisitors.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean dianaMinosInquisitorOutline = true;

    @Expose
    @ConfigOption(name = "Siamese Hittable Color", desc = "Color when Siamese is hittable.", subcategoryId = 1)
    @ConfigEditorColour
    public String dianaSiameseHittableColor = "0:250:255:0:255";

    @Expose
    @ConfigOption(name = "Waypoint Sounds", desc = "Enable sounds for waypoint creation.", subcategoryId = 2)
    @ConfigEditorBoolean
    public boolean dianaWaypointSounds = true;

    @Expose
    @ConfigOption(name = "Disable Explosion Sounds", desc = "Disable burrow digging explosion sounds.", subcategoryId = 2)
    @ConfigEditorBoolean
    public boolean dianaDisableDianaExplosionSounds = false;

    @Expose
    @ConfigOption(name = "Disable Ancestral Spade Cooldown Message", desc = "Mute Ancestral Spade cooldown message.", subcategoryId = 3)
    @ConfigEditorBoolean
    public boolean dianaCancelCooldownSpadeMessage = true;
}