package io.erosemberg.reader.parsing;

import io.erosemberg.reader.parsing.events.FortniteEventParser;
import lombok.experimental.UtilityClass;

/**
 * @author Erik Rosemberg
 * @since 22/12/2018
 */
@UtilityClass
public class StandardParsers {

    public static final FortniteEventParser FORTNITE_PARSER = new FortniteEventParser();

}
