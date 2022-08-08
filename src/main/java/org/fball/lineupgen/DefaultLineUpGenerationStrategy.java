package org.fball.lineupgen;
import org.fball.LineUp;
import org.fball.Nfl;
import org.fball.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class DefaultLineUpGenerationStrategy implements ILineUpGenerationStrategy {

    private Nfl nfl;
    private ArrayList<LineUp> lineupsResults = new ArrayList<>();
    private int pruneCount = 0;
    private int notValidCount = 0;
    @Override
    public List<LineUp> generateLineUp(Nfl nfl) {
        this.nfl = nfl;
        // Add Fixed Lineup players here
        var activeLineup = new LineUp();
        Hashtable<Integer, Double> spotsToEfficiency = new Hashtable<>();
        Hashtable<Integer, Double> spotsToScore = new Hashtable<>();
        Hashtable<Integer, Integer> spotsToSalary = new Hashtable<>();

        recurse(activeLineup, spotsToEfficiency, spotsToScore, spotsToSalary);
        Collections.sort(lineupsResults);

        return this.lineupsResults.stream().limit(3).toList();
    }

    public void recurse(LineUp lineUp, Hashtable<Integer, Double> effMap, Hashtable<Integer, Double> scoreMap,
                        Hashtable<Integer,Integer> spotToSalary){
        var players = nfl.getPlayersByPosition(lineUp.getNextPosition());
        for (Player p : players){
            lineUp.addPlayer(p);
            var spot = lineUp.getFilledSpots();
            if (spot == 1) {
                System.out.println(p);
            }
            var scoreContainer =
                    getScoreContainer(spot, lineUp, effMap, scoreMap, spotToSalary);

            if (!lineUp.isLineupValid()){
                notValidCount ++;
                lineUp.clearLastPlayer();
                continue;
            }
            if (!scoreContainer.shouldProceed()){
                pruneCount ++;
                lineUp.clearLastPlayer();
                continue;
            }
            if (scoreContainer.EmptyMapScores()) {
                effMap.put(spot, scoreContainer.efficiency);
                scoreMap.put(spot, scoreContainer.score);
                spotToSalary.put(spot, scoreContainer.salary);

                if (lineUp.isLineupValidAndFinal()){
                    lineupsResults.add(lineUp.deepCopy());
                }
            }
            else {
                if (scoreContainer.shouldUpdateCurrentScore()) {
                    scoreMap.put(spot, scoreContainer.score);
                    spotToSalary.put(spot, scoreContainer.salary);

                    if (lineUp.isLineupValidAndFinal()){
                        lineupsResults.add(lineUp.deepCopy());
                    }
                }
                if (scoreContainer.shouldUpdateCurrentEfficiency()) {
                    effMap.put(spot, scoreContainer.efficiency);
                }
            }

            if(spot != LineUp.TOTAL_SPOTS) {
                recurse(lineUp.deepCopy(), effMap, scoreMap, spotToSalary);
            }
            lineUp.clearLastPlayer();
        }
    }

    private ScoreContainer getScoreContainer(int spot, LineUp lineup, Hashtable<Integer, Double> effMap,
                                             Hashtable<Integer, Double> scoreMap, Hashtable<Integer,Integer> salarymap){
        var currentMaxEff = effMap.getOrDefault(spot, -1.0);
        var currentMaxScore = scoreMap.getOrDefault(spot, -1.0);
        var currentSalary = salarymap.getOrDefault(spot, -1);
        return new ScoreContainer(lineup, currentMaxScore, currentMaxEff, currentSalary, spot);
    }

    private class ScoreContainer {
        private double score;
        private double efficiency;
        private double currentMaxScore;
        private double currentMaxEfficiency;
        private int currentSalary;
        private int salary;
        private int spot;
        private ScoreContainer(LineUp lineUp, double currentMaxScore, double currentMaxEfficiency, int currentSalary, int spot){
            this.score = lineUp.getPoints();
            this.efficiency = lineUp.getEfficiency();
            this.salary = lineUp.getSalary();
            this.currentMaxScore = currentMaxScore;
            this.currentMaxEfficiency = currentMaxEfficiency;
            this.currentSalary = currentSalary;
            this.spot = spot;
        }

        private boolean shouldProceed() {
            if (EmptyMapScores()) {
                return true;
            }

            if (shouldUpdateCurrentScore()){
                return true;
            }

            if (lineupsResults.size() > 0){
                var topLineup= lineupsResults.get(lineupsResults.size()-1);
                var effSpot = topLineup.GetEfficiencyForSpot(spot);
                if (Double.compare(effSpot, efficiency) > 0){
                    return false;
                }
            }
            var val = currentMaxScore / currentSalary;
            var topScoreEff = Math.round(val * 100.0) / 100.0;
            if (Double.compare(topScoreEff, efficiency) <= 0){
                return true;
            }
            // Get Highest Score and check at the current spot that this guy is more efficient than top score at that spot

            return false;
        }

        private boolean EmptyMapScores() {
            if (Double.compare(currentMaxEfficiency, -1.0) == 0 || Double.compare(currentMaxScore, -1.0) == 0) {
                return true;
            }
            return false;
        }

        private boolean shouldUpdateCurrentScore(){
            if (currentMaxScore < score) {
                return true;
            }
            return false;
        }
        private boolean shouldUpdateCurrentEfficiency(){
            if (Double.compare(currentMaxEfficiency, efficiency) <= 0) {
                return true;
            }
            return false;
        }
    }
}
