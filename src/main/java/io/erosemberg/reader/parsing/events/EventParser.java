package io.erosemberg.reader.parsing.events;

import io.erosemberg.reader.data.Event;
import io.erosemberg.reader.gamedata.GameData;
import me.hugmanrique.jacobin.reader.ByteStreamReader;

import java.io.IOException;

/**
 * @author Erik Rosemberg
 * @since 22/12/2018
 */
public interface EventParser<T extends GameData> {

    /**
     * Returns an instance of {@link T} with data gathered from the {@link Event}
     * and the {@link ByteStreamReader}.
     *
     * @param event  the event to parse, must be non-null.
     * @param reader the stream of the replay we're reading
     *               must be non-null. Since it is not a clone, any changes
     *               will affect the actual reader being used by the {@link io.erosemberg.reader.data.ReplayReader}
     *               and may affect it's performance.
     * @throws IOException if an I/O error occurs.
     */
    T parse(Event event, ByteStreamReader reader) throws IOException;

    /**
     * Once we're done parsing, this will return the finalized GameData object.
     */
    T finalFetch();

}
