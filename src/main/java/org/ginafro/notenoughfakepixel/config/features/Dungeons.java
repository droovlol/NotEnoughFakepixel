package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;
import org.ginafro.notenoughfakepixel.config.gui.core.config.Position;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Dungeons {
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    // QOL (subcategoryId = 0)
    @Expose
    @ConfigOption(name = "Is Paul Active", desc = "Check if Paul is active as mayor with EZPZ perk.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsIsPaul = false;

    @Expose
    @ConfigOption(name = "Auto Close Chests", desc = "Automatically close chests in dungeons.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsAutoCloseChests = true;

    @Expose
    @ConfigOption(name = "Auto Ready Dungeon", desc = "Automatically ready up in dungeons.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsAutoReady = true;

    @Expose
    @ConfigOption(name = "Auto Ready Name (if nicked)", desc = "Name to search for when nicked.", subcategoryId = 0)
    @ConfigEditorText
    public static String dungeonsAutoReadyName = "your nicked name";

    @Expose
    @ConfigOption(name = "Wither & Blood Keys Tracers", desc = "Show tracers on wither and blood keys.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsKeyTracers = true;

    @Expose
    @ConfigOption(name = "Mute Irrelevant Messages", desc = "Mute bosses and crowd dialogs in chat.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsMuteIrrelevantMessages = true;

    @Expose
    @ConfigOption(name = "Salvage Items Saver", desc = "Prevent salvaging important and legendary+ items.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsSalvageItemsPrevention = true;

    @Expose
    @ConfigOption(name = "Show Correct Livid", desc = "Show the correct Livid.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsLividFinder = true;

    @Expose
    @ConfigOption(name = "Show Opened Chests in Croesus", desc = "Show opened chests in Croesus.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsShowOpenedChests = true;

    @Expose
    @ConfigOption(name = "Custom Spirit Leap GUI", desc = "Use custom GUI for Spirit Leap.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsSpiritLeapGUI = true;

    @Expose
    @ConfigOption(name = "Announce Leaped to Player", desc = "Announce leaped to player in party chat.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsLeapAnnounce = true;

    @Expose
    @ConfigOption(name = "Custom Click in Order Terminal GUI", desc = "Use custom GUI for Click in Order terminal.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsCustomGuiClickIn = true;

    @Expose
    @ConfigOption(name = "Custom Colors Terminal GUI", desc = "Use custom GUI for Colors terminal.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsCustomGuiColors = true;

    @Expose
    @ConfigOption(name = "Custom Maze Terminal GUI", desc = "Use custom GUI for Maze terminal.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsCustomGuiMaze = true;

    @Expose
    @ConfigOption(name = "Custom Starts With Terminal GUI", desc = "Use custom GUI for Starts With terminal.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsCustomGuiStartsWith = true;

    @Expose
    @ConfigOption(name = "Custom Panes Terminal GUI", desc = "Use custom GUI for Panes terminal.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsCustomGuiPanes = true;

    @Expose
    @ConfigOption(name = "Custom Terminal Scale", desc = "Scale of custom terminal GUIs.", subcategoryId = 0)
    @ConfigEditorSlider(minValue = 1.0f, maxValue = 5.0f, minStep = 0.1f)
    public static float dungeonsTerminalsScale = 2.0f;

    @Expose
    @ConfigOption(name = "Announce Blood Room Done", desc = "Announce when blood room is done spawning.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean dungeonsBloodReady = true;

    // Dungeon Map (subcategoryId = 1)
    @Expose
    @ConfigOption(name = "Dungeons Map", desc = "Enable dungeons map.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean dungeonsMap = true;

    @Expose
    @ConfigOption(name = "Dungeons Map Border Color", desc = "Color of the dungeons map border.", subcategoryId = 1)
    @ConfigEditorColour
    public static String dungeonsMapBorderColor = "0:100:0:0:255"; // Black with full opacity

    @Expose
    @ConfigOption(name = "Dungeons Map Scale", desc = "Scale of the dungeons map.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.1f, maxValue = 10.0f, minStep = 0.1f)
    public static float dungeonsMapScale = 1.0f;

    @Expose
    @ConfigOption(name = "Dungeons Map Offset X", desc = "Horizontal offset of the dungeons map.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1800.0f, minStep = 1.0f)
    public static float dungeonsMapOffsetX = 0.0f;

    @Expose
    @ConfigOption(name = "Dungeons Map Offset Y", desc = "Vertical offset of the dungeons map.", subcategoryId = 1)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1250.0f, minStep = 1.0f)
    public static float dungeonsMapOffsetY = 0.0f;

    @Expose
    @ConfigOption(name = "Dungeons Map Rotation", desc = "Enable rotation of the dungeons map.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean dungeonsRotateMap = true;

    @Expose
    @ConfigOption(name = "Edit Map Position", desc = "Adjust the dungeons map position visually.", subcategoryId = 1)
    @ConfigEditorButton(runnableId = "editDungeonsMapPosition", buttonText = "Edit Position")
    public static String editDungeonsMapPositionButton = "";

    // Puzzles (subcategoryId = 2)
    @Expose
    @ConfigOption(name = "Three Weirdos Solver", desc = "Enable Three Weirdos puzzle solver.", subcategoryId = 2)
    @ConfigEditorBoolean
    public static boolean dungeonsThreeWeirdos = true;

    @Expose
    @ConfigOption(name = "Water Solver", desc = "Enable Water puzzle solver.", subcategoryId = 2)
    @ConfigEditorBoolean
    public static boolean dungeonsWaterSolver = true;

    // Starred Mobs (subcategoryId = 3)
    @Expose
    @ConfigOption(name = "Fel Mobs Display", desc = "Display Fel mobs.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean dungeonsFelMob = true;

    @Expose
    @ConfigOption(name = "Fel Mob Color", desc = "Color of Fel mobs.", subcategoryId = 3)
    @ConfigEditorColour
    public static String dungeonsFelColor = "0:92:154:255:255";

    @Expose
    @ConfigOption(name = "Bat Mobs Display", desc = "Display Bat mobs.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean dungeonsBatMobs = true;

    @Expose
    @ConfigOption(name = "Bat Mob Color", desc = "Color of Bat mobs.", subcategoryId = 3)
    @ConfigEditorColour
    public static String dungeonsBatColor = "0:92:154:255:255";

    @Expose
    @ConfigOption(name = "Starred Mobs Display", desc = "Display style for starred mobs.", subcategoryId = 3)
    @ConfigEditorDropdown(values = {"Box", "Outline", "Disabled"})
    public static int dungeonsStarredMobs = 0;

    @Expose
    @ConfigOption(name = "Starred Mobs Color", desc = "Color of starred mobs.", subcategoryId = 3)
    @ConfigEditorColour
    public static String dungeonsStarredBoxColor = "0:92:154:255:255";

    @Expose
    @ConfigOption(name = "Starred Mobs ESP", desc = "Render starred mobs hitboxes through walls.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean dungeonsStarredMobsEsp = true;

    @Expose
    @ConfigOption(name = "Withermancer Color", desc = "Color of Withermancer.", subcategoryId = 3)
    @ConfigEditorColour
    public static String dungeonsWithermancerColor = "0:169:169:169:255";

    @Expose
    @ConfigOption(name = "Zombie Commander Color", desc = "Color of Zombie Commander.", subcategoryId = 3)
    @ConfigEditorColour
    public static String dungeonsZombieCommanderColor = "0:255:0:0:255";

    @Expose
    @ConfigOption(name = "Skeleton Master Color", desc = "Color of Skeleton Master.", subcategoryId = 3)
    @ConfigEditorColour
    public static String dungeonsSkeletonMasterColor = "0:255:100:0:255";

    @Expose
    @ConfigOption(name = "Stormy Color", desc = "Color of Stormy.", subcategoryId = 3)
    @ConfigEditorColour
    public static String dungeonsStormyColor = "0:173:216:230:255";

    // Floor 7 Terminals and Devices (subcategoryId = 4)
    @Expose
    @ConfigOption(name = "Terminal Starts With Solver", desc = "Enable Starts With terminal solver.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsTerminalStartsWithSolver = true;

    @Expose
    @ConfigOption(name = "Terminal Select Colors Solver", desc = "Enable Select Colors terminal solver.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsTerminalSelectColorsSolver = true;

    @Expose
    @ConfigOption(name = "Terminal Click In Order Solver", desc = "Enable Click In Order terminal solver.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsTerminalClickInOrderSolver = true;

    @Expose
    @ConfigOption(name = "Terminal Maze Solver", desc = "Enable Maze terminal solver.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsTerminalMazeSolver = true;

    @Expose
    @ConfigOption(name = "Terminal Correct Panes Solver", desc = "Enable Correct Panes terminal solver.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsTerminalCorrectPanesSolver = true;

    @Expose
    @ConfigOption(name = "Hide Terminal Incorrect Slots", desc = "Hide incorrect slots in terminals.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsTerminalHideIncorrect = true;

    @Expose
    @ConfigOption(name = "Prevent Terminal Missclicks", desc = "Prevent missclicks in terminals.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsPreventMissclicks = true;

    @Expose
    @ConfigOption(name = "Hide Tooltips", desc = "Hide tooltips in terminals.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsHideTooltips = true;

    @Expose
    @ConfigOption(name = "First Device Solver", desc = "Enable first device solver.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsFirstDeviceSolver = true;

    @Expose
    @ConfigOption(name = "Third Device Solver", desc = "Enable third device solver.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsThirdDeviceSolver = true;

    @Expose
    @ConfigOption(name = "Correct Color", desc = "Color for correct choices.", subcategoryId = 4)
    @ConfigEditorColour
    public static String dungeonsCorrectColor = "0:255:255:0:255";

    @Expose
    @ConfigOption(name = "Alternative Color", desc = "Alternative color for choices.", subcategoryId = 4)
    @ConfigEditorColour
    public static String dungeonsAlternativeColor = "0:255:255:0:255";

    @Expose
    @ConfigOption(name = "Terminal Waypoints", desc = "Show waypoints for terminals.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean dungeonsTerminalWaypoints = true;

    // Score & Secrets (subcategoryId = 5)
    @Expose
    @ConfigOption(name = "Score Overlay", desc = "Enable the dungeon score overlay.", subcategoryId = 2)
    @ConfigEditorBoolean
    public static boolean dungeonsScoreOverlay = true; // Already exists, keeping it

    @Expose
    @ConfigOption(name = "Score Overlay Offset X", desc = "Horizontal offset of the score overlay.", subcategoryId = 2)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1800.0f, minStep = 1.0f)
    public static float scoreOverlayOffsetX = 0.0f;

    @Expose
    @ConfigOption(name = "Score Overlay Offset Y", desc = "Vertical offset of the score overlay.", subcategoryId = 2)
    @ConfigEditorSlider(minValue = 0.0f, maxValue = 1250.0f, minStep = 1.0f)
    public static float scoreOverlayOffsetY = 128.0f;

    @Expose
    @ConfigOption(name = "Score Overlay Scale", desc = "Scale of the score overlay text.", subcategoryId = 2)
    @ConfigEditorSlider(minValue = 0.5f, maxValue = 5.0f, minStep = 0.1f)
    public static float scoreOverlayScale = 1.0f;

    @Expose
    @ConfigOption(name = "Score Overlay Background Color", desc = "Background color of the score overlay.", subcategoryId = 2)
    @ConfigEditorColour
    public static String scoreOverlayBackgroundColor = "0:150:0:0:0"; // Semi-transparent black

    @Expose
    @ConfigOption(name = "Edit Score Overlay Position", desc = "Adjust the score overlay position visually", subcategoryId = 2)
    @ConfigEditorButton(runnableId = "editScoreOverlayPosition", buttonText = "Edit Position")
    public static String editScoreOverlayPositionButton = "";

    @Expose
    @ConfigOption(name = "S+ Notifier", desc = "Notify when S+ is virtually reached.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean dungeonsSPlusNotifier = true;

    @Expose
    @ConfigOption(name = "S+ Message on Chat", desc = "Send a message when dungeon is about to be done.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean dungeonsSPlusMessage = true;

    @Expose
    @ConfigOption(name = "Custom S+ Message", desc = "Custom message for S+ notification.", subcategoryId = 5)
    @ConfigEditorText
    public static String dungeonsSPlusCustom = "";

    @Expose
    @ConfigOption(name = "Dungeon Cleared Notifier", desc = "Notify when dungeon is 100% completed.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean dungeonsClearedNotifier = true;

    @Expose
    @ConfigOption(name = "Show Item Secrets and Wither Essences", desc = "Show item secrets and wither essences through walls.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean dungeonsItemSecretsDisplay = true;

    @Expose
    @ConfigOption(name = "Item Secrets Color", desc = "Color of item secrets.", subcategoryId = 5)
    @ConfigEditorColour
    public static String dungeonsItemSecretsColor = "0:255:255:0:255";

    @Expose
    @ConfigOption(name = "Make Item Secrets Big", desc = "Make item secrets larger.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean dungeonsItemSecretsBig = true;

    @Expose
    @ConfigOption(name = "Item Secrets Scale", desc = "Scale of item secrets.", subcategoryId = 5)
    @ConfigEditorSlider(minValue = 0.1f, maxValue = 5.0f, minStep = 0.1f)
    public static float dungeonsScaleItemDrop = 3.5f;

    // Position for Score Overlay
    @Expose
    public static Position scoreOverlayPos = new Position(10, 10, false, true);

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : Dungeons.class.getDeclaredFields()) {
                if (field.isAnnotationPresent(Expose.class) && field.isAnnotationPresent(ConfigOption.class)) {
                    field.setAccessible(true);
                    staticFieldValues.put(field.getName(), field.get(null)); // null because static
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load static fields from the map after deserialization
    public void loadStaticFields() {
        try {
            for (Field field : Dungeons.class.getDeclaredFields()) {
                if (field.isAnnotationPresent(Expose.class) && field.isAnnotationPresent(ConfigOption.class)) {
                    field.setAccessible(true);
                    Object value = staticFieldValues.get(field.getName());
                    if (value != null) {
                        if (field.getType() == int.class && value instanceof Number) {
                            field.setInt(null, ((Number) value).intValue());
                        } else if (field.getType() == boolean.class && value instanceof Boolean) {
                            field.setBoolean(null, (Boolean) value);
                        } else {
                            field.set(null, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
