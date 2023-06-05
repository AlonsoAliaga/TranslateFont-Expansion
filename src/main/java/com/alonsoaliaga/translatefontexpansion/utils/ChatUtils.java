package com.alonsoaliaga.translatefontexpansion.utils;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    private static final Pattern PARSED_COLORS_PATTERN = Pattern.compile("§[a-f0-9klmnor]",Pattern.CASE_INSENSITIVE);
    private static final Pattern HEX_FORMAT_TO_PARSE = Pattern.compile("(&?#[a-f0-9]{6})",Pattern.CASE_INSENSITIVE);
    private static final Pattern PARSED_HEX_PATTERN = Pattern.compile("§x(§[a-f0-9]){6}",Pattern.CASE_INSENSITIVE);
    public static String parseAllFormatting(@Nonnull String string) {
        Matcher matcher = HEX_FORMAT_TO_PARSE.matcher(string);
        while(matcher.find()) {
            String match = matcher.group(0);
            string = string.replace(match,hexToParsedHex(match));
        }
        return ChatColor.translateAlternateColorCodes('&',string);
    }
    private static String hexToParsedHex(String hex) {
        if(hex.length() >= 7) {
            StringBuilder result = new StringBuilder("§x");
            hex = hex.substring(hex.length() - 6);
            for (char c : hex.toCharArray()) {
                result.append("§").append(c);
            }
            return result.toString();
        }
        return hex;
    }
    public static String revertFormatting(@Nonnull String string) {
        return revertParsedColors(revertParsedHex(string));
    }
    public static String revertFormatting(@Nonnull String string, String hexFormat) {
        return revertParsedColors(revertParsedHex(string,hexFormat));
    }
    public static String revertParsedColors(@Nonnull String string) {
        Matcher matcher = PARSED_COLORS_PATTERN.matcher(string);
        while(matcher.find()) {
            String match = matcher.group(0);
            string = string.replace(match,match.replace("§","&"));
        }
        return string;
    }
    public static String revertParsedHex(@Nonnull String string, String hexFormat) {
        if(hexFormat == null) return revertParsedHex(string);
        Matcher matcher = PARSED_HEX_PATTERN.matcher(string);
        while(matcher.find()) {
            String match = matcher.group(0);
            string = string.replace(match,hexFormat.replace("<hex-color>",match.substring(2).replace("§","")));
        }
        return string;
    }
    public static String revertParsedHex(@Nonnull String string) {
        Matcher matcher = PARSED_HEX_PATTERN.matcher(string);
        while(matcher.find()) {
            String match = matcher.group(0);
            string = string.replace(match,"&#"+match.substring(2).replace("§",""));
        }
        return string;
    }
}