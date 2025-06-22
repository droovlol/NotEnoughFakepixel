package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.PacketReadEvent;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.SoundUtils;
import org.ginafro.notenoughfakepixel.utils.TitleUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

@RegisterEvents
public class MinibossAlert {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onReceivePacket(PacketReadEvent event) {
        if (ScoreboardUtils.currentGamemode != Gamemode.SKYBLOCK) return;
        if (event.packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect packet = (S29PacketSoundEffect) event.packet;
            if (packet.getSoundName().equals("random.explode") && packet.getVolume() == 0.6f && packet.getPitch() == 9 / 7f) {
                if (Config.feature.slayer.slayerMinibossTitle) {
                    TitleUtils.showTitle(EnumChatFormatting.RED + "MINIBOSS!", 1000);
                }
                if (Config.feature.slayer.slayerMinibossSound) {
                    SoundUtils.playSound(mc.thePlayer.getPosition(), "random.orb", 1.0F, 1.0F);
                }
            }
        }
    }
}