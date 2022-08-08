package org.fball;

import org.fball.nflfilter.DefaultNflFilterStrategy;
import org.fball.nflfilter.IFilterNflStrategy;
import org.fball.playernames.IPlayerBlackListStrategy;

import java.util.ArrayList;
import java.util.Collections;

public class Nfl {
    public static IFilterNflStrategy strategy = new DefaultNflFilterStrategy();
    public static IPlayerBlackListStrategy pAllowStrategy;
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
        Collections.sort(flexArray, new PlayerPointSorter());
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
        if (pAllowStrategy != null){
            var bListPlayers = pAllowStrategy.getBlackList(nfl);
            if (bListPlayers.size() > 0) {
                Nfl.removeNflPlayers(bListPlayers, nfl);
            }
        }
        return strategy.filterNfl(nfl);
    }

    public Nfl copyNfl(){
        var newNfl = new Nfl();
        newNfl.QB = new ArrayList<>(this.QB);
        newNfl.RB = new ArrayList<>(this.RB);
        newNfl.WR = new ArrayList<>(this.WR);
        newNfl.TE = new ArrayList<>(this.TE);
        newNfl.DST = new ArrayList<>(this.DST);
        return newNfl;
    }

    private static void removeNflPlayers(ArrayList<Player> bList, Nfl nfl){
        nfl.QB = new ArrayList<>(nfl.QB.stream().filter(p -> !bList.contains(p)).toList());
        nfl.RB = new ArrayList<>(nfl.QB.stream().filter(p -> !bList.contains(p)).toList());
        nfl.WR = new ArrayList<>(nfl.QB.stream().filter(p -> !bList.contains(p)).toList());
        nfl.TE = new ArrayList<>(nfl.QB.stream().filter(p -> !bList.contains(p)).toList());
        nfl.DST = new ArrayList<>(nfl.QB.stream().filter(p -> !bList.contains(p)).toList());
    }

    private String printPosition(ArrayList<Player> players, String pos) {
        StringBuilder returnString = new StringBuilder("%s(%s): ".formatted(pos, players.size()));
        for(Player p : players){
            returnString.append(p);
        }
        return returnString + "\n";
    }
}
