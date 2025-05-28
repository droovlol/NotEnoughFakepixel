package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.secrets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterCommand;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RegisterCommand
public class StoreSecretsCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "storesecrets";
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
        saveSecrets(sender,SaveSecretCommand.secrets);
    }

    private void saveSecrets(ICommandSender sender, HashMap<String, ArrayList<Secret>> secrets) {
        if (secrets.isEmpty()) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[NEF] No secrets to store!"));
            return;
        }

        SecretsData data = new SecretsData(secrets);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            File file = new File(Config.configDirectory, "secrets.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(data));
            writer.flush();
            writer.close();
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "[NEF] Secrets stored successfully!"));
            RenderSecrets.loadSecrets();
        } catch (IOException e) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[NEF] Failed to store data"));
            e.printStackTrace();
        }
    }
}
