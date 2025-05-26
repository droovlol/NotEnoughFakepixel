package org.ginafro.notenoughfakepixel.features.skyblock.qol.slothighlight;

import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;

@RegisterEvents
public class ShowNotOpenedChestsGreen extends HightlightSlot {

    @Override
    public String getLoreLine() {
        return "No Chests Opened!";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getContainerName() {
        return "Croesus";
    }

    @Override
    public boolean getConfigOption() {
        return Config.feature.dungeons.dungeonsShowOpenedChests;
    }

}
