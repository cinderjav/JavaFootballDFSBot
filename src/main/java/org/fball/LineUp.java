package org.fball;

import java.util.Arrays;
import java.util.List;

public class LineUp implements Comparable<LineUp>{
    public static final int MAX_SALARY = 200;
    public static final int TOTAL_SPOTS = 9;
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

    //Support Setting Preconfigured Lineup
    public double getPoints(){
        return getSafePoints(this.qbOne) +
                getSafePoints(this.rbOne) + getSafePoints(this.rbTwo) +
                getSafePoints(this.wrOne) + getSafePoints(this.wrTwo) + getSafePoints(this.wrThree) +
                getSafePoints(this.teOne) +
                getSafePoints(this.flex) +
                getSafePoints(this.dst);
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
        return lu;
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

    private double getSafePoints(Player p){
        if (p != null){
            return p.getPlayerPoints();
        }
        else{
            return 0;
        }
    }

    @Override
    public String toString(){
        var returnedString = "---------------------------------------------------\n";
        returnedString += "Projected Points: %s".formatted(this.getPoints()) + "\n";
        returnedString += "Salary: %s".formatted(this.getSalary()) + "\n";
        returnedString += "Efficiency: %s".formatted(this.getEfficiency()) + "\n";
        returnedString += "QB: " + qbOne + "\n" + "RB1: " + rbOne + "\n" + "RB2: " + rbTwo + "\n" + "WR1: " + wrOne + "\n" +
                "WR2: " + wrTwo + "\n" + "WR3: " + wrThree + "\n" + "TE: " + teOne + "\n" +
                "FLEX: " + flex + "\n" + "DST: " + dst + "\n";
        returnedString += "---------------------------------------------------\n";
        return returnedString;
    }

    @Override
    public int compareTo(LineUp o) {
        return -Double.compare(this.getPoints(), o.getPoints());
    }
}
