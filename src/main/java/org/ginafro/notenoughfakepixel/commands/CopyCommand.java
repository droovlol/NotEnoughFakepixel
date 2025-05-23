package org.ginafro.notenoughfakepixel.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import org.ginafro.notenoughfakepixel.config.gui.utils.Utils;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterCommand;

@RegisterCommand
public class CopyCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "copytoclipboard";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/copytoclipboard <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) return;

        String text = String.join(" ", args);
        Utils.copyToClipboard(text);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}

