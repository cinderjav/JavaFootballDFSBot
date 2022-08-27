package org.fball.lineupgen;

import org.fball.LineUp;
import org.fball.Nfl;

import java.util.List;

/**
 * PlugIn Method Called By Template in Order to implement your own algorithms for generating lineup
 */
public interface ILineUpGenerationStrategy {
    List<LineUp> generateLineUp(Nfl nfl, LineUp initialLineUp);
}
