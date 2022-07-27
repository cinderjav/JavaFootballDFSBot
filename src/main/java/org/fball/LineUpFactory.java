package org.fball;

import org.fball.lineupgen.DefaultLineUpGenerationStrategy;
import org.fball.lineupgen.ILineUpGenerationStrategy;

import java.util.ArrayList;

public class LineUpFactory {

    public static ILineUpGenerationStrategy strategy = new DefaultLineUpGenerationStrategy();
    public static ArrayList<LineUp> generateBestLineUp(Nfl nfl){
        Nfl.filterNfl(nfl);
        var lineups = strategy.generateLineUp(nfl);
        for (LineUp lineUp : lineups) {
            if (!lineUp.isLineupValid()) {
                System.out.println("Invalid Lineup %s".formatted(lineUp));
                throw new RuntimeException("You have an invalid Lineup Returned From The Generation.");
            }
        }
        return lineups;
    }
}
