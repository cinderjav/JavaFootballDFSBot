package org.fball;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fball.lineupgen.DefaultLineUpGenerationStrategy;
import org.fball.nflfilter.DefaultNflFilterStrategy;
import org.fball.points.DefaultPointStrategy;
import org.fball.points.GabPointStrategy;
import org.fball.points.JavyPointStrategy;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        var nflPlayers = getPlayers();
        var nflPlayerCopy = nflPlayers.copyNfl();
        runCustomStrategyBot(nflPlayers);
        runDefaultStrategyBot(nflPlayerCopy);
    }

    public static void runCustomStrategyBot(Nfl nflPlayers){
        setCustomStrategies();
        runBot(nflPlayers);
    }
    public static void runDefaultStrategyBot(Nfl nflPlayers){
        setDefaultStrategies();
        runBot(nflPlayers);
    }

    private static void runBot(Nfl nflPlayers){
        Instant starts = Instant.now();
        var lineups = LineUpFactory.generateBestLineUp(nflPlayers);
        Instant ends = Instant.now();
        var elapsed = Duration.between(starts, ends).toMillis();
        System.out.println(lineups);
        System.out.println("Time Elapsed to Generate Lineup - " + elapsed/1000.0 + "\n");
    }

    private static void setDefaultStrategies(){
        Player.strategy = new DefaultPointStrategy();
        Nfl.strategy = new DefaultNflFilterStrategy();
        LineUpFactory.strategy = new DefaultLineUpGenerationStrategy();
    }

    private static void setCustomStrategies(){
        Player.strategy = new JavyPointStrategy();
        Nfl.strategy = new DefaultNflFilterStrategy();
        LineUpFactory.strategy = new DefaultLineUpGenerationStrategy();
    }

    public static Nfl getPlayers() throws URISyntaxException, IOException, InterruptedException {
        var uri = new URI("http://35.196.252.230:3000/");
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        var response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        var test = String.valueOf(response.body());
        ObjectMapper mapper = new ObjectMapper();
        Nfl players = mapper.readValue(test, Nfl.class);
        return players;
    }
}
