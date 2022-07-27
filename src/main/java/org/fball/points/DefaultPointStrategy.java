package org.fball.points;

import org.fball.Player;

public class DefaultPointStrategy implements ICustomPointStrategy {
    @Override
    public double getPoints(Player p) {
        return p.projectedBaseFpros;
    }
}
