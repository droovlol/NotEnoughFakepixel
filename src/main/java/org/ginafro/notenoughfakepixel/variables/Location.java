package org.ginafro.notenoughfakepixel.variables;

public enum Location {

    DWARVEN("sbm-", "sbm_sandbox-"),
    HUB("skyblock-", "skyblock_sandbox-"),
    PRIVATE_HUB("skyblock_private-", "none"),
    DUNGEON_HUB("sbdh-", "sbdh_sandbox-"),
    BARN("sbfarms-", "sbfarms_sandbox-"),
    PARK("sbpark-", "sbpark_sandbox-"),
    GOLD_MINE("sbmines-", "sbmines_sandbox-"),
    PRIVATE_ISLAND("sbi-", "sbi_sandbox-"),
    JERRY("sbj-", "sbj_sandbox-"),
    SPIDERS_DEN("sbspiders-", "sbspiders_sandbox-"),
    THE_END("sbend-", "sbend_sandbox-"),
    CRIMSON_ISLE("sbcris-", "sbcris_sandbox-"),
    DUNGEON("sbdungeon-", "sbdungeon_sandbox-"),
    NONE("", "");

    private String location;
    private String sandbox;

    Location(String location, String sandbox) {
        this.location = location;
        this.sandbox = sandbox;

    }

    public String getLocation() {
        return this.location;
    }

    public String getSandbox() {
        return this.sandbox;
    }

    public static Location getLocation(String s) {
        for (Location l : Location.values()) {
            if (l.getLocation().equals(s) || l.getSandbox().equals(s)) return l;
        }
        return NONE;
    }

    public boolean isDungeon() {
        return this == DUNGEON;
    }

    public boolean isHub() {
        return this == HUB || this == PRIVATE_HUB;
    }

    public boolean isCrimson() {
        return this == CRIMSON_ISLE;
    }

    public boolean isEnd() {
        return this == THE_END;
    }

}
