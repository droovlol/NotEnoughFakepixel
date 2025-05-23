package org.ginafro.notenoughfakepixel.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;

public class StringUtils {

    public static int romanToNumerical(String s) {
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            int current = value(s.charAt(i));
            int next = (i + 1 < s.length()) ? value(s.charAt(i + 1)) : 0;
            total += (current >= next) ? current : -current;
        }
        return total;
    }

    private static int value(char r) {
        switch (r) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return -1;
        }
    }

    private final static DecimalFormat TENTHS_DECIMAL_FORMAT = new DecimalFormat("#.#");
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.US);

    public static String cleanColourNotModifiers(String in) {
        return in.replaceAll("(?i)\\u00A7[0-9a-f]", "\u00A7r");
    }

    public static String substringBetween(String str, String open, String close) {
        return org.apache.commons.lang3.StringUtils.substringBetween(str, open, close);
    }

    public static int cleanAndParseInt(String str) {
        str = cleanColor(str);
        str = str.replace(",", "");
        return Integer.parseInt(str);
    }

    public static String shortNumberFormat(int n) {
        return shortNumberFormat(n, 0);
    }

    public static String shortNumberFormat(double n) {
        return shortNumberFormat(n, 0);
    }

    private static final char[] sizeSuffix = new char[]{'k', 'm', 'b', 't'};

    public static String shortNumberFormat(BigInteger bigInteger) {
        BigInteger thousand = BigInteger.valueOf(1000);
        int index = -1;
        while (bigInteger.compareTo(thousand) > 0 && index < sizeSuffix.length) {
            bigInteger = bigInteger.divide(thousand);
            index++;
        }
        return bigInteger + (index == -1 ? "" : String.valueOf(sizeSuffix[index]));
    }

    public static String cleanColor(String in) {
        return in.replaceAll("(?i)\\u00A7.", "");
    }

    public static String shortNumberFormat(double n, int iteration) {
        if (n < 0) return "-" + shortNumberFormat(-n, iteration);
        if (n < 1000) {
            if (n % 1 == 0) {
                return Integer.toString((int) n);
            } else {
                return String.format("%.2f", n);
            }
        }

        double d = ((double) (long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;
        return d < 1000
                ? (isRound || d > 9.99 ? (int) d * 10 / 10 : d + "") + "" + sizeSuffix[iteration]
                : shortNumberFormat(d, iteration + 1);
    }

    public static String removeLastWord(String string, String splitString) {
        String[] split = string.split(splitString);
        if (split.length == 0) return string;
        String rawTier = split[split.length - 1];
        return string.substring(0, string.length() - rawTier.length() - 1);
    }

    public static String firstUpperLetter(String text) {
        if (text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase(Locale.ROOT) + text.substring(1);
    }

    public static boolean isNumeric(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }

        for (char c : string.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static String formatToTenths(Number num) {
        return TENTHS_DECIMAL_FORMAT.format(num);
    }

    public static String formatNumber(Number num) {
        return NUMBER_FORMAT.format(num);
    }

    public static <T> Map<String, T> subMapWithKeysThatAreSuffixes(String prefix, NavigableMap<String, T> map) {
        if ("".equals(prefix)) return map;
        String lastKey = createLexicographicallyNextStringOfTheSameLength(prefix);
        return map.subMap(prefix, true, lastKey, false);
    }

    public static String createLexicographicallyNextStringOfTheSameLength(String input) {
        int lastCharPosition = input.length() - 1;
        String inputWithoutLastChar = input.substring(0, lastCharPosition);
        char lastChar = input.charAt(lastCharPosition);
        char incrementedLastChar = (char) (lastChar + 1);
        return inputWithoutLastChar + incrementedLastChar;
    }

    public static boolean containsSubstring(String[] keywords, String itemName) {
        for (String keyword : keywords) {
            if (itemName.contains(keyword)) {
                return true; // Found a match
            }
        }
        return false; // No match found
    }
}
