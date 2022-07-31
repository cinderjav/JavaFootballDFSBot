package org.fball.nflfilter;

import org.fball.Nfl;

/**
 * PlugIn Method Called By Template in Order to Filter/Sort/Modify incoming NFL players from API
 */
public interface IFilterNflStrategy {
    Nfl filterNfl(Nfl nfl);
}
