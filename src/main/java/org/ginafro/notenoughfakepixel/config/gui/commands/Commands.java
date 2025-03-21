package org.ginafro.notenoughfakepixel.config.gui.commands;

import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.ginafro.notenoughfakepixel.config.gui.config.ConfigEditor;
import org.ginafro.notenoughfakepixel.config.gui.core.GuiScreenElementWrapper;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.commons.lang3.StringUtils;

public class Commands {

    private static final SimpleCommand.ProcessCommandRunnable mainMenu = new SimpleCommand.ProcessCommandRunnable() {
        public void processCommand(ICommandSender sender, String[] args) {
            if (args.length > 0) {
                NotEnoughFakepixel.screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(NotEnoughFakepixel.feature, StringUtils.join(args, " ")));
            } else {
                NotEnoughFakepixel.screenToOpen = new GuiScreenElementWrapper(new ConfigEditor(NotEnoughFakepixel.feature));
            }
        }
    };

    public static void init() {
        ClientCommandHandler.instance.registerCommand(new SimpleCommand("nef", mainMenu));
        ClientCommandHandler.instance.registerCommand(new SimpleCommand("notenoughfakepixel", mainMenu));
    }
}
