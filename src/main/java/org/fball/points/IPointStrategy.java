package org.fball.points;

import org.fball.Player;

/**
 * PlugIn Method Called when retrieving player points to customize their scores.
 */
public interface IPointStrategy {
    double getPoints(Player p);
}
