package io.erosemberg.reader.parsing.events;

import io.erosemberg.reader.data.Event;
import io.erosemberg.reader.gamedata.fortnite.FortniteGameData;
import io.erosemberg.reader.gamedata.fortnite.FortniteWeaponTypes;
import io.erosemberg.reader.parsing.ParserOptions;
import io.erosemberg.reader.util.ByteUtils;
import io.erosemberg.reader.util.TimeUtils;
import me.hugmanrique.jacobin.reader.ByteStreamReader;

import java.io.IOException;

/**
 * @author Erik Rosemberg
 * @since 22/12/2018
 */
public class FortniteEventParser implements EventParser<FortniteGameData> {

    private FortniteGameData gameData = new FortniteGameData();

    @Override
    public FortniteGameData parse(Event event, ByteStreamReader reader, ParserOptions options) throws IOException {
        String group = event.getGroup();
        String metatag = event.getMetadata();
        if (group.equalsIgnoreCase("playerElim")) {
            reader.skip(45); // woo magic numbers!
            int killedLength = ByteUtils.adjustLength(reader.readInt32());
            String killed = reader.readUTF(killedLength).trim().replace("\u0000", "");
            int killerLength = ByteUtils.adjustLength(reader.readInt32());
            String killer = reader.readUTF(killerLength).trim().replace("\u0000", "");
            long weaponId = reader.readUInt32();
            //System.out.println("size = " + weaponId);

            FortniteWeaponTypes type = FortniteWeaponTypes.fromId(weaponId);
            if (type == FortniteWeaponTypes.UNKNOWN && options.isPrintUnknownWeapons()) {
                System.out.println("Unknown weapon type with ID " + weaponId + " at " + TimeUtils.msToTimestamp(event.getTime1()));
            }

            gameData.getKills().add(new FortniteGameData.Kill(killer, killed, type, event.getTime1(), event.getTime2()));
            gameData.getPlayers().add(killer);
            gameData.getPlayers().add(killed);
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
