package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.secrets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.config.features.Dungeons;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.events.SecretChestOpenedEvent;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.ginafro.notenoughfakepixel.utils.ColorUtils;
import org.ginafro.notenoughfakepixel.utils.RenderUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@RegisterEvents
public class RenderSecrets {
    private final ArrayList<Secret> secrets = new ArrayList<>();
    private String lastHash = "";
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e){
        if(secrets.isEmpty()) return;
        if(DungeonManager.checkEssentials() && !RoomDetection.hash.isEmpty()) {
            secrets.forEach(s -> {
                if (Minecraft.getMinecraft().thePlayer.getDistanceSq(s.getPos()) < 16) {
                    if (e.message.getUnformattedText().contains("You hear the sound of something opening")) {
                        if (s.type == SecretType.LEVER) {
                            System.out.println("Disabled Lever Secret");
                            s.enabled = false;
                        }
                    }
                    if (e.message.getUnformattedText().contains("Everyone gains an extra essence")) {
                        if (e.message.getUnformattedText().contains(Minecraft.getMinecraft().thePlayer.getName())) {
                            if (s.type == SecretType.ESSENCE) {
                                s.enabled = false;
                                System.out.println("Disabled Essence Secret");
                            }
                        }
                    }
                    if (e.message.getUnformattedText().contains("DUNGEON BUFF!")) {
                        if (e.message.getUnformattedText().contains(Minecraft.getMinecraft().thePlayer.getName())) {
                            if (s.type == SecretType.CHEST) {
                                s.enabled = false;
                                System.out.println("Disabled Blessing Chest Secret");
                            }
                        }
                    }

                } else if (Minecraft.getMinecraft().thePlayer.getDistanceSq(s.getPos()) < 225) {
                    if (e.message.getUnformattedText().contains("DUNGEON BUFF!")) {
                        if (e.message.getUnformattedText().contains(Minecraft.getMinecraft().thePlayer.getName())) {
                            if (s.type == SecretType.BAT) {
                                s.enabled = false;
                                System.out.println("Disabled Bat Secret");
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onOpen(SecretChestOpenedEvent e){
        if(secrets.isEmpty()) return;
        secrets.forEach(s -> {
            if (Minecraft.getMinecraft().thePlayer.getDistanceSq(s.getPos()) < 16) {
                if(s.type == SecretType.CHEST){
                    s.enabled = false;
                    System.out.println("Disabled Chest Secret");
                }
            }
        });
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e){
        if(secrets.isEmpty()) return;
        for(Secret s : secrets){
            if(Minecraft.getMinecraft().thePlayer.getDistanceSq(s.getPos()) < 4){
                if(s.type == SecretType.ITEM){
                    s.enabled = false;
                }
            }
            if(s.type == SecretType.WALL){
                if(e.world.getBlockState(s.getPos()).getBlock() == Blocks.air){
                    s.enabled = false;
                }
            }
            if(Minecraft.getMinecraft().thePlayer.getDistanceSq(s.getPos()) < 4){
                if(s.type == SecretType.ENTRANCE){
                    s.enabled = false;
                }
            }
            if(Mouse.isButtonDown(0)){
                if(s.type == SecretType.MUSHROOM){
                    if(e.world.getBlockState(s.getPos()).getBlock() == Blocks.air){
                        s.enabled = false;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent e){
        if(secrets.isEmpty()) return;
        if(e.getPlayer() == Minecraft.getMinecraft().thePlayer){
            for(Secret s : secrets){
                if(s.type == SecretType.MUSHROOM){
                        if(e.state.getBlock().equals(Blocks.red_mushroom)){
                            if(Minecraft.getMinecraft().thePlayer.getDistanceSq(s.getPos()) < 16){
                                s.enabled = false;
                            }
                        }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        if(Config.feature.dungeons.secretDisplay){
        if (DungeonManager.checkEssentials()) {
            String currentHash = RoomDetection.hash;
            if (!currentHash.isEmpty()) {
                if (!currentHash.equals(lastHash)) {
                    lastHash = currentHash;
                    secrets.clear();
                    loadSecrets();
                    if (SaveSecretCommand.secrets.containsKey(currentHash)) {
                        secrets.addAll(SaveSecretCommand.secrets.get(currentHash));
                    }
                }

                for (Secret sec : secrets) {
                    if(sec.type == SecretType.ANCHOR || !sec.enabled) continue;
                    BlockPos real = RoomDetection.rotateRelativeCoordToWorld(sec.pos);
                    Dungeons dung = Config.feature.dungeons;
                    String color = getColor(sec, dung);
                    boolean renderBox = true,renderBeam = true,renderText = true;
                        switch(dung.secretRender){
                            case 0:
                                renderBeam = false;
                                renderText = false;
                                break;
                            case 1:
                                renderText = false;
                                break;
                            case 3:
                                renderText = false;
                                renderBox = false;
                                break;
                            case 4:
                                renderBeam = false;
                                break;
                        }
                        Minecraft.getMinecraft().fontRendererObj.drawString("Text: " + renderText + " | Box: " + renderBox + " | Beam: " + renderBeam,150,15,-1);
                        Color colour = ColorUtils.getColor(color);
                        GlStateManager.pushMatrix();
                    try {
                        if (renderBeam) RenderUtils.renderBeaconBeam(real, colour.getRGB(), 1f, e.partialTicks);
                        if (renderBox) RenderUtils.highlightBlock(real, colour, true, e.partialTicks);
                        if (renderText) RenderUtils.renderWaypointText(sec.type.name, real.up(2), e.partialTicks);
                    } finally {
                        GlStateManager.popMatrix();
                    }
                    }
                }
            }
        }
    }

    private static String getColor(Secret sec, Dungeons dung) {
        String color = "";
        if(sec.type == SecretType.LEVER || sec.type == SecretType.WALL || sec.type == SecretType.ENTRANCE){
            color = dung.wallColor;
        }
        if(sec.type == SecretType.ESSENCE){
            color = dung.essenceColor;
        }
        if(sec.type == SecretType.ITEM){
            color = dung.itemColor;
        }
        if(sec.type == SecretType.CHEST){
            color = dung.chestColor;
        }
        return color;
    }


    public static void loadSecrets() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(Config.configDirectory,"secrets.json");
        try{
            if(!file.exists()){
                file.createNewFile();
                return;
            }
            FileReader reader = new FileReader(file);
            SecretsData data = gson.fromJson(reader,SecretsData.class);
            if (data != null && data.secrets != null) {
                SaveSecretCommand.secrets = data.secrets;
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
