package org.ginafro.notenoughfakepixel.config.gui.commands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.commons.lang3.StringUtils;
import org.ginafro.notenoughfakepixel.config.gui.Config;
import org.ginafro.notenoughfakepixel.config.gui.config.ConfigEditor;
import org.ginafro.notenoughfakepixel.config.gui.core.GuiScreenElementWrapper;

public class Commands {

    private static final SimpleCommand.ProcessCommandRunnable mainMenu = new SimpleCommand.ProcessCommandRunnable() {
        public void processCommand(ICommandSender sender, String[] args) {
            if (args.length > 0) {
                Config.screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(Config.feature, StringUtils.join(args, " ")));
            } else {
                Config.screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(Config.feature));
            }
        }
    };

    public static void init() {
        ClientCommandHandler.instance.registerCommand(new SimpleCommand("nef", mainMenu));
        ClientCommandHandler.instance.registerCommand(new SimpleCommand("notenoughfakepixel", mainMenu));
    }
}
