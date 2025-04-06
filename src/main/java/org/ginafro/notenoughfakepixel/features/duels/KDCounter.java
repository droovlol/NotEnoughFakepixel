package org.ginafro.notenoughfakepixel.features.duels;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.features.duels.Duels;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

public class KDCounter {

    private static int kills = 0;
    private static int deaths = 0;
    private String opponent = "";
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (ScoreboardUtils.currentGamemode != Gamemode.DUELS) return;
        String msg = e.message.getUnformattedText();

        if (msg.contains("Opponents: ")) {
            opponent = msg.replace("Opponents: ", "").trim();
        }

        String[] parts = msg.split(" ");
        if (parts.length > 0) {
            String firstWord = parts[0];
            if (opponent.equals(firstWord)) {
                kills++;
            } else if (mc.thePlayer.getName().equals(firstWord)) {
                deaths++;
            }
        }

        if (msg.contains("WINNER")) {
            kills = 0;
            deaths = 0;
            opponent = "";
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!NotEnoughFakepixel.feature.duels.kdCounterEnabled) return;
        if (ScoreboardUtils.currentGamemode != Gamemode.DUELS) return;

        draw(NotEnoughFakepixel.feature.duels.kdCounterOffsetX, NotEnoughFakepixel.feature.duels.kdCounterOffsetY, NotEnoughFakepixel.feature.duels.kdCounterScale, false);
    }

    private void draw(float x, float y, float scale, boolean example) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);

        String text = example ? "K/D: 3/2" : "K/D: " + kills + "/" + deaths;
        mc.fontRendererObj.drawString(text, 0, 0, -1);

        GlStateManager.popMatrix();
    }

    public void renderDummy() {
        draw(NotEnoughFakepixel.feature.duels.kdCounterOffsetX, NotEnoughFakepixel.feature.duels.kdCounterOffsetY, NotEnoughFakepixel.feature.duels.kdCounterScale, true);
    }

    public float getWidth() {
        return 45 * NotEnoughFakepixel.feature.duels.kdCounterScale; // "K/D: 10/10" is roughly 45 pixels wide at scale 1.0
    }

    public float getHeight() {
        return 11 * NotEnoughFakepixel.feature.duels.kdCounterScale; // Font height is ~11 pixels at scale 1.0
    }
}