package org.ginafro.notenoughfakepixel.features.mlf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.features.mlf.Info;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

import java.util.List;

public class Map {

    private String inc = "", bal = "", e1 = "", e2 = "";
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!Info.mlfInfoHud) return;
        if (ScoreboardUtils.currentGamemode != Gamemode.MLF) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int x = (int) Info.mlfInfoOffsetX;
        int y = (int) Info.mlfInfoOffsetY;

        // Parse background color (A:R:G:B format)
        String[] colorParts = Info.mlfInfoBackgroundColor.split(":");
        int alpha = Integer.parseInt(colorParts[0]);
        int red = Integer.parseInt(colorParts[1]);
        int green = Integer.parseInt(colorParts[2]);
        int blue = Integer.parseInt(colorParts[3]);
        int bgColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

        // Draw background
        Gui.drawRect(x, y, x + 35, y + 60, bgColor);

        // Update and draw text
        List<String> sideBarLines = ScoreboardUtils.getSidebarLines();
        updateLines(sideBarLines);
        mc.fontRendererObj.drawString("Income: " + inc, x + 2, y + 8, -1);
        mc.fontRendererObj.drawString("Balance: " + bal, x + 2, y + 17, -1);
        mc.fontRendererObj.drawString("Events: ", x + 2, y + 25, -1);
        mc.fontRendererObj.drawString("- " + e1, x + 2, y + 33, -1);
        mc.fontRendererObj.drawString("- " + e2, x + 2, y + 41, -1);
    }

    /** Updates the lines from the scoreboard */
    private void updateLines(List<String> sideBarLines) {
        if (sideBarLines == null) return;
        e1 = "";
        e2 = "";
        for (String s : sideBarLines) {
            if (s.contains("- ")) {
                if (e1.isEmpty()) {
                    e1 = StringUtils.stripControlCodes(s.replace("- ", ""));
                } else {
                    e2 = StringUtils.stripControlCodes(s.replace("- ", ""));
                }
            }
            if (s.contains("Balance")) {
                bal = StringUtils.stripControlCodes(s.replace("Balance: ", ""));
            }
            if (s.contains("Income")) {
                inc = StringUtils.stripControlCodes(s.replace("Income: ", ""));
            }
        }
    }

    /** Renders a dummy version for the position editor */
    public void renderDummy() {
        ScaledResolution sr = new ScaledResolution(mc);
        int x = (int) Info.mlfInfoOffsetX;
        int y = (int) Info.mlfInfoOffsetY;

        String[] colorParts = Info.mlfInfoBackgroundColor.split(":");
        int alpha = Integer.parseInt(colorParts[0]);
        int red = Integer.parseInt(colorParts[1]);
        int green = Integer.parseInt(colorParts[2]);
        int blue = Integer.parseInt(colorParts[3]);
        int bgColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

        Gui.drawRect(x, y, x + 35, y + 60, bgColor);
        mc.fontRendererObj.drawString("Income: 100", x + 2, y + 8, -1);
        mc.fontRendererObj.drawString("Balance: 500", x + 2, y + 17, -1);
        mc.fontRendererObj.drawString("Events: ", x + 2, y + 25, -1);
        mc.fontRendererObj.drawString("- Event 1", x + 2, y + 33, -1);
        mc.fontRendererObj.drawString("- Event 2", x + 2, y + 41, -1);
    }
}