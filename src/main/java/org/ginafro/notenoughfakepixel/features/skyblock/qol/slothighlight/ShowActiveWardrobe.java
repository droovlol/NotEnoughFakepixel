package org.ginafro.notenoughfakepixel.features.skyblock.qol.slothighlight;

import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;

@RegisterEvents
public class ShowActiveWardrobe extends HightlightSlot {

    @Override
    public String getLoreLine() {
        return "";
    }

    @Override
    public String getName() {
        return "Equipped";
    }

    @Override
    public String getContainerName() {
        return "Wardrobe";
    }

    @Override
    public boolean getConfigOption() {
        return Config.feature.qol.qolShowWardrobeSlot;
    }

}
