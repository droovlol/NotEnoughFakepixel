package org.ginafro.notenoughfakepixel.features.skyblock.slayers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.*;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.NefPacketBlockChange;
import org.ginafro.notenoughfakepixel.utils.*;
import org.ginafro.notenoughfakepixel.variables.Slayer;

import java.awt.*;
import java.util.*;
import java.util.List;

@RegisterEvents
public class VoidgloomSeraph {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final Map<Waypoint, Long> waypoints = new HashMap<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.side == net.minecraftforge.fml.relauncher.Side.CLIENT && !ScoreboardUtils.isSlayerActive){
            reset();
        }
    }

    private void reset() {
        waypoints.clear();
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (checkEssentials() || !Config.feature.slayer.slayerShowBeaconPath || ScoreboardUtils.currentSlayer != Slayer.VOIDGLOOM) {
            return;
        }

        if (ScoreboardUtils.isSlayerActive) {
            mc.addScheduledTask(() -> {
                waypoints.entrySet().removeIf(entry -> {
                    long elapsedTime = System.currentTimeMillis() - entry.getValue();
                    if (elapsedTime > 20000) return true;

                    BlockPos waypoint = entry.getKey().getBlockPos();
                    if (Config.feature.slayer.showTracerToBeacon) {
                        RenderUtils.draw3DLine(
                            new Vec3(waypoint.getX() + .5, waypoint.getY() + .5, waypoint.getZ() + .5),
                            mc.thePlayer.getPositionEyes(event.partialTicks),
                            ColorUtils.getColor(Config.feature.slayer.slayerBeaconColor),
                            8,
                            true,
                            event.partialTicks
                        );
                    }
                    RenderUtils.renderBeaconBeam(
                        waypoint,
                        ColorUtils.getColor(Config.feature.slayer.slayerBeaconColor).getRGB(),
                        1.0f,
                        event.partialTicks
                    );
                    return false;
                });
            });
        }
    }

    private static void notifyPlayer() {
        if (Config.feature.slayer.notifyBeaconInScreen
                && mc.thePlayer != null
                && mc.theWorld != null
                && mc.ingameGUI != null
                && mc.ingameGUI.getChatGUI() != null) {
            TitleUtils.showTitle(EnumChatFormatting.RED + "Beacon", 1000);
            SoundUtils.playSound(mc.thePlayer.getPosition(), "note.pling", 1.0f, 1.0f);
        }
    }


    public static void processBlockChange(NefPacketBlockChange packetIn) {
        if (!Config.feature.slayer.slayerShowBeaconPath) return;
        Block block = packetIn.getBlock();
        BlockPos position = packetIn.getPacket().getBlockPosition();

        // Check if the block is a beacon and add a waypoint
        if (block instanceof BlockBeacon && block.getLocalizedName().contains("Beacon") && ScoreboardUtils.isSlayerActive) {
            EntityPlayerSP player = mc.thePlayer;
            if (player == null || player.getPosition() == null) return;

            double distance = new Vec3(player.getPosition()).distanceTo(new Vec3(position));
            if (distance > 20) return;

            notifyPlayer();
            waypoints.put(new Waypoint("BEACON", new int[]{position.getX(), position.getY(), position.getZ()}), System.currentTimeMillis());
        } else if (block instanceof BlockAir) {
            // If the block is air, it means the beacon was removed
            waypoints.entrySet().removeIf(entry -> {
                if (entry.getKey().getBlockPos().equals(position)) {
                    return true;
                }
                return false;
            });
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        reset();
    }

    private static boolean checkEssentials() {
        return (mc.thePlayer == null) ||
                (!ScoreboardUtils.currentGamemode.isSkyblock()) ||
                (!ScoreboardUtils.currentLocation.isEnd());
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (Config.feature.slayer.slayerShowBeaconPath && ScoreboardUtils.currentGamemode.isSkyblock() && ScoreboardUtils.currentLocation.isEnd()) {
            String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
            if (message.contains("SLAYER QUEST COMPLETE!") || message.contains("SLAYER QUEST FAILED!")) {
                ScoreboardUtils.isSlayerActive = false;
                reset();
            }
        }
    }

}