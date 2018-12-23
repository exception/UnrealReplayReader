package io.erosemberg.reader.gamedata;

/**
 * @author Erik Rosemberg
 * @since 22/12/2018
 */
public interface GameData {

    /**
     * Cleans up the event parser so it can be used again without having to
     * worry about any duplicate entries or incorrect data.
     */
    default void cleanUp() {
        // TODO
    }

}
