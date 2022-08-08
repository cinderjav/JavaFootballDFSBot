package org.fball.lineupgen;

import org.fball.LineUp;
import org.fball.Nfl;
import org.fball.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultLineUpGenerationStrategy implements ILineUpGenerationStrategy{
    private ArrayList<LineUp> lineupsResults = new ArrayList<>();
    private Nfl nfl;
    @Override
    public List<LineUp> generateLineUp(Nfl nfl) {
        this.nfl = nfl;
        var lineUp = new LineUp();
        generateNextLineupOptions(lineUp, lineUp);
        lineupsResults.add(lineUp);
        recurse(lineUp);
        Collections.sort(lineupsResults);
        return this.lineupsResults.stream().limit(1).toList();
    }

    public void recurse(LineUp lineUp){
        if(lineUp.getSalary() == 200 || lineUp.complete){
            return;
        }
        var lineupNew = new LineUp();
        generateNextLineupOptions(lineupNew, lineUp);
        var pc = new PlayerUpdateContainer(lineupNew);
        var newLineUp = AnalyzeAndUpdate(lineUp, pc);
        this.lineupsResults.add(newLineUp);
        recurse(newLineUp);
    }

    private void generateNextLineupOptions(LineUp newLineUp, LineUp lineUp){
        newLineUp.qbOne = getNextPlayer(nfl.QB, lineUp.qbOne != null ? lineUp.qbOne.getPlayerPoints() : 0, newLineUp, lineUp);
        newLineUp.flex = getNextPlayer(nfl.FLEX, lineUp.flex != null ? lineUp.flex.getPlayerPoints(): 0, newLineUp, lineUp);;
        newLineUp.rbOne = getNextPlayer(nfl.RB, lineUp.rbOne != null ? lineUp.rbOne.getPlayerPoints(): 0, newLineUp, lineUp);
        newLineUp.rbTwo = getNextPlayer(nfl.RB, lineUp.rbTwo != null ? lineUp.rbTwo.getPlayerPoints(): 0, newLineUp, lineUp);
        newLineUp.wrOne = getNextPlayer(nfl.WR, lineUp.wrOne != null ? lineUp.wrOne.getPlayerPoints(): 0, newLineUp, lineUp);
        newLineUp.wrTwo = getNextPlayer(nfl.WR, lineUp.wrTwo != null ? lineUp.wrTwo.getPlayerPoints(): 0, newLineUp, lineUp);
        newLineUp.wrThree = getNextPlayer(nfl.WR, lineUp.wrThree != null ? lineUp.wrThree.getPlayerPoints(): 0, newLineUp, lineUp);
        newLineUp.teOne = getNextPlayer(nfl.TE, lineUp.teOne != null ? lineUp.teOne.getPlayerPoints(): 0, newLineUp, lineUp);
        newLineUp.dst = getNextPlayer(nfl.DST, lineUp.dst != null ? lineUp.dst.getPlayerPoints(): 0, newLineUp, lineUp);
    }

    public Player getNextPlayer(ArrayList<Player> players, double currentPoints, LineUp lineUp, LineUp lineUpOg){
        for (Player p : players){
            if (p.getPlayerPoints() > currentPoints && !isInLineUp(p, lineUp, lineUpOg)){
                return p;
            }
        }
        return null;
    }

    private boolean isInLineUp(Player p, LineUp lu, LineUp luOriginal){
        if (p == lu.qbOne){
            return true;
        }
        if (p == lu.rbOne){
            return true;
        }

        if (p == lu.rbTwo){
            return true;
        }

        if (p == lu.wrOne){
            return true;
        }

        if (p == lu.wrTwo){
            return true;
        }
        if (p == lu.wrThree){
            return true;
        }
        if (p == lu.flex){
            return true;
        }
        if (p == lu.teOne){
            return true;
        }
        if (p == lu.dst){
            return true;
        }

        if (p == luOriginal.qbOne){
            return true;
        }
        if (p == luOriginal.rbOne){
            return true;
        }

        if (p == luOriginal.rbTwo){
            return true;
        }

        if (p == luOriginal.wrOne){
            return true;
        }

        if (p == luOriginal.wrTwo){
            return true;
        }
        if (p == luOriginal.wrThree){
            return true;
        }
        if (p == luOriginal.flex){
            return true;
        }
        if (p == luOriginal.teOne){
            return true;
        }
        if (p == luOriginal.dst){
            return true;
        }
        return false;
    }

    // Updates Line to Next swap which lowers efficiency the least but increases points slowly up to optimum
    public LineUp AnalyzeAndUpdate(LineUp lineUp, PlayerUpdateContainer pc){
        var newLineUp = lineUp.deepCopy();
        var pUpgradeContainer = new PlayerDiffUpgradeContainer();
        var diffList = getUpgradeDiffScoreList(pc, lineUp, pUpgradeContainer);
        var start = 1;
        do {
            double max = diffList.get(diffList.size() - start);
            newLineUp = lineUp.deepCopy();
            if (Double.compare(max, 0.0) == 0){
                newLineUp.complete = true;
                return newLineUp;
            }
            UpdateLineUp(max, newLineUp, pc, pUpgradeContainer.qbDiffScore, pUpgradeContainer.rbOneDiffScore, pUpgradeContainer.rbTwoDiffScore,
                    pUpgradeContainer.wrOneDiffScore, pUpgradeContainer.wrTwoDiffScore, pUpgradeContainer.wrThreeDiffScore, pUpgradeContainer.teOneDiffScore,
                    pUpgradeContainer.flexDiffScore, pUpgradeContainer.dstDiffScore);
            start += 1;
            if (start > diffList.size()){
                newLineUp.complete = true;
                return newLineUp;
            }
        } while (!newLineUp.isLineupValid());
        return newLineUp;
    }

    private void UpdateLineUp(double test, LineUp newLineUp, PlayerUpdateContainer pc, double qbReplaceScore, double rbOneScore, double rbTwoScore,
                                double wrOneScore, double wrTwoScore, double wrThreeScore, double teScore, double flexScore, double dstScore){
        if (Double.compare(test, qbReplaceScore) == 0){
            newLineUp.qbOne = pc.qb;
            return;
        }
        if (Double.compare(test, rbOneScore) == 0){
            newLineUp.rbOne = pc.rbOne;
            return;
        }
        if (Double.compare(test, rbTwoScore) == 0){
            newLineUp.rbTwo = pc.rbTwo;
            return;
        }
        if (Double.compare(test, wrOneScore) == 0){
            newLineUp.wrOne = pc.wrOne;
            return;
        }
        if (Double.compare(test, wrTwoScore) == 0){
            newLineUp.wrTwo = pc.wrTwo;
            return;
        }
        if (Double.compare(test, wrThreeScore) == 0){
            newLineUp.wrThree = pc.wrThree;
            return;
        }
        if (Double.compare(test, teScore) == 0){
            newLineUp.teOne = pc.teOne;
            return;
        }
        if (Double.compare(test, flexScore) == 0){
            newLineUp.flex = pc.flex;
            return;
        }
        if (Double.compare(test, dstScore) == 0){
            newLineUp.dst = pc.dst;
            return;
        }
    }
    private double PlayerCompare(Player current, Player nextPlayer){
        var pointJump = nextPlayer.getPlayerPoints() - current.getPlayerPoints();
        var salaryDiff = nextPlayer.salary - current.salary;
        if (pointJump > 0 && salaryDiff < 0){
            return pointJump * Math.abs(salaryDiff);
        }
        return pointJump/salaryDiff;
    }

    private List<Double> getUpgradeDiffScoreList(PlayerUpdateContainer pc, LineUp lineUp, PlayerDiffUpgradeContainer playerDiffUpgradeContainer){
        var qbReplaceScore = pc.qb != null ? PlayerCompare(lineUp.qbOne, pc.qb) : 0.0;
        var rbOneScore =  pc.rbOne != null ? PlayerCompare(lineUp.rbOne, pc.rbOne): 0.0;
        var rbTwoScore = pc.rbTwo != null ? PlayerCompare(lineUp.rbTwo, pc.rbTwo): 0.0;
        var wrOneScore = pc.wrOne != null ? PlayerCompare(lineUp.wrOne, pc.wrOne): 0.0;
        var wrTwoScore = pc.wrTwo != null ? PlayerCompare(lineUp.wrTwo, pc.wrTwo): 0.0;
        var wrThreeScore = pc.wrThree != null ? PlayerCompare(lineUp.wrThree, pc.wrThree): 0.0;
        var flexScore = pc.flex != null ? PlayerCompare(lineUp.flex, pc.flex): 0.0;
        var teScore = pc.teOne != null ? PlayerCompare(lineUp.teOne, pc.teOne): 0.0;
        var dstScore = pc.dst != null ? PlayerCompare(lineUp.dst, pc.dst): 0.0;
        var diffList = Arrays.asList(qbReplaceScore, rbOneScore, rbTwoScore, wrOneScore, wrTwoScore, wrThreeScore, flexScore, teScore, dstScore);
        playerDiffUpgradeContainer.initDiff(diffList);
        Collections.sort(diffList);
        return diffList;
    }

    private class PlayerUpdateContainer {
        private final Player qb;
        private final Player rbOne;
        private final Player rbTwo;
        private final Player wrOne;
        private final Player wrTwo;
        private final Player wrThree;
        private final Player teOne;
        private final Player flex;
        private final Player dst;

        private PlayerUpdateContainer(LineUp lineUp){
            this.qb = lineUp.qbOne;
            this.rbOne = lineUp.rbOne;
            this.rbTwo = lineUp.rbTwo;
            this.wrOne = lineUp.wrOne;
            this.wrTwo = lineUp.wrTwo;
            this.wrThree = lineUp.wrThree;
            this.teOne = lineUp.teOne;
            this.flex = lineUp.flex;
            this.dst = lineUp.dst;
        }
    }
    private class PlayerDiffUpgradeContainer {
        private double qbDiffScore;
        private double rbOneDiffScore;
        private double rbTwoDiffScore;
        private double wrOneDiffScore;
        private double wrTwoDiffScore;
        private double wrThreeDiffScore;
        private double teOneDiffScore;
        private double flexDiffScore;
        private double dstDiffScore;

        private PlayerDiffUpgradeContainer(){
        }

        private void initDiff(List<Double> diffList){
            this.qbDiffScore = diffList.get(0);
            this.rbOneDiffScore = diffList.get(1);
            this.rbTwoDiffScore = diffList.get(2);
            this.wrOneDiffScore = diffList.get(3);
            this.wrTwoDiffScore = diffList.get(4);
            this.wrThreeDiffScore = diffList.get(5);
            this.flexDiffScore = diffList.get(6);
            this.teOneDiffScore = diffList.get(7);
            this.dstDiffScore = diffList.get(8);
        }
    }
}
