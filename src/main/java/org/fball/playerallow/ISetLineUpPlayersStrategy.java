package org.fball.playerallow;

import org.fball.LineUp;
import org.fball.Nfl;

/**
 * PlugIn Method Called By Template in Order to Set Fixed Players In Lineups
 */
public interface ISetLineUpPlayersStrategy {
    LineUp getFixedLineUp(Nfl nfl);
}
