package org.fball.playernames;

import org.fball.Nfl;
import org.fball.Player;

import java.util.ArrayList;

public class DefaultPlayerBlackListStrategy implements IPlayerBlackListStrategy {
    @Override
    public ArrayList<Player> getBlackList(Nfl nfl) {
        var blackL = new ArrayList<Player>();
        for (Player p : nfl.QB) {
            if (p.name.equalsIgnoreCase(PlayerNames.JOSH_ALLEN)){
                blackL.add(p);
            }
        }
        return blackL;
    }
}
