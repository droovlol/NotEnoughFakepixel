package org.ginafro.notenoughfakepixel.utils;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.variables.Area;
import org.ginafro.notenoughfakepixel.variables.DungeonFloor;
import org.ginafro.notenoughfakepixel.variables.Gamemode;
import org.ginafro.notenoughfakepixel.variables.Location;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RegisterEvents
public class ScoreboardUtils {

    public static Area currentArea = Area.NONE;
    public static Gamemode currentGamemode = Gamemode.LOBBY;
    public static Location currentLocation = Location.NONE;

    public static DungeonFloor currentFloor = DungeonFloor.NONE;
    public static int clearedPercentage = -1;

    @Getter
    @Setter
    private static Pattern floorPattern = Pattern.compile(" §7⏣ §cThe Catacombs §7\\(<?floor>.{2}\\)");

    public static void parseScoreboard() {
        Minecraft mc = Minecraft.getMinecraft();

        if (!Config.feature.debug.enableOutOfFakepixel && !mc.getCurrentServerData().serverIP.contains("fakepixel"))
            return;

        if (!mc.isSingleplayer()) {
            Scoreboard scoreboard = mc.theWorld.getScoreboard();
            ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);

            if (objective != null) {
                currentGamemode = Gamemode.getGamemode(ScoreboardUtils.cleanSB(objective.getDisplayName()));

                scoreboard.getSortedScores(objective).stream()
                        .filter(score -> score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#"))
                        .forEach(score -> {
                            String playerName = score.getPlayerName();

                            if (playerName.startsWith(" §7⏣ §cThe Catacombs §7")) {
                                String floor = playerName.replaceAll(" §7⏣ §cThe Catacombs §7\\(", "").replaceAll("\\)", "");
                                currentFloor = DungeonFloor.getFloor(floor);
                            }

                            if (playerName.startsWith("§fDungeon Cleared: ")) {
                                String percentage = StringUtils.stripControlCodes(playerName)
                                        .replaceAll("Dungeon Cleared: ", "")
                                        .replaceAll("%", "");
                                clearedPercentage = Integer.parseInt(percentage);
                            }
                        });
            }
        }

        if (currentGamemode == Gamemode.SKYBLOCK && !mc.isSingleplayer() && mc.getNetHandler() != null) {
            mc.getNetHandler().getPlayerInfoMap().stream()
                    .map(NetworkPlayerInfo::getDisplayName)
                    .filter(Objects::nonNull)
                    .map(displayName -> StringUtils.stripControlCodes(displayName.getUnformattedText()))
                    .forEach(name -> {
                        if (name.contains("Server: ")) {
                            currentLocation = Location.getLocation(
                                    name.replace("Server: ", "").replaceFirst("-\\d+", "-").trim()
                            );
                        } else if (name.contains("Area")) {
                            currentGamemode = Gamemode.SKYBLOCK;
                            currentArea = Area.getArea(name.replace("Area: ", ""));
                        } else if (name.contains("Dungeon")) {
                            currentGamemode = Gamemode.SKYBLOCK;
                            currentLocation = Location.DUNGEON;
                        }
                    });
        }
    }

    public static boolean scoreboardContains(String string) {
        return getScoreboardLines().stream()
                .map(line -> Utils.removeFormatting(cleanSB(line)))
                .anyMatch(line -> line.contains(string));
    }

    public static int getHubNumber() {
        return Optional.ofNullable(Minecraft.getMinecraft().getNetHandler())
                .map(handler -> handler.getPlayerInfoMap().stream()
                        .map(NetworkPlayerInfo::getDisplayName)
                        .filter(Objects::nonNull)
                        .map(displayName -> StringUtils.stripControlCodes(displayName.getUnformattedText()))
                        .filter(name -> name.contains("Server: "))
                        .map(name -> name.replace("Server: ", "").replaceAll("\\s+", ""))
                        .map(serverName -> {
                            Matcher matcher = Pattern.compile("skyblock-(\\d+)").matcher(serverName);
                            return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
                        })
                        .filter(hubNumber -> hubNumber != -1)
                        .findFirst()
                        .orElse(-1))
                .orElse(-1);
    }


    public static String cleanSB(String scoreboard) {
        return StringUtils.stripControlCodes(scoreboard).chars()
                .filter(c -> c > 20 && c < 127)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    @SuppressWarnings({"ExecutionException", "IllegalArgumentException"})
    public static List<String> getScoreboardLines() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return Collections.emptyList();

        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        if (scoreboard == null) return Collections.emptyList();

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return Collections.emptyList();

        List<Score> filteredScores;
        try {
            filteredScores = scoreboard.getSortedScores(objective).stream()
                    .filter(score -> score != null
                            && score.getPlayerName() != null
                            && !score.getPlayerName().startsWith("#"))
                    .collect(Collectors.toList());
        } catch (ConcurrentModificationException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }

        int size = filteredScores.size();
        return IntStream.range(Math.max(0, size - 15), size)
                .mapToObj(i -> {
                    Score score = filteredScores.get(i);
                    String playerName = score.getPlayerName();
                    ScorePlayerTeam team = scoreboard.getPlayersTeam(playerName);
                    return ScorePlayerTeam.formatPlayerName(team, playerName);
                })
                .collect(Collectors.toList());
    }


    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        currentLocation = Location.NONE;
        currentGamemode = Gamemode.LOBBY;
        currentArea = Area.NONE;
        currentFloor = DungeonFloor.NONE;
        clearedPercentage = -1;
    }

}