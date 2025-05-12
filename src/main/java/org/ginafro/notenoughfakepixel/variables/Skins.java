package org.ginafro.notenoughfakepixel.variables;

public enum Skins {

    ENDERMAN_HEAD("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0="),
    TEST("");

    private String s;

    Skins(String value) {
        this.s = value;
    }

    public String getSkin() {
        return this.s;
    }

}
