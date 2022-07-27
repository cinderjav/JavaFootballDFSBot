package org.fball.points;

import org.fball.Player;

public class DefaultPointStrategy implements IPointStrategy {
    @Override
    public double getPoints(Player p) {
        return p.projectedBaseFpros;
    }
}
