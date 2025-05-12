package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.variables.Gamemode;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

@RegisterEvents
public class DisableEndermanTeleport {

    @SubscribeEvent
    public void onEnderTeleport(EnderTeleportEvent event) {
        if (ScoreboardUtils.currentGamemode == Gamemode.SKYBLOCK && NotEnoughFakepixel.feature.qol.qolDisableEnderManTeleport) {
            event.setCanceled(true);
        }
    }
}