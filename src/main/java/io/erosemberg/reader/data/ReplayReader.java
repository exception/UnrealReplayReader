package io.erosemberg.reader.data;

import me.hugmanrique.jacobin.reader.ByteStreamReader;

import java.io.IOException;

/**
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
public class ReplayReader {

    private ByteStreamReader reader;

    /**
     * Creates a new instance of the ReplayReader.
     *
     * @param reader the ByteStreamReader from which we will be reading the data.
     */
    public ReplayReader(ByteStreamReader reader) {
        this.reader = reader;
    }

    public ReplayInfo read() throws IOException {
        ReplayInfo.ReplayInfoBuilder builder = ReplayInfo.builder();

        ReplayHeader header = ReplayHeader.readHeader(this.reader);

        System.out.println("Read replay header...");
        System.out.println("========= header =========");
        System.out.println(header.toString());
        System.out.println("======= end header =======");
        builder.header(header);

        return builder.build();
    }
}
