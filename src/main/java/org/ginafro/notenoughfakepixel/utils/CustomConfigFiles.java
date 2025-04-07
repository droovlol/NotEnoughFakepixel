package org.ginafro.notenoughfakepixel.utils;

import org.ginafro.notenoughfakepixel.NotEnoughFakepixel;

import java.io.File;

public enum CustomConfigFiles {

    FAIRY_SOULS(NotEnoughFakepixel.configDirectory + File.separator + "gainedsouls.json"),
    SLOT_LOCKING(NotEnoughFakepixel.configDirectory + File.separator +  "slotlocking.json");

    public String path;
    CustomConfigFiles(String s){
        path = s;
    }

}
