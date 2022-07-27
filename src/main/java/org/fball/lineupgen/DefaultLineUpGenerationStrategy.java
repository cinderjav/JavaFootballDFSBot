package org.fball.lineupgen;
import org.fball.LineUp;
import org.fball.Nfl;

import java.util.ArrayList;

public class DefaultLineUpGenerationStrategy implements ILineUpGenerationStrategy {
    @Override
    public ArrayList<LineUp> generateLineUp(Nfl nfl) {
        // Add Fixed Lineup players here
        ArrayList<LineUp> lineups = new ArrayList<LineUp>() {
            { add(new LineUp()); }
        };
        return lineups;
    }
}
