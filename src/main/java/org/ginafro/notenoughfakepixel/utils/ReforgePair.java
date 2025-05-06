package org.ginafro.notenoughfakepixel.utils;

public class ReforgePair {

    private float x;
    private float y;
    private String displayReforge;


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
