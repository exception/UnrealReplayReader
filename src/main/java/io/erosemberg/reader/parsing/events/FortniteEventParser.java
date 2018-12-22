package io.erosemberg.reader.parsing.events;

import com.google.common.base.Charsets;
import io.erosemberg.reader.data.Event;
import io.erosemberg.reader.gamedata.fortnite.FortniteGameData;
import io.erosemberg.reader.util.ByteUtils;
import me.hugmanrique.jacobin.reader.ByteStreamReader;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Erik Rosemberg
 * @since 22/12/2018
 */
public class FortniteEventParser implements EventParser<FortniteGameData> {

    private FortniteGameData gameData = new FortniteGameData();

    @Override
    public FortniteGameData parse(Event event, ByteStreamReader reader) throws IOException {
        String group = event.getGroup();
        String metatag = event.getMetadata();
        if (group.equalsIgnoreCase("playerElim")) {
            reader.skip(45); // woo magic numbers!
            int killedLength = ByteUtils.adjustLength(reader.readInt32());
            String killed = reader.readUTF8String(0, killedLength).trim().replace("\u0000", "");
            int killerLength = ByteUtils.adjustLength(reader.readInt32());
            String killer = reader.readUTF8String(0, killerLength).trim().replace("\u0000", "");

            gameData.getKills().add(new FortniteGameData.Kill(killer, killed));
            gameData.getPlayers().add(killer);
            gameData.getPlayers().add(killed);

            long size = reader.readUInt32();
            System.out.println("size = " + size);
        } else if (metatag.equalsIgnoreCase("AthenaMatchTeamStats")) {
            reader.readUInt32(); // ignore this value, always 0.
            long finalRanking = reader.readUInt32();
            long totalPlayers = reader.readUInt32();

            gameData.setTotalPlayers(totalPlayers);
            gameData.setFinalRanking(finalRanking);
        } else if (metatag.equalsIgnoreCase("AthenaMatchStats")) {
            reader.readUInt32();
            reader.readUInt32();
            reader.readUInt32();

            long totalElims = reader.readUInt32();
            gameData.setTotalElims(totalElims);
        }
        return gameData;
    }

    @Override
    public FortniteGameData finalFetch() {
        if (gameData.getPlayers().size() < gameData.getTotalPlayers()) {
            long diff = gameData.getTotalPlayers() - gameData.getPlayers().size();
            gameData.getWarnings().add("We were unable to track " + diff + " players. This is most likely due to them not getting kills/knocked/killed.");
        }
        return gameData;
    }
}
