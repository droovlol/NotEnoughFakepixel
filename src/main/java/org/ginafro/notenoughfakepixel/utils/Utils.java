package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class Utils {

    private static final char[] c = new char[]{'k', 'm', 'b', 't'};

    public static float map(float x, float inputStart, float inputEnd, float outputStart, float outputEnd) {
        return (x - inputStart) / (inputEnd - inputStart) * (outputEnd - outputStart) + outputStart;
    }

    public static float getScale() {
        int sc = Minecraft.getMinecraft().gameSettings.guiScale;
        if (sc == 0) {
            return 1.0f;
        }
        switch (sc) {
            case 2:
                return 1f / 2.0f;
            case 3:
                return 1f / 3.0f;
            case 4:
                return 1f / 4.0f;
            default:
                return 1.0f;
        }
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

    private String lockedEnchantment = "";

    public String getLockedEnchantment() {
        return lockedEnchantment;
    }

    public void setLockedEnchantment(String lockedEnchantment) {
        this.lockedEnchantment = lockedEnchantment;
    }

    private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-OR]");

    public String stripColor(final String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

}
