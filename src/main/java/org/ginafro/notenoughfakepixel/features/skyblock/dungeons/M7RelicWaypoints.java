package org.ginafro.notenoughfakepixel.features.skyblock.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RegisterEvents
public class M7RelicWaypoints {

    public enum RelicColor {
        PURPLE, GREEN, RED, ORANGE, BLUE
    }

    private static final Map<RelicColor, BlockPos> relicPositions = new HashMap<>();
    private static final Map<RelicColor, BlockPos> cauldronPositions = new HashMap<>();
    public boolean isFinalPhase = false;
    private Set<RelicColor> pickedRelics = new HashSet<>();
    private Set<RelicColor> placedRelics = new HashSet<>();
    private Set<RelicColor> myPickedRelics = new HashSet<>(); // Your personal pickups
    private Set<RelicColor> othersPickedRelics = new HashSet<>(); // Track teammates' pickups

    static {
        // Initialize relic positions
        relicPositions.put(RelicColor.PURPLE, new BlockPos(64, 8, 140));
        relicPositions.put(RelicColor.GREEN, new BlockPos(28, 6, 102));
        relicPositions.put(RelicColor.RED, new BlockPos(28, 6, 67));
        relicPositions.put(RelicColor.ORANGE, new BlockPos(100, 6, 64));
        relicPositions.put(RelicColor.BLUE, new BlockPos(99, 6, 102));

        // Initialize cauldron positions
        cauldronPositions.put(RelicColor.PURPLE, new BlockPos(62, 7, 49));
        cauldronPositions.put(RelicColor.GREEN, new BlockPos(57, 7, 52));
        cauldronPositions.put(RelicColor.RED, new BlockPos(59, 7, 50));
        cauldronPositions.put(RelicColor.ORANGE, new BlockPos(65, 7, 50));
        cauldronPositions.put(RelicColor.BLUE, new BlockPos(67, 7, 52));
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!DungeonManager.checkEssentialsF7()) return;
        if (!isFinalPhase) return;
        if (!Config.feature.dungeons.m7Relics) return;
        float partialTicks = event.partialTicks;

        for (RelicColor color : RelicColor.values()) {
            // Check if anyone (you OR others) picked the relic
            boolean anyonePicked = myPickedRelics.contains(color) || othersPickedRelics.contains(color);

            if (!anyonePicked) {
                BlockPos pos = relicPositions.get(color);
                int rgb = getColorRGB(color);
                RenderUtils.highlightBlock(pos, new Color(rgb), true, partialTicks);
            }
            else if (myPickedRelics.contains(color) && !placedRelics.contains(color)) {
                BlockPos cauldronPos = cauldronPositions.get(color);
                int rgb = getColorRGB(color);
                RenderUtils.renderBoundingBox(cauldronPos, rgb, partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent event) {
        if (!isFinalPhase) return;

        BlockPos clickedPos = event.pos;
        cauldronPositions.forEach((color, pos) -> {
            if (pos.equals(clickedPos) && pickedRelics.contains(color)) {
                placedRelics.add(color);
            }
        });
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().toLowerCase();
        String playerName = Minecraft.getMinecraft().thePlayer.getName().toLowerCase();

        if (message.contains(playerName) && message.contains("picked the corrupted")) {
            for (RelicColor color : RelicColor.values()) {
                String colorStr = color.name().toLowerCase();
                if (message.contains(colorStr + " relic!")) {
                    myPickedRelics.add(color);
                    break;
                }
            }
        }
        else if (message.contains("picked the corrupted")) {
            for (RelicColor color : RelicColor.values()) {
                String colorStr = color.name().toLowerCase();
                if (message.contains(colorStr + " relic!")) {
                    othersPickedRelics.add(color);
                    break;
                }
            }
        }

        if (event.message.getUnformattedText().contains("The Catacombs... are no more")) {
            isFinalPhase = true;
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        myPickedRelics.clear();
        othersPickedRelics.clear();
        placedRelics.clear();
    }

    private int getColorRGB(RelicColor color) {
        switch (color) {
            case PURPLE: return 0x800080;
            case GREEN: return 0x008000;
            case RED: return 0xFF0000;
            case ORANGE: return 0xFFA500;
            case BLUE: return 0x0000FF;
            default: return 0xFFFFFF;
        }
    }
}