package io.erosemberg.reader.gamedata.fortnite;

import io.erosemberg.reader.gamedata.GameData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Erik Rosemberg
 * @since 22/12/2018
 */
@Data
public class FortniteGameData implements GameData {

    private List<String> warnings = new ArrayList<>();

    private List<Kill> kills = new LinkedList<>();
    private Set<String> players = new HashSet<>();

    private long finalRanking;
    private long totalPlayers;
    private long totalElims;

    @Data
    @AllArgsConstructor
    public static class Kill {
        String killer;
        String killed;
        FortniteWeaponTypes type;
    }
}
