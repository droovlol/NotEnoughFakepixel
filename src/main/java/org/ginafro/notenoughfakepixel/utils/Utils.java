package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static net.minecraft.client.gui.Gui.*;

public class Utils {

    private static final char[] c = new char[]{'k', 'm', 'b', 't'};

    public static float map(float x, float inputStart, float inputEnd, float outputStart, float outputEnd) {
        return (x - inputStart) / (inputEnd - inputStart) * (outputEnd - outputStart) + outputStart;
    }

    public static String shortNumberFormat(double n, int iteration) {
        // This function will convert a number into a short number format.
        // For example, 1231 -> 1.2k, 1233000 -> 1.2m, 1323000000 -> 1.3b, 1000000000000 -> 1t

        double d = ((double) (long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;
        //this determines the class, i.e. 'k', 'm' etc
        //this decides whether to trim the decimals
        // (int) d * 10 / 10 drops the decimal
        return d < 1000 ? //this determines the class, i.e. 'k', 'm' etc
                (isRound || d > 9.99 ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "") // (int) d * 10 / 10 drops the decimal
                        + "" + c[iteration] : shortNumberFormat(d, iteration + 1);

    }



    public static @NotNull String commaFormat(double n) {
        // This function will only apply commas to a number.
        return String.format("%,d", (long) n);
    }

    public static String removeFormatting(String input) {
        return input.replaceAll("[§|&][0-9a-fk-or]", "");
    }


}
