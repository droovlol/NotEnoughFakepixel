package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.config.features.General;
import org.ginafro.notenoughfakepixel.variables.Constants;

public class Logger {

    public static void log(String message) {
        if (!General.debug) return;
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText(Constants.PREFIX + message)
            );
        }
    }

    //log tostring of every class
    public static void log(Object object){
        if (!General.debug) return;
        try {
            log(object.toString());
        } catch (Exception e) {
            logConsole("Failed to log object: " + object.getClass().getName());
        }
    }

    public static void logConsole(String message) {
        if (!General.debug) return;
        System.out.println(Constants.PREFIX + message);
    }

    public static void logConsole(Object object){
        if (!General.debug) return;
        try {
            logConsole(object.toString());
        } catch (Exception e) {
            logConsole("Failed to log object: " + object.getClass().getName());
        }
    }

}
