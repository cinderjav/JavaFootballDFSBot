package org.fball.nflfilter;

import org.fball.Nfl;
import org.fball.Player;

import java.util.ArrayList;
import java.util.Hashtable;

public class DefaultNflFilterStrategy implements IFilterNflStrategy {
    @Override
    public Nfl filterNfl(Nfl nfl) {
        defaultNflFilter(nfl);
        System.out.println("Post Filter \n%s".formatted(nfl));
        return nfl;
    }

    private void defaultNflFilter(Nfl nfl){
        // Can add specific players to filter for week here as well
        nfl.QB = filterPlayers(nfl.QB, 1);
        nfl.RB = filterPlayers(nfl.RB, 2);
        nfl.WR = filterPlayers(nfl.WR, 2);
        nfl.TE = filterPlayers(nfl.TE, 1);
        nfl.DST = filterPlayers(nfl.DST, 1);
    }
    private ArrayList<Player> filterPlayers(ArrayList<Player> players, int finalTarget){
        // Should enhance to rely on my p.getPoints and then can correctly filter
        Hashtable<Integer, Integer> salaryCountHash = new Hashtable<>();
        ArrayList<Player> playersPostFilter = new ArrayList<>();
        for (Player p : players) {
            if (salaryCountHash.containsKey(p.salary)) {
                var currentTarget = salaryCountHash.get(p.salary);
                if (currentTarget < finalTarget){
                    salaryCountHash.put(p.salary, currentTarget + 1);
                    playersPostFilter.add(p);
                }
            }else{
                salaryCountHash.put(p.salary, 1);
                playersPostFilter.add(p);
            }
        }

        return playersPostFilter;
    }
}
