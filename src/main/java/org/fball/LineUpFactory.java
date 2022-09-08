package org.fball;

import org.fball.lineupgen.DefaultLineUpGenerationStrategy;
import org.fball.lineupgen.ILineUpGenerationStrategy;

import java.util.ArrayList;
import java.util.List;

public class LineUpFactory {

    public static ILineUpGenerationStrategy strategy = new DefaultLineUpGenerationStrategy();
    public static List<LineUp> generateBestLineUp(Nfl nfl){
        nfl = Nfl.filterNfl(nfl);
        nfl.initFlexPlayers();
        System.out.println("Post Filter \n%s".formatted(nfl));
        // Need to ensure this is ahead of nfl filter in order to not filter out fixed guys.
        LineUp initialLineUp = LineUp.getInitialLineUp(nfl);
        initialLineUp.lockCurrentLineupSlots();

        var lineups = strategy.generateLineUp(nfl, initialLineUp);

        for (LineUp lineUp : lineups) {
            if (!lineUp.isLineupValidAndFinal()) {
                System.out.println("Invalid Lineup %s".formatted(lineUp));
                throw new RuntimeException("You have an invalid Lineup Returned From The Generation.");
            }
        }
        return lineups;
    }
}
