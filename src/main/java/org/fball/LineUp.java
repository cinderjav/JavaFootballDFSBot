package org.fball;

import org.fball.playerallow.ISetLineUpPlayersStrategy;

import java.util.Arrays;
import java.util.List;

public class LineUp implements Comparable<LineUp>{
    public static final int MAX_SALARY = 200;
    public static final int TOTAL_SPOTS = 9;
    public static ISetLineUpPlayersStrategy strategy;
    public Player qbOne;
    public Player rbOne;
    public Player rbTwo;
    public Player wrOne;
    public Player wrTwo;
    public Player wrThree;
    public Player teOne;
    public Player flex;
    public Player dst;
    public boolean complete = false;

    private boolean qbOneLocked = false;
    private boolean rbOneLocked = false;
    private boolean rbTwoLocked = false;
    private boolean wrOneLocked = false;
    private boolean wrTwoLocked = false;
    private boolean wrThreeLocked = false;
    private boolean teOneLocked = false;
    private boolean flexLocked = false;
    private boolean dstLocked = false;

    public double getPoints(){
        return getSafePoints(this.qbOne) +
                getSafePoints(this.rbOne) + getSafePoints(this.rbTwo) +
                getSafePoints(this.wrOne) + getSafePoints(this.wrTwo) + getSafePoints(this.wrThree) +
                getSafePoints(this.teOne) +
                getSafePoints(this.flex) +
                getSafePoints(this.dst);
    }

    public void lockCurrentLineupSlots(){
        if (this.qbOne != null){
            qbOneLocked = true;
        }
        if (this.rbOne != null){
            rbOneLocked = true;
        }
        if (this.rbTwo != null){
            rbTwoLocked = true;
        }
        if (this.wrOne != null){
            wrOneLocked = true;
        }
        if (this.wrTwo != null){
            wrTwoLocked = true;
        }
        if (this.wrThree != null){
            wrThreeLocked = true;
        }
        if (this.teOne != null){
            teOneLocked = true;
        }
        if (this.flex != null){
            flexLocked = true;
        }
        if (this.dst != null){
            dstLocked = true;
        }
    }

    public void copyLockedFields(LineUp old){
        this.qbOneLocked = old.qbOneLocked;
        this.rbOneLocked = old.rbOneLocked;
        this.rbTwoLocked = old.rbTwoLocked;
        this.wrOneLocked = old.wrOneLocked;
        this.wrTwoLocked = old.wrTwoLocked;
        this.wrThreeLocked = old.wrThreeLocked;
        this.teOneLocked = old.teOneLocked;
        this.flexLocked = old.flexLocked;
        this.dstLocked = old.dstLocked;
    }

    public boolean isQbOneLocked(){
        return qbOneLocked;
    }
    public boolean isRbOneLocked(){
        return rbOneLocked;
    }
    public boolean isRbTwoLocked(){
        return rbTwoLocked;
    }
    public boolean isWrOneLocked(){
        return wrOneLocked;
    }
    public boolean isWrTwoLocked(){
        return wrTwoLocked;
    }
    public boolean isWrThreeLocked(){
        return wrThreeLocked;
    }
    public boolean isTeOneLocked(){
        return teOneLocked;
    }
    public boolean isFlexLocked(){
        return flexLocked;
    }
    public boolean isDstLocked(){
        return dstLocked;
    }

    private boolean isSalaryValid() {
        return getSalary() <= MAX_SALARY;
    }

    public int getSalary(){
        return safeSalary(this.qbOne) +
                safeSalary(this.rbOne) + safeSalary(this.rbTwo) +
                safeSalary(this.wrOne) + safeSalary(this.wrTwo) + safeSalary(this.wrThree) +
                safeSalary(this.teOne) +
                safeSalary(this.flex) +
                safeSalary(this.dst);
    }

    public boolean isLineupValid(){
        return positionsValid() && isSalaryValid() && noDupesPresent();
    }

    public boolean isLineupValidAndFinal(){
        return positionsValid() && isSalaryValid() && noDupesPresent() && getEmptySpots() == 0;
    }

    public int getFilledSpots(){
        return TOTAL_SPOTS - this.getEmptySpots();
    }

    public double getEfficiency(){
        var val = this.getPoints() / this.getSalary();
        return Math.round(val * 100.0) / 100.0;
    }

    public LineUp deepCopy(){
        var lu = new LineUp();
        lu.qbOne = qbOne;
        lu.rbOne = rbOne;
        lu.rbTwo = rbTwo;
        lu.wrOne = wrOne;
        lu.wrTwo = wrTwo;
        lu.wrThree = wrThree;
        lu.teOne = teOne;
        lu.flex = flex;
        lu.dst = dst;
        lu.copyLockedFields(this);
        return lu;
    }

    public static LineUp getInitialLineUp(Nfl nfl){
        LineUp lineUp = new LineUp();
        if (strategy != null){
            var temp = strategy.getFixedLineUp(nfl);
            if(temp != null && temp.isLineupValid()){
                lineUp = temp;
            }
        }
        return lineUp;
    }

    private boolean noDupesPresent(){
        List<Integer> lineUpIds = Arrays.asList(getSafeId(qbOne), getSafeId(rbOne), getSafeId(rbTwo),
                getSafeId(wrOne), getSafeId(wrTwo), getSafeId(wrThree), getSafeId(teOne), getSafeId(flex), getSafeId(dst));
        var result = lineUpIds.stream().filter(x -> x != -1).distinct().count();
        return result == getFilledSpots();
    }

    private int getSafeId(Player p){
        return (p != null) ? p.yahooId : -1;
    }

    private boolean positionsValid(){
        var qbvalid = positionValidCompare(this.qbOne, Position.QB.name());
        var rbOneValid = positionValidCompare(this.rbOne, Position.RB.name());
        var rbTwoValid = positionValidCompare(this.rbTwo, Position.RB.name());
        var wrOneValid = positionValidCompare(this.wrOne, Position.WR.name());
        var wrTwoValid = positionValidCompare(this.wrTwo, Position.WR.name());
        var wrThreeValid = positionValidCompare(this.wrThree, Position.WR.name());
        var teValid = positionValidCompare(this.teOne, Position.TE.name());
        var flexValid = this.flex == null || this.flex.getPosition().equalsIgnoreCase(Position.RB.name()) ||
                this.flex.getPosition().equalsIgnoreCase(Position.WR.name()) || this.flex.getPosition().equalsIgnoreCase(Position.TE.name());
        var dstValid = positionValidCompare(this.dst, Position.DST.name());

        return qbvalid && rbOneValid && rbTwoValid && wrOneValid && wrTwoValid && wrThreeValid && teValid && flexValid && dstValid;
    }

    private boolean positionValidCompare(Player p, String positionName){
        return p == null || p.getPosition().equalsIgnoreCase(positionName);
    }

    private int safeSalary(Player p){
        if (p != null){
            return p.salary;
        }
        else{
            return 0;
        }
    }

    private int getEmptySpots(){
        List<Integer> lineUpIds = Arrays.asList(getSafeId(qbOne), getSafeId(rbOne), getSafeId(rbTwo),
                getSafeId(wrOne), getSafeId(wrTwo), getSafeId(wrThree), getSafeId(teOne), getSafeId(flex), getSafeId(dst));
        return (int)lineUpIds.stream().filter(x -> x == -1).count();
    }

    private double getSeasonAverage(){
        var qb = qbOne.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var rb1 = rbOne.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var rb2 = rbTwo.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var wr1 = wrOne.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var wr2 = wrTwo.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var wr3 = wrThree.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var te = teOne.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var fl = flex.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        var def = dst.pointsHistory.stream().mapToDouble(x -> x).summaryStatistics().getAverage();
        return (qb + rb1 + rb2 + wr1 + wr2 + wr3 + te + fl + def);
    }

    private double getFprosPoints() {
        return qbOne.projectedBaseFpros + rbOne.projectedBaseFpros + rbTwo.projectedBaseFpros + wrOne.projectedBaseFpros +
                wrTwo.projectedBaseFpros + wrThree.projectedBaseFpros + teOne.projectedBaseFpros + flex.projectedBaseFpros + dst.projectedBaseFpros;
    }

    private double getSafePoints(Player p){
        if (p != null){
            return p.getPlayerPoints();
        }
        else{
            return 0;
        }
    }

    /**
     * @return Players in lineup for print line formatting
     */
    public List<Player> getPlayersInLineup(){
        qbOne.lineupPosition = "QB";
        rbOne.lineupPosition = "RB1";
        rbTwo.lineupPosition = "RB2";
        wrOne.lineupPosition = "WR1";
        wrTwo.lineupPosition = "WR2";
        wrThree.lineupPosition = "WR3";
        teOne.lineupPosition = "TE";
        flex.lineupPosition = "Flex";
        dst.lineupPosition = "DST";
        
        return Arrays.asList(qbOne, rbOne, rbTwo, wrOne, wrTwo, wrThree, teOne, flex, dst);
    }

    @Override
    public String toString(){
        var returnedString = "Strategy: %s".formatted(Player.strategy.getClass().getName()) + "\n" + "Projected Points: %s".formatted(this.getPoints()) + "\n";
        returnedString += "FPros Base Projected Points: %s".formatted(this.getFprosPoints()) + "\n";
        returnedString += "Season Average: %s".formatted(this.getSeasonAverage()) + "\n";
        returnedString += "Salary: %s".formatted(this.getSalary()) + "\n";
        returnedString += "Efficiency: %s".formatted(this.getEfficiency()) + "\n";
        // returnedString += "QB: " + qbOne + "\n" + "RB1: " + rbOne + "\n" + "RB2: " + rbTwo + "\n" + "WR1: " + wrOne + "\n" +
        //         "WR2: " + wrTwo + "\n" + "WR3: " + wrThree + "\n" + "TE: " + teOne + "\n" +
        //         "FLEX: " + flex + "\n" + "DST: " + dst + "\n";
        // returnedString += "---------------------------------------------------\n";
        return returnedString;
    }

    @Override
    public int compareTo(LineUp o) {
        return -Double.compare(this.getPoints(), o.getPoints());
    }
}
