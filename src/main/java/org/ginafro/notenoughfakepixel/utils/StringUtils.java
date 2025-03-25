package org.ginafro.notenoughfakepixel.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;

public class StringUtils {

    public static int romanToNumerical(String s){
        int total = 0;
        for(int i =0;i < s.length();i++){
            char r = s.charAt(i);
            int s1 = value(r);
            if (i+1 <s.length()) {
                int s2 = value(s.charAt(i+1));
                //comparing the current character from its right character
                if (s1 >= s2) {
                    //if the value of current character is greater or equal to the next symbol
                    total = total + s1;
                }
                else {
                    //if the value of the current character is less than the next symbol
                    total = total - s1;
                }
            }
            else {
                total = total + s1;
            }
        }
        return total;
    }

    private static int value(char r) {
        if (r == 'I')
            return 1;
        if (r == 'V')
            return 5;
        if (r == 'X')
            return 10;
        if (r == 'L')
            return 50;
        if (r == 'C')
            return 100;
        if (r == 'D')
            return 500;
        if (r == 'M')
            return 1000;
        return -1;
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
        BigInteger THOUSAND = BigInteger.valueOf(1000);
        int i = -1;
        while (bigInteger.compareTo(THOUSAND) > 0 && i < sizeSuffix.length) {
            bigInteger = bigInteger.divide(THOUSAND);
            i++;
        }
        return bigInteger.toString() + (i == -1 ? "" : sizeSuffix[i]);
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
        try {
            String[] split = string.split(splitString);
            String rawTier = split[split.length - 1];
            return string.substring(0, string.length() - rawTier.length() - 1);
        } catch (StringIndexOutOfBoundsException e) {
            throw new RuntimeException("removeLastWord: '" + string + "'", e);
        }
    }

    public static String firstUpperLetter(String text) {
        if (text.isEmpty()) return text;
        String firstLetter = ("" + text.charAt(0)).toUpperCase(Locale.ROOT);
        return firstLetter + text.substring(1);
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
}
