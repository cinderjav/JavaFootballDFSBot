package org.fball;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fball.lineupgen.DefaultLineUpGenerationStrategy;
import org.fball.nflfilter.DefaultNflFilterStrategy;
import org.fball.points.DefaultPointStrategy;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        var nflPlayers = getPlayers();
        setStrategies();
        var lineups = LineUpFactory.generateBestLineUp(nflPlayers);
        System.out.println(lineups);
    }

    public static void setStrategies(){
        setDefaultStrategies();
    }

    private static void setDefaultStrategies(){
        Player.strategy = new DefaultPointStrategy();
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
