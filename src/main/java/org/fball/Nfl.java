package org.fball;

import org.fball.nflfilter.DefaultNflFilterStrategy;
import org.fball.nflfilter.IFilterNflStrategy;

import java.util.ArrayList;
import java.util.Collections;

public class Nfl {
    public static IFilterNflStrategy strategy = new DefaultNflFilterStrategy();
    public ArrayList<Player> QB;
    public ArrayList<Player> RB;
    public ArrayList<Player> WR;
    public ArrayList<Player> TE;
    public ArrayList<Player> DST;
    public ArrayList<Player> FLEX;

    @Override
    public String toString(){
        return printPosition(QB, Position.QB.name()) + printPosition(RB, Position.RB.name()) + printPosition(WR, Position.WR.name()) +
                printPosition(TE, Position.TE.name()) + printPosition(FLEX, Position.FLEX.name()) + printPosition(DST, Position.DST.name());
    }

    public ArrayList<Player> getFlexPlayers(){
        var flexArray = new ArrayList<Player>();
        flexArray.addAll(RB);
        flexArray.addAll(WR);
        flexArray.addAll(TE);
        Collections.sort(flexArray);
        return flexArray;
    }

    public void initFlexPlayers(){
        this.FLEX = getFlexPlayers();
    }

    public static Nfl filterNfl(Nfl nfl){
        if (strategy == null) {
            throw new RuntimeException("Must Specify Filter Strategy");
        }
        return strategy.filterNfl(nfl);
    }

    public ArrayList<Player> copy(ArrayList<Player> players) {
        return new ArrayList<>(players);
    }

    public ArrayList<Player> getPlayersByPosition(Position pos){
        var players = new ArrayList<Player>();
        switch (pos){
            case QB -> players = this.QB;
            case RB -> players = this.RB;
            case WR -> players = this.WR;
            case TE -> players = this.TE;
            case DST -> players = this.DST;
            case FLEX -> players = this.FLEX;
        }
        //return new NflPosition(players);
        return players;
    }

    private String printPosition(ArrayList<Player> players, String pos) {
        StringBuilder returnString = new StringBuilder("%s(%s): ".formatted(pos, players.size()));
        for(Player p : players){
            returnString.append(p);
        }
        return returnString + "\n";
    }
}