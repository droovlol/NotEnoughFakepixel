package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.features.Dungeons;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

public class MuteIrrelevantMessages {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event){
        if (!ScoreboardUtils.currentLocation.isDungeon()) return;
        if (!NotEnoughFakepixel.feature.dungeons.dungeonsMuteIrrelevantMessages) return;
        if (event.message.getUnformattedText().contains("[BOSS]") || event.message.getUnformattedText().contains("[CROWD]")) event.setCanceled(true);
    }
}
