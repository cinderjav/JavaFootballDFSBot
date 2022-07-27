package org.fball.lineupgen;

import org.fball.LineUp;
import org.fball.Nfl;

import java.util.ArrayList;

/**
 * PlugIn Method Called By Template in Order to implement your own algorithms for generating lineup
 */
public interface ILineUpGenerationStrategy {
    ArrayList<LineUp> generateLineUp(Nfl nfl);
}
