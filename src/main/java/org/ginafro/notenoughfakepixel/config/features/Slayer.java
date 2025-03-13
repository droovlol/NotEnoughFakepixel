package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorColour;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorDropdown;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

public class Slayer {

    @Expose
    @ConfigOption(name = "Slayer Minibosses Display", desc = "Draw a box around slayer minibosses.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean slayerMinibosses = true;

    @Expose
    @ConfigOption(name = "Miniboss Spawn Title", desc = "Show a title when a miniboss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean slayerMinibossTitle = true;

    @Expose
    @ConfigOption(name = "Miniboss Sound Notification", desc = "Play a sound when a miniboss spawns.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean slayerMinibossSound = true;

    @Expose
    @ConfigOption(name = "Slayer Minibosses Color", desc = "Color of slayer minibosses.", subcategoryId = 0)
    @ConfigEditorColour
    public String slayerColor = "0:92:154:255:255";

    @Expose
    @ConfigOption(name = "Slayer Bosses Display", desc = "Draw a box around slayer bosses.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean slayerBosses = true;

    @Expose
    @ConfigOption(name = "Slayer Bosses Color", desc = "Color of slayer bosses.", subcategoryId = 0)
    @ConfigEditorColour
    public String slayerBossColor = "0:92:154:255:255";

    @Expose
    @ConfigOption(name = "Faster Maddox Calling", desc = "Method for faster Maddox calling.", subcategoryId = 1)
    @ConfigEditorDropdown(values = {"Auto Open", "Semi Auto", "Disabled"})
    public int slayerMaddoxCalling = 1;

    @Expose
    @ConfigOption(name = "Slayer Boss Time", desc = "Show slayer boss time.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean slayerBossTimer = true;

    @Expose
    @ConfigOption(name = "Show Beacon Waypoint", desc = "Show waypoint for beacon.", subcategoryId = 2)
    @ConfigEditorBoolean
    public boolean slayerShowBeaconPath = true;

    @Expose
    @ConfigOption(name = "Beacon Color", desc = "Color of beacon waypoint.", subcategoryId = 2)
    @ConfigEditorColour
    public String slayerBeaconColor = "0:128:0:128:255";

    @Expose
    @ConfigOption(name = "Display Blaze Pillar Title", desc = "Display title when blaze pillar is nearby.", subcategoryId = 3)
    @ConfigEditorBoolean
    public boolean slayerFirePillarDisplay = true;

    @Expose
    @ConfigOption(name = "Blaze Attunements Display", desc = "Display blaze attunements.", subcategoryId = 3)
    @ConfigEditorBoolean
    public boolean slayerBlazeAttunements = true;
}