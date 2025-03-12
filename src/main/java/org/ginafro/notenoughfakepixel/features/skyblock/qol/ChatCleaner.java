package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.config.features.QualityOfLife;
import org.ginafro.notenoughfakepixel.utils.ChatUtils;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;

import java.util.regex.Pattern;

public class ChatCleaner {

    private Pattern sellingRankPattern = Pattern.compile("(?<rank>\\[[A-Za-z0-9_+]+\\] )?(?<username>\\w+:) (?<message>.*\\bselling\\b.*\\brank(s)?\\b.*)");
    private Pattern watchdogPattern = Pattern.compile("§4\\[WATCHDOG ANNOUNCEMENT]\n");
    private Pattern infoPattern = Pattern.compile("§b\\[PLAYER INFORMATION]\n");
    private Pattern friendJoinPattern = Pattern.compile("§aFriend > ");
    private Pattern potatoDropPattern = Pattern.compile("§r§6§lRARE DROP! §r§fPotato§r§b");
    private Pattern poisonousPotatoDropPattern = Pattern.compile("§r§6§lRARE DROP! §r§fPoisonous Potato§r§b");
    private Pattern carrotDropPattern = Pattern.compile("§r§6§lRARE DROP! §r§fCarrot§r§b");

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent event){
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (ChatUtils.middleBar.matcher(event.message.getFormattedText()).matches()) return;
        cancelMessage(QualityOfLife.qolDisableSellingRanks, event, sellingRankPattern);
        cancelMessage(QualityOfLife.qolDisableWatchdogInfo, event, watchdogPattern, true);
        cancelMessage(QualityOfLife.qolDisableWatchdogInfo, event, infoPattern, true);
        cancelMessage(QualityOfLife.qolDisableFriendJoin, event, friendJoinPattern, true);
        cancelMessage(QualityOfLife.qolDisableZombieRareDrops, event, potatoDropPattern, true);
        cancelMessage(QualityOfLife.qolDisableZombieRareDrops, event, poisonousPotatoDropPattern, true);
        cancelMessage(QualityOfLife.qolDisableZombieRareDrops, event, carrotDropPattern, true);
    }

    private void cancelMessage(boolean option, ClientChatReceivedEvent e, Pattern pattern, boolean formatted){
        if (!option) return;
        String message = e.message.getUnformattedText();
        if (formatted) message = e.message.getFormattedText();
        //System.out.println(message);

        if (pattern.matcher(message).find() || pattern.matcher(message).matches()){
            e.setCanceled(true);
        }
    }

    private void cancelMessage(boolean option, ClientChatReceivedEvent e, Pattern pattern){
        cancelMessage(option, e, pattern, false);
    }

}
