package org.ginafro.notenoughfakepixel.features.skyblock.dungeons.secrets;

import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class SecretsData {

    public final HashMap<String, ArrayList<Secret>> secrets;

    public SecretsData(HashMap<String, ArrayList<Secret>> secrets){
        this.secrets = secrets;
    }


}
