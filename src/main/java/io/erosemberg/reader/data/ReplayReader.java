package io.erosemberg.reader.data;

import com.google.common.primitives.UnsignedInteger;
import me.hugmanrique.jacobin.reader.ByteStreamReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static io.erosemberg.reader.util.ByteUtils.adjustLength;

/**
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
public class ReplayReader {

    private final int INDEX_NONE = -1;

    private ByteStreamReader reader;

    /**
     * Creates a new instance of the ReplayReader.
     *
     * @param reader the ByteStreamReader from which we will be reading the data.
     */
    public ReplayReader(ByteStreamReader reader) {
        this.reader = reader;
    }

    @SuppressWarnings("all")
    public ReplayInfo read() throws IOException {
        ReplayInfo.ReplayInfoBuilder builder = ReplayInfo.builder();

        ReplayHeader header = ReplayHeader.readHeader(this.reader);

        System.out.println("Read replay header...");
        System.out.println("========= header =========");
        System.out.println(header.toString());
        System.out.println("======= end header =======");
        builder.header(header);

        int totalSize = reader.available();
        int chunkIndex = 0;
        int checkpointIndex = 0;
        int replayDataIndex = 0;
        int eventIndex = 0;
        int chunkHeader = INDEX_NONE;

        List<Chunk> chunks = new LinkedList<>();
        List<Event> checkpoints = new LinkedList<>();
        List<Event> events = new LinkedList<>();
        List<ReplayData> dataChunks = new LinkedList<>();

        while (reader.available() != 0) {
            long typeOffset = reader.readInt64();

            ChunkType type = ChunkType.from(reader.readUInt32());
            System.out.println("type = " + type);
            int sizeInBytes = reader.readInt32();
            long dataOffset = reader.readInt64();

            Chunk chunk = new Chunk(type, sizeInBytes, typeOffset, dataOffset);
            chunks.add(chunk);
            chunkIndex += 1;

            switch (type) {
                case HEADER:
                    if (chunkHeader == INDEX_NONE) {
                        chunkHeader = chunkIndex;
                    } else {
                        throw new IllegalStateException("Found multiple header chunks");
                    }
                    break;
                case CHECKPOINT: {
                    int idLength = reader.readInt32();
                    String id = reader.readUTF8String(0, adjustLength(idLength));
                    int groupLength = reader.readInt32();
                    String group = reader.readUTF8String(0, adjustLength(groupLength));
                    int metadataLength = reader.readInt32();
                    String metadata = reader.readUTF8String(0, adjustLength(metadataLength));
                    long time1 = reader.readUInt32();
                    long time2 = reader.readUInt32();
                    int size = reader.readInt32();
                    long eventDataOffset = reader.readInt64();

                    Event event = new Event(checkpointIndex, id, group, metadata, UnsignedInteger.valueOf(time1), UnsignedInteger.valueOf(time2), size, eventDataOffset);
                    checkpoints.add(event);
                    checkpointIndex += 1;
                    break;
                }
                case REPLAY_DATA: {
                    long time1 = reader.readUInt32();
                    long time2 = reader.readUInt32();
                    int size = reader.readInt32();
                    long replayDataOffset = reader.readInt64();
                    long streamOffset = reader.readInt64();

                    ReplayData data = new ReplayData(replayDataIndex, UnsignedInteger.valueOf(time1), UnsignedInteger.valueOf(time2), size, replayDataOffset, streamOffset);
                    dataChunks.add(data);
                    replayDataIndex += 1;
                    break;
                }
                case EVENT: {
                    int idLength = reader.readInt32();
                    String id = reader.readUTF8String(0, adjustLength(idLength));
                    int groupLength = reader.readInt32();
                    String group = reader.readUTF8String(0, adjustLength(groupLength));
                    int metadataLength = reader.readInt32();
                    String metadata = reader.readUTF8String(0, adjustLength(groupLength));
                    long time1 = reader.readUInt32();
                    long time2 = reader.readUInt32();
                    int size = reader.readInt32();
                    long eventDataOffset = reader.readInt64();

                    Event event = new Event(eventIndex, id, group, metadata, UnsignedInteger.valueOf(time1), UnsignedInteger.valueOf(time2), size, eventDataOffset);
                    events.add(event);
                    eventIndex += 1;
                    break;
                }
                case UNKNOWN:
                    //System.out.println("Skipping unknown (cleared) chunk.");
                    break;
            }
        }

        return builder.build();
    }
}
