package io.erosemberg.reader.parsing.events;

import io.erosemberg.reader.data.Event;
import io.erosemberg.reader.gamedata.fortnite.FortniteGameData;
import io.erosemberg.reader.gamedata.fortnite.FortniteWeaponTypes;
import io.erosemberg.reader.parsing.ParserOptions;
import io.erosemberg.reader.util.ByteUtils;
import io.erosemberg.reader.util.TimeUtils;
import me.hugmanrique.jacobin.reader.LittleEndianDataReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Erik Rosemberg
 * @since 22/12/2018
 */
public class FortniteEventParser implements EventParser<FortniteGameData> {

    private FortniteGameData gameData = new FortniteGameData();

    @Override
    public FortniteGameData parse(Event event, LittleEndianDataReader reader, ParserOptions options) throws IOException {
        String group = event.getGroup();
        String metatag = event.getMetadata();
        if (group.equalsIgnoreCase("playerElim")) {
            reader.skip(45); // woo magic numbers!
            int killedLength = ByteUtils.adjustLength(reader.readInt32());
            byte[] killedBuffer = new byte[killedLength];
            reader.read(killedBuffer, 0, killedLength);
            String killed = properParse(killedBuffer, killedLength);
            int killerLength = ByteUtils.adjustLength(reader.readInt32());
            byte[] killerBuffer = new byte[killerLength];
            reader.read(killerBuffer, 0, killerLength);
            String killer = properParse(killerBuffer, killerLength);
            long weaponId = reader.readUInt32();

            FortniteWeaponTypes type = FortniteWeaponTypes.fromId(weaponId);
            boolean knock = FortniteWeaponTypes.isKnock(weaponId);
            if (type == FortniteWeaponTypes.UNKNOWN && options.isPrintUnknownWeapons()) {
                System.out.println("Unknown weapon type with ID " + weaponId + " at " + TimeUtils.msToTimestamp(event.getTime1()));
                knock = false;
            }

            gameData.getKills().add(new FortniteGameData.Kill(killer, killed, type, knock, event.getTime1(), event.getTime2()));
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

    /**
     * Due to Epic's way of handling weird characters, they are using two different encodings.
     * This method is checking if the second to last byte is 0, since the UTF-16 null terminator (\0)
     * is <code>0x0 0x0</code> vs UTF-8's <code>0x0</code>, hence the comparison of the 2nd to last byte
     * instead of the last byte.
     *
     * @param data the binary data gathered from the stream.
     * @param len the length of the binary length.
     */
    private String properParse(byte[] data, int len) {
        boolean isUtf16 = data[len - 2] == 0; // check if 2nd to last byte is 0x0.
        Charset charset = StandardCharsets.UTF_8;
        if (isUtf16) {
            charset = StandardCharsets.UTF_16LE;
        }

        return new String(data, charset);
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
