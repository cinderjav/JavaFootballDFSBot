package org.fball;

import java.util.Comparator;

public class PlayerPointSorter implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        return (int)-Math.ceil(o1.getPlayerPoints() - o2.getPlayerPoints());
    }
}
