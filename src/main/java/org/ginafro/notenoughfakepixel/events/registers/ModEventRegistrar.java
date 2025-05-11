package org.ginafro.notenoughfakepixel.events.registers;

import net.minecraftforge.common.MinecraftForge;
import org.ginafro.notenoughfakepixel.alerts.Alerts;
import org.ginafro.notenoughfakepixel.events.handlers.ConnectionHandler;
import org.ginafro.notenoughfakepixel.features.duels.KDCounter;
import org.ginafro.notenoughfakepixel.features.mlf.Map;
import org.ginafro.notenoughfakepixel.features.skyblock.chocolate.ChocolateFactory;
import org.ginafro.notenoughfakepixel.features.skyblock.crimson.AshfangHelper;
import org.ginafro.notenoughfakepixel.features.skyblock.crimson.AshfangOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.crimson.BossNotifier;
import org.ginafro.notenoughfakepixel.features.skyblock.diana.Diana;
import org.ginafro.notenoughfakepixel.features.skyblock.diana.GuessBurrow;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.*;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.devices.FirstDeviceSolver;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.devices.ThirdDeviceSolver;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.mobs.BatMobDisplay;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.mobs.FelMobDisplay;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.mobs.LividDisplay;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.mobs.StarredMobDisplay;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.puzzles.*;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.score.DungeonClearedNotifier;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.score.SPlusNotifier;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.score.ScoreManager;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.score.ScoreOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals.*;
import org.ginafro.notenoughfakepixel.features.skyblock.enchanting.EnchantingSolvers;
import org.ginafro.notenoughfakepixel.features.skyblock.enchanting.HideEnchantingTooltips;
import org.ginafro.notenoughfakepixel.features.skyblock.enchanting.PreventMissclicks;
import org.ginafro.notenoughfakepixel.features.skyblock.fishing.GreatCatchNotifier;
import org.ginafro.notenoughfakepixel.features.skyblock.mining.*;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.inventory.equipment.EquipmentOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage.StorageDataHandler;
import org.ginafro.notenoughfakepixel.features.skyblock.overlays.storage.StorageOverlay;
import org.ginafro.notenoughfakepixel.features.skyblock.qol.*;
import org.ginafro.notenoughfakepixel.features.skyblock.slayers.*;
import org.ginafro.notenoughfakepixel.features.skyblock.slotlocking.SlotLocking;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.utils.TablistParser;

public class ModEventRegistrar {

    public static void registerModEvents() {
        registerDungeons();
        registerMining();
        registerFishing();
        registerEnchanting();
        registerQOL();
        registerOverlays();
        registerModes();
        registerCrimson();
        registerDiana();
        registerSlayerEvents();
        registerParsers();
    }

    private static void registerEnchanting() {
        MinecraftForge.EVENT_BUS.register(new EnchantingSolvers());
        MinecraftForge.EVENT_BUS.register(new HideEnchantingTooltips());
        MinecraftForge.EVENT_BUS.register(new PreventMissclicks());
    }


     private static void registerOverlays(){
        MinecraftForge.EVENT_BUS.register(new StorageOverlay.StorageEvent());
        MinecraftForge.EVENT_BUS.register(new StorageDataHandler());
        MinecraftForge.EVENT_BUS.register(new EquipmentOverlay());
        //MinecraftForge.EVENT_BUS.register(new CustomBar());
    }

    private static void registerModes(){
        MinecraftForge.EVENT_BUS.register(new KDCounter());
        MinecraftForge.EVENT_BUS.register(new Map());
    }

    private static void registerFishing(){
        MinecraftForge.EVENT_BUS.register(new GreatCatchNotifier());
    }

    private static void registerCrimson(){
        MinecraftForge.EVENT_BUS.register(new AshfangOverlay());
        MinecraftForge.EVENT_BUS.register(new BossNotifier());
        MinecraftForge.EVENT_BUS.register(new AshfangHelper());
    }


    private static void registerMining(){
        MinecraftForge.EVENT_BUS.register(new MiningOverlay());
        MinecraftForge.EVENT_BUS.register(new DrillFuelParsing());
        MinecraftForge.EVENT_BUS.register(new AbilityNotifier());
        MinecraftForge.EVENT_BUS.register(new EventsMsgSupressor());
        MinecraftForge.EVENT_BUS.register(new DrillFix());
        MinecraftForge.EVENT_BUS.register(new PuzzlerSolver());
        MinecraftForge.EVENT_BUS.register(new RemoveGhostInvis());
    }

    private static void registerDungeons(){
        DungeonsMap map = new DungeonsMap();
        MinecraftForge.EVENT_BUS.register(map);

        MinecraftForge.EVENT_BUS.register(new WelcomeMessage());
        MinecraftForge.EVENT_BUS.register(new SalvageItemsSaver());
        MinecraftForge.EVENT_BUS.register(new ShowNotOpenedChests());

        MinecraftForge.EVENT_BUS.register(new StartingWithSolver());
        MinecraftForge.EVENT_BUS.register(new ClickOnColorsSolver());
        MinecraftForge.EVENT_BUS.register(new ClickInOrderSolver());
        MinecraftForge.EVENT_BUS.register(new MazeSolver());
        MinecraftForge.EVENT_BUS.register(new CorrectPanesSolver());
        MinecraftForge.EVENT_BUS.register(new FirstDeviceSolver());
        MinecraftForge.EVENT_BUS.register(new ThirdDeviceSolver());
        MinecraftForge.EVENT_BUS.register(new HideTooltips());
        MinecraftForge.EVENT_BUS.register(new WitherDoors());

        MinecraftForge.EVENT_BUS.register(new AutoReadyDungeon());
        MinecraftForge.EVENT_BUS.register(new AutoCloseChests());

        MinecraftForge.EVENT_BUS.register(new ThreeWeirdos());
        MinecraftForge.EVENT_BUS.register(new WaterSolver());
        MinecraftForge.EVENT_BUS.register(new BoulderSolver());
        MinecraftForge.EVENT_BUS.register(new SilverFishSolver());
        MinecraftForge.EVENT_BUS.register(new TeleportMazeSolver());
        MinecraftForge.EVENT_BUS.register(new CreeperSolver());

        MinecraftForge.EVENT_BUS.register(new WitherBloodKeysTracers());
        MinecraftForge.EVENT_BUS.register(new StarredMobDisplay());
        MinecraftForge.EVENT_BUS.register(new BatMobDisplay());
        MinecraftForge.EVENT_BUS.register(new FelMobDisplay());
        MinecraftForge.EVENT_BUS.register(new ItemSecretsDisplay());
        MinecraftForge.EVENT_BUS.register(new LividDisplay());

        MinecraftForge.EVENT_BUS.register(new DungeonManager());
        MinecraftForge.EVENT_BUS.register(new ScoreManager());
        MinecraftForge.EVENT_BUS.register(new ScoreOverlay());
        MinecraftForge.EVENT_BUS.register(new SPlusNotifier());
        MinecraftForge.EVENT_BUS.register(new DungeonClearedNotifier());
        MinecraftForge.EVENT_BUS.register(new MuteIrrelevantMessages());
        MinecraftForge.EVENT_BUS.register(new SpiritLeapHandler());
        MinecraftForge.EVENT_BUS.register(new SpiritLeapHandler.ChestGuiOverlayHandler());
        MinecraftForge.EVENT_BUS.register(new MiscDungFeatures());
        MinecraftForge.EVENT_BUS.register(new TerminalWaypoints());
        MinecraftForge.EVENT_BUS.register(new TerminalTracker());
    }

    private static void registerQOL(){
        MinecraftForge.EVENT_BUS.register(new SlotLocking());
        MinecraftForge.EVENT_BUS.register(new ChocolateFactory());
        MinecraftForge.EVENT_BUS.register(new ShowCurrentPet());
        MinecraftForge.EVENT_BUS.register(new ChatCleaner());
        MinecraftForge.EVENT_BUS.register(new VisualCooldowns());
        MinecraftForge.EVENT_BUS.register(new MiddleClickEvent());
        MinecraftForge.EVENT_BUS.register(new SoundRemover());
        MinecraftForge.EVENT_BUS.register(new ScrollableTooltips());
        MinecraftForge.EVENT_BUS.register(new FairySouls());
        MinecraftForge.EVENT_BUS.register(new AutoOpenMaddox());
        MinecraftForge.EVENT_BUS.register(new MidasStaff());
        MinecraftForge.EVENT_BUS.register(new WardrobeShortcut());
        MinecraftForge.EVENT_BUS.register(new PetsShortcut());
        MinecraftForge.EVENT_BUS.register(new WarpsShortcut());
        MinecraftForge.EVENT_BUS.register(new DisableEndermanTeleport());
        MinecraftForge.EVENT_BUS.register(new HideFlamingFists());
        MinecraftForge.EVENT_BUS.register(new MiscFeatures());
        MinecraftForge.EVENT_BUS.register(new ItemAnimations());
        MinecraftForge.EVENT_BUS.register(new Alerts());
        MinecraftForge.EVENT_BUS.register(new RelicWaypoints());
        MinecraftForge.EVENT_BUS.register(new Fullbright());
    }

    private static void registerDiana(){
        MinecraftForge.EVENT_BUS.register(new Diana());
        MinecraftForge.EVENT_BUS.register(new GuessBurrow());
        //MinecraftForge.EVENT_BUS.register(new WarpOverlay(new GuessBurrow()));
    }

    private static void registerParsers(){
        MinecraftForge.EVENT_BUS.register(new TablistParser());
        MinecraftForge.EVENT_BUS.register(new ScoreboardUtils());
        MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
    }

    private static void registerSlayerEvents() {
        MinecraftForge.EVENT_BUS.register(new SlayerMobsDisplay());
        MinecraftForge.EVENT_BUS.register(new VoidgloomSeraph());
        MinecraftForge.EVENT_BUS.register(new FirePillarDisplay());
        MinecraftForge.EVENT_BUS.register(new MinibossAlert());
        MinecraftForge.EVENT_BUS.register(new BlazeAttunements());
        MinecraftForge.EVENT_BUS.register(new SlayerTimer());
        MinecraftForge.EVENT_BUS.register(new SlayerHealthDisplay());
    }

}
