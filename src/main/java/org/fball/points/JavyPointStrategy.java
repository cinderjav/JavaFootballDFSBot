package org.fball.points;

import org.fball.Player;

import java.util.ArrayList;
import java.util.List;

public class JavyPointStrategy implements IPointStrategy {
    @Override
    public double getPoints(Player p) {
        var pointHistory = p.pointsHistory;
        var recentGames = pointHistory.stream().limit(3).toList();
        var stats = recentGames.stream().mapToDouble(x -> x).summaryStatistics();
        var recentScoring = p.projectedBaseFpros;
        if (stats.getCount() == 1){
            recentScoring = (recentGames.get(0) + p.projectedBaseFpros)/2;
        }else{
            recentScoring = stats.getAverage();
        }
        var std = 0.0;
        if (stats.getCount() > 1){
            std = getSTD(recentGames, stats.getAverage(), (int)stats.getCount());
        }

        if (stats.getCount() > 1){
            return ((recentScoring- (std/3)) * .25) + (p.projectedBaseFpros * .75);
        }

        return recentScoring;
    }

    private double getSTD(List<Double> games, double average, int count){
        var sumsq = 0.0;
        for(double num: games) {
            sumsq += Math.pow(num - average, 2);
        }
        return Math.sqrt(sumsq/count);
    }
}
