package io.erosemberg.reader.gamedata.fortnite;

import io.erosemberg.reader.gamedata.GameData;
import io.erosemberg.reader.util.TimeUtils;
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

    @Override
    public void cleanUp() {
        this.warnings.clear();
        this.kills.clear();
        this.players.clear();

        this.finalRanking = 0;
        this.totalPlayers = 0;
        this.totalElims = 0;
    }

    @Data
    @AllArgsConstructor
    public static class Kill {
        String killer;
        String killed;
        FortniteWeaponTypes type;

        boolean isElimination;

        long time1;
        long time2;

        /**
         * Returns the formatted timestamp in a mm:ss format.
         */
        public String getFormattedTimestamp() {
            return TimeUtils.msToTimestamp(time1);
        }
    }
}
