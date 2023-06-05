package com.alonsoaliaga.translatefontexpansion;

import com.alonsoaliaga.translatefontexpansion.utils.ChatUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TranslateFontExpansion extends PlaceholderExpansion implements Configurable, Cacheable {
    private final HashMap<String,FontFormat> formats = new HashMap<>();
    private final String hexFormat;
    List<Character> allowedColorCodes = new ArrayList<>();
    public TranslateFontExpansion() {
        boolean debug = false;
        try {
            debug = getPlaceholderAPI().getPlaceholderAPIConfig().isDebugMode();
        }catch (Throwable ignored){}
        hexFormat = getString("default-hex-format","&#<hex-color>");
        for (char c : "abcdefABCDEF0123456789klmnorx".toCharArray()) allowedColorCodes.add(c);
        ConfigurationSection formatsSection = getConfigSection("formats");
        if(formatsSection != null) {
            for (String formatIdentifier : formatsSection.getKeys(false)) {
                if(formatIdentifier.contains("_")) {
                    Bukkit.getConsoleSender().sendMessage("[TranslateFont-Expansion] Format identifier '"+formatIdentifier+"' cannot contain underscores. Skipping..");
                    continue;
                }
                ConfigurationSection formatSection = formatsSection.getConfigurationSection(formatIdentifier);
                if(formatSection != null) {
                    String toSearch = formatSection.getString("to-search", null);
                    String toReplace = formatSection.getString("to-replace", null);
                    if(toSearch == null) {
                        Bukkit.getConsoleSender().sendMessage("[TranslateFont-Expansion] 'to-search' is missing in '"+formatIdentifier+"' format. Skipping..");
                        continue;
                    }
                    if(toReplace == null) {
                        Bukkit.getConsoleSender().sendMessage("[TranslateFont-Expansion] 'to-replace' is missing in '"+formatIdentifier+"' format. Skipping..");
                        continue;
                    }
                    if(toSearch.length() != toReplace.length()) {
                        Bukkit.getConsoleSender().sendMessage("[TranslateFont-Expansion] 'to-search' and 'to-replace' in '"+formatIdentifier+"' format must be the same length. Skipping..");
                        continue;
                    }
                    HashMap<Character,Character> replacements = new HashMap<>();
                    for (int i = 0; i < toSearch.length(); i++) {
                        replacements.put(toSearch.charAt(i),toReplace.charAt(i));
                    }
                    FontFormat fontFormat = new FontFormat(formatIdentifier,replacements,
                            formatSection.getBoolean("options.force-uppercase",false),
                            formatSection.getBoolean("options.force-lowercase",false)
                    );
                    formats.put(formatIdentifier,fontFormat);
                }
            }
        }
    }
    @Override
    public void clear() {
        formats.clear();
        allowedColorCodes.clear();
    }
    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (params.equalsIgnoreCase("version")) {
            return getVersion();
        }
        if (params.equalsIgnoreCase("author")) {
            return getAuthor();
        }
        // %translatefont_to_small-caps_message%
        if (params.startsWith("to_")) {
            String[] parts = params.substring(3).split("_",2);
            if(parts.length >= 2) {
                String identifier = parts[0].replace("<percent>","%");
                if(identifier.contains("%")) identifier = PlaceholderAPI.setPlaceholders(p,identifier);
                if(identifier.contains("{")) identifier = PlaceholderAPI.setBracketPlaceholders(p,identifier);
                if(formats.containsKey(identifier)) {
                    FontFormat fontFormat = formats.get(identifier);
                    String message = ChatUtils.parseAllFormatting(
                            PlaceholderAPI.setBracketPlaceholders(p,
                                    PlaceholderAPI.setPlaceholders(p,parts[1].replace("<percent>","%"))));
                    if(fontFormat.isForceUppercase()) message = message.toUpperCase(Locale.ROOT);
                    else if(fontFormat.isForceLowercase()) message = message.toLowerCase(Locale.ROOT);
                    return replaceFont(message,fontFormat);
                }
            }
            return null;
        }
        // %translatefont_tounparsed_small-caps_message%
        if (params.startsWith("tounparsed_")) {
            String[] parts = params.substring(3).split("_",2);
            if(parts.length >= 2) {
                String identifier = parts[0].replace("<percent>","%");
                if(identifier.contains("%")) identifier = PlaceholderAPI.setPlaceholders(p,identifier);
                if(identifier.contains("{")) identifier = PlaceholderAPI.setBracketPlaceholders(p,identifier);
                if(formats.containsKey(identifier)) {
                    FontFormat fontFormat = formats.get(identifier);
                    String message = ChatUtils.parseAllFormatting(
                            PlaceholderAPI.setBracketPlaceholders(p,
                                    PlaceholderAPI.setPlaceholders(p,parts[1].replace("<percent>","%"))));
                    if(fontFormat.isForceUppercase()) message = message.toUpperCase(Locale.ROOT);
                    else if(fontFormat.isForceLowercase()) message = message.toLowerCase(Locale.ROOT);
                    return ChatUtils.revertFormatting(replaceFont(message,fontFormat),hexFormat);
                }
            }
            return null;
        }
        return null;
    }
    private String replaceFont(String message, FontFormat fontFormat) {
        if(fontFormat.isForceLowercase()) message = message.toLowerCase(Locale.ROOT);
        else if(fontFormat.isForceUppercase()) message = message.toUpperCase(Locale.ROOT);
        StringBuilder builder = new StringBuilder();
        boolean ignoreNext = false;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if(ignoreNext && allowedColorCodes.contains(c)) {
                builder.append(c);
                ignoreNext = false;
                continue;
            }else ignoreNext = false;
            if(c == '&' || c == '§') {
                builder.append(c);
                ignoreNext = true;
                continue;
            }
            builder.append(fontFormat.getReplacements().getOrDefault(c,c));
        }
        return builder.toString();
    }
    @Override
    public Map<String, Object> getDefaults() {
        final Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("default-hex-format","&#<hex-color>");
        defaults.put("formats.accent.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.accent.to-replace","ĀBÇÐÊFǴĦÎĴĶĿMŇήÖPQŘŞŢŬVŴXŸƵābčďéfǥĥɨĵķłmņŇǒpqřşŧùvŵxŷž⁰¹²³⁴⁵⁶⁷⁸⁹");
        defaults.put("formats.big-caps.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.big-caps.to-replace","ᗩᗷᑕᗪEᖴGᕼIᒍKᒪᗰᑎÑOᑭᑫᖇᔕTᑌᐯᗯ᙭YᘔᗩᗷᑕᗪEᖴGᕼIᒍKᒪᗰᑎñOᑭᑫᖇᔕTᑌᐯᗯ᙭Yᘔ0123456789");
        defaults.put("formats.bubble.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.bubble.to-replace","ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ⓪①②③④⑤⑥⑦⑧⑨");
        defaults.put("formats.currency.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.currency.to-replace","₳฿₵ĐɆ₣₲ⱧłJ₭Ⱡ₥₦ÑØ₱QⱤ₴₮ɄV₩ӾɎⱫ₳฿₵ĐɆ₣₲ⱧłJ₭Ⱡ₥₦ñØ₱QⱤ₴₮ɄV₩ӾɎⱫ0123456789");
        defaults.put("formats.elegant.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.elegant.to-replace","ąɓƈđε∱ɠɧïʆҡℓɱŋñσþҩŗşŧų√щхγẕąɓƈđε∱ɠɧïʆҡℓɱŋñσþҩŗşŧų√щхγẕ0123456789");
        defaults.put("formats.greek.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.greek.to-replace","αႦƈԃҽϝɠԋιʝƙʅɱɳñσρϙɾʂƚυʋɯxყȥαႦƈԃҽϝɠԋιʝƙʅɱɳñσρϙɾʂƚυʋɯxყȥ0123456789");
        defaults.put("formats.krypto.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.krypto.to-replace","คც८ძ૯Բ૭ҺɿʆқՆɱՈÑ૦ƿҩՐς੮υ౮ω૪עઽคც८ძ૯Բ૭ҺɿʆқՆɱՈՈ૦ƿҩՐς੮υ౮ω૪עઽ0123456789");
        defaults.put("formats.parenthesis.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.parenthesis.to-replace","⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵⒪⑴⑵⑶⑷⑸⑹⑺⑻⑼");
        defaults.put("formats.small-caps.options.force-lowercase",true);
        defaults.put("formats.small-caps.to-search","abcdefghijklmnñopqrstuvwxyzqæƀðʒǝɠɨłꟽɯœɔȣꝵʉγλπρψ0123456789-+");
        defaults.put("formats.small-caps.to-replace","ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴñᴏᴘǫʀsᴛᴜᴠᴡxʏᴢǫᴁᴃᴆᴣⱻʛᵻᴌꟺꟺɶᴐᴕꝶᵾᴦᴧᴨᴩᴪ₀₁₂₃₄₅₆₇₈₉₋₊");
        defaults.put("formats.spaced.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.spaced.to-replace","ＡＢＣＤＥＦＧＨＩＪＫＬＭＮÑＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎñｏｐｑｒｓｔｕｖｗｘｙｚ０１２３４５６７８９");
        defaults.put("formats.superscript.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.superscript.to-replace","ᴬᴮᶜᴰᴱᶠᴳᴴᴵᴶᴷᴸᴹᴺÑᴼᴾᵠᴿˢᵀᵁⱽᵂˣʸᶻᵃᵇᶜᵈᵉᶠᵍʰᶦʲᵏˡᵐⁿñᵒᵖᵠʳˢᵗᵘᵛʷˣʸᶻ⁰¹²³⁴⁵⁶⁷⁸⁹");
        defaults.put("formats.weird.to-search","ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789");
        defaults.put("formats.weird.to-replace","ǟɮƈɖɛʄɢɦɨʝӄʟʍռñօքզʀֆȶʊʋաӼʏʐǟɮƈɖɛʄɢɦɨʝӄʟʍռñօքզʀֆȶʊʋաӼʏʐ0123456789");
        return defaults;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "translatefont";
    }
    @Override
    public @NotNull String getAuthor() {
        return "AlonsoAliaga";
    }
    @Override
    public @NotNull String getVersion() {
        return "0.1-BETA";
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public boolean canRegister() {
        return true;
    }
}