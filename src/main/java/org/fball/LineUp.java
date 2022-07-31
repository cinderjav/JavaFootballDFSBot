package org.fball;

public class LineUp {
    public static final int MAX_SALARY = 200;
    public Player qbOne;
    public Player rbOne;
    public Player rbTwo;
    public Player wrOne;
    public Player wrTwo;
    public Player wrThree;
    public Player teOne;
    public Player flex;
    public Player dst;

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
        return getSalary() + (10 * getEmptySpots()) < MAX_SALARY;
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
        return positionsValid() && isSalaryValid();
    }

    private boolean positionsValid(){
        if (getEmptySpots() != 0){
            return false;
        }
        var qbvalid = this.qbOne.getPosition().equalsIgnoreCase(Position.QB.name());
        var rbvalid = this.rbOne.getPosition().equalsIgnoreCase(Position.RB.name()) && this.rbTwo.getPosition().equalsIgnoreCase(Position.RB.name());
        var wrValid = this.wrOne.getPosition().equalsIgnoreCase(Position.WR.name()) && this.wrTwo.getPosition().equalsIgnoreCase(Position.WR.name()) &&
                this.wrThree.getPosition().equalsIgnoreCase(Position.WR.name());
        var teValid = this.teOne.getPosition().equalsIgnoreCase(Position.TE.name());
        var flexPosition = this.flex.getPosition();
        var flexValid = flexPosition.equalsIgnoreCase(Position.RB.name()) || flexPosition.equalsIgnoreCase(Position.WR.name()) || flexPosition.equalsIgnoreCase(Position.TE.name());
        var dstValid = this.dst.getPosition().equalsIgnoreCase(Position.DST.name());
        return qbvalid && rbvalid && wrValid && teValid && flexValid && dstValid;
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
        int count = 0;
        if (this.qbOne == null){
            count ++;
        }
        if (this.rbOne == null){
            count ++;
        }
        if (this.rbTwo == null){
            count ++;
        }
        if (this.wrOne == null){
            count ++;
        }
        if (this.wrTwo == null){
            count ++;
        }
        if (this.wrThree == null){
            count ++;
        }
        if (this.teOne == null){
            count ++;
        }
        if (this.flex == null){
            count ++;
        }
        if (this.dst == null){
            count ++;
        }
        return count;
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
        returnedString += qbOne + "\n" + rbOne + "\n" + rbTwo + "\n" + wrOne + "\n" + wrTwo + "\n" + wrThree + "\n" + flex + "\n" + dst + "\n";
        returnedString += "---------------------------------------------------\n";
        return returnedString;
    }
}
