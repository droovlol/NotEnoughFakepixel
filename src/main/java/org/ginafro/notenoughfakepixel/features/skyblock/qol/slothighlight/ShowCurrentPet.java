package org.ginafro.notenoughfakepixel.features.skyblock.qol.slothighlight;

import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;

import java.awt.*;

@RegisterEvents
public class ShowCurrentPet extends HightlightSlot {

    @Override
    public String getLoreLine() {
        return "Click to despawn!";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getContainerName() {
        return "Pets";
    }

    @Override
    public boolean getConfigOption() {
        return Config.feature.qol.qolShowPetEquipped;
    }

    @Override
    public Color getHighlightColor() {
        return ColorUtils.getColor(Config.feature.qol.qolPetEquippedColor);
    }

    @Override
    public boolean highlightOnlyFirst() {
        return true;
    }

}
