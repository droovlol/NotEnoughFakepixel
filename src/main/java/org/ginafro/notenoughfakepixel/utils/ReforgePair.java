package org.ginafro.notenoughfakepixel.utils;

public class ReforgePair {

    private final float x;
    private final float y;
    private final String displayReforge;


    public ReforgePair(float x, float y, String displayReforge) {
        this.x = x;
        this.y = y;
        this.displayReforge = displayReforge;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getEnchant() {
        return displayReforge;
    }
}
