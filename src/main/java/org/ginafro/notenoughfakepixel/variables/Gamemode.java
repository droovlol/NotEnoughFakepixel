package org.ginafro.notenoughfakepixel.variables;

public enum Gamemode {

    LOBBY("FAKEPIXEL"),
    SKYWARS("SKYWARS"),
    BEDWARS("BED WARS"),
    SKYBLOCK("SKYBLOCK"),
    MLF("MY LITTLE FARM"),
    MURDERMYSTERY("MURDER MYSTERY"),
    DUELS("DUELS"),
    CATACOMBS("CATACOMB");

    private String s;

    Gamemode(String s) {
        this.s = s;
    }

    public String getScoreboardMessage() {
        return s;
    }

    public static Gamemode getGamemode(String s) {
        for (Gamemode gm : Gamemode.values()) {
            if (s.contains(gm.s)) {
                return gm;
            }
        }
        return LOBBY;
    }

    public boolean isSkyblock() {

        return this.equals(SKYBLOCK);
    }

}
