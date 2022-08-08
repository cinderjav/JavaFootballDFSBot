package org.fball;

import org.fball.lineupgen.DefaultLineUpGenerationStrategy;
import org.fball.lineupgen.ILineUpGenerationStrategy;

import java.util.List;

public class LineUpFactory {

    public static ILineUpGenerationStrategy strategy = new DefaultLineUpGenerationStrategy();
    public static List<LineUp> generateBestLineUp(Nfl nfl){
        nfl = Nfl.filterNfl(nfl);
        nfl.initFlexPlayers();
        System.out.println("Post Filter \n%s".formatted(nfl));

        var lineups = strategy.generateLineUp(nfl);

        for (LineUp lineUp : lineups) {
            if (!lineUp.isLineupValidAndFinal()) {
                System.out.println("Invalid Lineup %s".formatted(lineUp));
                throw new RuntimeException("You have an invalid Lineup Returned From The Generation.");
            }
        }
        return lineups;
    }
}
