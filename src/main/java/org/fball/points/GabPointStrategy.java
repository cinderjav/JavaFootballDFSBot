package org.fball.points;

import org.fball.Player;

public class GabPointStrategy implements IPointStrategy{
    @Override
    public double getPoints(Player p) {
        var pointHistory = p.pointsHistory;
        var lastSixGames = pointHistory.stream().limit(6).toList();
        var stats = lastSixGames.stream().mapToDouble(x -> x).summaryStatistics();
        var average = stats.getAverage();
        var sumsq = 0.0;
        for(double num: lastSixGames) {
            sumsq += Math.pow(num - average, 2);
        }
        var std =  Math.sqrt(sumsq/stats.getCount());

        if (stats.getCount() < 6) {
            return p.projectedBaseFpros;
        }
        return (p.projectedBaseFpros) * (average/std);
    }
}
