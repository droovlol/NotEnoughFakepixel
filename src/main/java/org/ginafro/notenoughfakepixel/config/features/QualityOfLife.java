package org.ginafro.notenoughfakepixel.config.features;

import com.google.gson.annotations.Expose;
import org.ginafro.notenoughfakepixel.config.gui.core.config.annotations.*;
import org.lwjgl.input.Keyboard;

public class QualityOfLife {

    @Expose
    @ConfigOption(name = "Fullbright", desc = "Enable fullbright.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolFullbright = true;

    @Expose
    @ConfigOption(name = "1.12 Crops Height", desc = "Use 1.12 crops height.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolCropsHeight = false;

    @Expose
    @ConfigOption(name = "Disable Block Breaking Particles", desc = "Disable block breaking particles.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolHideBlockBreakingParticles = false;

    @Expose
    @ConfigOption(name = "Always Sprint", desc = "Always sprint.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolAlwaysSprint = true;

    @Expose
    @ConfigOption(name = "Disable Rain", desc = "Disables rain rendering.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolDisableRain = true;

    @Expose
    @ConfigOption(name = "Disable Potion Effects in Inventory", desc = "Disable potion effects in inventory.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolDisablePotionEffects = true;

    @Expose
    @ConfigOption(name = "Middle Click on Terminals and Enchanting", desc = "Enable middle click on terminals and enchanting GUI.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolMiddleClickChests = true;

    @Expose
    @ConfigOption(name = "Visual Cooldowns", desc = "Use weapon durability as a cooldown timer.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolVisualCooldowns = true;

    @Expose
    @ConfigOption(name = "Item Rarity Display", desc = "Show visual circle indicating item rarity.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolItemRarity = true;

    @Expose
    @ConfigOption(name = "Item Rarity opacity", desc = "Adjust how visible item rarity background is.", subcategoryId = 0)
    @ConfigEditorSlider(minValue = 0f, maxValue = 1.0f, minStep = 0.05f)
    public float qolItemRarityOpacity = 0.6f;

    @Expose
    @ConfigOption(name = "Disable Enderman Teleport", desc = "Disable enderman teleportation.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolDisableEnderManTeleport = true;

    @Expose
    @ConfigOption(name = "Copy Chat Message", desc = "Enable copying chat messages.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolCopyChatMsg = true;

    @Expose
    @ConfigOption(name = "Full Block Lever", desc = "Make levers full blocks.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolFullBlockLever = true;

    @Expose
    @ConfigOption(name = "Block Placing Items", desc = "Prevent items from being placed as blocks.", subcategoryId = 0)
    @ConfigEditorBoolean
    public boolean qolBlockPlacingItems = true;

    @Expose
    @ConfigOption(name = "Fairy Soul Waypoints", desc = "Enable fairy soul waypoints.", subcategoryId = 1)
    @ConfigEditorBoolean
    public boolean fairySoulWaypoints = false;

    @Expose
    @ConfigOption(name = "Fairy Soul Waypoints Color", desc = "Color of fairy soul waypoints.", subcategoryId = 1)
    @ConfigEditorColour
    public String fairySoulWaypointsColor = "0:100:255:255:255";

    @Expose
    @ConfigOption(name = "Etherwarp Overlay", desc = "Show etherwarp overlay.", subcategoryId = 2)
    @ConfigEditorBoolean
    public boolean qolEtherwarpOverlay = true;

    @Expose
    @ConfigOption(name = "Etherwarp Overlay Color", desc = "Color of the etherwarp overlay.", subcategoryId = 2)
    @ConfigEditorColour
    public String qolEtherwarpOverlayColor = "0:100:0:255:0";

    @Expose
    @ConfigOption(name = "Etherwarp Sound", desc = "Sound played on etherwarp.", subcategoryId = 2)
    @ConfigEditorDropdown(values = {"Default", "mob.blaze.hit", "note.pling", "random.orb", "mob.enderdragon.hit", "mob.cat.meow"})
    public int qolEtherwarpSound = 0;

    @Expose
    @ConfigOption(name = "Wardrobe Shortcut", desc = "Enable wardrobe shortcut.", subcategoryId = 3)
    @ConfigEditorBoolean
    public boolean qolShortcutWardrobe = false;

    @Expose
    @ConfigOption(name = "Wardrobe Shortcut Key", desc = "Keybind for wardrobe shortcut.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_R)
    public int qolWardrobeKey = Keyboard.KEY_R;

    @Expose
    @ConfigOption(name = "Pets Shortcut", desc = "Enable pets shortcut.", subcategoryId = 3)
    @ConfigEditorBoolean
    public boolean qolShortcutPets = false;

    @Expose
    @ConfigOption(name = "Pets Shortcut Key", desc = "Keybind for pets shortcut.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_P)
    public int qolPetsKey = Keyboard.KEY_P;

    @Expose
    @ConfigOption(name = "Warps Shortcuts", desc = "Enable warps shortcuts.", subcategoryId = 3)
    @ConfigEditorBoolean
    public boolean qolShortcutWarps = false;

    @Expose
    @ConfigOption(name = "Warp Island Shortcut Key", desc = "Keybind for warp island.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_F7)
    public int qolShortcutWarpIs = Keyboard.KEY_F7;

    @Expose
    @ConfigOption(name = "Warp Hub Shortcut Key", desc = "Keybind for warp hub.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_F8)
    public int qolShortcutWarpHub = Keyboard.KEY_F8;

    @Expose
    @ConfigOption(name = "Warp Dungeon Hub Shortcut Key", desc = "Keybind for warp dungeon hub.", subcategoryId = 3)
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_F9)
    public int qolShortcutWarpDh = Keyboard.KEY_F9;

    @Expose
    @ConfigOption(name = "Show Pet Equipped", desc = "Show equipped pet.", subcategoryId = 4)
    @ConfigEditorBoolean
    public boolean qolShowPetEquipped = true;

    @Expose
    @ConfigOption(name = "Pet Equipped Color", desc = "Color of equipped pet indicator.", subcategoryId = 4)
    @ConfigEditorColour
    public String qolPetEquippedColor = "0:190:0:255:0";

    @Expose
    @ConfigOption(name = "Disable Watchdog & Info Messages", desc = "Disable watchdog and info messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public boolean qolDisableWatchdogInfo = true;

    @Expose
    @ConfigOption(name = "Disable Friend Join/Left Messages", desc = "Disable friend join/left messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public boolean qolDisableFriendJoin = false;

    @Expose
    @ConfigOption(name = "Disable Zombie Rare Drops Messages", desc = "Disable zombie rare drops messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public boolean qolDisableZombieRareDrops = true;

    @Expose
    @ConfigOption(name = "Disable 'Selling Ranks' Messages", desc = "Disable 'selling ranks' messages.", subcategoryId = 5)
    @ConfigEditorBoolean
    public boolean qolDisableSellingRanks = false;

    @Expose
    @ConfigOption(name = "Scrollable Tooltips", desc = "Enable scrollable tooltips.", subcategoryId = 6)
    @ConfigEditorBoolean
    public boolean qolScrollableTooltips = true;

    @Expose
    @ConfigOption(name = "No Hurt Camera", desc = "Disable hurt camera effect.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolNoHurtCam = true;

    @Expose
    @ConfigOption(name = "Disable Jerry-chine Gun Sounds", desc = "Disable Jerry-chine gun sounds.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolDisableJerryChineGunSounds = true;

    @Expose
    @ConfigOption(name = "Disable AOTE Teleport Sounds", desc = "Disable Aspect of the End teleport sounds.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolDisableAoteSounds = false;

    @Expose
    @ConfigOption(name = "Disable Hyperion Explosion", desc = "Disable Hyperion explosion effects.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolDisableHyperionExplosions = true;

    @Expose
    @ConfigOption(name = "Disable Thunderlord Bolt", desc = "Disable Thunderlord bolt effects.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolDisableThunderlordBolt = true;

    @Expose
    @ConfigOption(name = "Minimum Midas Staff Animation and Sounds", desc = "Reduce Midas Staff animation and sounds.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolDisableMidaStaffAnimation = false;

    @Expose
    @ConfigOption(name = "Hide Flaming Fists", desc = "Hide flaming fists.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolHideFlamingFists = false;

    @Expose
    @ConfigOption(name = "Hide Dead Mobs", desc = "Hide dead mobs.", subcategoryId = 7)
    @ConfigEditorBoolean
    public boolean qolHideDyingMobs = true;

    @Expose
    @ConfigOption(name = "Damage Commas", desc = "Add commas to damage numbers.", subcategoryId = 8)
    @ConfigEditorBoolean
    public boolean qolDmgCommas = true;

    @Expose
    @ConfigOption(name = "Damage Formatter", desc = "Format damage numbers (e.g., 167k instead of 167000).", subcategoryId = 8)
    @ConfigEditorBoolean
    public boolean qolDmgFormatter = true;

    @Expose
    @ConfigOption(name = "Item Animation Toggle", desc = "Change the look of your held item.", subcategoryId = 9)
    @ConfigEditorBoolean
    public boolean customAnimations = false;

    @Expose
    @ConfigOption(name = "Item Animation Size", desc = "Scales the size of your currently held item. Default: 0", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -1.5f, maxValue = 1.5f, minStep = 0.05f)
    public float customSize = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Scale Swing", desc = "Also scale the size of the swing animation.", subcategoryId = 9)
    @ConfigEditorBoolean
    public boolean doesScaleSwing = true;

    @Expose
    @ConfigOption(name = "Item Animation X", desc = "Moves the held item. Default: 0", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -1.5f, maxValue = 1.5f, minStep = 0.05f)
    public float customX = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Y", desc = "Moves the held item. Default: 0", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -1.5f, maxValue = 1.5f, minStep = 0.05f)
    public float customY = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Z", desc = "Moves the held item. Default: 0", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -1.5f, maxValue = 1.5f, minStep = 0.05f)
    public float customZ = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Yaw", desc = "Rotates your held item. Default: 0", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -180f, maxValue = 180f, minStep = 1f)
    public float customYaw = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Pitch", desc = "Rotates your held item. Default: 0", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -180f, maxValue = 180f, minStep = 1f)
    public float customPitch = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Roll", desc = "Rotates your held item. Default: 0", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -180f, maxValue = 180f, minStep = 1f)
    public float customRoll = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Speed", desc = "Speed of the swing animation.", subcategoryId = 9)
    @ConfigEditorSlider(minValue = -2f, maxValue = 1f, minStep = 0.05f)
    public float customSpeed = 0f;

    @Expose
    @ConfigOption(name = "Item Animation Ignore Haste", desc = "Makes the chosen speed override haste modifiers.", subcategoryId = 9)
    @ConfigEditorBoolean
    public boolean ignoreHaste = true;

    @Expose
    @ConfigOption(name = "Item Animation Drinking Fix", desc = "Pick how to handle drinking animations.", subcategoryId = 9)
    @ConfigEditorDropdown(values = {"No fix", "Rotationless", "Fixed"})
    public int drinkingSelector = 2;

    @Expose
    @ConfigOption(name = "Item Animation Reset Item Values", desc = "Vanilla Look! Closes Settings GUI.", subcategoryId = 9)
    @ConfigEditorButton(runnableId = "resetItemValues", buttonText = "Reset!")
    public String resetItemValuesButton = "";

    // Method to handle the reset button functionality
    public void resetItemValues() {
        customSize = 0f;
        customX = 0f;
        customY = 0f;
        customZ = 0f;
        customRoll = 0f;
        customPitch = 0f;
        customYaw = 0f;
        doesScaleSwing = true;
        ignoreHaste = true;
        customSpeed = 0f;
    }
}