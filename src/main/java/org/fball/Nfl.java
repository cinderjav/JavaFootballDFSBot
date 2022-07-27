package org.fball;

import org.fball.nflfilter.DefaultNflFilterStrategy;
import org.fball.nflfilter.IFilterNflStrategy;

import java.util.ArrayList;

public class Nfl {
    public static IFilterNflStrategy strategy = new DefaultNflFilterStrategy();
    public ArrayList<Player> QB;
    public ArrayList<Player> RB;
    public ArrayList<Player> WR;
    public ArrayList<Player> TE;
    public ArrayList<Player> DST;

    @Override
    public String toString(){
        return printPosition(QB, "QB") + printPosition(RB, "RB") + printPosition(WR, "WR") +
                printPosition(TE, "TE") + printPosition(DST, "DST");
    }

    public ArrayList<Player> getFlexPlayers(){
        var flexArray = new ArrayList<Player>();
        flexArray.addAll(RB);
        flexArray.addAll(WR);
        return flexArray;
    }

    public static void filterNfl(Nfl nfl){
        if (strategy == null) {
            throw new RuntimeException("Must Specify Filter Strategy");
        }
        strategy.filterNfl(nfl);
    }

    private String printPosition(ArrayList<Player> players, String pos) {
        StringBuilder returnString = new StringBuilder("%s(%s): ".formatted(pos, players.size()));
        for(Player p : players){
            returnString.append(p);
        }
        return returnString + "\n";
    }
}
