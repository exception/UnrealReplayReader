package io.erosemberg.reader.parsing;

import lombok.Builder;
import lombok.Data;

/**
 * @author Erik Rosemberg
 * @since 23/12/2018
 */
@Builder
@Data
public class ParserOptions {

    private boolean debug;
    private boolean printUnknownWeapons;

}
