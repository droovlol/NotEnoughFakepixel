package org.ginafro.notenoughfakepixel.features.skyblock.qol;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.ginafro.notenoughfakepixel.Configuration;
import org.ginafro.notenoughfakepixel.utils.ScoreboardUtils;
import org.ginafro.notenoughfakepixel.variables.Gamemode;

public class JacobAuctionTimer {

    public boolean jacob , auction;

    public void onTick(TickEvent.ServerTickEvent e){
        //if(Configuration.jATimer){
            if(ScoreboardUtils.currentGamemode == Gamemode.SKYBLOCK){

           // }
        }
    }

}
