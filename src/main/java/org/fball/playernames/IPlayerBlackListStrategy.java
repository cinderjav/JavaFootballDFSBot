package org.fball.playernames;

import org.fball.Nfl;
import org.fball.Player;

import java.util.ArrayList;

/**
 * PlugIn Method Called By Template in Order to Remove/Allocate Players From/To LineUps
 */
public interface IPlayerBlackListStrategy {
    ArrayList<Player> getBlackList(Nfl nfl);
}
