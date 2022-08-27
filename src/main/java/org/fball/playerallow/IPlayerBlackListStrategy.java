package org.fball.playerallow;

import org.fball.Nfl;
import org.fball.Player;

import java.util.ArrayList;

/**
 * PlugIn Method Called By Template in Order to Remove Players From Nfl
 */
public interface IPlayerBlackListStrategy {
    ArrayList<Player> getBlackList(Nfl nfl);
}
