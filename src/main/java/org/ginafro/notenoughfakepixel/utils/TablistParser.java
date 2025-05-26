package org.ginafro.notenoughfakepixel.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.mixin.Accesors.AccessorGuiPlayerTabOverlay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@RegisterEvents
public class TablistParser {

    private static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(new PlayerComparator());
    private static long lastTime = 0;

    public static int mithrilPowder = 0;
    public static List<String> commissions = new ArrayList<>();

    public static int secretPercentage = 0;
    public static int deaths = 0;
    public static String time = "";

    public static int crypts = 0;

    public static String currentOpenChestName = "";
    public static String lastOpenChestName = "";

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        public int compare(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
            ScorePlayerTeam team1 = o1.getPlayerTeam();
            ScorePlayerTeam team2 = o2.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(
                            o1.getGameType() != WorldSettings.GameType.SPECTATOR,
                            o2.getGameType() != WorldSettings.GameType.SPECTATOR
                    )
                    .compare(
                            team1 != null ? team1.getRegisteredName() : "",
                            team2 != null ? team2.getRegisteredName() : ""
                    )
                    .compare(o1.getGameProfile().getName(), o2.getGameProfile().getName()).result();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onGuiOpen(GuiOpenEvent event) {
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;
        if (event.gui == null) return;

        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            ContainerChest container = (ContainerChest) chest.inventorySlots;

            currentOpenChestName = container.getLowerChestInventory().getDisplayName().getUnformattedText();
            lastOpenChestName = currentOpenChestName;
        } else {
            currentOpenChestName = "";
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (lastTime < 20) {
            lastTime++;
            return;
        }
        if (e.phase != TickEvent.Phase.END) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (!ScoreboardUtils.currentGamemode.isSkyblock()) return;

        List<NetworkPlayerInfo> players =
                playerOrdering.sortedCopy(Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());

        List<String> result = new ArrayList<>();

        for (NetworkPlayerInfo info : players) {
            String name = Minecraft.getMinecraft().ingameGUI.getTabList()
                    .getPlayerName(info);
            result.add(name);
        }

        // Lists to store data
        List<String> serverInfo = new ArrayList<>();
        List<String> accountInfo = new ArrayList<>();

        // Flags to track sections
        boolean isPlayerStats = false;
        boolean isServerInfo = false;
        boolean isAccountInfo = false;
        boolean foundCommisions = false;
        commissions = new ArrayList<>();

        // Regex to remove Minecraft formatting codes
        Pattern formatPattern = Pattern.compile("§[0-9a-fklmnor]");

        // Parse the data
        for (String line : result) {
            // Check for section headers
            if (line.contains("§3§l Server Info§r")) {
                isServerInfo = true;
                isAccountInfo = false;
                isPlayerStats = false;
                continue;
            } else if (line.contains("§6§lAccount Info§r")) {
                isServerInfo = false;
                isAccountInfo = true;
                isPlayerStats = false;
                continue;
            } else if (line.contains("§2§lPlayer Stats§r")) {
                isServerInfo = false;
                isAccountInfo = false;
                isPlayerStats = true;
                continue;
            }

            String cleanLine = formatPattern.matcher(line).replaceAll("").trim();

            // SERVER INFO SECTION
            if (isServerInfo) {
                // Extract Server Time
                if (cleanLine.startsWith("Time: ")) {
                    time = cleanLine.substring(6); // Extract after "Time: "
                }

                // Parsing mithril powder
                if (cleanLine.contains("Mithril Powder: ")) {
                    mithrilPowder = Integer.parseInt(cleanLine.split(" ")[2].replace(",", ""));
                }

                // Parsing secrets percentage
                if (cleanLine.contains("Secrets Found: ")) {
                    secretPercentage = Integer.parseInt(cleanLine.split(" ")[2].replace("%", ""));
                }

                // Parsing commissions
                if (foundCommisions) {
                    if (cleanLine.isEmpty()) {
                        foundCommisions = false;
                    } else {
                        commissions.add(cleanLine);
                    }
                }

                if (cleanLine.contains("Commissions")) {
                    foundCommisions = true;
                }

                serverInfo.add(cleanLine);

                // PLAYER STATS SECTION
            } else if (isPlayerStats) {
                // Extract Deaths count (in brackets)
                if (cleanLine.startsWith("Deaths: ")) {
                    String[] parts = cleanLine.split("\\(");
                    if (parts.length > 1) {
                        deaths = Integer.parseInt(parts[1].replace(")", "").trim());
                    }
                }
                // Extract Crypts count
                if (cleanLine.startsWith("Crypts: ")) {
                    if (cleanLine.substring(8).trim().contains("/")) {
                        crypts = 0;
                    } else {
                        crypts = Integer.parseInt(cleanLine.substring(8).trim());
                    }
                }
                // ACCOUNT INFO SECTION
            } else if (isAccountInfo) {
                accountInfo.add(cleanLine);
            }
        }

        // FOOTER (COOKIE & BOOSTER)
        try {
            String[] footer = ((AccessorGuiPlayerTabOverlay) Minecraft.getMinecraft().ingameGUI.getTabList())
                    .getFooter().getFormattedText()
                    .split("\n");
        } catch (Exception ignored) {
        }

        lastTime = 0;
    }
}
