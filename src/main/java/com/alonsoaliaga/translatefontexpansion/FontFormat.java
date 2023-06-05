package com.alonsoaliaga.translatefontexpansion;

import java.util.HashMap;

public class FontFormat {
    private String identifier;
    private HashMap<Character,Character> replacements;
    private boolean forceUppercase;
    private boolean forceLowercase;
    public FontFormat(String identifier, HashMap<Character, Character> replacements, boolean forceUppercase, boolean forceLowercase) {
        this.identifier = identifier;
        this.replacements = replacements;
        this.forceUppercase = forceUppercase;
        this.forceLowercase = forceLowercase;
    }
    public HashMap<Character, Character> getReplacements() {
        return replacements;
    }
    public String getIdentifier() {
        return identifier;
    }
    public boolean isForceUppercase() {
        return forceUppercase;
    }
    public boolean isForceLowercase() {
        return forceLowercase;
    }
}