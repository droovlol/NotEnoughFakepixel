package org.ginafro.notenoughfakepixel.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.features.skyblock.dungeons.terminals.TerminalSimulator;
import org.ginafro.notenoughfakepixel.envcheck.registers.RegisterCommand;

@RegisterCommand
public class TerminalSimulatorCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "termsim";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/termsim";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Config.screenToOpen = new TerminalSimulator();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}