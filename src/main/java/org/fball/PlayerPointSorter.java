package org.fball;

import java.util.Comparator;

public class PlayerPointSorter implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        //System.out.println("Comparing " + o1 + " And " + o2 + "Equals" + (int)-Math.ceil(o1.getPlayerPoints() - o2.getPlayerPoints()));
        var result = o1.getPlayerPoints() - o2.getPlayerPoints();
        if (Double.compare(result, 0) < 0) {
            return 1;
        }
        if (Double.compare(result, 0) > 0) {
            return -1;
        }
        return 0;
    }
}
