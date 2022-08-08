package org.fball.nflfilter;

import org.fball.Nfl;
import org.fball.Player;
import org.fball.PlayerPointSorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

public class DefaultNflFilterStrategy implements IFilterNflStrategy {
    @Override
    public Nfl filterNfl(Nfl nfl) {
        defaultNflFilter(nfl);
        return nfl;
    }

    private void defaultNflFilter(Nfl nfl){
        // Can add specific players to filter for week here as well
        Collections.sort(nfl.QB, new PlayerPointSorter());
        Collections.sort(nfl.QB);
        Collections.sort(nfl.RB, new PlayerPointSorter());
        Collections.sort(nfl.RB);
        Collections.sort(nfl.WR, new PlayerPointSorter());
        Collections.sort(nfl.WR);
        Collections.sort(nfl.TE, new PlayerPointSorter());
        Collections.sort(nfl.TE);
        Collections.sort(nfl.DST, new PlayerPointSorter());
        Collections.sort(nfl.DST);

        nfl.QB = filterPlayers(nfl.QB, 1);
        nfl.RB = filterPlayers(nfl.RB, 3);
        nfl.WR = filterPlayers(nfl.WR, 3);
        nfl.TE = filterPlayers(nfl.TE, 2);
        nfl.DST = filterPlayers(nfl.DST, 1);
    }
    private ArrayList<Player> filterPlayers(ArrayList<Player> players, int finalTarget){
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
