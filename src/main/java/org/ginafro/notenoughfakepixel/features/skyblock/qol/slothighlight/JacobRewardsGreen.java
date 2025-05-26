package org.ginafro.notenoughfakepixel.features.skyblock.qol.slothighlight;

import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;

@RegisterEvents
public class JacobRewardsGreen extends HightlightSlot {

    @Override
    public String getLoreLine() {
        return "Click to claim reward!";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getContainerName() {
        return "Your contests";
    }

    @Override
    public boolean getConfigOption() {
        return Config.feature.qol.qolShowJacobRewards;
    }

}
