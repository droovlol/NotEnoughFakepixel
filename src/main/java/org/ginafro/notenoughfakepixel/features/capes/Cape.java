package org.ginafro.notenoughfakepixel.features.capes;

import net.minecraft.util.ResourceLocation;

public class Cape {

    public int width, height;
    public int capeID;
    public String capeFile;

    public Cape(int w, int h, int id, String file) {
        width = w;
        height = h;
        capeID = id;
        capeFile = file;
    }

}
