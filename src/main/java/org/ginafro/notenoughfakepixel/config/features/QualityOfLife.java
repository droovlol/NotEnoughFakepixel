package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class QualityOfLife {

    // Instance field to store static values for serialization
    @Expose
    private Map<String, Object> staticFieldValues = new HashMap<>();

    // General (subcategoryId = 0)
    @Expose
    @ConfigOption(name = "Fullbright", desc = "Enable fullbright.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolFullbright = true;

    @Expose
    @ConfigOption(name = "1.12 Crops Height", desc = "Use 1.12 crops height.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolCropsHeight = false;

    @Expose
    @ConfigOption(name = "Disable Block Breaking Particles", desc = "Disable block breaking particles.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolHideBlockBreakingParticles = false;

    @Expose
    @ConfigOption(name = "Always Sprint", desc = "Always sprint.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolAlwaysSprint = true;

    @Expose
    @ConfigOption(name = "Disable Rain", desc = "Disables rain rendering.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolDisableRain = true;

    @Expose
    @ConfigOption(name = "Disable Potion Effects in Inventory", desc = "Disable potion effects in inventory.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolDisablePotionEffects = true;

    @Expose
    @ConfigOption(name = "Show Enchant Level", desc = "Show enchant level on book icons.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolShowEnchantLevel = true;

    @Expose
    @ConfigOption(name = "Middle Click on Terminals and Enchanting", desc = "Enable middle click on terminals and enchanting GUI.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolMiddleClickChests = true;

    @Expose
    @ConfigOption(name = "Visual Cooldowns", desc = "Use weapon durability as a cooldown timer.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolVisualCooldowns = true;

    @Expose
    @ConfigOption(name = "Item Rarity Display", desc = "Show visual circle indicating item rarity.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolItemRarity = true;

    @Expose
    @ConfigOption(name = "Disable Enderman Teleport", desc = "Disable enderman teleportation.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolDisableEnderManTeleport = true;

    @Expose
    @ConfigOption(name = "Copy Chat Message", desc = "Enable copying chat messages.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolCopyChatMsg = true;

    @Expose
    @ConfigOption(name = "Full Block Lever", desc = "Make levers full blocks.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolFullBlockLever = true;

    @Expose
    @ConfigOption(name = "Block Placing Items", desc = "Prevent items from being placed as blocks.", subcategoryId = 0)
    @ConfigEditorBoolean
    public static boolean qolBlockPlacingItems = true;

    // Fairy Souls (subcategoryId = 1)
    @Expose
    @ConfigOption(name = "Fairy Soul Waypoints", desc = "Enable fairy soul waypoints.", subcategoryId = 1)
    @ConfigEditorBoolean
    public static boolean fairySoulWaypoints = true;

    @Expose
    @ConfigOption(name = "Fairy Soul Waypoints Color", desc = "Color of fairy soul waypoints.", subcategoryId = 1)
    @ConfigEditorColour
    public static String fairySoulWaypointsColor = "0:100:0:0:255";

    // Etherwarp (subcategoryId = 2)
    @Expose
    @ConfigOption(name = "Etherwarp Overlay", desc = "Show etherwarp overlay.", subcategoryId = 2)
    @ConfigEditorBoolean
    public static boolean qolEtherwarpOverlay = true;

    @Expose
    @ConfigOption(name = "Etherwarp Overlay Color", desc = "Color of the etherwarp overlay.", subcategoryId = 2)
    @ConfigEditorColour
    public static String qolEtherwarpOverlayColor = "0:100:255:0:100";

    @Expose
    @ConfigOption(name = "Etherwarp Sound", desc = "Sound played on etherwarp.", subcategoryId = 2)
    @ConfigEditorDropdown(values = {"Default", "mob.blaze.hit", "note.pling", "random.orb", "mob.enderdragon.hit", "mob.cat.meow"})
    public static int qolEtherwarpSound = 0;

    // Shortcuts (subcategoryId = 3)
    @Expose
    @ConfigOption(name = "Wardrobe Shortcut", desc = "Enable wardrobe shortcut.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean qolShortcutWardrobe = true;

    @Expose
    @ConfigOption(name = "Wardrobe Shortcut Key", desc = "Keybind for wardrobe shortcut.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_R)
    public static int qolWardrobeKey = Keyboard.KEY_R;

    @Expose
    @ConfigOption(name = "Pets Shortcut", desc = "Enable pets shortcut.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean qolShortcutPets = true;

    @Expose
    @ConfigOption(name = "Pets Shortcut Key", desc = "Keybind for pets shortcut.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_P)
    public static int qolPetsKey = Keyboard.KEY_P;

    @Expose
    @ConfigOption(name = "Warps Shortcuts", desc = "Enable warps shortcuts.", subcategoryId = 3)
    @ConfigEditorBoolean
    public static boolean qolShortcutWarps = true;

    @Expose
    @ConfigOption(name = "Warp Island Shortcut Key", desc = "Keybind for warp island.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_F7)
    public static int qolShortcutWarpIs = Keyboard.KEY_F7;

    @Expose
    @ConfigOption(name = "Warp Hub Shortcut Key", desc = "Keybind for warp hub.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_F8)
    public static int qolShortcutWarpHub = Keyboard.KEY_F8;

    @Expose
    @ConfigOption(name = "Warp Dungeon Hub Shortcut Key", desc = "Keybind for warp dungeon hub.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_F9)
    public static int qolShortcutWarpDh = Keyboard.KEY_F9;

    // Pets (subcategoryId = 4)
    @Expose
    @ConfigOption(name = "Show Pet Equipped", desc = "Show equipped pet.", subcategoryId = 4)
    @ConfigEditorBoolean
    public static boolean qolShowPetEquipped = true;

    @Expose
    @ConfigOption(name = "Pet Equipped Color", desc = "Color of equipped pet indicator.", subcategoryId = 4)
    @ConfigEditorColour
    public static String qolPetEquippedColor = "0:190:255:190:255";

    // Chat (subcategoryId = 5)
    @Expose
    @ConfigOption(name = "Disable Watchdog & Info Messages", desc = "Disable watchdog and info messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean qolDisableWatchdogInfo = true;

    @Expose
    @ConfigOption(name = "Disable Friend Join/Left Messages", desc = "Disable friend join/left messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean qolDisableFriendJoin = false;

    @Expose
    @ConfigOption(name = "Disable Zombie Rare Drops Messages", desc = "Disable zombie rare drops messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean qolDisableZombieRareDrops = true;

    @Expose
    @ConfigOption(name = "Disable 'Selling Ranks' Messages", desc = "Disable 'selling ranks' messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public static boolean qolDisableSellingRanks = false;

    // Items (subcategoryId = 6)
    @Expose
    @ConfigOption(name = "Scrollable Tooltips", desc = "Enable scrollable tooltips.", subcategoryId = 6)
    @ConfigEditorBoolean
    public static boolean qolScrollableTooltips = true;

    // Sounds & Animations (subcategoryId = 7)
    @Expose
    @ConfigOption(name = "No Hurt Camera", desc = "Disable hurt camera effect.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolNoHurtCam = true;

    @Expose
    @ConfigOption(name = "Disable Jerry-chine Gun Sounds", desc = "Disable Jerry-chine gun sounds.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolDisableJerryChineGunSounds = true;

    @Expose
    @ConfigOption(name = "Disable AOTE Teleport Sounds", desc = "Disable Aspect of the End teleport sounds.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolDisableAoteSounds = false;

    @Expose
    @ConfigOption(name = "Disable Hyperion Explosion", desc = "Disable Hyperion explosion effects.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolDisableHyperionExplosions = true;

    @Expose
    @ConfigOption(name = "Disable Thunderlord Bolt", desc = "Disable Thunderlord bolt effects.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolDisableThunderlordBolt = true;

    @Expose
    @ConfigOption(name = "Minimum Midas Staff Animation and Sounds", desc = "Reduce Midas Staff animation and sounds.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolDisableMidaStaffAnimation = false;

    @Expose
    @ConfigOption(name = "Hide Flaming Fists", desc = "Hide flaming fists.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolHideFlamingFists = false;

    @Expose
    @ConfigOption(name = "Hide Dead Mobs", desc = "Hide dead mobs.", subcategoryId = 7)
    @ConfigEditorBoolean
    public static boolean qolHideDyingMobs = true;

    // Damage Formatter (subcategoryId = 8)
    @Expose
    @ConfigOption(name = "Damage Commas", desc = "Add commas to damage numbers.", subcategoryId = 8)
    @ConfigEditorBoolean
    public static boolean qolDmgCommas = true;

    @Expose
    @ConfigOption(name = "Damage Formatter", desc = "Format damage numbers (e.g., 167k instead of 167000).", subcategoryId = 8)
    @ConfigEditorBoolean
    public static boolean qolDmgFormatter = true;

    // Sync static fields to the map before saving
    public void saveStaticFields() {
        try {
            staticFieldValues.clear();
            for (Field field : QualityOfLife.class.getDeclaredFields()) {
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
            for (Field field : QualityOfLife.class.getDeclaredFields()) {
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
