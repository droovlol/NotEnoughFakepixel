package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigEditorBoolean;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.ConfigOption;

public class Fishing {

    @Expose
    @ConfigOption(name = "Notify Legendary Creatures", desc = "Notify when a legendary creature is caught.")
    @ConfigEditorBoolean
    public boolean fishingLegendaryCreatures = true;

    @Expose
    @ConfigOption(name = "Notify on Trophy Fish", desc = "Notify when a trophy fish is caught.")
    @ConfigEditorBoolean
    public boolean fishingTrophyFish = true;
}