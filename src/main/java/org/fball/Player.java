package org.fball;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.fball.points.DefaultPointStrategy;
import org.fball.points.IPointStrategy;

import java.util.ArrayList;
public class Player implements Comparable<Player>{
    public static IPointStrategy strategy = new DefaultPointStrategy();
    public String name;
    public int salary;
    public String position;
    @JsonProperty("page_url")
    public String pageUrl;
    @JsonProperty("opponent_id")
    public String opponentId;
    @JsonProperty("yahoo_id")
    public int yahooId;
    @JsonProperty("bye_week")
    public String byeWeek;
    @JsonProperty("rank_average")
    public double rankAverage;
    @JsonProperty("rank_std")
    public double rankStd;
    @JsonProperty("cbs_id")
    public String cbsId;
    public String team;
    @JsonProperty("team_name")
    public String teamName;
    @JsonProperty("is_home")
    public boolean isHome;
    @JsonProperty("player_owned_avg")
    public double playerOwnedAvg;
    @JsonProperty("projected_base_yahoo")
    public double projectedBaseYahoo;
    public double ppg;
    @JsonProperty("ppg_std")
    public double ppgStd;
    @JsonProperty("points_history")
    public ArrayList<Double> pointsHistory;
    @JsonProperty("projected_base_fpros")
    public double projectedBaseFpros;
    public String grade;
    public String position_rank;
    @JsonProperty("proj_base_fpros_efficiency")
    public double projBaseFprosEfficiency;
    @JsonProperty("proj_base_yah_efficiency")
    public double projBaseYahEfficiency;

    //Plugin for Custom Player Scorer
    public double getPlayerPoints(){
        if (strategy == null) {
            throw new RuntimeException("Must specify points Strategy.");
        }
        return strategy.getPoints(this);
    }

    public String getPosition (){
        return this.position;
    }

    public double getEfficiency(){
        var val =  getPlayerPoints()/salary;
        return Math.round(val * 100.0) / 100.0;
    }

    @Override
    public String toString(){
        return this.name + " [pts:%s] [sal:%s] [eff:%s], ".formatted(this.getPlayerPoints(), this.salary, this.getEfficiency());
    }

    @Override
    public int compareTo(Player o) {
        return -Double.compare(this.getEfficiency(), o.getEfficiency());
    }
}
