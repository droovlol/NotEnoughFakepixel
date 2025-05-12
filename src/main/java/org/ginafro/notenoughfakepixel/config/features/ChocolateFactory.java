package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorColour;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

public class ChocolateFactory {

    @Expose
    @ConfigOption(name = "Show Waypoints on Chocolate Eggs", desc = "Show waypoints on chocolate eggs.")
    @ConfigEditorBoolean
    public boolean chocolateChocolateEggWaypoints = true;

    @Expose
    @ConfigOption(name = "Show best upgrade on Chocolate factory", desc = "Show the best upgrade available.")
    @ConfigEditorBoolean
    public boolean chocolateChocolateShowBestUpgrade = true;

    @Expose
    @ConfigOption(name = "Chocolate Eggs Waypoints Color", desc = "Color of chocolate eggs waypoints.")
    @ConfigEditorColour
    public String chocolateChocolateEggWaypointsColor = "0:210:105:30:255";
}