package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

@RegisterEvents
public class DisableEndermanTeleport {

    @SubscribeEvent
    public void onEnderTeleport(EnderTeleportEvent event) {
        if (ScoreboardUtils.currentGamemode == Gamemode.SKYBLOCK && Config.feature.qol.qolDisableEnderManTeleport) {
            event.setCanceled(true);
        }
    }
}