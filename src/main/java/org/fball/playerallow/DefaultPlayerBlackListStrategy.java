package org.fball.playerallow;

import org.fball.Nfl;
import org.fball.Player;
import org.fball.PlayerNames;

import java.util.ArrayList;

public class DefaultPlayerBlackListStrategy implements IPlayerBlackListStrategy {
    @Override
    public ArrayList<Player> getBlackList(Nfl nfl) {
        var blackL = new ArrayList<Player>();
        //Would be good just to iterate through entire nfl here. Should create a method for that.
//        for (Player p : nfl.WR) {
//            if (p.name.equalsIgnoreCase(PlayerNames.TREYLON_BURKS)){
//                blackL.add(p);
//            }
//        }
        return blackL;
    }
}
