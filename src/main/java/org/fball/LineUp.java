package org.fball;

import java.util.Arrays;
import java.util.Hashtable;
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

    public void addPlayer(Player p) {
        var filledSpots = this.getFilledSpots();
        switch(filledSpots){
            case 0 -> this.qbOne = p;
            case 1 -> this.rbOne = p;
            case 2 -> this.rbTwo = p;
            case 3 -> this.wrOne = p;
            case 4 -> this.wrTwo = p;
            case 5 -> this.wrThree = p;
            case 6 -> this.teOne = p;
            case 7 -> this.flex = p;
            case 8 -> this.dst = p;
        }
    }

    public void clearLastPlayer() {
        var filledSpots = this.getFilledSpots();
        switch(filledSpots){
            case 1 -> this.qbOne = null;
            case 2 -> this.rbOne = null;
            case 3 -> this.rbTwo = null;
            case 4 -> this.wrOne = null;
            case 5 -> this.wrTwo = null;
            case 6 -> this.wrThree = null;
            case 7 -> this.teOne = null;
            case 8 -> this.flex = null;
            case 9 -> this.dst = null;
        }
    }

    public double getPoints(){
        return getSafePoints(this.qbOne) +
                getSafePoints(this.rbOne) + getSafePoints(this.rbTwo) +
                getSafePoints(this.wrOne) + getSafePoints(this.wrTwo) + getSafePoints(this.wrThree) +
                getSafePoints(this.teOne) +
                getSafePoints(this.flex) +
                getSafePoints(this.dst);
    }

    private boolean isSalaryValid() {
        var empSpots = getEmptySpots();
        var underSalary = getSalary() + (10 * empSpots) <= MAX_SALARY;
        if (!underSalary){
            return false;
        }
        return true;
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

    public Position getNextPosition(){
        return mapSpotsToPosition();
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

    public double GetEfficiencyForSpot(int spotend){
        if (spotend < 0) {
            return 0;
        }
        var spot = spotend;
        var pts = this.qbOne.getPlayerPoints();
        var sal = safeSalary(qbOne);
        spot -= 1;
        if (spot > 0){
            pts += this.rbOne.getPlayerPoints();
            sal += safeSalary(this.rbOne);
            spot -= 1;
        }
        if (spot > 0){
            pts += this.rbTwo.getPlayerPoints();
            sal += safeSalary(this.rbTwo);
            spot -= 1;
        }
        if (spot > 0){
            pts += this.wrOne.getPlayerPoints();
            sal += safeSalary(this.wrOne);
            spot -= 1;
        }
        if (spot > 0){
            pts += this.wrTwo.getPlayerPoints();
            sal += safeSalary(this.wrTwo);
            spot -= 1;
        }
        if (spot > 0){
            pts += this.wrThree.getPlayerPoints();
            sal += safeSalary(this.wrThree);
            spot -= 1;
        }
        if (spot > 0){
            pts += this.teOne.getPlayerPoints();
            sal += safeSalary(this.teOne);
            spot -= 1;
        }
        if (spot > 0){
            pts += this.flex.getPlayerPoints();
            sal += safeSalary(this.flex);
            spot -= 1;
        }
        if (spot > 0){
            pts += this.dst.getPlayerPoints();
            sal += safeSalary(this.dst);
        }

        var val = pts / sal;
        return Math.round(val * 100.0) / 100.0;
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

    private Position mapSpotsToPosition() {
        var filledSpots = this.getFilledSpots();
        var pos = Position.QB;
        switch(filledSpots){
            case 0 -> pos = Position.QB;
            case 1,2 -> pos = Position.RB;
            case 3,4,5 -> pos = Position.WR;
            case 6 -> pos = Position.TE;
            case 7 -> pos = Position.FLEX;
            case 8 -> pos = Position.DST;
        }
        return pos;
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
