package org.ginafro.notenoughfakepixel.variables;

public enum Rarity {

    NONE("None"),
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary"),
    MYTHIC("Mythic"),
    DIVINE("Divine");

    public final String rarity;

    Rarity(String rarity) {
        this.rarity = rarity;
    }

    public static Rarity fromString(String rarity) {
        for (Rarity r : Rarity.values()) {
            if (rarity.toLowerCase().contains(r.rarity.toLowerCase())) {
                return r;
            }
        }
        return NONE;
    }

}
