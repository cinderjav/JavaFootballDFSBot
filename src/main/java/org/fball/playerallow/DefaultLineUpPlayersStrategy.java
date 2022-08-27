package org.fball.playerallow;

import org.fball.LineUp;
import org.fball.Nfl;
import org.fball.Player;
import org.fball.PlayerNames;

public class DefaultLineUpPlayersStrategy implements ISetLineUpPlayersStrategy{
    @Override
    public LineUp getFixedLineUp(Nfl nfl) {
        var lineUp = new LineUp();
//        for (Player p : nfl.WR) {
//            if (p.name.equalsIgnoreCase(PlayerNames.STEFON_DIGGS)){
//                lineUp.wrOne = p;
//            }
//        }

        return lineUp;
    }
}
