package org.fball;

import java.util.ArrayList;
import java.util.Iterator;

public class NflPosition implements Iterable<Player> {
    ArrayList<Player> players = new ArrayList<>();

    public NflPosition(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }
}
