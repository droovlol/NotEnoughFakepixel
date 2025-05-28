package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.secrets;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterCommand;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterEvents;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.DungeonManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@RegisterCommand
@RegisterEvents
public class SaveSecretCommand extends CommandBase {


    public static HashMap<String, ArrayList<Secret>> secrets = new HashMap<>();

    @Override
    public String getCommandName() {
        return "savesecret";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) return;

        String hash = RoomDetection.hash;
        if (!hash.isEmpty() && RoomDetection.blockBelow != null) {
            BlockPos pos = RoomDetection.worldToRelative(new BlockPos(sender.getCommandSenderEntity()));

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW +
                    "[NEF] Block at secret location: " + RoomDetection.blockBelow.getRegistryName()));

            Secret secret = new Secret(pos, SecretType.get(args[0]), RoomDetection.blockBelow);

            ArrayList<Secret> poses = secrets.getOrDefault(hash, new ArrayList<>());
            poses.add(secret);
            secrets.put(hash, poses);

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN +
                    "[NEF] Saved Secret At " + pos.getX() + " | " + pos.getY() + " | " + pos.getZ() +
                    " With Item Type: " + Objects.requireNonNull(SecretType.get(args[0])).name));
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED +
                    "[NEF] Room hash is empty, cannot save secret."));
        }
    }
}
