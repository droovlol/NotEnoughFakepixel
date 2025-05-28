package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.secrets;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

@RegisterEvents
public class RoomDetection {
    public enum ROOMSIZE {
        ONE_ONE("1x1"),
        ONE_TWO("2x1"),
        ONE_THREE("3x1"),
        ONE_FOUR("4x1"),
        TWO_TWO("2x2"),
        L_SHAPE("L"),
        NONE("");

        public final String name;
        ROOMSIZE(String s){
            name = s;
        }

        public static ROOMSIZE get(String name) {
            for (ROOMSIZE size : values()) {
                if (size.name.equals(name)) return size;
            }
            return NONE;
        }
    }

    public static int yLvl = -1;
    int ticks = 0;

    public static int left = 0, right = 0, north = 0, south = 0;
    public static int width = 0, height = 0;
    public static int centerX = 0, centerZ = 0;
    public static BlockPos temp = new BlockPos(-5,81,-4);
    public static int rotationAngle = -1;
    public static Block blockBelow = null;
    public static String hash = "";
    public boolean closerToLeft = false, closerToNorth = false;
    ROOMSIZE size = ROOMSIZE.NONE;

    @SubscribeEvent
    public void onTick(RenderGameOverlayEvent.Post e){
        if(e.type != RenderGameOverlayEvent.ElementType.ALL) return;
        ticks++;
        if(!Config.feature.dungeons.secretDisplay) return;

        if(ticks % (int)(Config.feature.dungeons.secretUpdate * 20) == 0 && DungeonManager.checkEssentials()){
            checkYLVL();
            checkRoomSize();
            getRotation(Minecraft.getMinecraft().theWorld);
            hash = getRoomHash(Minecraft.getMinecraft().theWorld,Minecraft.getMinecraft().thePlayer);

        }
        if(ticks % 20 == 0 && DungeonManager.checkEssentials()){
            World w = Minecraft.getMinecraft().theWorld;
            EntityPlayerSP sp = Minecraft.getMinecraft().thePlayer;
            if(w != null && sp != null){
                IBlockState state = w.getBlockState(sp.getPosition().down());
                if(state != null){
                    blockBelow = state.getBlock();
                    if(blockBelow == Blocks.chest){
                        blockBelow = w.getBlockState(sp.getPosition().down(2)).getBlock();
                    }
                }
            }
        }
        if(Config.feature.debug.showSecretDebug) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.fontRendererObj.drawString("Room : " + size.name, 15, 5, -1);
            mc.fontRendererObj.drawString("Left & Right: " + left + " | " + right, 15, 15, -1);
            mc.fontRendererObj.drawString("North & South: " + north + " | " + south, 15, 25, -1);
            mc.fontRendererObj.drawString("Width & Height: " + width + " | " + height, 15, 35, -1);
            mc.fontRendererObj.drawString("Room Hash: " + hash, 15, 45, -1);
            mc.fontRendererObj.drawString("Secret At: " + rotateRelativeCoordToWorld(temp), 15, 55, -1);
            BlockPos pos = new BlockPos(mc.thePlayer);
            mc.fontRendererObj.drawString("Rotation Angle: " + rotationAngle, e.resolution.getScaledWidth() - 250, 15, -1);
            mc.fontRendererObj.drawString("Relative Position: " + worldToRelative(pos), e.resolution.getScaledWidth() - 250, 5, -1);
            mc.fontRendererObj.drawString("Actual WorldPos: " + pos, e.resolution.getScaledWidth() - 250, 25, -1);
            mc.fontRendererObj.drawString("Back Converted: " + rotateRelativeCoordToWorld(worldToRelative(pos)), e.resolution.getScaledWidth() - 250, 35, -1);
            if (blockBelow != null) mc.fontRendererObj.drawString("Block Below: " + blockBelow.getRegistryName(), e.resolution.getScaledWidth() - 250, 45, -1);
        }
    }

    private void checkYLVL() {
        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        if(p == null) return;
        World w = p.worldObj;
        if(w == null) return;
        for(int i = 200;i > 65;i--){
            BlockPos pos = new BlockPos(p.posX,i,p.posZ);
            Block b = w.getBlockState(pos).getBlock();
            if(b != Blocks.air){
                yLvl = i;
                break;
            }
        }
    }

    private void checkRoomSize() {
        if (yLvl == -1) return;

        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        if (p == null || p.worldObj == null) return;
        World w = p.worldObj;

        BlockPos pos = new BlockPos(p.posX, yLvl, p.posZ);
        Block centerBlock = w.getBlockState(pos).getBlock();
        if (centerBlock == Blocks.air) return;

        for (int x = 0; x < 136; x++) {
            BlockPos check = new BlockPos(p.posX - x, yLvl, p.posZ);
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                left = check.getX();
                break;
            }
        }

        for (int x = 0; x < 136; x++) {
            BlockPos check = new BlockPos(p.posX + x, yLvl, p.posZ);
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                right = check.getX();
                break;
            }
        }

        for (int z = 0; z < 136; z++) {
            BlockPos check = new BlockPos(p.posX, yLvl, p.posZ - z);
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                north = check.getZ();
                break;
            }
        }

        for (int z = 0; z < 136; z++) {
            BlockPos check = new BlockPos(p.posX, yLvl, p.posZ + z);
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                south = check.getZ();
                break;
            }
        }

        width = right - left;
        height = south - north;

        String name = width/32 + "x" + height/32;
        if(height > width){
            name = height/32 + "x" + width/32;
        }

        if(isLShaped()){
            size = ROOMSIZE.L_SHAPE;
            setLShapeCenterAndRotation();
        } else {
            centerX = left + width / 2;
            centerZ = north + height / 2;
            size = ROOMSIZE.get(name);
        }
    }

    private boolean isLShaped() {
        EntityPlayerSP sp = Minecraft.getMinecraft().thePlayer;
        if (sp == null) return false;
        World w = sp.worldObj;
        if (w == null) return false;

        if (width <= 32 && height <= 32) return false;

        if (width == height) {
            BlockPos center = new BlockPos(left + width / 2, yLvl, north + height / 2);
            return w.getBlockState(center).getBlock() == Blocks.air;
        }

        if(width > 64 || height > 64) return false;

        int centerX = left + width / 2;
        int centerZ = north + height / 2;

        if (width > height) {
            closerToLeft = Math.abs(sp.posX - left) <= Math.abs(sp.posX - right);
            BlockPos pos = closerToLeft ? new BlockPos(right - 16, yLvl, centerZ) : new BlockPos(left + 16, yLvl, centerZ);
            ROOMSIZE rsize = getRoomSize(pos, w);
            return rsize == ROOMSIZE.TWO_TWO;
        } else {
            closerToNorth = Math.abs(sp.posZ - north) <= Math.abs(sp.posZ - south);
            BlockPos pos = closerToNorth ? new BlockPos(centerX, yLvl, south - 16) : new BlockPos(centerX, yLvl, north + 16);
            ROOMSIZE rsize = getRoomSize(pos, w);
            return rsize == ROOMSIZE.TWO_TWO;
        }
    }

    private void setLShapeCenterAndRotation() {
        centerX = left + width / 2;
        centerZ = north + height / 2;
       if(width > height){
           centerX = left + (width/2);
           centerZ = north + height;
       }else{
           centerZ = north + height/2;
           centerX = right - width;
       }

    }

    private void getRotation(World w) {
        if (hash.isEmpty() || !SaveSecretCommand.secrets.containsKey(hash)) {
            rotationAngle = 0;
            return;
        }

        ArrayList<Secret> secrets = SaveSecretCommand.secrets.get(hash);
        int bestRotation = 0;
        int leastWrong = Integer.MAX_VALUE;

        for (int rot = 0; rot < 4; rot++) {
            int wrong = 0;
            rotationAngle = rot;

            for (Secret secret : secrets) {
                BlockPos rotated = rotateRelativeCoordToWorld(secret.pos);
                Block blockToCheck = w.getBlockState(rotated.down()).getBlock();
                    if (blockToCheck != secret.getBlock()) wrong++;
            }

            if (wrong < leastWrong) {
                leastWrong = wrong;
                bestRotation = rot;
            }

            if (leastWrong == 0) break;
        }

        rotationAngle = bestRotation;
    }
    public static BlockPos rotateRelativeCoordToWorld(BlockPos relative) {
        int x = relative.getX();
        int z = relative.getZ();

        int rx = 0, rz = 0;
        switch (rotationAngle % 4) {
            case 0: rx = x;rz = z;break;
            case 1: rx = -z;rz = x;break;
            case 2: rx = z;rz = x;break;
            case 3: rx = z;rz = -x;break;
        }

        return new BlockPos(centerX + rx, relative.getY(), centerZ + rz);
    }

    public static BlockPos worldToRelative(BlockPos worldPos) {
        int dx = worldPos.getX() - centerX;
        int dz = worldPos.getZ() - centerZ;

        int rx = 0, rz = 0;
        switch (rotationAngle % 4) {
            case 0: rx = dx;rz = dz;break;
            case 1: rx = dz;rz = -dx;break;
            case 2: rx = -dx;rz = -dz;break;
            case 3: rx = -dz; rz = dx;break;
        }

        return new BlockPos(rx, worldPos.getY(), rz);
    }

    private String getRoomHash(World w, EntityPlayerSP sp) {
        Iterable<BlockPos> poses;
        if (size != ROOMSIZE.L_SHAPE) {
            poses = BlockPos.getAllInBox(
                    new BlockPos(left, yLvl - 3, north),
                    new BlockPos(right, yLvl - 20, south)
            );
        } else {
            boolean closeToLeft = Math.abs(sp.posX - left) <= Math.abs(sp.posX - right);
            boolean closeToNorth = Math.abs(sp.posZ - north) <= Math.abs(sp.posZ - south);

            BlockPos start, end;
            if (width == height) {
                if(closeToLeft && !closeToNorth){
                    start = new BlockPos(left,yLvl - 3,south);
                    end = new BlockPos(left + 32,yLvl - 20 , south - 32);
                } else if(closeToLeft) {
                    start = new BlockPos(left,yLvl - 3,north);
                    end = new BlockPos(left + 32,yLvl - 20 , north + 32);
                } else if(closeToNorth) {
                    start = new BlockPos(right,yLvl - 3,north);
                    end = new BlockPos(right - 32,yLvl - 20 , north + 32);
                } else {
                    start = new BlockPos(right,yLvl - 3,south);
                    end = new BlockPos(right - 32,yLvl - 20 , south - 32);
                }
            } else if (width > height) {
                if (closeToLeft) {
                    start = new BlockPos(right, yLvl - 3, north);
                    end = new BlockPos(right - 32, yLvl - 20, north + 32);
                } else {
                    start = new BlockPos(left, yLvl - 3, north);
                    end = new BlockPos(left + 32, yLvl - 20, north + 32);
                }
            } else {
                if (closeToNorth) {
                    start = new BlockPos(right, yLvl - 3, south);
                    end = new BlockPos(right - 32, yLvl - 20, south - 32);
                } else {
                    start = new BlockPos(right, yLvl - 3, north);
                    end = new BlockPos(right - 32, yLvl - 20, north + 32);
                }
            }

            poses = BlockPos.getAllInBox(start, end);
        }

        ArrayList<String> blockNames = new ArrayList<>();
        for (BlockPos p : poses) {
            blockNames.add(w.getBlockState(p).getBlock().getRegistryName());
        }
        Collections.sort(blockNames);

        StringBuilder sb = new StringBuilder();
        for (String block : blockNames) {
            sb.append(block).append(",");
        }

        return getSHA256Hash(sb.toString());
    }

    private String getSHA256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private ROOMSIZE getRoomSize(BlockPos pos,World w) {
        int l = 0,r = 0,u = 0,d = 0;
        for (int x = 0; x < 128; x++) {
            BlockPos check = new BlockPos(pos.getX() - x, yLvl, pos.getZ());
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                l = check.getX();
                break;
            }
        }

        for (int x = 0; x < 128; x++) {
            BlockPos check = new BlockPos(pos.getX() + x, yLvl, pos.getZ());
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                r = check.getX();
                break;
            }
        }

        for (int z = 0; z < 128; z++) {
            BlockPos check = new BlockPos(pos.getX(), yLvl, pos.getZ() - z);
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                u = check.getZ();
                break;
            }
        }

        for (int z = 0; z < 128; z++) {
            BlockPos check = new BlockPos(pos.getX(), yLvl, pos.getZ() + z);
            if (w.getBlockState(check).getBlock() == Blocks.air) {
                d = check.getZ();
                break;
            }
        }
        int wi = r - l;
        int h = d-u;
        String name = wi/32+"x"+h/32;
        if(h > wi){
            name = h/32+"x"+wi/32;
        }
        size = ROOMSIZE.get(name);
        return size;
    }
}
