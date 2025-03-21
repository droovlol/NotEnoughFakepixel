package org.ginafro.notenoughfakepixel.features.skyblock.crimson;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.utils.ChatUtils;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.SoundUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

public class BossNotifier {
    private final static String countdownSound = "random.orb";
    private final static String titleSoundMageOutlawReady = "mob.wither.spawn";
    private final static String titleTextBossReady = " Ready";
    private final boolean[] bladesoulScheduled = new boolean[]{false,false,false,false,false,false};
    private final boolean[] mageOutlawScheduled = new boolean[]{false,false,false,false,false,false};
    private static final boolean[] ashfangScheduled = new boolean[]{false,false,false,false,false,false};
    private static final boolean[] barbarianDukeXScheduled = new boolean[]{false,false,false,false,false,false};
    private long mageOutlawLastKill = -1;
    private long mageOutlawReady = -1;
    private long ashfangLastKill = -1;
    private long ashfangReady = -1;
    private long barbarianDukeXLastKill = -1;
    private long barbarianDukeXReady = -1;
    private long bladesoulLastKill = -1;
    private long bladesoulReady = -1;
    private final int spawnBladesoulSeconds = 124;
    private final int spawnOutlawSeconds = 125;
    private final int spawnAshfangSeconds = 124;
    private final int spawnBarbarianDukeXSeconds = 124;
    private final String joinText = " spawning in ";

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if (Crimson.checkEssentials()) return;
        if (NotEnoughFakepixel.feature.crimson.crimsonBladesoulNotifier) {
            playCountdown("Bladesoul", bladesoulReady, bladesoulLastKill,bladesoulScheduled);
        }
        if (NotEnoughFakepixel.feature.crimson.crimsonMageOutlawNotifier) {
            playCountdown("Mage Outlaw", mageOutlawReady, mageOutlawLastKill,mageOutlawScheduled);
        }
        if (NotEnoughFakepixel.feature.crimson.crimsonAshfangNotifier) {
            playCountdown("Ashfang", ashfangReady, ashfangLastKill,ashfangScheduled);
        }
        if (NotEnoughFakepixel.feature.crimson.crimsonBarbarianDukeXNotifier) {
            playCountdown("Barbarian Duke X", barbarianDukeXReady, barbarianDukeXLastKill,barbarianDukeXScheduled);
        }
    }

    @SubscribeEvent
    public void onChat(@NotNull ClientChatReceivedEvent e){
        if (Crimson.checkEssentials()) return;

        if (NotEnoughFakepixel.feature.crimson.crimsonBladesoulNotifier) {
            Matcher matcher = Pattern.compile("BLADESOUL DOWN!").matcher(e.message.getUnformattedText());
            if (matcher.find()) {
                bladesoulLastKill = System.currentTimeMillis();
                bladesoulReady = bladesoulLastKill + spawnBladesoulSeconds*1000;
                Arrays.fill(bladesoulScheduled, true);
                return;
            }
        }

        if (NotEnoughFakepixel.feature.crimson.crimsonMageOutlawNotifier) {
            Matcher matcher = Pattern.compile("MAGE OUTLAW DOWN!").matcher(e.message.getUnformattedText());
            if (matcher.find()) {
                mageOutlawLastKill = System.currentTimeMillis();
                mageOutlawReady = mageOutlawLastKill + spawnOutlawSeconds*1000;
                Arrays.fill(mageOutlawScheduled, true);
                return;
            }
        }

        if (NotEnoughFakepixel.feature.crimson.crimsonAshfangNotifier) {
            Matcher matcher = Pattern.compile("ASHFANG DOWN!").matcher(e.message.getUnformattedText());
            if (matcher.find()) {
                ashfangLastKill = System.currentTimeMillis();
                ashfangReady = ashfangLastKill + spawnAshfangSeconds*1000;
                Arrays.fill(ashfangScheduled, true);
                return;
            }
        }

        if (NotEnoughFakepixel.feature.crimson.crimsonBarbarianDukeXNotifier) {
            Matcher matcher = Pattern.compile("BARBARIAN DUKE X DOWN!").matcher(e.message.getUnformattedText());
            if (matcher.find()) {
                barbarianDukeXLastKill = System.currentTimeMillis();
                barbarianDukeXReady = barbarianDukeXLastKill + spawnBarbarianDukeXSeconds*1000;
                Arrays.fill(barbarianDukeXScheduled, true);
                return;
            }
        }
    }

    @SubscribeEvent()
    public void onWorldUnload(WorldEvent.Unload event) {
        if (Crimson.checkEssentials()) return;
        if (NotEnoughFakepixel.feature.crimson.crimsonBladesoulNotifier) {
            Arrays.fill(bladesoulScheduled, false);
            bladesoulReady = -1;
            bladesoulLastKill = -1;
        }
        if (NotEnoughFakepixel.feature.crimson.crimsonMageOutlawNotifier) {
            Arrays.fill(mageOutlawScheduled, false);
            mageOutlawReady = -1;
            mageOutlawLastKill = -1;
        }
        if (NotEnoughFakepixel.feature.crimson.crimsonAshfangNotifier) {
            Arrays.fill(ashfangScheduled, false);
            ashfangReady = -1;
            ashfangLastKill = -1;
        }
        if (NotEnoughFakepixel.feature.crimson.crimsonBarbarianDukeXNotifier) {
            Arrays.fill(barbarianDukeXScheduled, false);
            barbarianDukeXReady = -1;
            barbarianDukeXLastKill = -1;
        }
    }

    private void playCountdown(String boss, long timeBossReady, long bossLastKill, boolean[] bossScheduled) {
        if (timeBossReady != -1 && bossLastKill != -1) {
            // timer passed
            if (bossScheduled[0] && System.currentTimeMillis() > timeBossReady) {
                notifyTitle(boss);
                bossScheduled[0] = false;
            } else if (bossScheduled[1] && System.currentTimeMillis() > timeBossReady-123000) {
                notifyChat(boss,120);
                bossScheduled[1] = false;
            } else if (bossScheduled[2] && System.currentTimeMillis() > timeBossReady-60000) {
                notifyChat(boss,60);
                bossScheduled[2] = false;
            } else if (bossScheduled[3] && System.currentTimeMillis() > timeBossReady-30000) {
                notifyChat(boss,30);
                bossScheduled[3] = false;
            } else if (bossScheduled[4] && System.currentTimeMillis() > timeBossReady-10000) {
                notifyChat(boss,10);
                bossScheduled[4] = false;
            }  else if (bossScheduled[5] && System.currentTimeMillis() > timeBossReady-5000) {
                notifyChat(boss,5);
                //playCountdownSound(5);
                bossScheduled[5] = false;
            }
        }
    }

    private void notifyChat(String boss, int seconds){
        if (seconds == 120) {
            ChatUtils.notifyChat(EnumChatFormatting.YELLOW + boss + joinText + "2 minutes");
        } else if (seconds == 60) {
            ChatUtils.notifyChat(EnumChatFormatting.YELLOW + boss + joinText + "1 minute");
        } else {
            ChatUtils.notifyChat(EnumChatFormatting.YELLOW + boss + joinText + seconds + " seconds");
        }
        SoundUtils.playSound(new int[]{Minecraft.getMinecraft().thePlayer.getPosition().getX(),
                Minecraft.getMinecraft().thePlayer.getPosition().getY(),
                Minecraft.getMinecraft().thePlayer.getPosition().getZ()},countdownSound,2.0f,1.0f);
    }

    private void notifyTitle(String boss) {
        Minecraft.getMinecraft().ingameGUI.displayTitle(EnumChatFormatting.GOLD + boss + titleTextBossReady, "", 2, 25, 2);
        SoundUtils.playSound(new int[]{Minecraft.getMinecraft().thePlayer.getPosition().getX(),
                Minecraft.getMinecraft().thePlayer.getPosition().getY(),
                Minecraft.getMinecraft().thePlayer.getPosition().getZ()}, titleSoundMageOutlawReady, 2.0f, 1.0f);
        if (boss.equals("Bladesoul")){
            bladesoulScheduled[0] = false;
        } else if (boss.equals("Mage Outlaw")) {
            mageOutlawScheduled[0] = false;
        } else if (boss.equals("Ashfang")) {
            ashfangScheduled[0] = false;
        } else if (boss.equals("Barbarian Duke X")) {
            barbarianDukeXScheduled[0] = false;
        }
    }

    private static void playCountdownSound(int times){
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(4);
        for (int i = 0; i < times; i++) {
            exec.schedule(new Runnable() {
                public void run() {
                    SoundUtils.playSound(new int[]{Minecraft.getMinecraft().thePlayer.getPosition().getX(),
                            Minecraft.getMinecraft().thePlayer.getPosition().getY(),
                            Minecraft.getMinecraft().thePlayer.getPosition().getZ()},countdownSound,2.0f,1.0f+ (float)(times*2)/10);
                }
            }, i, TimeUnit.SECONDS);
        }
    }

    public static boolean[] getAshfangScheduled() {
        return ashfangScheduled;
    }
}