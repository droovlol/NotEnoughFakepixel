package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.secrets;

public enum SecretType{

    CHEST("Chest"),
    ITEM("Item"),
    ESSENCE("Essence"),
    BAT("Bat"),
    LEVER("Lever"),
    MUSHROOM("Mushroom"),
    ANCHOR(""),
    WALL("Wall"),
    ENTRANCE("Entrance");

    public String name;
    SecretType(String s){
        this.name = s;
    }

    public static SecretType get(String s){
        for(SecretType type : values()){
            if(s.equalsIgnoreCase(type.name)){
                return type;
            };
        }
        return null;
    }

}
