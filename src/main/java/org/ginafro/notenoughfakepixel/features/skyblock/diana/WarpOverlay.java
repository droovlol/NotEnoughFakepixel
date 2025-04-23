package org.ginafro.notenoughfakepixel.features.skyblock.diana;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.lwjgl.input.Keyboard;

public class WarpOverlay {
    private static final WarpLocation[] WARP_LOCATIONS = {
            new WarpLocation("Crypts", new Vec3(187, 74, -86), "/warp crypts"),
            new WarpLocation("Hub", new Vec3(-2, 70, -68), "/warp hub"),
            new WarpLocation("Castle", new Vec3(-248, 130, 44), "/warp castle"),
            new WarpLocation("Dark Auction", new Vec3(91, 74, 173), "/warp da"),
            new WarpLocation("Museum", new Vec3(-69, 76, 80), "/warp museum")
    };

    private String displayText = null;
    private String warpCommand = null;
    private boolean wasKeyPressed = false;
    private final GuessBurrow guessBurrow;

    public WarpOverlay(GuessBurrow guessBurrow) {
        this.guessBurrow = guessBurrow;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !ScoreboardUtils.currentLocation.isHub()) {
            displayText = null;
            warpCommand = null;
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) {
            displayText = null;
            warpCommand = null;
            return;
        }

        Vec3 playerPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        Vec3 guessPoint = guessBurrow.guessPoint;

        if (guessPoint != null && distance(playerPos, guessPoint) <= 150.0) {
            displayText = null;
            warpCommand = null;
            return;
        }

        displayText = null;
        warpCommand = null;
        for (WarpLocation loc : WARP_LOCATIONS) {
            if (distance(playerPos, loc.position) <= 100.0) {
                String keyName = Keyboard.getKeyName(NotEnoughFakepixel.feature.diana.warpKeybind);
                displayText = "Warp to " + loc.name + " (" + (keyName != null ? keyName : "None") + ")";
                warpCommand = loc.command;
                break;
            }
        }

        if (warpCommand != null) {
            boolean isKeyDown = Keyboard.isKeyDown(NotEnoughFakepixel.feature.diana.warpKeybind);
            if (isKeyDown && !wasKeyPressed && mc.thePlayer != null) {
                mc.thePlayer.sendChatMessage(warpCommand);
            }
            wasKeyPressed = isKeyDown;
        } else {
            wasKeyPressed = false;
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL || displayText == null) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        int width = scaledRes.getScaledWidth();
        int height = scaledRes.getScaledHeight();

        int x = width / 2 - mc.fontRendererObj.getStringWidth(displayText) / 2;
        int y = 10;
        mc.fontRendererObj.drawStringWithShadow(displayText, x, y, 0xFFFFFF);
    }

    private static double distance(Vec3 v1, Vec3 v2) {
        double dx = v2.xCoord - v1.xCoord;
        double dy = v2.yCoord - v1.yCoord;
        double dz = v2.zCoord - v1.zCoord;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static class WarpLocation {
        final String name;
        final Vec3 position;
        final String command;

        WarpLocation(String name, Vec3 position, String command) {
            this.name = name;
            this.position = position;
            this.command = command;
        }
    }
}